package xyz.mongo.ds;

import junit.framework.Assert;

import org.junit.Test;

import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.util.impl.ExistColExecuter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class ExistColTest extends AbstractColTest{

	protected void insertExecuter(MongoDataSourceImpl service) {
		service.addCollExecuter("exist", new ExistColExecuter());
	}
	
	@Test
	public void test()throws Exception{
		DBObject params=(DBObject)JSON.parse("{}");
		boolean  exist= (Boolean)service.col("foo", "allExist", params);
		Assert.assertFalse(exist);
		DBCollection coll=db.getCollection("foo");
		for(int i=1;i<5;i++){
			BasicDBObject object=new BasicDBObject();
			object.append("key", "val"+i);
			coll.insert(object);
			exist= (Boolean)service.col("foo", "allExist", params);
			Assert.assertTrue(exist);
		}
	}

}
