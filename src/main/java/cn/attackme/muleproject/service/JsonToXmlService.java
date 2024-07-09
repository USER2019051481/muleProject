package cn.attackme.muleproject.service;

import cn.attackme.muleproject.dto.JsonGlobalDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.IOException;

public interface JsonToXmlService {
    // 解析json字符串,json包括全局配置和flow组件
    String loadJsonFromFile(String json) throws IOException;
//     解析json字符串,json包括整个项目的全局配置
    String loadGlobalJsonFromFile(String json) throws JsonProcessingException;
//    解析json字符串,json包括一个业务流的所有flow组件
    String loadFlowJsonFromFile(String json) throws JsonProcessingException;

    String loadGlobalConfigJsonFromFile(String json ) throws JsonProcessingException;
    JsonGlobalDTO loadJsonToJsonGlobalDTO(String json) throws JsonProcessingException;



}
