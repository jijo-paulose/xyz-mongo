package xyz.mongo.util;

import java.util.Map;

import com.mongodb.DBObject;

public interface IJsonStringUtil {
     String merge(String src,Map<String,Object> params);
     String merge(String src,DBObject params);
}
