package xyz.mongo.tcp.service.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xyz.mongo.IBizHandler;
import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.IMongoDataSourceManager;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonDsBizHandler implements IBizHandler{
    private static final Log LOG=LogFactory.getLog(JsonDsBizHandler.class);
    
    private IMongoDataSource mds;
    
    public JsonDsBizHandler(){
    	String classpath=System.getProperty("springConfig","jsonDsHandler.xml");
    	ApplicationContext context=new ClassPathXmlApplicationContext(classpath);
    	IMongoDataSourceManager mongoDataSourceManager=(IMongoDataSourceManager)context.getBean("mongoDataSourceManager");
    	mds =mongoDataSourceManager.getMongoDataSource();
    }
    
	@Override
	public Object exec(Object... args) {
		String message=(String)args[0];
		DBObject dbobj=(DBObject)JSON.parse(message);
		checkDbObj(dbobj);
		String coll=(String)dbobj.get("col");
		String method=(String)dbobj.get("method");
		DBObject params=(DBObject)dbobj.get("params");
		//return mds.col(coll, method, params);
		return null;//如何处理例外，是一个难缠的问题
	}
	
	private void checkDbObj(DBObject dbobj){
		if(dbobj.containsField("col")){
			throw new IllegalArgumentException("args must has a col param");
		}
		if(dbobj.containsField("method")){
			throw new IllegalArgumentException("args must has a method param");
		}
		if(dbobj.containsField("params")){
			throw new IllegalArgumentException("args must has a params param");
		}
	}
    
    
}
