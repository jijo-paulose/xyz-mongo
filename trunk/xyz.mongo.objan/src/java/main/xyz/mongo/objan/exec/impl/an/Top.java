package xyz.mongo.objan.exec.impl.an;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 里皮，top 
 * @author zmc
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Top {
     String value();
     DV[] dv() default {};
     String fields() default "{}";
     String sorts() default "";
     int limit() default 100;
     Class vo();
}
