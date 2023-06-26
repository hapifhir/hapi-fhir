package test;

import java.util.List;

import org.junit.jupiter.api.Test;

import ca.uhn.test.ant.single.TestConfigDstu2;
import ca.uhn.test.ant.multi.*;
import test.ResourceTest;

public class TestAntTask {

	@Test
	public void testGeneratedListReferencingGenerics() {
		// This won't compile if tinder didn't generate the right names...
		TestConfigDstu2 config = new TestConfigDstu2();
		List<ResourceTest> generics = config.testProvidersDstu2();
		for (ResourceTest generic : generics) {
			String name = generic.getResourceName();
		}
	}
	
}
