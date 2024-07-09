package cn.attackme.muleproject.service.serviceImpl;

import cn.attackme.muleproject.dto.JsonGlobalDTO;
import cn.attackme.muleproject.dto.JsonGlobalXmlDTO;
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
    // =========================================头文件和版本处理
    @Override
    public void loadVersion(StringBuilder sb) {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n") ;
    }

    @Override
    public void loadHeadFile(StringBuilder sb) {
        sb.append("<mule xmlns:wsc=\"http://www.mulesoft.org/schema/mule/wsc\"\n" +
                "\txmlns:db=\"http://www.mulesoft.org/schema/mule/db\"\n" +
                "\txmlns:apikit=\"http://www.mulesoft.org/schema/mule/mule-apikit\"\n" +
                "\txmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "\txmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd \n" +
                "http://www.mulesoft.org/schema/mule/mule-apikit http://www.mulesoft.org/schema/mule/mule-apikit/current/mule-apikit.xsd \n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/wsc http://www.mulesoft.org/schema/mule/wsc/current/mule-wsc.xsd\">\n") ;

    }

    //==========================================全局配置处理==========================================================
    @Override
    public void loadHttpListener(JsonGlobalDTO globalConfig, StringBuilder sb) {
        sb.append("\t<http:listener-config name=\""+globalConfig.getName()+"\" doc:name=\"HTTP Listener config\" doc:id=\""+globalConfig.getId()+"\" basePath=\""+globalConfig.getBasePath()+"\">\n" +
                "\t\t<http:listener-connection host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"\" />\n" +
                "\t</http:listener-config>\n") ;
//        sb.append("\t<http:listener-config name=\""+globalConfig.getName()+"\" host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"\" basePath=\""+globalConfig.getBasePath()+"\" doc:name=\"HTTP Listener Configuration\"/>") ;
    }
    @Override
    public void loadMysqlConfiguration(JsonGlobalDTO globalConfig,StringBuilder sb) {
        sb.append("\t<db:config name=\""+globalConfig.getName()+"\" doc:name=\"Database Config\" doc:id=\""+globalConfig.getId()+"\" >\n" +
                "\t\t<db:my-sql-connection host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"\" user=\""+globalConfig.getUsername()+"" +
                "\" password=\""+globalConfig.getPassword()+
                "\" database=\""+globalConfig.getDatabase()+"\" driver=\""+globalConfig.getDriver()+"\"   />\n" +
                "\t</db:config>\n") ;

    }

    @Override
    public void loadSQLServerConfiguration(JsonGlobalDTO globalConfig, StringBuilder sb) {
        sb.append("\t<db:config name=\""+globalConfig.getName()+"\" doc:name=\"Database Config\" doc:id=\""+globalConfig.getId()+"\" >\n" +
                "\t\t<db:mssql-connection host=\""+globalConfig.getHost()+"\" instanceName=\""+globalConfig.getInstanceName()+"\" port=\""+ globalConfig.getPort()+"" +
                "\" user=\""+globalConfig.getUsername()+"\" password=\""+globalConfig.getPassword()+"\" databaseName=\""+globalConfig.getDatabase()+"\" driver=\""+globalConfig.getDriver()+"\" />\n" +
                "\t</db:config>\n") ;
    }

    @Override
    public void loadOracleConfiguration(JsonGlobalDTO globalConfig, StringBuilder sb) {
        sb.append("\t<db:config name=\""+globalConfig.getName()+"\" doc:name=\"Database Config\" doc:id=\""+globalConfig.getId()+"\" >\n" +
                "\t\t<db:oracle-connection host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"" +
                "\" user=\""+globalConfig.getUsername()+"\" password=\""+globalConfig.getPassword()+"\" instance=\""+globalConfig.getInstanceName()+
                "\" serviceName=\""+globalConfig.getServiceName()+"\" driver=\""+globalConfig.getDriver()+"\" />\n" +
                "\t</db:config>\n") ;

    }

    @Override
    public void loadPostgreSQLConfiguration(JsonGlobalDTO globalConfig, StringBuilder sb) {
        // TODO : 没有protocol属性
        sb.append("\t<db:config name=\""+globalConfig.getName()+"\" doc:name=\"Database Config\" doc:id=\""+globalConfig.getId()+"\" >\n" +
                "\t\t<db:postgre-sql-connection host=\""+globalConfig.getHost()+"\" port=\""+globalConfig.getPort()+"\" user=\""+globalConfig.getUsername()+"\" password=\""+globalConfig.getPassword()+"" +
                "\" database=\""+globalConfig.getDatabase()+"\" driver=\""+globalConfig.getDriver()+"\"   />\n" +
                "\t</db:config>\n") ;
    }

    // version:win64-7.3.4-2019
    // protocol version: 7.16.0
    @Override
    public void loadRequestConfiguration(JsonGlobalDTO globalDTO, StringBuilder sb) {
        sb.append("\t<http:request-config name=\""+globalDTO.getName()+"\" doc:name=\"HTTP Request configuration\" " +
                "doc:id=\""+globalDTO.getId()+"\" basePath=\""+globalDTO.getBasePath()+"\" >\n" +
                "\t\t<http:request-connection protocol=\""+globalDTO.getProtocol()+"\" host=\""+globalDTO.getHost()+"\" port=\""+globalDTO.getPort()+"\" />\n" +
                "\t</http:request-config>\n") ;
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
//            log.info("看一下： "+jsonNodeDTO.getChildNodes());
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

    @Override
    public void loadSetPayloadStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb , int depth) {
        Map<String, Object> data = jsonNodeDTO.getData();
        creatTab(sb,depth);
        sb.append("<set-payload value=\""+data.get("payloadValue")+"\" " +
                "doc:name=\"Set Payload\" doc:id=\""+jsonNodeDTO.getId()+"\" />\n") ;
    }

    @Override
    public void loadRequestStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb , int depth) {
        Map<String , Object> data = jsonNodeDTO.getData() ;

        creatTab(sb,depth);
        sb.append("<http:request method=\""+data.get("method")+"\" " +
                "doc:name=\""+data.get("displayName")+"\"" +
                " doc:id=\""+jsonNodeDTO.getId()+"\" " +
                "config-ref=\""+data.get("connectorConfiguration")+"\" " +
                "path=\""+data.get("path")+"\">\n" );
        creatTab(sb,depth+1);
        sb.append("<http:body ><![CDATA["+data.get("body")+"]]></http:body>\n") ;
        creatTab(sb,depth+1);
        sb.append("<http:headers ><![CDATA[#[output application/java\n" +
                "---\n" +
                ""+data.get("headers")+"]]]></http:headers>\n") ;
        creatTab(sb,depth+1);
        sb.append("<http:uri-params ><![CDATA[#[output application/java\n" +
                "---\n" +
                ""+data.get("urlParameters")+"]]]></http:uri-params>\n") ;
        creatTab(sb,depth+1);
        sb.append("<http:query-params ><![CDATA[#[output application/java\n" +
                "---\n" +
                ""+data.get("queryParameters")+"]]]></http:query-params>\n") ;
        creatTab(sb,depth);
        sb.append("</http:request>\n");


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
//            log.info("看一下： "+jsonNodeDTO.getChildNodes());
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

    @Override
    public void loadMuleEnd(StringBuilder sb) {
        sb.append("</mule>") ;
    }


}
