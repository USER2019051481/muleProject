package cn.attackme.muleproject.service;

import cn.attackme.muleproject.dto.CanvasDTO;
import cn.attackme.muleproject.entity.CanvasEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface CanvasService {
    Map<String, Object> saveCanvas(CanvasDTO canvasDTO, String username) throws JsonProcessingException;
    List<Map<String, Object>> getCanvases(String username) throws JsonProcessingException;
    Map<String, Object> getCanvasesByID(Long id) throws JsonProcessingException;
    void updateCanvasName(Long id, String newName);
    void updateCanvas(Long id, String newCanvas);
    void deleteCanvas(Long id);
    CanvasDTO convertToDTO(String canvasJson, String canvasName, String username);
    CanvasEntity convertToCanvas(CanvasDTO canvasDTO);
}
