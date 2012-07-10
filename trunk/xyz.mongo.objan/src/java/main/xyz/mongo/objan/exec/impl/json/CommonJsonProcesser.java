package xyz.mongo.objan.exec.impl.json;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import xyz.mongo.ds.util.ICollectionExecuter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class CommonJsonProcesser implements IJsonProcesser {
	private static final Log LOG = LogFactory.getLog(CommonJsonProcesser.class);
    /**
     * 这里通过注入,可以注入的实例有：CountColExecuter，DistinctColExecuter，ExistColExecuter
     * InsertColExecuter，UpdateColExecuter，RemoveColExecuter
     * 这样写的目的是少些几个实现，好重用以前可以重用的东西呗
     */
	private ICollectionExecuter collectionExecuter;

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			MongoTemplate template)throws Exception {
		BasicDBObject transParams=new BasicDBObject();
		transParams.putAll(params);
		Class entityClass = (Class) params.get("entityClass");
		DBCollection coll=template.getCollection(template.getCollectionName(entityClass));
		return collectionExecuter.process(valueObj, coll, transParams);
	}

	public void setCollectionExecuter(ICollectionExecuter collectionExecuter) {
		this.collectionExecuter = collectionExecuter;
	}

}
