package ca.uhn.fhir.context;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Device;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ModelScannerTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	private final FhirVersionEnum myVersionEnum = FhirVersionEnum.R4;

	@Test
	public void scan_device_includesPatientCompartment() {
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> definitions = new HashMap<>();
		Collection<Class<? extends IBase>> resourceTypes = new ArrayList<>();
		resourceTypes.add(Device.class);

		// test
		ModelScanner scanner = new ModelScanner(myFhirContext, myVersionEnum, definitions, resourceTypes);

		// verify
		assertFalse(scanner.getNameToResourceDefinition()
			 .get("device").getSearchParamsForCompartmentName("Patient")
			 .isEmpty());
	}
}
