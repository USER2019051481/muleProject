package cn.attackme.muleproject.service;

import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;

import java.util.List;

public interface JsonGlobalXmlService {
    List<JsonGlobalXmlEntity> findAllByUserName(String userName);
    JsonGlobalXmlEntity findByUserNameAndGlobalName(String userName ,String globalName) ;
    int deleteByIdAndUserName(String id,String userName);
    void save(JsonGlobalXmlEntity jsonGlobalXmlEntity) ;
    void updateGlobalXmlAndGlobalNameByIdAndUserName(String id, String userName,String globalXml, String globalName);
}
