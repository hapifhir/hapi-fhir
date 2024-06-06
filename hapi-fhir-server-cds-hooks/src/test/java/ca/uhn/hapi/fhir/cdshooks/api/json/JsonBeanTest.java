package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.test.util.HasGetterOrSetterForAllJsonFieldsAssert;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonBeanTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonBeanTest.class);

	@Test
	public void testAllCdsHooksJsonClasses() {
		Reflections reflections = new Reflections("ca.uhn.hapi.fhir.cdshooks.api.json");

		Set<Class<? extends IModelJson>> allJsonClasses =
			reflections.getSubTypesOf(IModelJson.class);

		assertThat(allJsonClasses).contains(CdsServiceJson.class);
		for (Class<? extends IModelJson> item : allJsonClasses) {
			HasGetterOrSetterForAllJsonFieldsAssert.assertThat(item).hasGetterOrSetterForAllJsonFields();
		}

		ourLog.info("Tested {} Json classes", allJsonClasses.size());
	}


}
