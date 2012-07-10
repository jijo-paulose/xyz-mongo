package xyz.mongo.objan.exec.impl.an;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Col {

	/**
	 * collectionClass的类，不可少
	 * @return 
	 */
	Class collectionClass();
	/**
	 * 一般是collectionClass的类名字，第一个小写
	 * @return
	 */
	String collectionName() default "";
	
	Class idClass() default String.class;
	String idName() default "id";
}
