package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
/**
 * json格式：
 * {
 * type：one,
 * value:
 * dv:
 * fields:
 * sorts:
 * }
 * @author zmc
 *
 */
public class OneColExecuter extends OneStepColExecuter {
	private static final Log LOG = LogFactory.getLog(OneColExecuter.class);

	public Object exec(DBObject methodQuery, DBCollection coll,
			DBObject transParams, DBObject value, DBObject fields)
			throws Exception {
		DBObject orderBy=null;
		String sorts=(String)methodQuery.get("sorts");
		if(null!=sorts&&!sorts.trim().equals("")){
			String rs=jsonStringUtil.merge(sorts, transParams);
			orderBy=(DBObject) JSON.parse(rs);
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("Now one for {table:"+coll.getName()+" ,value:"+value+",fields:"+fields+",sorts:"+orderBy+"}");
		}
		if (null == orderBy) {
			Object obj = coll.findOne(value, fields);
			System.out.println("obj one is:"+obj);
			return obj;
		} else {
			DBCursor cursor = coll.find(value, fields);
			cursor.sort(orderBy);
			if (cursor.hasNext()) {
				return cursor.next();
			}
		}
		return null;

	}

}
