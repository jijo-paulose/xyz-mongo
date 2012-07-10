package xyz.mongo.ds;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import xyz.mongo.ds.impl.MongoDataSourceImpl;
import xyz.mongo.ds.util.impl.GroupColExecuter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class GroupColTest extends AbstractColTest {

	protected void insertExecuter(MongoDataSourceImpl service) {
		service.addCollExecuter("group", new GroupColExecuter());
	}

	protected List<String> getColNames() {
		List<String> list = new ArrayList<String>();
		list.add("testGroup");
		return list;
	}

	@Test
	public void all() throws Exception {
		DBObject params = (DBObject) JSON.parse("{}");
		DBObject cnt = (DBObject) service.col("testGroup", "only", params);
		System.out.println(cnt);
	}

	

}
