package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.config.JwtTokenUtil;
import cn.attackme.muleproject.dto.CanvasDTO;
import cn.attackme.muleproject.service.CanvasService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/canvas")
public class CanvasController {
    @Autowired
    private CanvasService canvasService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/save")
    public ResponseEntity<?> saveCanvas(@RequestBody String canvasData) {
        try {
            String username = jwtTokenUtil.getAuthenticatedUsername();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(canvasData);
            String canvas = jsonNode.get("canvas").toString();
            String name = jsonNode.get("name").textValue();
            CanvasDTO canvasDTO = canvasService.convertToDTO(canvas, name, username);
            canvasService.saveCanvas(canvasDTO);
            return ResponseEntity.ok("画布保存成功");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少参数："+e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("画布保存失败"+e.getMessage());
        }
    }

    @GetMapping("get")
    public ResponseEntity<?> getHistoricalCanvases() {
        try {
            String username = jwtTokenUtil.getAuthenticatedUsername();
            List<Map<String, Object>> canvas = canvasService.getCanvases(username);
            return ResponseEntity.ok(canvas);
        }  catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取画布失败"+e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCanvasName(@RequestBody String canvasData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(canvasData);
            Long id = Long.valueOf(jsonNode.get("id").textValue());
            String name = jsonNode.get("name").textValue();
            canvasService.updateCanvasName(id, name);
            return ResponseEntity.ok("画布名更新成功");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少参数："+e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("画布名更新失败"+e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCanvas(@RequestBody String canvasData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(canvasData);
            Long id = Long.valueOf(jsonNode.get("id").textValue());
            canvasService.deleteCanvas(Long.valueOf(id));
            return ResponseEntity.ok("画布删除成功");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少参数："+e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("画布删除失败"+e.getMessage());
        }
    }
}
