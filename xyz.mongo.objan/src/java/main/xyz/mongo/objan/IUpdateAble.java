package xyz.mongo.objan;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface IUpdateAble {
      void queryUpdate(final Query query,final Update update);
}
