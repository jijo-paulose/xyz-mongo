package xyz.mongo.ds.util;

import com.mongodb.WriteResult;

public interface IWriterCheck {
     void check(WriteResult result);
}
