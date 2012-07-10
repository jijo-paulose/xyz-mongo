package xyz.mongo.ds.servlet.utils.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.servlet.utils.IServiceLocator;
import xyz.mongo.ds.servlet.view.IResult;
import xyz.mongo.ds.servlet.view.IResultManager;
import xyz.mongo.ds.servlet.view.impl.DefaultResultManager;
import xyz.mongo.ds.servlet.view.impl.DispatcherResult;
import xyz.mongo.ds.servlet.view.impl.FreeMarkerResult;
import xyz.mongo.ds.servlet.view.impl.JsonResult;
import xyz.mongo.ds.servlet.view.impl.JsonpResult;
import xyz.mongo.ds.servlet.view.impl.VelocityResult;
import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.ds.util.impl.CountColExecuter;
import xyz.mongo.ds.util.impl.DistinctColExecuter;
import xyz.mongo.ds.util.impl.ExistColExecuter;
import xyz.mongo.ds.util.impl.GroupColExecuter;
import xyz.mongo.ds.util.impl.InsertColExecuter;
import xyz.mongo.ds.util.impl.ListColExecuter;
import xyz.mongo.ds.util.impl.MultiStepColExecuter;
import xyz.mongo.ds.util.impl.OneColExecuter;
import xyz.mongo.ds.util.impl.PageColExecuter;
import xyz.mongo.ds.util.impl.RemoveColExecuter;
import xyz.mongo.ds.util.impl.SaveColExecuter;
import xyz.mongo.ds.util.impl.SonColExecuter;
import xyz.mongo.ds.util.impl.TopColExecuter;
import xyz.mongo.ds.util.impl.TransValueColExecuter;
import xyz.mongo.ds.util.impl.UpdateColExecuter;

import com.mongodb.Mongo;

public class DefaultServiceLocator implements IServiceLocator {
	private static final Log LOG=LogFactory.getLog(DefaultServiceLocator.class);
	
	private DefaultResultManager resultManager;
	private MongoDataSourceImpl mongoDataSource;
	public void addResult(String type,IResult result){
		resultManager.addResult(type, result);
	}
	
	public void addCollExecuter(String name, ICollectionExecuter executer) {
		mongoDataSource.addCollExecuter(name, executer);
	}
	
	public DefaultServiceLocator(){
		resultManager=new DefaultResultManager();
		resultManager.addResult("jsp", new DispatcherResult());
		resultManager.addResult("ftl", new FreeMarkerResult());
		resultManager.addResult("vm", new VelocityResult());
		resultManager.addResult("json", new JsonResult());
		resultManager.addResult("jsonp", new JsonpResult());
		
		mongoDataSource=new MongoDataSourceImpl();
		
		//mongo
		String host=System.getProperty("mongo-host", "localhost");
		String port=System.getProperty("mongo-port", "27017");
		String dbName=System.getProperty("mongo-db", "jmwpt");
		int portInt=Integer.parseInt(port);
		try {
			Mongo mongo=new Mongo(host,portInt);
			mongoDataSource.setMongo(mongo);
		} catch (Throwable e) {
			LOG.error("error while get mongo", e);
		} 
		mongoDataSource.setDbName(dbName);
		Map<String,ICollectionExecuter> cmap=new HashMap();
		cmap.put("count", new CountColExecuter());
		cmap.put("distinct", new DistinctColExecuter());
		cmap.put("exist", new ExistColExecuter());
		cmap.put("group", new GroupColExecuter());
		cmap.put("insert", new InsertColExecuter());
		cmap.put("list", new ListColExecuter());
		cmap.put("multi", new MultiStepColExecuter());
		cmap.put("one", new OneColExecuter());
		cmap.put("page", new PageColExecuter());
		cmap.put("remove", new RemoveColExecuter());
		cmap.put("save", new SaveColExecuter());
		cmap.put("son", new SonColExecuter());
		cmap.put("top", new TopColExecuter());
		cmap.put("trans", new TransValueColExecuter());
		cmap.put("update", new UpdateColExecuter());
		mongoDataSource.setCollExecuters(cmap);
	}

	@Override
	public IResultManager getReulstManager() {
		return resultManager;
	}

	@Override
	public IMongoDataSource getMongoDataSource() {
		return mongoDataSource;
	}

	@Override
	public Object get(String name) {
		return resultManager.get(name,null);
	}
    
}
