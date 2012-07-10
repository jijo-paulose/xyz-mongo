package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
/**
 * json格式：
 * {
 *      type:top，
 * 		value：
 *      dv:
 * 		fields：
 * 		sorts：
 *      limit:
 * }
 * @author zmc
 *
 */
public class TopColExecuter extends OneStepColExecuter {
	private static final Log LOG = LogFactory.getLog(TopColExecuter.class);

	public Object exec(DBObject methodQuery, DBCollection coll,
			DBObject transParams, DBObject value, DBObject fields)
			throws Exception {
		DBObject orderBy = null;
		String sorts = (String)methodQuery.get("sorts");
		if (null != sorts && !sorts.trim().equals("")) {
			String rs = jsonStringUtil.merge(sorts, transParams);
			orderBy = (DBObject) JSON.parse(rs);
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("Now list for {table:"+coll.getName()+" ,value:"+value+",fields:"+fields+",sorts:"+orderBy+"}");
		}
		String limit=(String)methodQuery.get("limit");
		int top=Integer.parseInt(limit);
		if (null == orderBy) {
			return coll.find(value, fields).limit(top).toArray();
		} else {
			return coll.find(value, fields).sort(orderBy).limit(top).toArray();
		}

	}

}
