package cn.attackme.muleproject.dto;

import lombok.Data;

import java.util.Map;

@Data
public class JsonGlobalDTO {
    private String id;
    private String name;
    private String type ;
    private String host;
    private String port;
    // HTTP
    private String protocol;
    private String basePath;
    // database
    private String connection;
    private String username;
    private String password;
    private String database;




}
