package ca.uhn.fhir.rest.api;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.util.TestUtil;

public class QualifiedParamListTest {

	@Test
	public void testSplit1() {
		List<String> actual = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null,"aaa");
		assertEquals(1, actual.size());
		assertEquals("aaa", actual.get(0));
	}
	
	@Test
	public void testSplit2() {
		List<String> actual = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null,"aaa,bbb");
		assertEquals(2, actual.size());
		assertEquals("aaa", actual.get(0));
		assertEquals("bbb", actual.get(1));
	}
	
	@Test
	public void testSplit3() {
		List<String> actual = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null,"aaa,b\\,bb");
		System.out.println(actual);
		assertEquals(2, actual.size());
		assertEquals("aaa", actual.get(0));
		assertEquals("b,bb", actual.get(1));
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
