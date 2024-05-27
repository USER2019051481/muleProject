package cn.attackme.muleproject.service;

import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.IOException;

public interface JsonToXmlService {
    // 解析json文件
    String loadJsonFromFile(String json) throws IOException;
}
