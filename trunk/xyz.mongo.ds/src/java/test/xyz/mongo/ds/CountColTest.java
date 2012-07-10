package xyz.mongo.ds;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.util.impl.CountColExecuter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class CountColTest extends AbstractColTest {

	protected void insertExecuter(MongoDataSourceImpl service) {
		service.addCollExecuter("count", new CountColExecuter());
	}

	protected List<String> getColNames() {
		List<String> list = new ArrayList<String>();
		list.add("testGroup");
		return list;
	}

	@Test
	public void all() throws Exception {
		DBObject params = (DBObject) JSON.parse("{}");
		long cnt = (Long) service.col("countTest", "all", params);
		Assert.assertEquals(0, cnt);
		DBCollection coll = db.getCollection("countTest");
		for (int i = 1; i < 5; i++) {
			BasicDBObject object = new BasicDBObject();
			object.append("key", i);
			coll.insert(object);
			cnt = (Long) service.col("countTest", "all", params);
			Assert.assertEquals(i, cnt);
		}
	}

	@Test
	public void param() throws Exception {
		DBObject params = (DBObject) JSON.parse("{param:'abcd1'}");
		long cnt = (Long) service.col("countTest", "param", params);
		Assert.assertEquals(0, cnt);
		DBCollection coll = db.getCollection("countTest");
		for (int i = 1; i < 5; i++) {
			BasicDBObject object = new BasicDBObject();
			object.append("key", "abcd" + i);
			params = (DBObject) JSON.parse("{param:'abcd" +i+"'}");
			coll.insert(object);
			cnt = (Long) service.col("countTest", "param", params);
			Assert.assertEquals(1, cnt);
		}
	}

}
