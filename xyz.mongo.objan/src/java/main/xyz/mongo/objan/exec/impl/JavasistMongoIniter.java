package xyz.mongo.objan.exec.impl;

import java.util.HashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IMongoIniter;
import xyz.mongo.objan.exec.MongoAbstract;

public class JavasistMongoIniter implements IMongoIniter{
	private static final Log LOG=LogFactory.getLog(JavasistMongoIniter.class);
	private static Map<String,String> METHOD_BODY_MAP=new HashMap();
	static{
		METHOD_BODY_MAP.put("short", "{return 0;}");
		METHOD_BODY_MAP.put("long", "{return 0l;}");
		METHOD_BODY_MAP.put("int", "{return 0;}");
		METHOD_BODY_MAP.put("boolean", "{return true;}");
		METHOD_BODY_MAP.put("double", "{return 0.0d;}");
		METHOD_BODY_MAP.put("char", "{return 0;}");
		METHOD_BODY_MAP.put("float", "{return 0f;}");
		METHOD_BODY_MAP.put("byte", "{return 0;}");
		METHOD_BODY_MAP.put("void", "{}");
	}
	
	private IAnUtil anUtil;
	
	private boolean canNoMet=false;

	public void init(MongoAbstract mongo) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(mongo.getClass())); 
		//pool.insertClassPath(new ClassClassPath(this.getClass())); 
		if(LOG.isInfoEnabled()){
			LOG.info("will get ctclass:"+mongo.getTargetClass());
		}
		CtClass cc = pool.get(mongo.getTargetClass());
		cc.setModifiers(Modifier.PUBLIC);
		for (CtMethod method : cc.getMethods()) {
			if(getMet(method)){
				String rname=method.getReturnType().getName();
				String body="{ return null; }";
				if(METHOD_BODY_MAP.containsKey(rname)){
					body=METHOD_BODY_MAP.get(rname);
				}
				CtMethod me=CtNewMethod.make(method.getReturnType(), method.getName(), method.getParameterTypes(), method.getExceptionTypes(), body, cc);
				me.setModifiers(Modifier.PUBLIC);
				cc.addMethod(me);
			}
		}
		Class realClass = cc.toClass();
		mongo.setTarget(realClass.newInstance());
		mongo.setTargetType(realClass);
		pool.removeClassPath(new ClassClassPath(mongo.getClass()));
	}
	private boolean getMet(CtMethod method)throws Exception{
		if(anUtil.isAn(method)){
			return true;
		}
		if(method.isEmpty()){
			return canNoMet;
		}
		return false;
	}
	public void setCanNoMet(boolean canNoMet) {
		this.canNoMet = canNoMet;
	}
	public void setAnUtil(IAnUtil anUtil) {
		this.anUtil = anUtil;
	}
}
