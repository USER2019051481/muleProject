package cn.attackme.muleproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class JsonGraphDTO {
    private List<JsonGlobalDTO> globalConfig ;
    private List<JsonNodeDTO> nodes ;
    private List<JsonEdgeDTO> edges ;

}
