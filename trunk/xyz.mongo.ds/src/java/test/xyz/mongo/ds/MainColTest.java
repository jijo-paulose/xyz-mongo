package xyz.mongo.ds;

import org.junit.Test;

import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.util.impl.CountColExecuter;
import xyz.mongo.ds.util.impl.JsonColsResource;
import xyz.mongo.ds.util.impl.ListColExecuter;
import xyz.mongo.ds.util.impl.MultiStepColExecuter;
import xyz.mongo.ds.util.impl.OneColExecuter;
import xyz.mongo.ds.util.impl.PageColExecuter;
import xyz.mongo.ds.util.impl.SonColExecuter;
import xyz.mongo.ds.util.impl.TransValueColExecuter;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MainColTest {
	private String[] methods = new String[] { "allsortpage","allsort","all", "byId", "allCount",
			"byIdHhh" };
	private String jsonDir = "/db";
	
	@Test
	public void transtest()throws Exception {
		IMongoDataSource service = getIMongoDataSource();
		DBObject params=(DBObject)JSON.parse("{id:'asdf'}");
		Object res = service.col("foo", "trans",params );
		System.out.println(res.toString());
	}
	
	@Test
	public void jointest()throws Exception {
		IMongoDataSource service = getIMongoDataSource();
		DBObject params=(DBObject)JSON.parse("{id:'asdf'}");
		Object res = service.col("foo", "join",params );
		System.out.println(res.toString());
	}
	
	@Test
	public void findByIdInBaseTable()throws Exception {
		IMongoDataSource service = getIMongoDataSource();
		DBObject params=(DBObject)JSON.parse("{id:'asdf'}");
		Object res = service.col("foo", "findById", params);
		System.out.println(res.toString());
	}
	
	@Test
	public void findProsByIdInBaseTable()throws Exception {
		IMongoDataSource service = getIMongoDataSource();
		DBObject params=(DBObject)JSON.parse("{id:'asdf',names:['hhh']}");
		Object res = service.col("foo", "findProsById", params);
		System.out.println(res.toString());
	}

	@Test
	public void test() throws Exception {
		IMongoDataSource service = getIMongoDataSource();
		DBObject params=(DBObject)JSON.parse("{page:{pageSize:1,pageNumber:1}}");
		Object res = service.col("foo", "allsortpage", params);
		System.out.println(res.toString());
		
		params=(DBObject)JSON.parse("{}");
		res = service.col("foo", "allsort", params);
		System.out.println(res.toString());
		
		res = service.col("foo", "all", params);
		System.out.println(res.toString());
		res = service.col("foo", "allCount", params);
		System.out.println(res.toString());
		
		params=(DBObject)JSON.parse("{id:'asdf'}");
		res = service.col("foo", "byId", params);
		System.out.println(res.toString());
		res = service.col("foo", "byIdHhh", params);
		System.out.println(res.toString());

	}

	private IMongoDataSource getIMongoDataSource() throws Exception {
		System.err.println("will get servie");
		MongoDataSourceImpl service = new MongoDataSourceImpl();
		Mongo mongo = new Mongo();
		service.setMongo(mongo);
		service.setDbName("jmwpt");
		service.addCollExecuter("list", new ListColExecuter());
		service.addCollExecuter("one", new OneColExecuter());
		service.addCollExecuter("count", new CountColExecuter());
		service.addCollExecuter("son", new SonColExecuter());
		service.addCollExecuter("page", new PageColExecuter());
		service.addCollExecuter("trans", new TransValueColExecuter());
		MultiStepColExecuter msce=new MultiStepColExecuter();
		msce.setColExecuters(service.getCollExecuters());
		service.addCollExecuter("multi", msce);
		System.err.println("servie has setColExecuters");
		JsonColsResource colResource = new JsonColsResource();
		colResource.setCheck(false);
		colResource.setJsonDir(jsonDir);
		service.setCollectionsResource(colResource);
		System.err.println("servie has setColExecuters");
		return service;
	}
}
