package cn.attackme.muleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "canvas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CanvasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "canvas_name",columnDefinition = "VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci")
    private String canvasName;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "canvas_json", columnDefinition = "LONGTEXT")
    private String canvasJson;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
