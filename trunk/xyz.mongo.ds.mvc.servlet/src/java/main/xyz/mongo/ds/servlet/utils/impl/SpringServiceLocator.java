package xyz.mongo.ds.servlet.utils.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.servlet.utils.IServiceLocator;
import xyz.mongo.ds.servlet.view.IResultManager;

public class SpringServiceLocator implements IServiceLocator {
	private static final String[] APP_CONTEXT = new String[] { "app-xyz.xml" };
	private static final String MONGO_DATA_SOURCE="mongoDataSource";
	private static final String RESULT_MANAGER="resultManager";
	
	private ApplicationContext context;
	
	public SpringServiceLocator(){
		context=new ClassPathXmlApplicationContext(APP_CONTEXT);
	}
	
	@Override
	public IResultManager getReulstManager() {
		return (IResultManager)context.getBean(RESULT_MANAGER);
	}

	@Override
	public IMongoDataSource getMongoDataSource() {
		return (IMongoDataSource)context.getBean(MONGO_DATA_SOURCE);
	}

	@Override
	public Object get(String name) {
		return context.getBean(name);
	}
    
}
