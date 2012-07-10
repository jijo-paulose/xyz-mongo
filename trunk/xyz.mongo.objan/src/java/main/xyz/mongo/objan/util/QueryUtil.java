package xyz.mongo.objan.util;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

public final class QueryUtil {
	private static final Log LOG = LogFactory.getLog(QueryUtil.class);
    
	private QueryUtil(){}
	/**
	 * 将一个Class的所有属性做成Query字符串
	 * @param clazz 
	 * @return
	 */
	public static String buildQueryFiled(Class<? extends Object> clazz){
		try {
			StringBuilder sb=new StringBuilder("{");
			boolean first=true;
			for (Field field : clazz.getDeclaredFields()) {
				String name=field.getName();
				if("id".equals(name)){
					continue;
				}
				if(!first){
					sb.append(",");
				}else{
					first=false;
				}
				sb.append(name).append(":1");
			}
			sb.append("}");
			return sb.toString();
		} catch (Throwable t) {
			if(LOG.isErrorEnabled()){
				LOG.error("trans document to object error",t);
			}
		}
		return "{}";
	}
	
	/**
	 * 将一个Class的所有属性做成Query对象实例
	 * @param clazz
	 * @return
	 */
	public static Query buildQuery(Class<? extends Object> clazz){
		String queryString=buildQueryFiled(clazz);
		Query query=new BasicQuery("{}",queryString);
		return query;
	}
	
	/**
	 * 将一个Class的所有属性做成Query对象实例
	 * @param clazz
	 * @param value {'_id':'123'}
	 * @return
	 */
	public static Query buildQuery(String value,Class clazz){
		String queryString=buildQueryFiled(clazz);
		Query query=new BasicQuery(value,queryString);
		return query;
	}

	
}
