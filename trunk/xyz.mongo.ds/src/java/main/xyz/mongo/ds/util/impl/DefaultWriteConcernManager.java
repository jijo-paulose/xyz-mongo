package xyz.mongo.ds.util.impl;

import com.mongodb.WriteConcern;

import xyz.mongo.ds.util.IWriteConcernManager;

public class DefaultWriteConcernManager implements IWriteConcernManager{
	private boolean use;
	private WriteConcern writeConcern;

	@Override
	public boolean isUse() {
		return use;
	}

	@Override
	public WriteConcern getWriteConcern() {
		if(!use)throw new IllegalStateException("no use write concern");
		return prepareWriteConcern(writeConcern);
	}
	
	protected WriteConcern prepareWriteConcern(WriteConcern writeConcern){
		return writeConcern;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public void setWriteConcern(WriteConcern writeConcern) {
		this.writeConcern = writeConcern;
	}

}
