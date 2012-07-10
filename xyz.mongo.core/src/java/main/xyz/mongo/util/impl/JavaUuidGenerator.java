package xyz.mongo.util.impl;

import java.util.UUID;


public class JavaUuidGenerator extends AbstgractUuidGenerator {
	public String next() {
		return UUID.randomUUID().toString();
	}
}
