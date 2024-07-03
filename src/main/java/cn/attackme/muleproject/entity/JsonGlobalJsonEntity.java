package cn.attackme.muleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jsonglobal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonGlobalJsonEntity {
    @Id
    private String id;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "global_json",length = 1024)
    private String globalJson ;
    @Column(name = "global_name")
    private String globalName ;
}
