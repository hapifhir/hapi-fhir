package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.ConfigurationException;

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
		assertEquals(null, ReflectionUtil.newInstanceOrReturnNull("foo.Foo", List.class));
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

}
