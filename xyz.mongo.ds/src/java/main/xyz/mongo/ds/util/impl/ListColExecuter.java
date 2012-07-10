package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
/**
 * json格式：
 * {
 *      type:list，
 * 		value：
 *      dv:
 * 		fields：
 * 		sorts：
 * }
 * @author zmc
 *
 */
public class ListColExecuter extends OneStepColExecuter {
	private static final Log LOG = LogFactory.getLog(ListColExecuter.class);

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
		if (null == orderBy) {
			return coll.find(value, fields).toArray();
		} else {
			return coll.find(value, fields).sort(orderBy).toArray();
		}

	}

}
