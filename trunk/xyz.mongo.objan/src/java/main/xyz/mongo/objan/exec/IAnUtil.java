package xyz.mongo.objan.exec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javassist.CtMethod;

public interface IAnUtil {
	Annotation getAn(Method method,Class clasz);
	Annotation getAn(Method method);
	boolean isAn(Method method);
	boolean isAn(CtMethod method)throws Exception;
	String getAnType(Annotation an);
}
