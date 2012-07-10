package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.util.IJsonStringUtil;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public final class ValueObjectUtil {
	private static final Log LOG = LogFactory.getLog(ValueObjectUtil.class);
	
    private ValueObjectUtil(){
    	
    }
    
    public static DBObject getValue(DBObject methodQuery, IJsonStringUtil jsonStringUtil,DBObject transParams){
    	if(methodQuery.containsField("dv")){
    		StringBuilder sb=new StringBuilder("{");
    		BasicDBList dvs=(BasicDBList)methodQuery.get("dv");
    		boolean first=true;
    		for(Object dv:dvs){
    			DBObject query=(DBObject)dv;
    			String from = (String)query.get("value");
				if (LOG.isDebugEnabled()) {
					LOG.debug("from--" + from);
				}
				String to = null;
				try {
					to = jsonStringUtil.merge(from, transParams);
				} catch (Throwable e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("error while merge dv string " + from
								+ " and params: " + transParams, e);
					}
				}
				// 避免用户写错vo，返回字符长度为0的字符串啊
				if (null!=to&&!"".equals(to.trim())) {
					continue;
				}
				if (!first) {
					sb.append(",");
				} else {
					first = false;
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("to--" + to);
				}
				sb.append(to);
    		}
    		sb.append("}");
    		DBObject valueDbo = (DBObject) JSON.parse(sb.toString());
    		if(LOG.isDebugEnabled()){
    			LOG.debug("dv is:"+valueDbo);
    		}
    		return valueDbo;
    	}
    	String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		DBObject valueDbo = (DBObject) JSON.parse(value);
		if(LOG.isDebugEnabled()){
			LOG.debug("value is:"+valueDbo);
		}
        return valueDbo;
    }
}
