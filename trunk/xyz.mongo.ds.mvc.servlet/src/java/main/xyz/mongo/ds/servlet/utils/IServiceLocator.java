package xyz.mongo.ds.servlet.utils;

import xyz.mongo.ds.IMongoDataSourceManager;
import xyz.mongo.ds.servlet.view.IResultManager;

public interface IServiceLocator extends IMongoDataSourceManager{
	IResultManager getReulstManager();
	Object get(String name);
}
