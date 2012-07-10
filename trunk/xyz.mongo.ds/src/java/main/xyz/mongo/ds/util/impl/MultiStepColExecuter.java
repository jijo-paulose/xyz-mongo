package xyz.mongo.ds.util.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionExecuter;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
/**
 * json格式：
 * {
 * type:multi,
 * name:
 * return:
 * multi:[{
 *          type:
 *          check:
 *          msg:
 *          table:
 *          还有其他都有的东西：
 *          }
 *       ]
 * }
 * @author zmc
 *
 */
public class MultiStepColExecuter implements ICollectionExecuter{
    private static final Log LOG=LogFactory.getLog(MultiStepColExecuter.class);
    
    private Map<String,ICollectionExecuter> colExecuters=new HashMap();
    
	public void setColExecuters(Map<String, ICollectionExecuter> colExecuters) {
		this.colExecuters = colExecuters;
	}

	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		if(LOG.isDebugEnabled()){
			LOG.debug("Now multi for {table:"+coll.getName()+"}");
		}
		Map<String,Object> resmap=new HashMap();
		BasicDBList dbs=(BasicDBList)methodQuery.get("multi");
		for(Object mq:dbs){
			DBObject query=(DBObject)mq;
			String type=(String)query.get("type");
			ICollectionExecuter executer=colExecuters.get(type);
			if(null==executer){
				throw new IllegalStateException("can't support executer type :"+type);
			}
			DBCollection real=null;
			String table=(String)query.get("table");
			if(null!=table&&!"".equals(table.trim())){
				DB db=coll.getDB();
				real=db.getCollection(table);
			}else{
				real=coll;
			}
			Object stepRes=executer.process(query, real, transParams);
			String check=(String)methodQuery.get("check");
			if(null!=check&&!"".equals(check.trim())){
				if("null".equals(check)){
					if(null==stepRes){
						throw new RuntimeException((String)methodQuery.get("msg"));
					}
				}else if("notnull".equals(check)){
					if(null!=stepRes){
						throw new RuntimeException((String)methodQuery.get("msg"));
					}
				}else if("zero".equals(check)){
					if(0==(Integer)stepRes){
						throw new RuntimeException((String)methodQuery.get("msg"));
					}
				}else if("nozero".equals(check)){
					if(0!=(Integer)stepRes){
						throw new RuntimeException((String)methodQuery.get("msg"));
					}
				}else if("son".equals(check)){
					throw new RuntimeException("Sorry,now can't support son property");
				}
			}
			//放到上下文
			if(null==stepRes){
				break;
			}
			String name=(String)query.get("name");
			transParams.put(name, stepRes);//最后一步呢，先不判断
			//要不要放到结果中
			if("true".equals(query.get("return"))){
				resmap.put(name, stepRes);
			}
		}
		if(resmap.size()==1){
			return resmap.values().toArray()[0];
		}
		return resmap;

	}

	public Map<String, ICollectionExecuter> getColExecuters() {
		return colExecuters;
	}
	
	

}
