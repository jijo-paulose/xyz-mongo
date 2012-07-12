package xyz.mongo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Read {
     String name() default "one";
     /**
      * 设置一些泛型类型，比如List之后的类型，由于java
      * 泛型的擦拭实现，导致这些东西不好得到，所以，必须在此添加
      * @return
      */
     Class ctype() default Object.class;
}
