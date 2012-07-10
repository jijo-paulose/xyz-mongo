package xyz.mongo.objan.exec.impl.json;

import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;

public interface IJsonProcesser {
	Object process( DBObject valueObj,Map<String, Object> params,MongoTemplate template)throws Exception ;
}
