package test;

import static org.junit.Assert.*;
import hello.MyCal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMyCal {
	private MyCal a = new MyCal();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		int result=a.myadd(1,2);
		assertEquals(result,3);
	}

}
