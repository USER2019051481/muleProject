package cn.attackme.muleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "globalxml")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonGlobalXmlEntity {
    @Id
    private String id;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "global_xml")
    private String globalXml ;
    @Column(name = "global_name")
    private String globalName ;



}
