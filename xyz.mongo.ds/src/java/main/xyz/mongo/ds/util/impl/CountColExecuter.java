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
 *     type:count,
 *     value:
 *     dv:
 * }
 * @author zmc
 *
 */
/**
 * {'$or':[{'auditStatus':{'$ne':'1'}},{'auditStatus':{ '$exists' : false}}],
 * '$or':[{'accountStatus':'0'},{'accountStatus':{ '$exists' : false}}],
 * 'contractAccount':{'$exists':false},
 * 'isDel':{'$ne':'1'}
 * }
 * @author Administrator
 *
 */
public class CountColExecuter implements ICollectionExecuter {
	private static final Log LOG = LogFactory.getLog(CountColExecuter.class);
	protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();

	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		
		DBObject valueDbo = ValueObjectUtil.getValue(methodQuery, jsonStringUtil, transParams);
		return coll.count(valueDbo);
	}


}
