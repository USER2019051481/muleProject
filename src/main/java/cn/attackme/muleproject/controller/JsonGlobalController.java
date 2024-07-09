package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.config.JwtTokenUtil;
import cn.attackme.muleproject.dto.JsonGlobalDTO;
import cn.attackme.muleproject.dto.JsonGlobalJsonDTO;
import cn.attackme.muleproject.dto.JsonGlobalXmlDTO;
import cn.attackme.muleproject.entity.JsonGlobalJsonEntity;
import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;
import cn.attackme.muleproject.service.JsonGlobalJsonService;
import cn.attackme.muleproject.service.JsonToXmlService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/GlobalJson")
@Slf4j
public class JsonGlobalController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil ;
    @Resource
    private JsonGlobalJsonService jsonGlobalJsonService ;
    @Autowired
    private JsonToXmlService jsonToXmlService;
    /**
     * 保存全局配置（POST）
     * @param request 头部的token
     * @param json 全局配置
     * @return
     * @throws IOException
     */
    @PostMapping("/saveGlobalConfigJson")
    public ResponseEntity<?> SaveGlobalConfigjsonToXml(HttpServletRequest request, @RequestBody String json) throws IOException {{
        // 设置一个UUID给全局配置
        String jsonId = UUID.randomUUID().toString();
//
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
        String updatedJson = addIdToJson(json, jsonId);

        // 3、将全局配置ID，用户名，全局配置，全局配置名存入数据库
        JsonGlobalJsonEntity jsonGlobalJsonEntity = new JsonGlobalJsonEntity(jsonId,userName,updatedJson,xmlName, LocalDateTime.now());
        try {
            jsonGlobalJsonService.save(jsonGlobalJsonEntity);
        } catch (Exception e) {
            log.error("保存数据到数据库失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("保存数据到数据库失败");
        }



        // 将字符串转成json对象返回
        JsonGlobalJsonDTO jsonGlobalJsonDTO = new JsonGlobalJsonDTO(jsonGlobalJsonEntity.getId(),updatedJson);
        // 返回字节数组作为响应体，并设置响应头
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jsonGlobalJsonDTO);

    }}

    // 给组件添加UUID
    private static String addIdToJson(String json, String id) throws JsonProcessingException {
        // 1、创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        // 2、添加 "id" 字段并为其赋值
        ((ObjectNode) jsonNode).put("id", id);
        // 3、将修改后的 JsonNode 对象转换回 JSON 字符串
        String updatedJson = objectMapper.writeValueAsString(jsonNode);
        return updatedJson;
    }

    /**
     * 通过用户名查找所有的全局配置
     * @param request
     * @return
     */
    @GetMapping("/getGlobalConfigJson")
    public ResponseEntity<?> GetGlobalConfigjsonToJson(HttpServletRequest request)  {
//         通过request拿到token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if(jwtToken == null){
//
//            return ResponseEntity.status(404).body("没有令牌，无法解析出token") ;
//        }
//        // 通过token获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();
        // 根据用户名去数据库查询xml
        List<JsonGlobalJsonEntity> allJson = jsonGlobalJsonService.findAllByUserNameOrderByTime(userName);
        // 将查询到的xml通过数组的形式返回给前端
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(allJson);
    }

    /**
     * 通过userName查找所有的全局配置
     * @param request
     * @return
     */
    @GetMapping("/getGlobalConfigJsonByName")
    public ResponseEntity<?> GetGlobalConfigJsonByName(HttpServletRequest request , @RequestParam String globalName){
//        // 通过request拿到token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if(jwtToken == null){
//            return ResponseEntity.status(404).body("没有令牌，无法解析出token") ;
//        }
//        // 通过token获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        String userName = jwtTokenUtil.getAuthenticatedUsername();
        // 通过用户名和全局配置名来查找全局配置
        JsonGlobalJsonEntity jsonGlobalXml = jsonGlobalJsonService.findByUserNameAndGlobalName(userName, globalName);
        if(jsonGlobalXml == null){
            return ResponseEntity.ok(new HashMap<>()) ;

        }
        JsonGlobalJsonDTO jsonGlobalJsonDTO = new JsonGlobalJsonDTO(jsonGlobalXml.getId(), jsonGlobalXml.getGlobalJson());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jsonGlobalJsonDTO);
    }

    /**
     * 更新全局配置
     * @param request
     * @param json
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping("/updateGlobalConfigJson")
    public ResponseEntity<?> UpdateGlobalConfigJson(HttpServletRequest request ,@RequestBody String json) throws JsonProcessingException {
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
        String jsonId = jsonGlobalDTO.getId();
        JsonGlobalJsonDTO jsonGlobalJson = new JsonGlobalJsonDTO(jsonId, json);

        // 更新指定数据
        try {
            jsonGlobalJsonService.updateGlobalJsonAndGlobalNameByIdAndUserName(jsonId,userName,
                    json,jsonGlobalDTO.getName());
        } catch (Exception e) {
            log.info("更新失败");
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(jsonGlobalJson) ;
    }

    /**
     * 删除全局配置
     * @param request
     * @param id
     * @return
     */
    @DeleteMapping("/deleteGlobalConfigJson")
    public ResponseEntity<?> deleteGlobalConfigJsonByName(HttpServletRequest request, @RequestParam String id) {
//        // 通过 request 拿到 token
//        String jwtToken = jwtTokenUtil.getTokenFromRequest(request);
//        if (jwtToken == null) {
//            return ResponseEntity.status(404).body("没有令牌，无法解析出 token");
//        }
//
//        // 通过 token 获取到用户名
//        String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
        // 通过token得到username
        String userName = jwtTokenUtil.getAuthenticatedUsername();
        log.info("id: {} username: {}", id, userName);

        int deleteCount = jsonGlobalJsonService.deleteByIdAndUserName(id, userName);
        log.info("deleteByIdAndUserName: {}", deleteCount);

        if (deleteCount > 0) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("删除失败");
        }
    }

}
