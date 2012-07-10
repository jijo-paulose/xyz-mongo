package xyz.mongo.ds.util.impl;

import xyz.mongo.ds.util.IWriterCheck;

import com.mongodb.WriteResult;

public  class ThrowOnErrorWriteCheck implements IWriterCheck {
    private boolean throwOnError;

	public void setThrowOnError(boolean throwOnError) {
		this.throwOnError = throwOnError;
	}
	
	
	public void check(WriteResult result){
		if(throwOnError){
			result.getLastError().throwOnError();
		}
	}
}
