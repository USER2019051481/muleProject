package cn.attackme.muleproject.dto;

import cn.attackme.muleproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanvasDTO {
    private Long id;

    private String canvasName;

    private Date createTime;

    private String canvasJson;

    private User user;
}
