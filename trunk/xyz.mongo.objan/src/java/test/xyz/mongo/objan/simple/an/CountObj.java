package xyz.mongo.objan.simple.an;

import xyz.mongo.Read;
import xyz.mongo.objan.exec.impl.an.Col;
import xyz.mongo.objan.exec.impl.an.Count;

@Col(collectionClass=First.class)
public class CountObj {
	@Count(value="{}")
    public long count(@Read(name="test")First test){
		return 0;
    }
}
