package test;

import java.util.List;

import org.junit.Test;

import ca.uhn.test.generic.single.TestConfigDstu1;
import ca.uhn.test.generic.multi.*;
import test.ResourceTest;

public class TestGenerics {

	@Test
	public void testGeneratedListReferencingGenerics() {
		// This won't compile if tinder didn't generate the right names...
		TestConfigDstu1 config = new TestConfigDstu1();
		List<ResourceTest> generics = config.testProvidersDstu1();
		for (ResourceTest generic : generics) {
			String name = generic.getResourceName();
		}
	}
	
}
