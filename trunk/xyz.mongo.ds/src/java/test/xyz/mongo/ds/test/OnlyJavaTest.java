package xyz.mongo.ds.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.ds.util.impl.CountColExecuter;
import xyz.mongo.ds.util.impl.DistinctColExecuter;
import xyz.mongo.ds.util.impl.ExistColExecuter;
import xyz.mongo.ds.util.impl.GroupColExecuter;
import xyz.mongo.ds.util.impl.InsertColExecuter;
import xyz.mongo.ds.util.impl.JsonColsResource;
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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import com.mongodb.BasicDBList;

public class OnlyJavaTest {
	private static IMongoDataSource mds;
	
	private static String uuid;
	
	@Before
	public static void setUp()throws Exception {
		mds=new MongoDataSourceImpl();
		MongoDataSourceImpl mongoDataSource=(MongoDataSourceImpl)mds;
		//mongo
		Mongo mongo=new Mongo("localhost",27017);
		mongoDataSource.setMongo(mongo);
		//数据库名字
		mongoDataSource.setDbName("testdb");
		//设置各种支持的执行
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
		//json文件的读取，传递
		JsonColsResource collectionsResource=new JsonColsResource();
		collectionsResource.setJsonDir("xyz/mongo/ds/test/");
		mongoDataSource.setCollectionsResource(collectionsResource);
		//添加一个,以供以后来使用
		String para="{uName:'xyz',psword:'pw',email:'email@ss.com',role:'ss',moduls:'aaaa',team:200}";
		DBObject params=(DBObject)JSON.parse(para);
		mds.col("user", "add", params);
	}

	//下面是user.json的3个testcase
	@Test
	public void add()throws Exception{
		String para="{uName:'uname',psword:'pw',email:'email@ss.com',role:'ss',moduls:'aaaa',team:100}";
		DBObject params=(DBObject)JSON.parse(para);
		uuid=(String)mds.col("user", "add", params);
		Assert.assertEquals(28, uuid.length());
	}
	
	@Test
	public void findAll()throws Exception{
		BasicDBList list=(BasicDBList)mds.col("user", "findAll", null);
		Assert.assertEquals(2,list.size());
	}
	
	@Test
	public void findPage()throws Exception{
		String para="{page:{size:20,number:1}}";
		DBObject params=(DBObject)JSON.parse(para);
		DBObject mypage=(DBObject)mds.col("user", "findPage", params);
	}
	
	//下面是basetable里面的25个test case
	@Test
	public void findById()throws Exception{
		DBObject params=new BasicDBObject();
		params.put("id", uuid);
		DBObject res=(DBObject)mds.col("user", "findById", params);
		Assert.assertNotNull(res);
		Assert.assertEquals(uuid, res.get("_id"));
		Assert.assertEquals("uname", res.get("uName"));
		Assert.assertEquals("pw", res.get("psword"));
		Assert.assertEquals("email@ss.com", res.get("email"));
		Assert.assertEquals("ss", res.get("role"));
		Assert.assertEquals("aaaa", res.get("moduls"));
		Assert.assertEquals(100, res.get("team"));
	}
	
	@Test
	public void findFieldsById()throws Exception{
		DBObject params=new BasicDBObject();
		params.put("id", uuid);
		params.put("id", new String[]{"pw","email"});
		DBObject res=(DBObject)mds.col("user", "findById", params);
		Assert.assertNotNull(res);
		Assert.assertNull(res.get("uName"));
		Assert.assertEquals("pw", res.get("psword"));
		Assert.assertEquals("email@ss.com", res.get("email"));
	}
}
