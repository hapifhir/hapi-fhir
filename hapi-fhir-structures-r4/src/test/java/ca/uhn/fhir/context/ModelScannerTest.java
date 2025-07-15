package ca.uhn.fhir.context;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.ListResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ModelScannerTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	private final FhirVersionEnum myVersionEnum = FhirVersionEnum.R4;

	static List<Arguments> scannedResourcesInput() {
		List<Arguments> args = new ArrayList<>();

		// 1 Group -> member
		{
			args.add(
				 Arguments.of(Group.class, "group", new String[] { "member" })
			);
		}

		// 2 ListResource -> source, subject
		{
			args.add(
				 Arguments.of(ListResource.class, "list", new String[] { "source", "subject" })
			);
		}

		return args;
	}

	@ParameterizedTest
	@MethodSource("scannedResourcesInput")
	public void scan_resource_omitsSPsForPatientCompartment(Class<? extends IBase> theClazz, String theResourceName, String[] theSpsFormerlyInPatientCompartment) {
		// setup
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> definitions = new HashMap<>();
		Collection<Class<? extends IBase>> resourceTypes = new ArrayList<>();
		resourceTypes.add(theClazz);

		// test
		ModelScanner scanner = new ModelScanner(myFhirContext, myVersionEnum, definitions, resourceTypes);

		// verify
		for (String sp : theSpsFormerlyInPatientCompartment) {
			List<RuntimeSearchParam> spsInPatientCompartment = scanner.getNameToResourceDefinition()
				 .get(theResourceName).getSearchParamsForCompartmentName("Patient");
			assertFalse(spsInPatientCompartment
				 .stream().anyMatch(s -> s.getName().equals(sp)));
		}
	}

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
