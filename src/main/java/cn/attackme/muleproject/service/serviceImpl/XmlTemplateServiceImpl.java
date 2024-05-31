package cn.attackme.muleproject.service.serviceImpl;

import cn.attackme.muleproject.dto.JsonGlobalDTO;
import cn.attackme.muleproject.dto.JsonNodeDTO;
import cn.attackme.muleproject.service.XmlTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class XmlTemplateServiceImpl implements XmlTemplateService {
    //==========================================全局配置处理==========================================================
    @Override
    public void loadHttpListener(JsonGlobalDTO globalConfig, StringBuilder sb) {
        sb.append("\t<http:listener-config name=\""+globalConfig.getName()+"\" doc:name=\"HTTP Listener config\" doc:id=\""+globalConfig.getId()+"\" basePath=\""+globalConfig.getBasePath()+"\">\n" +
                "\t\t<http:listener-connection host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"\" />\n" +
                "\t</http:listener-config>\n") ;
    }
    @Override
    public void loadMysqlConfiguration(JsonGlobalDTO globalConfig,StringBuilder sb) {

        sb.append("\t<db:config name=\""+globalConfig.getName()+"\" doc:name=\"Database Config\" doc:id=\""+globalConfig.getId()+"\" >\n" +
                "\t\t<db:my-sql-connection host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"\" user=\""+globalConfig.getUsername()+"\" password=\""+globalConfig.getPassword()+"\" database=\""+globalConfig.getDatabase()+"\" />\n" +
                "\t</db:config>\n") ;

    }

    //========================================头部处理===========================================================

    private  void creatTab(StringBuilder sb, int depth) {
        // 添加制表符作为开头
        for (int i = 0; i < depth; i++) {
            sb.append("\t");
        }
    }
    @Override
    public void loadListenerStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) {
        Map<String, Object> data = jsonNodeDTO.getData();
        creatTab(sb, depth);
        sb.append("<http:listener doc:name=\""+data.get("displayName")+"\" doc:id=\""+jsonNodeDTO.getId()+"\" config-ref=\""+data.get("connectorConfiguration")+"\" path=\""+data.get("path")+"\"/>\n") ;
    }


    @Override
    public void loadChoiceStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) {
        creatTab(sb,depth);
        Map<String, Object> data = jsonNodeDTO.getData();
        sb.append("<choice doc:name=\""+data.get("displayName")+"\" doc:id=\""+jsonNodeDTO.getId()+"\">\n") ;
    }

    @Override
    public void loadFlowReferenceStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) {
        Map<String, Object> data = jsonNodeDTO.getData();
        creatTab(sb,depth);
        sb.append("<flow-ref doc:name=\""+data.get("displayName")+"\" doc:id=\""+jsonNodeDTO.getId()+"\" name=\""+data.get("flowName")+"\"/>\n") ;
    }

    @Override
    public void loadLoggerStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) {
        Map<String, Object> data = jsonNodeDTO.getData();
        creatTab(sb,depth);
        sb.append("<logger level=\""+data.get("level")+"\" doc:name=\""+data.get("displayName")+"\" doc:id=\""+jsonNodeDTO.getId()+"\" message=\""+data.get("message")+"\"/>\n") ;
    }

    @Override
    public void loadSubFlowStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) {
        Map<String, Object> data = jsonNodeDTO.getData();
        creatTab(sb,depth);
        sb.append("<sub-flow name=\""+data.get("displayName")+"\" doc:id=\""+jsonNodeDTO.getId()+"\" >\n") ;
    }

    @Override
    public void loadDatabaseStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) {
        Map<String, Object> data = jsonNodeDTO.getData();
        creatTab(sb,depth);
        sb.append("<db:"+data.get("operation").toString().toLowerCase()+" doc:name=\""+data.get("operation")+"\" doc:id=\""+jsonNodeDTO.getId()+"\" config-ref=\""+data.get("connectorConfiguration")+"\">\n");
        creatTab(sb,depth+1);
        sb.append("<db:sql ><![CDATA["+data.get("sqlCommand")+"]]></db:sql>\n");

    }

    @Override
    public void loadChoiceWhenStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb, int depth) {
        creatTab(sb,depth);
        Map<String, Object> data = jsonNodeDTO.getData();
        sb.append("<when expression=\"#["+data.get("expression")+"]\">\n");

    }

    @Override
    public void loadChoiceDefaultStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb, int depth) {

        if(jsonNodeDTO.getChildNodes().isEmpty() || jsonNodeDTO.getChildNodes() == null){
            log.info("看一下： "+jsonNodeDTO.getChildNodes());
            return ;
        }
        creatTab(sb,depth);
        sb.append("<otherwise >\n") ;
    }

    @Override
    public void loadForEachStart(JsonNodeDTO node, StringBuilder sb, int depth) {
        Map<String, Object> data = node.getData();
        creatTab(sb,depth);
            sb.append("<foreach doc:name=\""+data.get("displayName")+"\" doc:id=\""+node.getId()+"\" >\n") ;

    }

    @Override
    public  void loadFlowStart(JsonNodeDTO node,StringBuilder sb){
        Map<String,Object> data = node.getData() ;
        sb.append("\t<flow name=\""+data.get("displayName")+"\" doc:id=\""+node.getId()+"\">\n") ;

    }


    //==========================尾部处理==============================


    @Override
    public void loadChoiceEnd(JsonNodeDTO jsonNodeDTO, StringBuilder sb, int depth) {
            creatTab(sb,depth);
            sb.append("</choice>\n") ;
    }





    @Override
    public void loadDatabaseEnd(JsonNodeDTO jsonNodeDTO, StringBuilder sb, int depth) {
        creatTab(sb,depth);
        Map<String, Object> data = jsonNodeDTO.getData();
        sb.append("</db:"+data.get("operation").toString().toLowerCase()+">\n") ;

    }

    @Override
    public void loadChoiceWhenEnd(JsonNodeDTO jsonNodeDTO, StringBuilder sb, int depth) {
        creatTab(sb,depth);
        sb.append("</when>\n") ;
    }

    @Override
    public void loadChoiceDefaultEnd(JsonNodeDTO jsonNodeDTO, StringBuilder sb, int depth) {
        if(jsonNodeDTO.getChildNodes().isEmpty() || jsonNodeDTO.getChildNodes() == null){
            log.info("看一下： "+jsonNodeDTO.getChildNodes());
            return ;
        }
        creatTab(sb,depth);
        sb.append("</otherwise>\n") ;
    }

    @Override
    public void loadForEachEnd(JsonNodeDTO node, StringBuilder sb, int depth) {
        creatTab(sb,depth);
        sb.append("</foreach>\n");

    }

    @Override
    public void loadSubFlowEnd(JsonNodeDTO node, StringBuilder sb, int depth) {
        creatTab(sb,depth);
        sb.append("</sub-flow>\n") ;
    }

    @Override
    public  void loadFlowEnd(StringBuilder sb){
        sb.append("\t</flow>\n") ;
    }
}
