package xyz.mongo.ds.servlet.utils.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.impl.DefaultMongoDataSourceManager;
import xyz.mongo.ds.servlet.utils.IServiceLocator;
import xyz.mongo.ds.servlet.view.IResult;
import xyz.mongo.ds.servlet.view.IResultManager;
import xyz.mongo.ds.servlet.view.impl.DefaultResultManager;
import xyz.mongo.ds.servlet.view.impl.DispatcherResult;
import xyz.mongo.ds.servlet.view.impl.FreeMarkerResult;
import xyz.mongo.ds.servlet.view.impl.JsonResult;
import xyz.mongo.ds.servlet.view.impl.JsonpResult;
import xyz.mongo.ds.servlet.view.impl.VelocityResult;

public class DefaultServiceLocator extends DefaultMongoDataSourceManager implements IServiceLocator {
	private static final Log LOG=LogFactory.getLog(DefaultServiceLocator.class);
	
	private DefaultResultManager resultManager;
	//private MongoDataSourceImpl mongoDataSource;
	public void addResult(String type,IResult result){
		resultManager.addResult(type, result);
	}
	

	
	public DefaultServiceLocator(){
		super();
		resultManager=new DefaultResultManager();
		resultManager.addResult("jsp", new DispatcherResult());
		resultManager.addResult("ftl", new FreeMarkerResult());
		resultManager.addResult("vm", new VelocityResult());
		resultManager.addResult("json", new JsonResult());
		resultManager.addResult("jsonp", new JsonpResult());

	}

	@Override
	public IResultManager getReulstManager() {
		return resultManager;
	}



	@Override
	public Object get(String name) {
		return resultManager.get(name,null);
	}
    
}
