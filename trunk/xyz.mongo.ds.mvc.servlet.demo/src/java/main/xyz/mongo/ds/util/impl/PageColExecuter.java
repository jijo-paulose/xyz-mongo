package xyz.mongo.ds.util.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
/**
 * json格式:
 * {
 * type:page,
 * page:{
 *    pageSize:
 *    pageNumber:
 * },
 * sorts:
 * value:
 * dv:
 * fields:
 * }
 * @author zmc
 *
 */
public class PageColExecuter extends OneStepColExecuter {
	private static final Log LOG = LogFactory.getLog(PageColExecuter.class);

	public Object exec(DBObject methodQuery, DBCollection coll,
			DBObject transParams, DBObject value, DBObject fields)
			throws Exception {
		DBObject page = (DBObject)transParams.get("page");
		int pageSize = Integer.parseInt((String)page.get("size"));
		int pageNumber = Integer.parseInt((String)page.get("number"));
		long totalCnt = coll.getCount(value, fields);
		List result = new ArrayList();
		DBObject orderBy = null;
		String sorts =(String) methodQuery.get("sorts");
		if (null != sorts && !sorts.trim().equals("")) {
			String rs = jsonStringUtil.merge(sorts, transParams);
			orderBy = (DBObject) JSON.parse(rs);
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("Now page for {table:"+coll.getName()+" ,value:"+value+",fields:"+fields+",sorts:"+orderBy+",page:"+page+"}");
		}
		if (null == orderBy) {
			List list = coll.find(value, fields).skip(
					(pageNumber - 1) * pageSize).limit(pageSize).toArray();
			result.addAll(list);
		} else {
			List list = coll.find(value, fields).sort(orderBy).skip(
					(pageNumber - 1) * pageSize).limit(pageSize).toArray();
			result.addAll(list);
		}
		BasicDBObject mypage = new BasicDBObject();
		mypage.append("result", result);
		mypage.append("number", pageNumber);
		mypage.append("nowSize", result.size());
		mypage.append("totalCnt", totalCnt);
		mypage.append("size", pageSize);
		int pageCount = (int) (totalCnt / pageSize);
		if (0 < totalCnt % pageSize) {
			pageCount++;
		}
		mypage.append("totalPages", pageCount);

		return mypage;
	}

}
