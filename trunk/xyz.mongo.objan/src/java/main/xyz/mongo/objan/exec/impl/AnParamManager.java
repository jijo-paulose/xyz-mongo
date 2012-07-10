package xyz.mongo.objan.exec.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

import xyz.mongo.Read;
import xyz.mongo.objan.exec.IParamManager;
import xyz.mongo.objan.exec.impl.an.Col;

public class AnParamManager implements IParamManager{

	@Override
	public Map<String, Object> params(MethodInvocation invocation) {
		Map<String, Object> result=new HashMap<String, Object>() ;
		Method method=invocation.getMethod();
		Object[] args=invocation.getArguments();
		Annotation[][] annotationArray = method.getParameterAnnotations();
		for (int i = 0, len = args.length; i < len; i++) {
			Annotation[] annotation = annotationArray[i];
			Annotation annotationT = (Annotation) annotation[0];
				if(null!=annotationT){
					if(annotationT instanceof Read){
						result.put(((Read)annotationT).name(), args[i]);
					}
				} 
		}
		//最后，防止Collection Class信息
		Class clasz=method.getDeclaringClass();
		Annotation[] anArray = clasz.getAnnotations();
		for(Annotation an:anArray){
			if(an instanceof Col){
				Col col=(Col)an;
				Class collectionClass=col.collectionClass();
				result.put("entityClass", collectionClass);
				break;
			}
		}
		return result;
	}

}
