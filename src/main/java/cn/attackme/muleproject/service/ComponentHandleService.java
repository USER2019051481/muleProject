package cn.attackme.muleproject.service;

import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

public interface ComponentHandleService {
     Map<String, Object> DbGlobalConfig(Element element);
     Map<String, Object> HttpListenerGlobalConfig(Element element);
     Map<String, Object> HttpRequestGlobalConfig(Element element);
     Map<String, Object> FlowConfig(Element element);
     Map<String, Object> ListenerConfig(Element element);
     Map<String, Object> RequestConfig(Element element);
     Map<String, Object> LoggerConfig(Element element);
     Map<String, Object> DatabaseConfig(Element element);
     Map<String, Object> ForEachConfig(Element element);
     Map<String, Object> FlowReferenceConfig(Element element);
     Map<String, Object> ChoiceConfig(Element element);
     Map<String, Object> ChoiceDefaultConfig(Element element);
     Map<String, Object> ChoiceWhenConfig(Element element);
     Map<String, Object> SetPayloadConfig(Element element);
}
