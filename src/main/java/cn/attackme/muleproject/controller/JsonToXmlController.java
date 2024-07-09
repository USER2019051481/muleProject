package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.config.JwtTokenUtil;
import cn.attackme.muleproject.dto.JsonGlobalDTO;
import cn.attackme.muleproject.dto.JsonGlobalXmlDTO;
import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;
import cn.attackme.muleproject.repository.JsonGlobalXmlRepository;
import cn.attackme.muleproject.service.JsonGlobalXmlService;
import cn.attackme.muleproject.service.JsonToXmlService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/JsonToXml")
@Slf4j
public class JsonToXmlController {

    @Autowired
    private JsonToXmlService jsonToXmlService ;

    @Autowired
    private JwtTokenUtil jwtTokenUtil ;
    @Autowired
    private JsonGlobalXmlService jsonGlobalXmlService ;

    /**
     * 一个业务流的json转xml文件（包括全局配置和普通组件）
     * @param json
     * @return
     * @throws IOException
     */
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

//    ------------------将全局配置和普通组件提取出来转xml----------------------------


    /**
     * 整个项目的全局配置json转xml文件(只含有全局配置)
     * @param json
     * @return
     * @throws IOException
     */
    @PostMapping("/globalXml")
    public ResponseEntity<?> globalJsonToXml(@RequestBody String json) throws IOException {
        // 将json文件转成xml文件
        String xmlString = jsonToXmlService.loadGlobalJsonFromFile(json);

        // 设置响应头，指定文件名为原始文件名加上 .xml 后缀
        String outputFileName = "globalXml-"+UUID.randomUUID().toString() ;// 构造输出文件名
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", outputFileName);

        // 将字符串转为byte数组，Original byte[]
        byte[] fileContent = xmlString.getBytes();

        // 返回字节数组作为响应体，并设置响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_XML)
                .body(fileContent);
    }

    /**
     * 将业务流的json转xml文件
     * @param json
     * @return
     * @throws IOException
     */
    @PostMapping("/flowXml")
    public ResponseEntity<?> flowJsonToXml(@RequestBody String json) throws IOException{
        // 将json文件转成xml文件
        String xmlString = jsonToXmlService.loadFlowJsonFromFile(json);

        // 设置响应头，指定文件名为原始文件名加上 .xml 后缀
        String outputFileName = "flowXml-"+UUID.randomUUID().toString() ;// 构造输出文件名
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", outputFileName);

        // 将字符串转为byte数组，Original byte[]
        byte[] fileContent = xmlString.getBytes();

        // 返回字节数组作为响应体，并设置响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_XML)
                .body(fileContent);
    }


//    --------------- 对于单个json转xml的增删改查---------------------------------

    /**
     * 保存全局配置（POST）
     * @param request 头部的token
     * @param json 全局配置
     * @return
     * @throws IOException
     */
    @PostMapping("/saveGlobalConfigXml")
    public ResponseEntity<?> SaveGlobalConfigjsonToXml(HttpServletRequest request, @RequestBody String json) throws IOException {{
        // 设置一个UUID给全局配置
        String xmlId = UUID.randomUUID().toString();

//        //获取Token和username
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if(jwtToken == null){
//            return ResponseEntity.status(404).body("没有令牌，无法解析出token") ;
//        }
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();

        // 将json中的name解析出来，方便后面通过name查询使用
        JsonGlobalDTO jsonGlobalDTO = jsonToXmlService.loadJsonToJsonGlobalDTO(json);
        String xmlName = jsonGlobalDTO.getName();

        // 将json文件转成xml文件,并将xmlId写入json
        // 1、创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        // 2、添加 "id" 字段并为其赋值
        ((ObjectNode) jsonNode).put("id", xmlId);
        // 3、将修改后的 JsonNode 对象转换回 JSON 字符串
        String updatedJson = objectMapper.writeValueAsString(jsonNode);

        // 得到完整的xml格式数据
        String xmlString = jsonToXmlService.loadGlobalConfigJsonFromFile(updatedJson);

        // 3、将全局配置ID，用户名，全局配置，全局配置名存入数据库
        JsonGlobalXmlEntity jsonGlobalXmlEntity = new JsonGlobalXmlEntity(xmlId,userName,xmlString,xmlName);
        try {
            jsonGlobalXmlService.save(jsonGlobalXmlEntity);
        } catch (Exception e) {
            log.error("保存数据到数据库失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("保存数据到数据库失败");
        }



        // 将字符串转成json对象返回
        JsonGlobalXmlDTO jsonGlobalXmlDTO = new JsonGlobalXmlDTO(jsonGlobalXmlEntity.getId(),xmlString);
        // 返回字节数组作为响应体，并设置响应头
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                  .body(jsonGlobalXmlDTO);

    }}

    /**
     * 通过userName查找所有的全局配置
     * @param request
     * @return
     */
    @GetMapping("/getGlobalConfigXml")
    public ResponseEntity<?> GetGlobalConfigjsonToXml(HttpServletRequest request)  {
//        // 通过request拿到token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if(jwtToken == null){
//            return ResponseEntity.status(404).body("没有令牌，无法解析出token") ;
//        }
//        // 通过token获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();

        // 根据用户名去数据库查询xml
        List<JsonGlobalXmlEntity> allXml = jsonGlobalXmlService.findAllByUserName(userName);
        // 将查询到的xml通过数组的形式返回给前端
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(allXml);
    }

    @GetMapping("/getGlobalConfigXmlByName")
    public ResponseEntity<?> GetGlobalConfigXmlByName(HttpServletRequest request , @RequestParam String globalName){
//        // 通过request拿到token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if(jwtToken == null){
//            return ResponseEntity.status(404).body("没有令牌，无法解析出token") ;
//        }
//        // 通过token获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();

        // 通过用户名和全局配置名来查找全局配置
        JsonGlobalXmlEntity jsonGlobalXml = jsonGlobalXmlService.findByUserNameAndGlobalName(userName, globalName);
        JsonGlobalXmlDTO jsonGlobalXmlDTO = new JsonGlobalXmlDTO(jsonGlobalXml.getId(), jsonGlobalXml.getGlobalXml());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jsonGlobalXmlDTO);
    }


    @PutMapping("/updateGlobalConfigXml")
    public ResponseEntity<?> UpdateGlobalConfigXml(HttpServletRequest request ,@RequestBody String json) throws JsonProcessingException {
//        // 通过全局配置ID和用户名找到指定数据
//        // 1、通过request拿到token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if(jwtToken == null){
//            return ResponseEntity.status(404).body("没有令牌，无法解析出token") ;
//        }
//        // 2、通过token获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();
        // 3、将json解析成对象
        JsonGlobalDTO jsonGlobalDTO = jsonToXmlService.loadJsonToJsonGlobalDTO(json);
        String xml = jsonToXmlService.loadGlobalConfigJsonFromFile(json);
        String xmlId = jsonGlobalDTO.getId();
        JsonGlobalXmlDTO jsonGlobalXml = new JsonGlobalXmlDTO(xmlId, xml);

        // 更新指定数据
        jsonGlobalXmlService.updateGlobalXmlAndGlobalNameByIdAndUserName(xmlId,userName,
                xml,jsonGlobalDTO.getName());
        return ResponseEntity.ok(jsonGlobalXml) ;
    }

    /**
     * 删除全局配置
     * @param request
     * @param id
     * @return
     */
    @DeleteMapping("/deleteGlobalConfigXml")
    public ResponseEntity<?> deleteGlobalConfigXmlByName(HttpServletRequest request, @RequestParam String id) {
//        // 通过 request 拿到 token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if (jwtToken == null) {
//            return ResponseEntity.status(404).body("没有令牌，无法解析出 token");
//        }
//
//        // 通过 token 获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();
        log.info("id: {} username: {}", id, userName);

        int deleteCount = jsonGlobalXmlService.deleteByIdAndUserName(id, userName);
        log.info("deleteByIdAndUserName: {}", deleteCount);

        if (deleteCount > 0) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("删除失败");
        }
    }


}
