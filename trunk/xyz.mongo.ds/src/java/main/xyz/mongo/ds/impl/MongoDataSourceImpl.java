package xyz.mongo.ds.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.ds.util.ICollectionsResource;
import xyz.mongo.ds.util.impl.MultiStepColExecuter;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDataSourceImpl implements IMongoDataSource {
	private static final Log LOG = LogFactory.getLog(MongoDataSourceImpl.class);

	private Mongo mongo;

	private String dbName;

	private ICollectionsResource collectionsResource;

	private boolean checkargs = true;

	// 运行时不改，hashmap比concurrenthashmap快
	private Map<String, ICollectionExecuter> collExecuters = new HashMap<String, ICollectionExecuter>();

	public Map<String, ICollectionExecuter> getCollExecuters() {
		return collExecuters;
	}

	public Object col(String coll, String method, DBObject params)
			throws Exception {
		DBObject methodQuery = collectionsResource.methodQuery(coll, method);
		String type = (String) methodQuery.get("type");
		ICollectionExecuter executer = collExecuters.get(type);
		if (null == executer) {
			throw new IllegalStateException("can't support executer type :"
					+ type);
		}
		// check args
		if (checkargs) {
			BasicDBList list = (BasicDBList) methodQuery.get("checkargs");
			for (Object arg : list) {
				if (!params.containsField(arg + "")) {
					throw new IllegalStateException("Service 4 " + coll
							+ " coll ," + method + " method must has a " + arg
							+ " param");
				}
			}
		}

		DB db = mongo.getDB(dbName);
		DBCollection dbcoll = db.getCollection(coll);
		// 做一下参数校验
		// checkParams(methodQuery,params,coll);
		// 添加几个常量和那边一直
		params.put("xyzRegexFlag", "cdgimstux");
		params.put("xyzNow", System.currentTimeMillis());
		Object res = executer.process(methodQuery, dbcoll, params);
		return res;
	}

	public DBObject db(String db, String method, DBObject params)
			throws Exception {
		throw new RuntimeException("Don't support yet!");
	}

	
	public void addCollExecuter(String name, ICollectionExecuter executer) {
		collExecuters.put(name, executer);
		//修复multi的bug
		if(!name.equals("multi")){
			if (collExecuters.containsKey("multi")) {
				MultiStepColExecuter msmf = (MultiStepColExecuter) collExecuters
						.get("multi");
				msmf.getColExecuters().put(name, executer);
			}
		}else{
			if (collExecuters.containsKey("multi")) {
				MultiStepColExecuter msmf = (MultiStepColExecuter) collExecuters
						.get("multi");
				for (Iterator<String> it = collExecuters.keySet().iterator(); it
						.hasNext();) {
					String key = it.next();
					if (!"multi".equals(key)) {
						ICollectionExecuter finder = collExecuters.get(key);
						msmf.getColExecuters().put(key, finder);
					}
				}
			}
		}
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setCollectionsResource(ICollectionsResource collectionsResource) {
		this.collectionsResource = collectionsResource;
	}

	public void setCollExecuters(Map<String, ICollectionExecuter> collExecuters) {
		this.collExecuters = collExecuters;
		// 设置那个multi的东东
		if (collExecuters.containsKey("multi")) {
			MultiStepColExecuter msmf = (MultiStepColExecuter) collExecuters
					.get("multi");
			for (Iterator<String> it = collExecuters.keySet().iterator(); it
					.hasNext();) {
				String key = it.next();
				if (!"multi".equals(key)) {
					ICollectionExecuter finder = collExecuters.get(key);
					msmf.getColExecuters().put(key, finder);
				}
			}
		}
	}

	public void setCheckargs(boolean checkargs) {
		this.checkargs = checkargs;
	}

}
