package com.mountainsofmars.jtorcontroller.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MyFirstJUnitTest {
	@Test
	public void simpleAdd() {
		int result = 1;
		int expected = 1;
		assertEquals(result, expected);
	}
}
