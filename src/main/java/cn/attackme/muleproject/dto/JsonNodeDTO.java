package cn.attackme.muleproject.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class JsonNodeDTO {
    private String id ;
    private String type;
    private Map<String, Object> data; // 更改 data 字段的类型为 Map<String, Object>
    private String parentNode ;
    private List<String> childNodes ;
    private String defaultNode ;
}
