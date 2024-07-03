package cn.attackme.muleproject.service;

import cn.attackme.muleproject.dto.JsonGlobalDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.IOException;

public interface JsonToXmlService {
    // 解析json文件
    String loadJsonFromFile(String json) throws IOException;

    String loadGlobalConfigJsonFromFile(String json ) throws JsonProcessingException;
    JsonGlobalDTO loadJsonToJsonGlobalDTO(String json) throws JsonProcessingException;

}
