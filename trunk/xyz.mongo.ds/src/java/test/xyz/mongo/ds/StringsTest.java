package xyz.mongo.ds;

import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class StringsTest {

	@Test
	public void test() {
		String strings="{args:['a','b']}";
		DBObject obj=(DBObject)JSON.parse(strings);
		Object args=obj.get("args");
		System.out.println(args);
		System.out.println(args.getClass());
		BasicDBList list=(BasicDBList)args;
		for(Object s:list){
			System.out.println(s);
		}
	}

}
