package xyz.mongo.objan.simple.an;

import xyz.mongo.Read;
import xyz.mongo.objan.exec.impl.an.Col;

@Col(collectionClass=First.class)
public class AnObj {

    public First test(@Read(name="test")First test){
		test.setId("myId");
		test.setName("myName");
    	return test;
    }
}
