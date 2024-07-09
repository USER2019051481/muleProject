package cn.attackme.muleproject.service;

import cn.attackme.muleproject.dto.JsonGlobalDTO;
import cn.attackme.muleproject.dto.JsonGlobalXmlDTO;
import cn.attackme.muleproject.dto.JsonNodeDTO;
import springfox.documentation.spring.web.json.Json;

public interface XmlTemplateService {
//    -----------------------------处理版本和头文件
    void loadVersion(StringBuilder sb);
    void loadHeadFile(StringBuilder sb) ;

//    ------------------------------ 全局配置
    /**
     * 处理HttpListener
     * @param globalConfig
     * @param sb
     */
    void loadHttpListener(JsonGlobalDTO globalConfig, StringBuilder sb) ;

    /**
     * 处理MysqlConfiguration
     * @param globalConfig
     * @param sb
     */
    void loadMysqlConfiguration(JsonGlobalDTO globalConfig,StringBuilder sb) ;
    void loadSQLServerConfiguration(JsonGlobalDTO globalConfig, StringBuilder sb);

    void loadOracleConfiguration(JsonGlobalDTO globalConfig, StringBuilder sb);

    void loadPostgreSQLConfiguration(JsonGlobalDTO globalConfig, StringBuilder sb);

    void loadRequestConfiguration(JsonGlobalDTO globalDTO,StringBuilder sb) ;

    // -----------------------------------------flow组件开始

    void loadListenerStart(JsonNodeDTO jsonNodeDTO, StringBuilder sb,int depth) ;
    void loadChoiceStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadFlowReferenceStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadLoggerStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadSubFlowStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadDatabaseStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadChoiceWhenStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadChoiceDefaultStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;

    void loadFlowStart(JsonNodeDTO node,StringBuilder sb) ;

    void loadSetPayloadStart(JsonNodeDTO jsonNodeDTO,StringBuilder sb , int depth) ;
    void loadRequestStart(JsonNodeDTO jsonNodeDTO , StringBuilder sb , int depth) ;
//====================================处理结尾======================================================

    void loadForEachStart(JsonNodeDTO node, StringBuilder result, int depth);




    void loadChoiceEnd(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;

    void loadDatabaseEnd(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadChoiceWhenEnd(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;
    void loadChoiceDefaultEnd(JsonNodeDTO jsonNodeDTO,StringBuilder sb,int depth) ;


    void loadForEachEnd(JsonNodeDTO node, StringBuilder result, int depth);

    void loadSubFlowEnd(JsonNodeDTO node, StringBuilder sb, int i);

    void loadFlowEnd(StringBuilder sb) ;
// -----------------------------------------以mule结尾

    void loadMuleEnd(StringBuilder sb) ;

}
