package ca.uhn.fhir.testutil;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.util.QueryUtil;

public class QueryUtilTest {

	@Test
	public void testSplit1() {
		List<String> actual = QueryUtil.splitQueryStringByCommasIgnoreEscape("aaa");
		assertEquals(1, actual.size());
		assertEquals("aaa", actual.get(0));
	}
	
	@Test
	public void testSplit2() {
		List<String> actual = QueryUtil.splitQueryStringByCommasIgnoreEscape("aaa,bbb");
		assertEquals(2, actual.size());
		assertEquals("aaa", actual.get(0));
		assertEquals("bbb", actual.get(1));
	}
	
	@Test
	public void testSplit3() {
		List<String> actual = QueryUtil.splitQueryStringByCommasIgnoreEscape("aaa,b\\,bb");
		System.out.println(actual);
		assertEquals(2, actual.size());
		assertEquals("aaa", actual.get(0));
		assertEquals("b,bb", actual.get(1));
	}

}
