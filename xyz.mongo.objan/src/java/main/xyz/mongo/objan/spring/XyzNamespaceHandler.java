package xyz.mongo.objan.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class XyzNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		 registerBeanDefinitionParser("mongo", new MongoDefinitionParser()); 
	}

}
