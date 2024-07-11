package cn.attackme.muleproject.config;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FlowComponentUtil {
    public List<String> getChildId(Element element, Namespace docNamespace){
        List<String> childNodes = new ArrayList<>();
        List<Element> children = element.getChildren();
        for (Element child: children) {
            childNodes.add(child.getAttributeValue("id",docNamespace));
        }
        return childNodes;
    }

    public String extractStringFromSquare(String expression){
        String extracted = Optional.ofNullable(expression)
                .map(expr -> {
                    int startIndex = expr.indexOf('[');
                    int endIndex = expr.indexOf(']');
                    return (startIndex != -1 && endIndex != -1) ? expr.substring(startIndex + 1, endIndex) : expr;
                })
                .orElse(null);
        return extracted;
    }

    public String extractStringFromCurly(String expression){
        String extracted = Optional.ofNullable(expression)
                .map(expr -> {
                    int startIndex = expr.indexOf('{');
                    int endIndex = expr.indexOf('}');
                    return (startIndex != -1 && endIndex != -1) ? "{" + expr.substring(startIndex + 1, endIndex) + "}" : expr;
                })
                .orElse(null);
        return extracted;
    }

    public String getCDATA(Element child){
        String cdata = (child.getContent(0) != null && child.getContent(0).getCType().name().equals("CDATA")) ?
                child.getContent(0).getValue() :
                (child.getContent(0) != null && child.getContent().size() == 1 && child.getContent(0).getCType().name().equals("Text")) ?
                        child.getContent(0).getValue() :
                        (child.getContent(1) != null && child.getContent(1).getCType().name().equals("CDATA")) ?
                                child.getContent(1).getValue() :
                                null;
        return cdata;
    }

    public Map<String, Object> getSqlStatement(Element element, Map<String, Object> data){
        List<Element> children = element.getChildren();
        for (Element child: children) {
            if(child.getName().equals("sql")){
                String parameters = getCDATA(child);
                data.put("sqlCommand", extractStringFromSquare(parameters));
            }
            else if(child.getName().equals("input-parameters")){
                String parameters = getCDATA(child);
                data.put("inputParameters", extractStringFromSquare(parameters));
            }
        }
        return data;
    }


    public Map<String, Object> getRequestStatement(Element element, Map<String, Object> data){
        List<Element> children = element.getChildren();
        for (Element child: children) {
            String tagName = child.getName();
            switch (tagName) {
                case "body":
                    data.put("body",getCDATA(child));
                    break;
                case "headers":
                    data.put("headers",extractStringFromCurly(getCDATA(child)));
                    break;
                case "uri-params":
                    data.put("urlParameters",extractStringFromCurly(getCDATA(child)));
                    break;
                case "query-params":
                    data.put("queryParameters",extractStringFromCurly(getCDATA(child)));
                    break;
                default:
                    break;
            }
        }
        return data;
    }
}
