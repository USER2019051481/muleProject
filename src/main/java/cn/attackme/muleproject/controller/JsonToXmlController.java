package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.service.JsonToXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.util.UUID;

@Controller
@RequestMapping("/JsonToXml")
public class JsonToXmlController {

    @Autowired
    private JsonToXmlService jsonToXmlService ;
    @PostMapping("/xml")
    public ResponseEntity<?> jsonToXml(@RequestBody String json) throws IOException {
        // 将json文件转成xml文件
        String xmlString = jsonToXmlService.loadJsonFromFile(json);

        // 将xmlString转换为二进制流
        byte[] xmlBytes = xmlString.getBytes();

        // 写入到文件

        String outputFileName = "xml-"+UUID.randomUUID().toString() ;// 构造输出文件名
        try (FileOutputStream outputStream = new FileOutputStream(outputFileName)) {
            outputStream.write(xmlBytes); // 将 xmlBytes 写入到文件中
        }

        // 设置响应头，指定文件名为原始文件名加上 .xml 后缀
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", outputFileName);

        // 读取生成的XML文件内容为字节数组
        byte[] fileContent;
        try (InputStream inputStream = new FileInputStream(outputFileName)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[4096];
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            buffer.flush();
            fileContent = buffer.toByteArray();
        }

        // 删除生成的临时文件
        File outputFile = new File(outputFileName);
        outputFile.delete();

        // 返回字节数组作为响应体，并设置响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_XML)
                .body(fileContent);
    }
}
