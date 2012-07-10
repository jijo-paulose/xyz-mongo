package xyz.mongo.objan.exec.impl.an;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Documented
public @interface ObjUpt {
	/**
	 * 类型，缺省是set
	 * @return
	 */
	 String type() default "set";
	 String key();
	 /**
	  * 需要动态替换的东西，
	  * @return
	  */
     String value() default "";
     /**
      * 动态替换的东西是null时，是否可以忽略
      * @return
      */
     boolean nullCheck() default false;
     
     long lv() default -1L;
}
