package cn.attackme.muleproject.service.serviceImpl;

import cn.attackme.muleproject.service.ComponentHandleService;
import cn.attackme.muleproject.service.XmlToJsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

@Service
public class XmlToJsonServiceImpl implements XmlToJsonService {

    @Autowired
    private ComponentHandleService componentHandleService;

    private Namespace docNamespace = Namespace.getNamespace("doc", "http://www.mulesoft.org/schema/mule/documentation");
    private Namespace otherwiseName = Namespace.getNamespace("", "http://www.mulesoft.org/schema/mule/core");

    public String convertXmlToJson(MultipartFile file) throws Exception {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> globalConfigsArray = new ArrayList<>();
        List<Element> flowElements = new ArrayList<>();
        //预处理xml
        InputStream inputStream = processXmlFile(file) ;
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputStream);
        Element rootElement = document.getRootElement();

        processElement(rootElement,  flowElements, globalConfigsArray, nodes);

        Map<String, Object> result = new HashMap<>();
        result.put("globalConfigs", globalConfigsArray);
        result.put("nodes", nodes);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }

    //递归处理flow及其子结点
    public void processElement(Element element, List<Element> flowElements, List<Map<String, Object>> globalConfigsArray,
                               List<Map<String, Object>> nodes) {
        String tagName = element.getName();
        switch (tagName) {
            case "config":
                globalConfigsArray.add(componentHandleService.DbGlobalConfig(element));
                break;
            case "listener-config":
                globalConfigsArray.add(componentHandleService.HttpListenerGlobalConfig(element));
                break;
            case "flow":
            case "sub-flow":
                flowElements.add(element);
                nodes.add(componentHandleService.FlowConfig(element));
                break;
            case "listener":
                nodes.add(componentHandleService.ListenerConfig(element));
                break;
            case "when":
                nodes.add(componentHandleService.ChoiceWhenConfig(element));
                break;
            case "otherwise":
                nodes.add(componentHandleService.ChoiceDefaultConfig(element));
                break;
            case "choice":
                nodes.add(componentHandleService.ChoiceConfig(element));
                break;
            case "flow-ref":
                nodes.add(componentHandleService.FlowReferenceConfig(element));
                break;
            case "select":
            case "delete":
            case "insert":
            case "update":
                nodes.add(componentHandleService.DatabaseConfig(element));
                break;
            case "foreach":
                nodes.add(componentHandleService.ForEachConfig(element));
                break;
            case "logger":
                nodes.add(componentHandleService.LoggerConfig(element));
                break;
            default:
                break;
        }
        for (Element child : element.getChildren()) {
            processElement(child, flowElements, globalConfigsArray, nodes);
        }
    }

    public InputStream processXmlFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputStream);
        Element rootElement = document.getRootElement();

        // 递归处理每个节点
        preProcessXml(rootElement);

        // 输出修改后的XML文件到内存中的ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(document, outputStream);

        // 将ByteArrayOutputStream转换为InputStream
        byte[] xmlBytes = outputStream.toByteArray();
        InputStream modifiedInputStream = new ByteArrayInputStream(xmlBytes);

        return modifiedInputStream;
    }

    public void preProcessXml(Element element) {
        //添加otherwise
        if ("choice".equals(element.getName())) {
            boolean hasOtherwise = false;
            List<Element> children = element.getChildren();
            for (Element child : children) {
                if ("otherwise".equals(child.getName())) {
                    hasOtherwise = true;
                    break;
                }
            }
            if (!hasOtherwise) {
                // 创建并添加新的 ChoiceDefault 元素
                Element choiceDefault = new Element("otherwise",otherwiseName);
                element.addContent(choiceDefault);
            }
        }
        if ("when".equals(element.getName()) || "otherwise".equals(element.getName())) {
            if (element.getAttribute("id",docNamespace) == null) {
                element.setAttribute("id", UUID.randomUUID().toString(),docNamespace);
            }
        }
        // 递归处理子节点
        List<Element> children = element.getChildren();
        for (Element child : children) {
            preProcessXml(child);
        }
    }
}
