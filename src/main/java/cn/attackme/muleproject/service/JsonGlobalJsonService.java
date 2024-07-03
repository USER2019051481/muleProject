package cn.attackme.muleproject.service;

import cn.attackme.muleproject.entity.JsonGlobalJsonEntity;
import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;

import java.util.List;

public interface JsonGlobalJsonService {
    List<JsonGlobalJsonEntity> findAllByUserName(String userName);
    JsonGlobalJsonEntity findByUserNameAndGlobalName(String userName ,String globalName) ;
    int deleteByIdAndUserName(String id,String userName);
    void save(JsonGlobalJsonEntity jsonGlobalJsonEntity) ;
    void updateGlobalJsonAndGlobalNameByIdAndUserName(String id, String userName,String globalJson, String globalName);
}
