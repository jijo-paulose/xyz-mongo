package xyz.mongo.ds.util;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public interface ICollectionExecuter {
	Object process(DBObject methodQuery,DBCollection coll,DBObject paramMap)throws Exception;
}
