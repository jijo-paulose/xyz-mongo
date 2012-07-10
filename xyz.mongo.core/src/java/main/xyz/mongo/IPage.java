package xyz.mongo;

import java.util.List;

public interface IPage<Entity, ID> {
	List<Object> getResult();
	long getNumber();
	long getNowSize();
	long getTotalCnt();
	long getSize();
	long getTotalPages();
	
}
