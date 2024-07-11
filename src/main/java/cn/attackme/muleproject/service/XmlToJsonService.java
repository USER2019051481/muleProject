package cn.attackme.muleproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface XmlToJsonService {
    String convertXmlToJson(MultipartFile file) throws Exception;
}
