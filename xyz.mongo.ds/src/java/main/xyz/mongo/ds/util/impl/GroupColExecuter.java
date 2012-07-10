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
 * http://www.mongodb.org/display/DOCS/Aggregation
 * 
 * json格式：
 * {
 *    type:group
 *     key: Fields to group by.
 *     reduce: The reduce function aggregates (reduces) the objects iterated.String
 *     initial: initial value of the aggregation counter object.
 *     keyf: An optional function returning a "key object" to be used as the grouping key
 *     cond: An optional condition that must be true for a row to be considered.
 *         这个改用value和DV来做，和其他的兼容
 *     finalize: An optional function to be run on each item in the result set just before the item is returned.string
 * }
 * @author zmc
 *
 */
public class GroupColExecuter implements ICollectionExecuter {
	private static final Log LOG = LogFactory.getLog(GroupColExecuter.class);
	protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();

	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		
		DBObject valueDbo = ValueObjectUtil.getValue(methodQuery, jsonStringUtil, transParams);
		DBObject key=(DBObject)methodQuery.get("key");
		DBObject initial=(DBObject)methodQuery.get("initial");
		String reduce=(String)methodQuery.get("reduce");
		String finalize=(String)methodQuery.get("finalize");
		return coll.group(key, valueDbo, initial, reduce, finalize);
	}


}
