package xyz.mongo.objan.exec.impl.an;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Documented
public @interface DV {
	/**
	 * 是否校验，一些常量不需要
	 * @return
	 */
	 boolean check() default true;
	 /**
	  * 需要动态替换的东西，如果不能替换，就忽略
	  * @return
	  */
     String value();
}
