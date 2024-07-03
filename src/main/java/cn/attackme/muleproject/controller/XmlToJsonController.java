package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.service.XmlToJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@ResponseBody
@RequestMapping("/XmlToJson")
public class XmlToJsonController {

    @Autowired
    private XmlToJsonService xmlToJsonService;

    @PostMapping("/json")
    public ResponseEntity<?> convertXMLToJSON(@RequestParam("file") MultipartFile file)  {
        try {
            String result = xmlToJsonService.convertXmlToJson(file);
            // 设置 Content-Type 为 application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
