package xyz.mongo.ds.util;

import com.mongodb.WriteConcern;

public interface IWriteConcernManager {
     boolean isUse();
     WriteConcern getWriteConcern();
}
