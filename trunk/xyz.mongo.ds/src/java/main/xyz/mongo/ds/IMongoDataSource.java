package xyz.mongo.ds;

import com.mongodb.DBObject;

public interface IMongoDataSource {
    Object col(String coll,String method,DBObject params)throws Exception;
    DBObject db(String coll,String method,DBObject params)throws Exception;
}
