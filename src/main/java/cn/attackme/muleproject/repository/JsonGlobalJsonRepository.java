package cn.attackme.muleproject.repository;

import cn.attackme.muleproject.entity.JsonGlobalJsonEntity;
import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JsonGlobalJsonRepository  extends JpaRepository<JsonGlobalJsonEntity, String> {
    List<JsonGlobalJsonEntity> findAllByUserName(String userName);

    JsonGlobalJsonEntity findByUserNameAndGlobalName(String userName ,String globalName) ;

    int deleteByIdAndUserName(String id, String userName);

    @Modifying
    @Query("UPDATE JsonGlobalJsonEntity e SET e.globalJson = :globalJson, e.globalName = :globalName WHERE e.id = :id AND e.userName=:userName")
    void updateGlobalJsonAndGlobalNameByIdAndUserName(String id, String userName,String globalJson, String globalName);
}
