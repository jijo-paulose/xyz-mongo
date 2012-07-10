package xyz.mongo.objan.util;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import xyz.mongo.objan.exec.impl.an.DV;
import xyz.mongo.util.IJsonStringUtil;

public final class DvUtil {
	private static final Log LOG = LogFactory.getLog(DvUtil.class);
	
   private DvUtil(){
   }
   
   public static String dv(DV[] dvs,IJsonStringUtil jsonStringUtil,Map<String, Object> params){
	   StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (DV dv : dvs) {
			String from = dv.value();
			if (LOG.isDebugEnabled()) {
				LOG.debug("from--" + from);
			}
			String to = null;
			try {
				to = jsonStringUtil.merge(from, params);
			} catch (Throwable e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("error while merge dv string " + from
							+ " and params: " + params, e);
				}
			}
			// 避免用户写错vo，返回字符长度为0的字符串啊
			if (!StringUtils.hasText(to)) {
				continue;
			}
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("to--" + to);
			}
			sb.append(to);
		}
		sb.append("}");
		return sb.toString();
   }
   
   public static String dv(String[] dvs,IJsonStringUtil jsonStringUtil,Map<String, Object> params){
	   StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (String from : dvs) {
			//String from = dv.value();
			if (LOG.isDebugEnabled()) {
				LOG.debug("from--" + from);
			}
			String to = null;
			try {
				to = jsonStringUtil.merge(from, params);
			} catch (Throwable e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("error while merge dv string " + from
							+ " and params: " + params, e);
				}
			}
			// 避免用户写错vo，返回字符长度为0的字符串啊
			if (!StringUtils.hasText(to)) {
				continue;
			}
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("to--" + to);
			}
			sb.append(to);
		}
		sb.append("}");
		return sb.toString();
   }
}
