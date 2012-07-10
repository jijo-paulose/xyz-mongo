package xyz.mongo.util.impl;

import org.bson.types.ObjectId;


public class ObjectUuidGenerator extends AbstgractUuidGenerator{


	public String next() {
		return ObjectId.get().toString()+"xyz";
	}

}
