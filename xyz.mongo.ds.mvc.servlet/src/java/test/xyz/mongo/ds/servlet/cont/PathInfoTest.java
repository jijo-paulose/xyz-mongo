package xyz.mongo.ds.servlet.cont;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathInfoTest {

	@Test
	public void test() {
		String pathinfo="/table!method!/hee/ggg/ddd.jsp";
		for(String ps:pathinfo.split("!")){
			System.out.println(ps);
		}
		pathinfo="/table!method";
		for(String ps:pathinfo.split("!")){
			System.out.println(ps);
		}
	}

}
