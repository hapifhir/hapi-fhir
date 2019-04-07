package ca.uhn.fhir.util;

import ca.uhn.fhir.context.ConfigurationException;
import com.google.common.collect.Lists;
import com.google.common.truth.Truth;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

	@Test
	public void testFindDefaultMethod() throws NoSuchMethodException {
		Collection<Method> methods = ReflectionUtil.getAllMethods(DummyClass.class, true, true);
		List<Method> method = Lists.newArrayList(DummyClass.class.getMethod("whatEver"));
		Truth.assertThat(methods).containsAnyIn(method);
	}

	class DummyClass implements DummyInterfaceWithDefaultMethod {

	}

	interface DummyInterfaceWithDefaultMethod {
		default void whatEver() {

		}
	}

}
