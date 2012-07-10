package xyz.mongo.util.impl;

import junit.framework.Assert;

import org.junit.Test;

import xyz.mongo.util.IBeanUtil;

public class BeanUtilTest {

	@Test
	public void copy() throws Throwable{
		From from=new From();
		from.setA("a");
		from.setB("b");
		To to=new To();
		IBeanUtil util=new DefaultBeanUtil();
		util.copy(from, to);
		Assert.assertEquals("a", to.getA());
	}
	
	@Test
	public void son() throws Throwable{
		From from=new From();
		from.setA("a");
		from.setB("b");
		IBeanUtil util=new DefaultBeanUtil();
		String son=(String)util.prop(from, "a");
		Assert.assertEquals("a", son);
	}
	
	static class From{
		private String a;
		private String b;
		public String getA() {
			return a;
		}
		public void setA(String a) {
			this.a = a;
		}
		public String getB() {
			return b;
		}
		public void setB(String b) {
			this.b = b;
		}
	}
	
	static class To{
		private String a;
		private String b;
		public String getA() {
			return a;
		}
		public void setA(String a) {
			this.a = a;
		}
		public String getB() {
			return b;
		}
		public void setB(String b) {
			this.b = b;
		}
	}

}
