package cn.attackme.muleproject.service;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ComponentHandleService {

    private Namespace docNamespace = Namespace.getNamespace("doc", "http://www.mulesoft.org/schema/mule/documentation");
    private Namespace otherwiseName = Namespace.getNamespace("", "http://www.mulesoft.org/schema/mule/core");

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
        config.put("database", content.getAttributeValue("database"));
        config.put("driver", "目前缺少");
        String tagName = element.getName();
        switch (tagName) {
            case "mssql-connection":
                config.put("connection", "SQL Server");
                config.put("instanceName", content.getAttributeValue("instanceName"));
                break;
            case "my-sql-connection":
                config.put("connection", "MySQL");
                break;
            case "oracle-connection":
                config.put("connection", "Oracle");
                config.put("instanceName", content.getAttributeValue("instanceName"));
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

    public Map<String, Object> FlowConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        String elementType = "flow".equals(element.getName()) ? "Flow" : "sub-flow".equals(element.getName()) ? "SubFlow" : "Unknown";
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", elementType);
        data.put("displayName", element.getAttributeValue("name"));
        config.put("data",data);
        config.put("childNodes", getChildId(element));
        return config;
    }

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

    public Map<String, Object> LoggerConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Logger");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        data.put("message",element.getAttributeValue("message"));
        data.put("level",element.getAttributeValue("level"));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    public Map<String, Object> DatabaseConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Database");
        data.put("displayName", "Database");
        data.put("operation",element.getAttributeValue("name",docNamespace));
        data.put("connectorConfiguration",element.getAttributeValue("config-ref"));
        Element sqlElement = (Element) element.getContent(1);
        String sqlCommand = null;
        if (sqlElement != null) {
            sqlCommand = (sqlElement.getContent(0) != null && sqlElement.getContent(0).getCType().name()=="CDATA") ?
                    sqlElement.getContent(0).getValue() :
                    (sqlElement.getContent(1) != null && sqlElement.getContent(1).getCType().name()=="CDATA") ?
                            sqlElement.getContent(1).getValue() :
                            null;}
        data.put("sqlCommand", sqlCommand);
        data.put("inputParameters","目前缺少");
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        return config;
    }

    public Map<String, Object> ForEachConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "ForEach");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        config.put("childNodes", getChildId(element));
        return config;
    }

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

    public Map<String, Object> ChoiceConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "Choice");
        data.put("displayName", element.getAttributeValue("name",docNamespace));
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        Element otherwise = element.getChild("otherwise",otherwiseName);
        if (otherwise != null) {
            config.put("defaultNode", otherwise.getAttributeValue("id", docNamespace));
        } else {
            config.put("defaultNode", "null"); // 例如设置一个默认值
        }
        config.put("childNodes", getChildId(element));
        return config;
    }

    public Map<String, Object> ChoiceDefaultConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "ChoiceDefault");
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        config.put("childNodes", getChildId(element));
        return config;
    }

    public Map<String, Object> ChoiceWhenConfig(Element element) {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        config.put("id", element.getAttributeValue("id",docNamespace));
        config.put("type", "ChoiceWhen");
        String expression = element.getAttributeValue("expression");
        String extracted = Optional.ofNullable(expression)
                .map(expr -> {
                    int startIndex = expr.indexOf('[');
                    int endIndex = expr.indexOf(']');
                    return (startIndex != -1 && endIndex != -1) ? expr.substring(startIndex + 1, endIndex) : null;
                })
                .orElse(null);

        data.put("expression", extracted);
        config.put("data",data);
        config.put("parentNode",element.getParentElement().getAttributeValue("id",docNamespace));
        config.put("childNodes", getChildId(element));
        return config;
    }

    public List<String> getChildId(Element element){
        List<String> childNodes = new ArrayList<>();
        List<Element> children = element.getChildren();
        for (Element child: children) {
            childNodes.add(child.getAttributeValue("id",docNamespace));
        }
        return childNodes;
    }
}
