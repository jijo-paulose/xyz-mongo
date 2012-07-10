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
 * type：son,
 * value:
 * dv:
 * sorts:
 * son:
 * }
 * @author zmc
 *
 */
public class SonColExecuter  extends OneStepColExecuter {
	private static final Log LOG = LogFactory.getLog(SonColExecuter.class);

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
		DBObject parent = null;
		if (null == orderBy) {
			parent = coll.findOne(value, fields);
		} else {
			DBCursor cursor = coll.find(value, fields);
			cursor.sort(orderBy);
			if (cursor.hasNext()) {
				parent = cursor.next();
			}
		}
		if (null != parent) {
			String son = (String)methodQuery.get("son");
			return parent.get(son);
		}
		return null;
	}

}
