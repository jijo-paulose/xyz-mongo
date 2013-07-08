package xyz.mongo.tcp.service.json;

import xyz.mongo.IPageRequest;

public class DefaultPageRequest implements IPageRequest{
    private long number;
    private long size;
    private int cnt;
    
	@Override
	public long getNumber() {
		return number;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public int getCnt() {
		return cnt;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
		
}