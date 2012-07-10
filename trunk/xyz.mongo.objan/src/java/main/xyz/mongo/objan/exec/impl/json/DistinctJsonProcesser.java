package xyz.mongo.objan.exec.impl.json;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DistinctJsonProcesser implements IJsonProcesser {
	private static final Log LOG = LogFactory
			.getLog(DistinctJsonProcesser.class);
	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			MongoTemplate template) throws Exception {
		Class entityClass = (Class) params.get("entityClass");
		String value = (String) valueObj.get("value");
		String realValue = jsonStringUtil.merge(value, params);

		DBObject query = (DBObject) JSON.parse(realValue);
		String key = (String) valueObj.get("key");
		return template.getCollection(template.getCollectionName(entityClass))
				.distinct(key, query);
	}

	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

}
