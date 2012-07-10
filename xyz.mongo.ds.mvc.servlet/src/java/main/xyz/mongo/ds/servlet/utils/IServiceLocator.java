package xyz.mongo.ds.servlet.utils;

import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.servlet.view.IResultManager;

public interface IServiceLocator {
	IResultManager getReulstManager();
	IMongoDataSource getMongoDataSource();
	Object get(String name);
}
