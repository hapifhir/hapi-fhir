package test;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.test.customstructs.resource.Patient;

public class TestProfileGenerator {

	@Test
	public void testIncludes() {
		
		assertEquals("Patient.link.other", Patient.INCLUDE_LINK_OTHER.getValue());
		
	}
	
}
