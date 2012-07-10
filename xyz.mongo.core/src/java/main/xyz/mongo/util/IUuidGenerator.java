package xyz.mongo.util;


public interface IUuidGenerator {
     String next();
     
     void nextAndSet(Object obj);
}
