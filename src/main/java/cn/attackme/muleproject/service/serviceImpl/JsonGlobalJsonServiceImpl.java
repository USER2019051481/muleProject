package cn.attackme.muleproject.service.serviceImpl;

import cn.attackme.muleproject.entity.JsonGlobalJsonEntity;
import cn.attackme.muleproject.repository.JsonGlobalJsonRepository;
import cn.attackme.muleproject.service.JsonGlobalJsonService;
import cn.attackme.muleproject.service.JsonGlobalXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JsonGlobalJsonServiceImpl implements JsonGlobalJsonService {

    @Resource
    private JsonGlobalJsonRepository jsonGlobalJsonRepository ;
    @Override
    public List<JsonGlobalJsonEntity> findAllByUserName(String userName) {
        return jsonGlobalJsonRepository.findAllByUserName(userName);
    }

    @Override
    public JsonGlobalJsonEntity findByUserNameAndGlobalName(String userName, String globalName) {
        return jsonGlobalJsonRepository.findByUserNameAndGlobalName(userName,globalName);
    }

    @Override
    @Transactional
    public int deleteByIdAndUserName(String id, String userName) {
        return jsonGlobalJsonRepository.deleteByIdAndUserName(id,userName);
    }

    @Override
    @Transactional
    public void save(JsonGlobalJsonEntity jsonGlobalJsonEntity) {
        jsonGlobalJsonRepository.save(jsonGlobalJsonEntity) ;
    }

    @Override
    @Transactional
    public void updateGlobalJsonAndGlobalNameByIdAndUserName(String id, String userName, String globalJson, String globalName) {
        jsonGlobalJsonRepository.updateGlobalJsonAndGlobalNameByIdAndUserName(id,userName,globalJson,globalName);
    }
}
