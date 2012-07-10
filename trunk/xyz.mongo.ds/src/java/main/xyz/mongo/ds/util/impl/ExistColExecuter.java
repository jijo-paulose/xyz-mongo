package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
/**
 * json格式：
 * {
 *     type:exist,
 * 	   value：
 *     dv:
 * }
 * @author zmc
 *
 */
public class ExistColExecuter extends CountColExecuter {
	private static final Log LOG = LogFactory.getLog(ExistColExecuter.class);

	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		return (Long)super.process(methodQuery, coll, transParams)>0;
	}

}
