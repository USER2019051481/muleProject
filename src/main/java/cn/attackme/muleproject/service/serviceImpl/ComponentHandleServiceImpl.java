package cn.attackme.muleproject.service.serviceImpl;
import cn.attackme.muleproject.config.FlowComponentUtil;
import cn.attackme.muleproject.service.ComponentHandleService;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ComponentHandleServiceImpl implements ComponentHandleService {

    private Namespace docNamespace = Namespace.getNamespace("doc", "http://www.mulesoft.org/schema/mule/documentation");
    private Namespace otherwiseName = Namespace.getNamespace("", "http://www.mulesoft.org/schema/mule/core");

    @Autowired
    private FlowComponentUtil flowComponentUtil;

    @Override
    public Map<String, Object> DbGlobalConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("name", element.getAttributeValue("name"));
        config.put("type", "Database");
        Element content = (Element) element.getContent().get(1);
        config.put("host", content.getAttributeValue("host"));
        config.put("port", content.getAttributeValue("port"));
        config.put("username", content.getAttributeValue("user"));
        config.put("password", content.getAttributeValue("password"));
        config.put("driver", content.getAttributeValue("driver"));
        String tagName = content.getName();
        switch (tagName) {
            case "mssql-connection":
                config.put("connection", "SQL Server");
                config.put("instanceName", content.getAttributeValue("instanceName"));
                config.put("database", content.getAttributeValue("databaseName"));
                break;
            case "my-sql-connection":
                config.put("connection", "MySQL");
                config.put("database", content.getAttributeValue("database"));
                break;
            case "oracle-connection":
                config.put("connection", "Oracle");
                config.put("instanceName", content.getAttributeValue("instance"));
                config.put("serviceName", content.getAttributeValue("serviceName"));
                break;
            case "postgre-sql-connection":
                config.put("connection", "PostgreSQL");
                break;
            default:
                break;
        }
        return config;
    }

    @Override
    public Map<String, Object> HttpListenerGlobalConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("name", element.getAttributeValue("name"));
        config.put("type", "Listener");
        String protocol = "HTTP Listener config".equals(element.getAttributeValue("name",docNamespace)) ? "HTTP" : "HTTPS Listener config".equals(element.getAttributeValue("name",docNamespace)) ? "HTTPS" : "Unknown";
        config.put("protocol", protocol);
        Element content = (Element) element.getContent().get(1);
        config.put("host", content.getAttributeValue("host"));
        config.put("port", content.getAttributeValue("port"));
        config.put("basePath", element.getAttributeValue("basePath"));

        return config;
    }

    @Override
    public Map<String, Object> HttpRequestGlobalConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("name", element.getAttributeValue("name"));
        config.put("type", "Request");
        Element content = (Element) element.getContent().get(1);
        config.put("protocol", content.getAttributeValue("protocol"));
        config.put("host", content.getAttributeValue("host"));
        config.put("port", content.getAttributeValue("port"));
        config.put("basePath", element.getAttributeValue("basePath"));
        return config;
    }

    @Override
    public Map<String, Object> FlowConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        String elementType = "flow".equals(element.getName()) ? "Flow" : "sub-flow".equals(element.getName()) ? "SubFlow" : "Unknown";
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", elementType);
        data.put("displayName", element.getAttributeValue("name"));
        config.put("data",data);
        config.put("childNodes", flowComponentUtil.getChildId(element,docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> ListenerConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Listener");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        data.put("connectorConfiguration",element.getAttributeValue("config-ref"));
        data.put("path",element.getAttributeValue("path"));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> RequestConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Request");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        data.put("connectorConfiguration",element.getAttributeValue("config-ref"));
        data.put("method",element.getAttributeValue("method"));
        data.put("path",element.getAttributeValue("path"));
        data.put("url",element.getAttributeValue("url"));
        flowComponentUtil.getRequestStatement(element,data);
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> LoggerConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Logger");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        data.put("message",flowComponentUtil.extractStringFromSquare(element.getAttributeValue("message")));
        data.put("level",element.getAttributeValue("level"));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> DatabaseConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Database");
        data.put("displayName", "Database");
        data.put("operation",element.getAttributeValue("name",docNamespace));
        data.put("connectorConfiguration",element.getAttributeValue("config-ref"));
        flowComponentUtil.getSqlStatement(element, data);
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> ForEachConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "ForEach");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        config.put("childNodes", flowComponentUtil.getChildId(element,docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> FlowReferenceConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "FlowReference");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        data.put("flowName",element.getAttributeValue("name"));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> ChoiceConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Choice");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        Element otherwise = element.getChild("otherwise",otherwiseName);
//        if (otherwise != null) {
//            config.put("defaultNode", otherwise.getAttributeValue("id", docNamespace));
//        } else {
//            config.put("defaultNode", "null"); // 例如设置一个默认值
//        }
        config.put("defaultNode", otherwise.getAttributeValue("id", docNamespace));
        config.put("childNodes", flowComponentUtil.getChildId(element,docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> ChoiceDefaultConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "ChoiceDefault");
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        config.put("childNodes", flowComponentUtil.getChildId(element,docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> ChoiceWhenConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "ChoiceWhen");
        String extracted = flowComponentUtil.extractStringFromSquare(element.getAttributeValue("expression"));
        data.put("expression", extracted);
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        config.put("childNodes", flowComponentUtil.getChildId(element,docNamespace));
        return config;
    }

    @Override
    public Map<String, Object> SetPayloadConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "SetPayload");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        data.put("payloadValue",flowComponentUtil.extractStringFromSquare(element.getAttributeValue("value")));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

}
