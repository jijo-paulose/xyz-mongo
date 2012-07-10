package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;
/**
 * json格式：
 * {
 * type：trans,
 * value:
 * name:
 * }
 * @author zmc
 *
 */
public class TransValueColExecuter implements ICollectionExecuter {
	private static final Log LOG = LogFactory.getLog(TransValueColExecuter.class);
	protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();

	public Object process(DBObject methodQuery, DBCollection coll,
			DBObject paramMap) throws Exception {
		String value=jsonStringUtil.merge((String)methodQuery.get("value"), paramMap);
		DBObject valueDbo = (DBObject) JSON.parse(value);
		String name=(String)methodQuery.get("name");
		paramMap.put(name, valueDbo);
		return value;
	}
	
	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}
}
