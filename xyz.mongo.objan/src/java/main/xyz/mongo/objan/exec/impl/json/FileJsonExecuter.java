package xyz.mongo.objan.exec.impl.json;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import xyz.mongo.XyzConstants;
import xyz.mongo.ds.util.ICollectionsResource;
import xyz.mongo.objan.exec.IExecuter;

import com.mongodb.DBObject;

public class FileJsonExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(FileJsonExecuter.class);

	protected MongoTemplate template;
	
	private Map<String,IJsonProcesser> jsonProcessers=new HashMap<String,IJsonProcesser>();
	
	private ICollectionsResource collectionsResource;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) throws Exception{
		Method method = invocation.getMethod();
		//这里获取Collection name和method name，和那里面的东西对应起来,如果不使用注解，如何放这个东西进去，是个问题
		String collectionName=(String)params.get(XyzConstants.ENTITY_NAME);
		String methodName=method.getName();
		DBObject methodQuery=collectionsResource.methodQuery(collectionName, methodName);
        String type=(String)methodQuery.get("type");
        if(!jsonProcessers.containsKey(type)){
        	throw new IllegalStateException("not have a json processer named:"+type);
        }
        IJsonProcesser processer=jsonProcessers.get(type);
        return processer.process(methodQuery, params, template);
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	public void setJsonProcessers(Map<String, IJsonProcesser> jsonProcessers) {
		this.jsonProcessers = jsonProcessers;
	}

	public void setCollectionsResource(ICollectionsResource collectionsResource) {
		this.collectionsResource = collectionsResource;
	}

}
