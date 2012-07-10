package xyz.mongo.ds.servlet.view.impl;

import java.util.HashMap;
import java.util.Map;

import xyz.mongo.ds.servlet.view.IResult;
import xyz.mongo.ds.servlet.view.IResultManager;

public class DefaultResultManager implements IResultManager{
     private Map<String,IResult> results=new HashMap();

	@Override
	public IResult get(String type, String path) {
		if(!results.containsKey(type)){
			throw new RuntimeException("can't find result 4 type :"+type);
		}
		return results.get(type);
	}

	public void setResults(Map<String, IResult> results) {
		this.results = results;
	}
	
	public void addResult(String type,IResult result){
		results.put(type, result);
	}
	
	public void removeResult(String type){
		results.remove(type);
	}
	
}
