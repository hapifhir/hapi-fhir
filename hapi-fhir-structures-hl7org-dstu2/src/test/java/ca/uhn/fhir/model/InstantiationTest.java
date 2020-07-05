package ca.uhn.fhir.model;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;

public class InstantiationTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testResources() throws IOException, ClassNotFoundException {
		FhirContext ctx = FhirContext.forDstu2Hl7Org();

		Properties prop = new Properties();
		prop.load(ctx.getVersion().getFhirVersionPropertiesFile());
		for (Entry<Object, Object> next : prop.entrySet()) {
			if (next.getKey().toString().startsWith("resource.")) {
				Class<? extends IBaseResource> clazz = (Class<? extends IBaseResource>) Class.forName(next.getValue().toString());
				RuntimeResourceDefinition res = ctx.getResourceDefinition(clazz);

				scanChildren(new HashSet<Class<?>>(), clazz, res);
			}
		}
	}

	private void scanChildren(HashSet<Class<?>> theHashSet, Class<? extends IBase> theClazz, BaseRuntimeElementCompositeDefinition<?> theRes) {
		for (BaseRuntimeChildDefinition next : theRes.getChildren()) {
			if (next.getElementName().contains("_")) {
				fail("Element name " + next.getElementName() + " in type " + theClazz + " contains illegal '_'");
			}

			if (next instanceof RuntimeChildResourceBlockDefinition) {
				RuntimeChildResourceBlockDefinition nextBlock = (RuntimeChildResourceBlockDefinition) next;
				for (String nextName : nextBlock.getValidChildNames()) {
					BaseRuntimeElementCompositeDefinition<?> elementDef = (BaseRuntimeElementCompositeDefinition<?>) nextBlock.getChildByName(nextName);
					if (theHashSet.add(elementDef.getImplementingClass())) {
						scanChildren(theHashSet, elementDef.getImplementingClass(), elementDef);
					}
				}
			}

		}
	}

}
