package xyz.mongo.objan.exec.impl.json;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;

import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class UpdateJsonProcesser implements IJsonProcesser {
	private static final Log LOG = LogFactory.getLog(UpdateJsonProcesser.class);
	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			MongoTemplate template) throws Exception {
		Class entityClass = (Class) params.get("entityClass");

		String value = (String) valueObj.get("value");
		String realValue = jsonStringUtil.merge(value, params);

		final DBObject query = (DBObject) JSON.parse(realValue);
		final boolean multi = (Boolean) valueObj.get("multi");
		String upStr = (String) valueObj.get("update");
		String realUpdate = jsonStringUtil.merge(upStr, params);
		final DBObject up = (DBObject) JSON.parse(realUpdate);
		return template.execute(entityClass, new CollectionCallback<Void>() {
			public Void doInCollection(DBCollection collection)
					throws MongoException, DataAccessException {
				collection.update(query, up, false, multi);
				return null;
			};
		});
	}

	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

}
