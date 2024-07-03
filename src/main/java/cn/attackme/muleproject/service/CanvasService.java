package cn.attackme.muleproject.service;

import cn.attackme.muleproject.config.JwtTokenUtil;
import cn.attackme.muleproject.dto.CanvasDTO;
import cn.attackme.muleproject.entity.CanvasEntity;
import cn.attackme.muleproject.entity.User;
import cn.attackme.muleproject.repository.CanvasRepository;
import cn.attackme.muleproject.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class CanvasService {
    @Autowired
    private CanvasRepository canvasRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Map<String, Object> saveCanvas(CanvasDTO canvasDTO, String username) throws JsonProcessingException {
        Map<String, Object> canvasMap = new HashMap<>();
        Long userId = userRepository.findByUsername(username).getId();
        CanvasEntity savedvanvas = canvasRepository.save(convertToCanvas(canvasDTO));
        canvasMap.put("id", savedvanvas.getId());
        canvasMap.put("name", savedvanvas.getCanvasName());
        Object json = new ObjectMapper().readValue(savedvanvas.getCanvasJson(), Object.class);
        canvasMap.put("canvas", json);
        return canvasMap;
    }

    public List<Map<String, Object>> getCanvases(String username) throws JsonProcessingException {
        Long userId = userRepository.findByUsername(username).getId();
        List<CanvasEntity> canvases = canvasRepository.findByUserId(userId);

        List<Map<String, Object>> canvasList = new ArrayList<>();
        for (CanvasEntity canvas : canvases) {
            Map<String, Object> canvasMap = new HashMap<>();
            canvasMap.put("id", canvas.getId());
            canvasMap.put("name", canvas.getCanvasName());
            Object json = new ObjectMapper().readValue(canvas.getCanvasJson(), Object.class);
            canvasMap.put("canvas", json);
            canvasList.add(canvasMap);
        }
        return canvasList;
    }

    public Map<String, Object> getCanvasesByID(Long id) throws JsonProcessingException {
        Optional<CanvasEntity> canvasOptional = canvasRepository.findById(id);
        if (!canvasOptional.isPresent()) {
            throw new RuntimeException("该画布不存在");
        }
        CanvasEntity canvas = canvasOptional.get();
        Map<String, Object> canvasMap = new HashMap<>();
        canvasMap.put("id", canvas.getId());
        canvasMap.put("name", canvas.getCanvasName());
        Object json = new ObjectMapper().readValue(canvas.getCanvasJson(), Object.class);
        canvasMap.put("canvas", json);
        return canvasMap;
    }

    public void updateCanvasName(Long id, String newName) {
        String username = jwtTokenUtil.getAuthenticatedUsername();
//        Long userId = userRepository.findByUsername(username).getId();
//        String oldName = canvasRepository.findCanvasNameById(id);
        if (!canvasRepository.existsById(id)) {
            throw new IllegalArgumentException("画布不存在");
        }
        else {
            canvasRepository.updateNameById(id, newName);
        }
    }

    public void updateCanvas(Long id, String newCanvas) {
        if (!canvasRepository.existsById(id)) {
            throw new IllegalArgumentException("画布不存在");
        }
        canvasRepository.updateCanvasJsonById(id, newCanvas);
    }

    public void deleteCanvas(Long id){
        canvasRepository.deleteById(id);
    }

    public CanvasDTO convertToDTO(String canvasJson, String canvasName, String username){
        User user = userRepository.findByUsername(username);
        CanvasDTO  canvasDTO = new CanvasDTO();
        canvasDTO.setCanvasJson(canvasJson);
        canvasDTO.setCanvasName(canvasName);
        canvasDTO.setUser(user);
        canvasDTO.setCreateTime(new Date());
        return canvasDTO;
    }

    public CanvasEntity convertToCanvas(CanvasDTO canvasDTO) {
        CanvasEntity canvasEntity = new CanvasEntity();
        canvasEntity.setCanvasName(canvasDTO.getCanvasName());
        canvasEntity.setCanvasJson(canvasDTO.getCanvasJson());
        canvasEntity.setUser(canvasDTO.getUser());
        canvasEntity.setCreateTime(canvasDTO.getCreateTime());
        return canvasEntity;
    }

}
