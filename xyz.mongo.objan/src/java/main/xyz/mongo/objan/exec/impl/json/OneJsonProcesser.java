package xyz.mongo.objan.exec.impl.json;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.StringUtils;

import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.objan.util.LogUtil;
import xyz.mongo.util.IBeanUtil;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class OneJsonProcesser implements IJsonProcesser {
	private static final Log LOG = LogFactory.getLog(OneJsonProcesser.class);
	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil.getInstance();
	protected IBeanUtil beanUtil;

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			MongoTemplate template) throws Exception {
		Class entityClass = (Class) params.get("entityClass");
		String value = (String) valueObj.get("value");
		String realValue = jsonStringUtil.merge(value, params);
		String fields = (String) valueObj.get("fields");
		String realFields = jsonStringUtil.merge(fields, params);
		BasicQuery query = new BasicQuery(realValue, realFields);
		String sorts = (String) valueObj.get("sorts");
		if (StringUtils.hasText(sorts)) {
			DBObject sortObject = (DBObject) JSON.parse(sorts);
			query.setSortObject(sortObject);
		}
		if (null != query.getSortObject()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("now use list to get one");
			}
			query.limit(1);
			LogUtil.log(LOG, query, null);
			List list = template.find(query, entityClass);
			if (null == list || list.isEmpty()) {
				return null;
			}
			if(valueObj.containsField("vo")){
				Class clasz=Class.forName((String)valueObj.get("vo"));
				return beanUtil.copy(list.get(0), clasz);
			}
			return list.get(0);
		} else {
			if(valueObj.containsField("vo")){
				Class clasz=Class.forName((String)valueObj.get("vo"));
				return beanUtil.copy(template.findOne(query, entityClass), clasz);
			}
			return template.findOne(query, entityClass);
		}
		// 现在先不考虑,那些子对象VO的属性拷贝问题，现在都在一个地方来做的
	}

	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

	public void setBeanUtil(IBeanUtil beanUtil) {
		this.beanUtil = beanUtil;
	}

}
