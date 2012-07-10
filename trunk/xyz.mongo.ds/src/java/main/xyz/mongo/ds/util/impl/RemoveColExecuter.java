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
 * type：remove,
 * value:
 * }
 * @author zmc
 *
 */
public class RemoveColExecuter  implements ICollectionExecuter{
    private static final Log LOG=LogFactory.getLog(RemoveColExecuter.class);
    
    protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();
    
    private IWriterCheck writerCheck;
    private IWriteConcernManager writeConcernManager;
	
	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		DBObject valueDbo = (DBObject) JSON.parse(value);
		WriteResult result=null;
        if(writeConcernManager.isUse()){
        	result=coll.remove(valueDbo, writeConcernManager.getWriteConcern());
        }else{
        	result=coll.remove(valueDbo);
        }
		writerCheck.check(result);
	    return "void";
	}
	
	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

	public void setWriterCheck(IWriterCheck writerCheck) {
		this.writerCheck = writerCheck;
	}

	public void setWriteConcernManager(IWriteConcernManager writeConcernManager) {
		this.writeConcernManager = writeConcernManager;
	}
}
