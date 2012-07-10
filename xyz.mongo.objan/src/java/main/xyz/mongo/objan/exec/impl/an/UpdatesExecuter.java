package xyz.mongo.objan.exec.impl.an;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class UpdatesExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(UpdatesExecuter.class);

	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	protected IAnUtil anUtil;
	
	protected MongoTemplate template;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) {
		Method method = invocation.getMethod();
		Updates update = (Updates) anUtil.getAn(method, Updates.class);
		if (null == update) {
			throw new IllegalStateException("Update can't be null 4 method:"
					+ method.getName());
		}
		final boolean multi=update.multi();
		Class entityClass = (Class) params.get("entityClass");
		String value = update.value();
	    String realValue = jsonStringUtil.merge(value, params);
	    final DBObject query = (DBObject) JSON.parse(realValue);
		String upStr=update.update();
		String realUpdate=jsonStringUtil.merge(upStr, params);
		final DBObject up = (DBObject) JSON.parse(realUpdate);
		if(0<update.objUpts().length){
			Update myup=new Update();
			for(ObjUpt upt:update.objUpts()){
				String type=upt.type();
				String key=upt.key();
				Object obj=get(upt.lv(),upt.value(),params);
				//对于传入参数是null，不更新的校验支持
				if(null==obj&&upt.nullCheck()&&!"unset".equals(type)){
					continue;
				}
				if("set".equals(type)){
					myup.set(key, obj);
				}else if("unset".equals(type)){
					myup.unset(key);
				}else if("pop".equals(type)){
					//myup.pop(key, 3);
				}else if("addToSet".equals(type)){
					myup.addToSet(key, obj);
				}else if("inc".equals(type)){
					myup.inc(key, upt.lv());
				}else if("pull".equals(type)){
					myup.pull(key, obj);
				}else if("pullAll".equals(type)){
					if(obj instanceof List){
						myup.pullAll(key, ((List)obj).toArray());
					}else{
						myup.pullAll(key, (Object[])obj);
					}
				}else if("push".equals(type)){
					myup.addToSet(key, obj);
				}else if("pushAll".equals(type)){
					if(obj instanceof List){
						myup.pushAll(key, ((List)obj).toArray());
					}else{
						myup.pushAll(key, (Object[])obj);
					}
				}else if("rename".equals(type)){
					//do nothing
				}
			}
			DBObject updateObj = myup.getUpdateObject();
			for (String key : updateObj.keySet()) {
				Map tmp=(Map)template.getConverter().convertToMongoType(updateObj.get(key));
				updateObj.put(key, tmp);
				if(up.containsField(key)){
					DBObject first=(DBObject)up.get(key);
					first.putAll(tmp);
					up.put(key, first);
				}else{
					up.put(key, tmp);
				}
			}
		}
		return template.execute(entityClass, new CollectionCallback<Void>() {
			public Void doInCollection(DBCollection collection)
					throws MongoException, DataAccessException {
				collection.update(query, up, false, multi);
				return null;
			};
		});
	}
	
	private Object get(long lv,String value,Map<String, Object> map){
		if(-1L!=lv)return lv;
		if(value.startsWith("${")&&value.endsWith("}")){
			value=value.substring(2,value.length()-1);
			int index=value.indexOf(".");
			if(-1==index){
				return map.get(value);
			}
			//这个地方需要做一下，用什么，ognl，PropertyUtils，还是其他的东西，cglib BeanOptions，没有完成呢
			//return BeanUtil.getField(map.get(value.substring(0, index)), value.substring( index+1));
		}
		return value;
	}

	/**
	 * @param anUtil
	 *            the anUtil to set
	 */
	public void setAnUtil(IAnUtil anUtil) {
		this.anUtil = anUtil;
	}

	/**
	 * @param jsonStringUtil
	 *            the jsonStringUtil to set
	 */
	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}


}
