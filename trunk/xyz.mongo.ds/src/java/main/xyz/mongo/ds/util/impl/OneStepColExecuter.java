package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public abstract class OneStepColExecuter implements ICollectionExecuter{
    private static final Log LOG=LogFactory.getLog(OneStepColExecuter.class);
    
    private static final String PAGE="page";
    
    protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();
    
	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		//String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		String fields=jsonStringUtil.merge((String)methodQuery.get("fields"), transParams);
		
		DBObject valueDbo = ValueObjectUtil.getValue(methodQuery, jsonStringUtil, transParams);
		DBObject fieldsDbo = (DBObject) JSON.parse(fields);

		return exec(methodQuery,coll,transParams,valueDbo,fieldsDbo);
	}
	
	protected abstract Object exec(DBObject methodQuery, DBCollection coll,DBObject transParams,DBObject value,DBObject fields)throws Exception ;
	
	

}
