package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.ds.util.IWriteConcernManager;
import xyz.mongo.ds.util.IWriterCheck;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
/**
 * json格式：
 * {
 * type：update,
 * value:
 * update:
 * multi:
 * }
 * @author zmc
 *
 */
public class UpdateColExecuter implements ICollectionExecuter{
    private static final Log LOG=LogFactory.getLog(UpdateColExecuter.class);
    
    protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();
    
    private IWriterCheck writerCheck;
    private IWriteConcernManager writeConcernManager;
    
	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		DBObject valueDbo = (DBObject) JSON.parse(value);
		String update=jsonStringUtil.merge((String)methodQuery.get("update"), transParams);
		boolean multi=true;
		if(methodQuery.containsField("multi")){
			multi=(Boolean)methodQuery.get("multi");
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("Now update "+(multi?"multi":"one")+" for {table:"+coll.getName()+" ,value:"+value+",update:"+update+"}");
		}
		DBObject updateDbo = (DBObject) JSON.parse(update);
		WriteResult result=null;
        if(writeConcernManager.isUse()){
        	result=coll.update(valueDbo, updateDbo, false, multi,writeConcernManager.getWriteConcern());
        }else{
        	result=coll.update(valueDbo, updateDbo, false, multi);
        }
	    writerCheck.check(result);
	    return "void";
	}

	public void setWriterCheck(IWriterCheck writerCheck) {
		this.writerCheck = writerCheck;
	}

	public void setWriteConcernManager(IWriteConcernManager writeConcernManager) {
		this.writeConcernManager = writeConcernManager;
	}
}
