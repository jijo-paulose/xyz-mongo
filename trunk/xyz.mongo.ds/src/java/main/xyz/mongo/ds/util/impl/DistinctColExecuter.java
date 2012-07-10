package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
/**
 * json格式：
 * {
 *    type:distinct,
 *    value：
 *    key:
 * }
 */
public class DistinctColExecuter implements ICollectionExecuter {
	private static final Log LOG = LogFactory.getLog(DistinctColExecuter.class);

	 protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();
	 
	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		DBObject valueDbo = (DBObject) JSON.parse(value);
		String key=(String)methodQuery.get("key");
		if (LOG.isDebugEnabled()) {
			LOG.debug("Now distinct for {table:" + coll.getName() + " ,value:"
					+ value + ",distinct:" + key + "}");
		}
		return coll.distinct(key, valueDbo);
	}

	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

}
