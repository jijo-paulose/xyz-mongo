package xyz.mongo.ds;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.util.impl.JsonColsResource;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public abstract class AbstractColTest {
	 protected IMongoDataSource service;
	 protected Mongo mongo;
	 protected DB db;
	 
     protected String getDbName(){
    	 return "jmwpt";
     }
     
     protected List<String> getColNames(){
    	 List<String> list=new ArrayList<String>();
    	 list.add("foo");
    	 return list;
     }
     
     protected abstract void insertExecuter(MongoDataSourceImpl service);
     
     @Before
     public void setUp()throws Exception{
    	service = new MongoDataSourceImpl();
 		mongo = new Mongo();
 		db=mongo.getDB(getDbName());
 		MongoDataSourceImpl sservice=(MongoDataSourceImpl)service;
 		sservice.setMongo(mongo);
 		sservice.setDbName(getDbName());
        insertExecuter(sservice);
 		JsonColsResource colResource = new JsonColsResource();
 		colResource.setCheck(false);
 		colResource.setJsonDir("/db");
 		sservice.setCollectionsResource(colResource);
 		//drop col and create empty
 		for(String name:getColNames()){
 			if(db.collectionExists(name)){
 				DBCollection col=db.getCollection(name);
 				col.drop();
 			}
 			db.createCollection(name, new BasicDBObject());
 		}
     }
     
     @After
     public void tearDown()throws Exception{
    	//drop col
  		for(String name:getColNames()){
  			if(db.collectionExists(name)){
  				DBCollection col=db.getCollection(name);
  				col.drop();
  			}
  		}
     }
}
