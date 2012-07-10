package xyz.mongo.objan.exec.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.aopalliance.intercept.MethodInvocation;

import xyz.mongo.Base;
import xyz.mongo.IBase;
import xyz.mongo.objan.exec.IParamManager;
import xyz.mongo.objan.exec.impl.an.Col;

public class JavasisstParamManager implements IParamManager{

	@Override
	public Map<String, Object> params(MethodInvocation invocation) throws Exception{
		Map<String, Object> result=new HashMap<String, Object>() ;
		Method method=invocation.getMethod();
		String name=method.getName();
		Class clasz=method.getDeclaringClass();
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(clasz)); 
		CtClass cc = pool.get(clasz.getName());
		CtMethod cm = cc.getDeclaredMethod(name);
		pool.removeClassPath(new ClassClassPath(clasz));
		 MethodInfo methodInfo = cm.getMethodInfo();  
         CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
         LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
         if (null==attr)  {
        	 pool.removeClassPath(new ClassClassPath(clasz));
             throw new IllegalStateException(name+" method must has params");
         }
         int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
         Object[] args=invocation.getArguments();
         for (int i = 0,len=cm.getParameterTypes().length; i < len; i++) {
        	 String paramName=attr.variableName(i + pos); 
        	 result.put(paramName, args[i]);
         }
         //最后，防止Collection Class信息
         //用注解，还是集成IBase，反射实现呢
         if(invocation.getThis() instanceof Base){
        	 IBase base=(IBase)invocation.getThis();//这点使用反射,现在还没有做完呢
        	 result.put("entityClass", base.getEntityType());
         }else{
     		Annotation[] anArray = clasz.getAnnotations();
     		for(Annotation an:anArray){
     			if(an instanceof Col){
     				Col col=(Col)an;
     				Class collectionClass=col.collectionClass();
     				result.put("entityClass", collectionClass);
     				break;
     			}
     		}
         }
         pool.removeClassPath(new ClassClassPath(clasz));
		return result;
	}

}
