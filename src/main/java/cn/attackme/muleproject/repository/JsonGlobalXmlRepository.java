package cn.attackme.muleproject.repository;

import cn.attackme.muleproject.dto.JsonGlobalXmlDTO;
import cn.attackme.muleproject.entity.CanvasEntity;
import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springfox.documentation.spring.web.json.Json;

import java.util.List;


@Repository
public interface JsonGlobalXmlRepository extends JpaRepository<JsonGlobalXmlEntity, String> {

    List<JsonGlobalXmlEntity> findAllByUserName(String userName);
    JsonGlobalXmlEntity findByUserNameAndGlobalName(String userName ,String globalName) ;
    int deleteByIdAndUserName(String id, String userName);
    @Modifying
    @Query("UPDATE JsonGlobalXmlEntity e SET e.globalXml = :globalXml, e.globalName = :globalName WHERE e.id = :id AND e.userName=:userName")
    void updateGlobalXmlAndGlobalNameByIdAndUserName(String id, String userName,String globalXml, String globalName);
}
