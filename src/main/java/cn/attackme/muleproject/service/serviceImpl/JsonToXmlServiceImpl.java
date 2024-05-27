package cn.attackme.muleproject.service.serviceImpl;

import cn.attackme.muleproject.dto.JsonEdgeDTO;
import cn.attackme.muleproject.dto.JsonGlobalDTO;
import cn.attackme.muleproject.dto.JsonGraphDTO;
import cn.attackme.muleproject.dto.JsonNodeDTO;
import cn.attackme.muleproject.service.JsonToXmlService;
import cn.attackme.muleproject.service.XmlTemplateService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
public class JsonToXmlServiceImpl implements JsonToXmlService {

    @Resource
    private XmlTemplateService xmlTemplateService ;
    /**
     * 将文件中的数据按照节点和连接线，解析出来
     * @param json  传入json对象
     * @throws IOException
     */
    @Override
    public String loadJsonFromFile(String json) throws IOException {
        // 创建 ObjectMapper
        // 如果 JSON 数据中的字段值为空字符串 ("")，它们将被解析为 Java 对象的 null。
        ObjectMapper mapper = JsonMapper.builder()
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .build();

        // 从输入流读取JSON数据并将其解析为Graph对象
        JsonGraphDTO graph = mapper.readValue(json, JsonGraphDTO.class);
        List<JsonNodeDTO> nodes = graph.getNodes();
        List<JsonEdgeDTO> edges = graph.getEdges();
        List<JsonGlobalDTO> globalConfigs = graph.getGlobalConfig();
        StringBuilder sb = new StringBuilder() ;
        Set<String> writtenNodes = new HashSet<>(); // 用于存储已写入的节点的ID

        // 先写mule
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n") ;
        sb.append("<mule xmlns:db=\"http://www.mulesoft.org/schema/mule/db\"\n" +
                "    xmlns:http=\"http://www.mulesoft.org/schema/mule/http\"\n" +
                "    xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "                        http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "                        http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\">\n") ;

        // 写全局的配置文件
        if(globalConfigs.isEmpty()||globalConfigs==null){
            //没有全局配置
            log.info("此json文件没有全局配置！");
        }else{
            loadGlobalProperties(globalConfigs,sb);
        }



        // 写flow
        sb.append("\t<flow name=\"\" doc:id=\"\">\n") ;
        // 逐个解析edge
        for (JsonEdgeDTO edge : edges) {
            String source = edge.getSource();
            String target = edge.getTarget();
            // 通过source和target找到node
            JsonNodeDTO nodeSource = findNodeById(source, nodes);
            JsonNodeDTO nodeTarget = findNodeById(target, nodes);
            if (nodeSource == null || nodeTarget == null) {
                log.info("找不到id为" + source + " 的节点或者id为：" + target + " 的节点！！！");
                continue; // 如果找不到节点，跳过当前边
            }


            writeNodeXml(writtenNodes, source, nodeSource, sb,nodes,2);
            writeNodeXml(writtenNodes, target, nodeTarget, sb,nodes,2);

        }
        sb.append("\t</flow>\n") ;

        // 判断有没有子flow

        for (JsonNodeDTO node : nodes) {
            String nodeType = node.getType();
            if ("subFlow".equals(nodeType)) {
                xmlTemplateService.loadSubFlowStart(node,sb,1);
                // 找到未写入的节点，将其写入 XML
                writeNodeXml(writtenNodes, node.getId(), node, sb, nodes,1);
                xmlTemplateService.loadSubFlowEnd(node,sb,1);
            }
        }

        sb.append("</mule>") ;
        System.out.print("xml为：\n"+sb.toString());
        return sb.toString() ;

    }

    private void loadGlobalProperties(List<JsonGlobalDTO> globalConfigs,StringBuilder sb ) {
        for (JsonGlobalDTO globalConfig : globalConfigs) {
            String type = globalConfig.getType() ;

            if (type != null) {
                if("Listener".equals(type)){
                    // 处理HTTP Listener
                    xmlTemplateService.loadHttpListener(globalConfig,sb) ;
                }
                if("Database".equals(type)){
                    // 处理mysqlConfiguration
                    if("MySQL".equals(globalConfig.getConnection())){
                        xmlTemplateService.loadMysqlConfiguration(globalConfig,sb) ;
                    }
                    // TODO 还有其他类型数据库配置未处理

                }
                // TODO 还有其他类型的type未处理
            }


        }
    }



    private void writeNodeXml(Set<String> writtenNodes, String id, JsonNodeDTO node, StringBuilder sb,List<JsonNodeDTO> nodes,int depth) {
        if(!writtenNodes.contains(id)){
            String nodeSourceString = loadNode(node,nodes,new StringBuilder(),depth,writtenNodes);
            sb.append(nodeSourceString) ;
            writtenNodes.add(id) ;
        }
    }

    private JsonNodeDTO findNodeById(String source, List<JsonNodeDTO> nodes) {
        for (JsonNodeDTO node : nodes) {
            if(node.getId().equals(source)){
                return node ;
            }
        }
        return null ;
    }

    private String loadNode(JsonNodeDTO node, List<JsonNodeDTO> nodes, StringBuilder result, int depth,Set<String> writtenNodes) {
        // 判断此节点是什么类型的
        String type = node.getType();
        List<String> childNodes = node.getChildNodes();
        String defaultNode = node.getDefaultNode();

        StringBuilder dataBuilder = new StringBuilder();
        if (node.getData() == null) {
            // 没有data
            if(depth == 0){
                result.append(String.format("<%s=",type)) ;
            }else{
                result.append(String.format("%" + (depth * 2) + "s<%s ", "", type));
            }
        } else {
            // 有data
            if("listener".equals(type)){
                xmlTemplateService.loadListenerStart(node,result,depth);
            }else if("choice".equals(type)){
                xmlTemplateService.loadChoiceStart(node,result,depth);
            }else if("flowReference".equals(type)){
                xmlTemplateService.loadFlowReferenceStart(node,result, depth);
            }else if("choiceDefault".equals(type)){
                xmlTemplateService.loadChoiceDefaultStart(node,result, depth);
            }else if("choiceWhen".equals(type)){
                xmlTemplateService.loadChoiceWhenStart(node,result, depth);
            }else if("database".equals(type)){
                xmlTemplateService.loadDatabaseStart(node,result, depth);
            }else if("forEach".equals(type)){
                xmlTemplateService.loadForEachStart(node,result, depth);
            }else if("logger".equals(type)){
                xmlTemplateService.loadLoggerStart(node,result, depth);
            }
//            for (Map.Entry<String, Object> entry : data.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue().toString();
//                dataBuilder.append(key).append("=\"").append(value).append("\" ");
//            }
//            if(depth == 0){
//                result.append(String.format("<%s %s",type,dataBuilder.toString())) ;
//            }else{
//                result.append(String.format("%" + (depth * 2) + "s<%s %s ", "", type, dataBuilder.toString()));
//            }
        }

//         判断是否有内嵌
        if (childNodes == null || childNodes.isEmpty()) {
//            // 没有内嵌
//            // 将节点字符串添加到结果
//            result.append("/>\n");
//            根据type进行尾部处理
        } else {
//            result.append(">\n");
//             子节点中是否有defaultNode
            int defaultFlag = 0 ;
            // 处理具有子节点的情况
            for (String childNode : childNodes) {
                if (childNode.equals(defaultNode)) {
                    // 如果是defaultNode，放到最后处理
                    defaultFlag=1 ;
                    continue;
                }
                JsonNodeDTO nodeChild = findNodeById(childNode, nodes);
                writtenNodes.add(nodeChild.getId());
                loadNode(nodeChild, nodes, result, depth + 1, writtenNodes);

            }
            if(defaultFlag==1){
                //处理defaultNode
                JsonNodeDTO nodeChild = findNodeById(defaultNode, nodes);
                writtenNodes.add(nodeChild.getId());
                loadNode(nodeChild, nodes, result, depth + 1, writtenNodes);
            }
        }

            // 根据type进行尾部处理
            if("choice".equals(type)){
                xmlTemplateService.loadChoiceEnd(node,result,depth);
            }else if("choiceDefault".equals(type)){
                xmlTemplateService.loadChoiceDefaultEnd(node,result, depth);
            }else if("choiceWhen".equals(type)){
                xmlTemplateService.loadChoiceWhenEnd(node,result, depth);
            }else if("database".equals(type)){
                xmlTemplateService.loadDatabaseEnd(node,result, depth);
            }else if("forEach".equals(type)){
                xmlTemplateService.loadForEachEnd(node,result, depth);
            }
//            if(depth == 0){
//                result.append(String.format("</%s>\n",type)) ;
//            }else{
//                result.append(String.format("%" + (depth * 2) + "s</%s>\n", "", type));
//            }

//        }


        return result.toString();
}


}
