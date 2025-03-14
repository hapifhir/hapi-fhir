package ca.uhn.fhir.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ReflectionUtilTest {

	@Test
	public void testNewInstance() {
		assertEquals(ArrayList.class, ReflectionUtil.newInstance(ArrayList.class).getClass());
	}

	@Test
	public void testNewInstanceOrReturnNullString() {
		assertEquals(ArrayList.class, ReflectionUtil.newInstanceOrReturnNull(ArrayList.class.getName(), List.class).getClass());
	}

	@Test
	public void testNewInstanceOrReturnNullWrong1() {
		assertNull(ReflectionUtil.newInstanceOrReturnNull("foo.Foo", List.class));
	}

	@Test
	public void testNewInstanceOrReturnNullWrong2() {
		try {
			ReflectionUtil.newInstanceOrReturnNull("java.lang.String", List.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1787) + "java.lang.String is not assignable to interface java.util.List", e.getMessage());
		}
	}

	@Test
	public void testNewInstanceFail() {
		try {
			ReflectionUtil.newInstance(List.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1784) + "Failed to instantiate java.util.List", e.getMessage());
		}
	}

	@Test
	public void testTypeExists() {
		assertFalse(ReflectionUtil.typeExists("ca.Foo"));
		assertTrue(ReflectionUtil.typeExists(String.class.getName()));
	}

	@Test
	public void testDescribeMethod() throws NoSuchMethodException {
		Method method = String.class.getMethod("startsWith", String.class, int.class);
		String description = ReflectionUtil.describeMethodInSortFriendlyWay(method);
		assertEquals("startsWith returns(boolean) params(java.lang.String, int)", description);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotation {}

	static class TestClass1 {
		@TestAnnotation
		private String field1;
	}

	static class TestClass2 {
		private String field2;
	}

	static class TestClass3 {
		@TestAnnotation
		private String field3;
	}

	static class TestClass4 {
		private TestClass1 param1;
		private TestClass2 param2;
		private TestClass3 param3;

		void setParams(TestClass1 param1, TestClass2 param2, TestClass3 param3) {
		}
	}

	@Test
	public void testGetMethodParamsWithClassesWithFieldsWithAnnotation() throws NoSuchMethodException {
		Method method = TestClass4.class.getDeclaredMethod("setParams", TestClass1.class, TestClass2.class, TestClass3.class);
		List<Class<?>> result = ReflectionUtil.getMethodParamsWithClassesWithFieldsWithAnnotation(method, TestAnnotation.class);

		assertEquals(2, result.size());
		assertTrue(result.contains(TestClass1.class));
		assertTrue(result.contains(TestClass3.class));
	}
}
