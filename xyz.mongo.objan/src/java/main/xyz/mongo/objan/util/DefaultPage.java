package xyz.mongo.objan.util;

import java.util.List;

import xyz.mongo.IPage;

public class DefaultPage<Entity,ID> implements IPage{
    private List<Entity> result;
    private long number;
    private long nowSize;
    private long totalCnt;
    private long size;
    private long totalPages;
    
	@Override
	public List<Entity> getResult() {
		return result;
	}

	@Override
	public long getNumber() {
		return number;
	}

	@Override
	public long getNowSize() {
		return nowSize;
	}

	@Override
	public long getTotalCnt() {
		return totalCnt;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public long getTotalPages() {
		return totalPages;
	}

	public void setResult(List<Entity> result) {
		this.result = result;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public void setNowSize(long nowSize) {
		this.nowSize = nowSize;
	}

	public void setTotalCnt(long totalCnt) {
		this.totalCnt = totalCnt;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

}
