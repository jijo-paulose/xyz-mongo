package xyz.mongo.objan.exec.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.CtMethod;
import xyz.mongo.objan.exec.IAnUtil;

public class DefaultAnUtil implements IAnUtil{
    private Map<String,String> anNameType=new HashMap<String,String>();
    private Map<String,String> anTypeName=new HashMap<String,String>();
    
	@Override
	public Annotation getAn(Method method, Class clasz) {
		for(Annotation an:method.getAnnotations()){
			if(an.annotationType().getName().equals(clasz.getName())){
				return an;
			}
		}
		return null;
	}
	
	@Override
	public String getAnType(Annotation an){
		String cName=an.annotationType().getName();
		if(anTypeName.containsKey(cName)){
			return anTypeName.get(cName);
		}
		throw new IllegalStateException("can't find execute type for "+an);
	}
	
	@Override
	public Annotation getAn(Method method) {
		for(Annotation an:method.getAnnotations()){
			String cName=an.annotationType().getName();
			System.err.println("an name--"+cName);
			if(anNameType.containsValue(cName)){
				return an;
			}
		}
		return null;
	}

	@Override
	public boolean isAn(Method method) {
		for(Annotation an:method.getAnnotations()){
			if(anNameType.containsValue(an.annotationType().getName())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isAn(CtMethod method)throws Exception {
		for(Object an:method.getAnnotations()){
			if(anNameType.containsValue(an.getClass().getName())){
				return true;
			}
		}
		return false;
	}

	public void setAnNames(Map<String, String> anNames) {
		this.anNameType = anNames;
		for(String key:anNameType.keySet()){
			String value=anNameType.get(key);
			anTypeName.put(value, key);
			System.out.println("key-"+key);
			System.out.println("value-"+value);
		}
	}

}
