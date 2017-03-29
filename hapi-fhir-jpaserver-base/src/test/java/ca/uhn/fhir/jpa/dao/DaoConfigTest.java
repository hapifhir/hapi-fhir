package ca.uhn.fhir.jpa.dao;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class DaoConfigTest {

	@Test
	public void testValidLogicalPattern() {
		new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://foo")));
		new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://foo*")));
	}

	@Test
	public void testInvalidLogicalPattern() {
		try {
			new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://*foo")));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Base URL wildcard character (*) can only appear at the end of the string: http://*foo", e.getMessage());
		}
		try {
			new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://foo**")));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Base URL wildcard character (*) can only appear at the end of the string: http://foo**", e.getMessage());
		}
	}

}
