package cn.attackme.muleproject.service;

import cn.attackme.muleproject.dto.CanvasDTO;
import cn.attackme.muleproject.entity.CanvasEntity;
import cn.attackme.muleproject.entity.User;
import cn.attackme.muleproject.repository.CanvasRepository;
import cn.attackme.muleproject.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class CanvasService {
    @Autowired
    private CanvasRepository canvasRepository;
    @Autowired
    private UserRepository userRepository;

    public void saveCanvas(CanvasDTO canvasDTO) {
        canvasRepository.save(convertToCanvas(canvasDTO));
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

    public void updateCanvasName(Long id, String newName) {
        canvasRepository.updateCanvasNameById(id, newName);
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
