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
	public void testNewInstanceFail() {
		try {
			ReflectionUtil.newInstance(List.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Failed to instantiate java.util.List", e.getMessage());
		}
	}
}
