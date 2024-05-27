package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.config.JwtTokenUtil;
import cn.attackme.muleproject.dto.CanvasDTO;
import cn.attackme.muleproject.service.CanvasService;
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
    public ResponseEntity<?> saveCanvas(@RequestParam String canvas, @RequestParam String name) {
        try {
            String username = jwtTokenUtil.getAuthenticatedUsername();
            CanvasDTO canvasDTO = canvasService.convertToDTO(canvas, name, username);
            canvasService.saveCanvas(canvasDTO);
            return ResponseEntity.ok("画布保存成功");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("画布保存失败");
        }

    }

    @GetMapping("get")
    public ResponseEntity<?> getHistoricalCanvases() {
        try {
            String username = jwtTokenUtil.getAuthenticatedUsername();
            List<Map<String, Object>> canvas = canvasService.getCanvases(username);
            return ResponseEntity.ok(canvas);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取画布失败");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCanvasName(@RequestParam Long id, @RequestParam String name) {
        try {
            canvasService.updateCanvasName(id, name);
            return ResponseEntity.ok("画布名更新成功");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("画布名更新失败");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCanvas(@RequestParam Long id) {
        try {
            canvasService.deleteCanvas(id);
            return ResponseEntity.ok("画布删除成功");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("画布删除失败");
        }
    }
}
