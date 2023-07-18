package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.test.util.HasGetterOrSetterForAllJsonFields;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class JsonBeanTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonBeanTest.class);

	@Test
	public void testAllCdsHooksJsonClasses() {
		Reflections reflections = new Reflections("ca.uhn.hapi.fhir.cdshooks.api.json");

		Set<Class<? extends IModelJson>> allJsonClasses =
			reflections.getSubTypesOf(IModelJson.class);

		assertThat(allJsonClasses, hasItem(CdsServiceJson.class));
		for (Class<? extends IModelJson> item : allJsonClasses) {
			assertThat(item, HasGetterOrSetterForAllJsonFields.hasGetterOrSetterForAllJsonFields());
		}

		ourLog.info("Tested {} Json classes", allJsonClasses.size());
	}


}
