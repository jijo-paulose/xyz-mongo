package xyz.mongo.util;

import java.util.List;

public interface IBeanUtil {
     void copy(Object from,Object to);
     Object copy(Object from,Class toClass);
     void listCopy(List list,Class toClass);
     Object prop(Object father,String son);
}
