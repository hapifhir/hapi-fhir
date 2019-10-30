package ca.uhn.fhir.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
			assertEquals("java.lang.String is not assignable to interface java.util.List", e.getMessage());
		}
	}

	@Test
	public void testNewInstanceFail() {
		try {
			ReflectionUtil.newInstance(List.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Failed to instantiate java.util.List", e.getMessage());
		}
	}
}
