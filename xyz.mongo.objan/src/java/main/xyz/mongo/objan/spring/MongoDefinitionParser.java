package xyz.mongo.objan.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import xyz.mongo.objan.exec.MongoAbstract;

public class MongoDefinitionParser extends AbstractSingleBeanDefinitionParser{
	
     public MongoDefinitionParser(){   	
     }
     
     protected Class getBeanClass(Element element) {
         return MongoAbstract.class; 
      }

      protected void doParse(Element element, BeanDefinitionBuilder bean) {
         String targetClass = element.getAttribute("type");
         if (StringUtils.hasText(targetClass)) {
             bean.addPropertyValue("targetClass", targetClass);
         }
         String targetProperties = element.getAttribute("targetProperties");
         if (StringUtils.hasText(targetProperties)) {
        	 bean.addPropertyReference("targetProperties", targetProperties);
         }
     
      }
}
