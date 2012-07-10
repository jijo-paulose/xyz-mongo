package xyz.mongo.objan.exec.impl.an;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Updates {
     String value();
     String update();
     boolean multi() default true;
     ObjUpt[] objUpts() default {};
}
