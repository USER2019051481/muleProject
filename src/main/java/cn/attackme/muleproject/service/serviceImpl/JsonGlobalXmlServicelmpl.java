package cn.attackme.muleproject.service.serviceImpl;

import cn.attackme.muleproject.entity.JsonGlobalXmlEntity;
import cn.attackme.muleproject.repository.JsonGlobalXmlRepository;
import cn.attackme.muleproject.service.JsonGlobalXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JsonGlobalXmlServicelmpl implements JsonGlobalXmlService {
    @Autowired
    private JsonGlobalXmlRepository jsonGlobalXmlRepository ;
    @Override
    public List<JsonGlobalXmlEntity> findAllByUserName(String userName) {
        return jsonGlobalXmlRepository.findAllByUserName(userName);
    }

    @Override
    public JsonGlobalXmlEntity findByUserNameAndGlobalName(String userName, String globalName) {
        return jsonGlobalXmlRepository.findByUserNameAndGlobalName(userName,globalName);
    }

    @Transactional
    @Override
    public int deleteByIdAndUserName(String id, String userName) {
        return jsonGlobalXmlRepository.deleteByIdAndUserName(id, userName);
    }

    @Override
    public void save(JsonGlobalXmlEntity jsonGlobalXmlEntity) {
        jsonGlobalXmlRepository.save(jsonGlobalXmlEntity) ;
    }

    @Override
    @Transactional
    public void updateGlobalXmlAndGlobalNameByIdAndUserName(String id,String userName, String globalXml, String globalName) {
        jsonGlobalXmlRepository.updateGlobalXmlAndGlobalNameByIdAndUserName(id,userName,globalXml,globalName);
    }
}
