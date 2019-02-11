package org.hl7.fhir.convertors;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.Resource;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

public class VersionConvertor_10_30Test {

	@Test
	public void testConvert() throws FHIRException {
		
		NullVersionConverterAdvisor30 advisor = new NullVersionConverterAdvisor30();
		VersionConvertor_10_30 converter = new VersionConvertor_10_30(advisor);
		
		org.hl7.fhir.instance.model.Observation input = new org.hl7.fhir.instance.model.Observation();
		input.setEncounter(new org.hl7.fhir.instance.model.Reference("Encounter/123"));
		
		org.hl7.fhir.dstu3.model.Observation output = converter.convertObservation(input);
		String context = output.getContext().getReference();
		
		assertEquals("Encounter/123", context);
	}

	@Test
	public void testConvertSpecimen() throws FHIRException {

		NullVersionConverterAdvisor30 advisor = new NullVersionConverterAdvisor30();
		VersionConvertor_10_30 converter = new VersionConvertor_10_30(advisor);

		Specimen spec = new Specimen();
		CodeableConcept cc = new CodeableConcept();
		Coding coding = new Coding();
		coding.setSystem("test_system");
		coding.setCode("test_code");
		coding.setDisplay("test_display");
		cc.addCoding(coding);
		spec.setType(cc);
		spec.setId("76c1143a-974a-4dfe-8f1a-4292b02d323d");
		spec.setReceivedTime(new Date());
		spec.addExtension().setUrl("testurl!").setValue(new StringType("Yup its an extension"));
		spec.setSubject(new Reference("Patient/123"));
		spec.getRequest().add(new Reference("Practitioner/321"));
		Specimen.SpecimenCollectionComponent specimenCollectionComponent = new Specimen.SpecimenCollectionComponent();
		specimenCollectionComponent.setCollected(new DateTimeType(new Date()));
		SimpleQuantity simpleQuantity = new SimpleQuantity();
		simpleQuantity.setUnit("buckets");
		simpleQuantity.setValue(1000L);

		specimenCollectionComponent.setQuantity(simpleQuantity);
		spec.setCollection(specimenCollectionComponent);
		Specimen.SpecimenContainerComponent specimenContainerComponent = new Specimen.SpecimenContainerComponent();
		specimenContainerComponent.getExtension().add(new Extension().setUrl("some_url").setValue(new StringType("some_value")));
		spec.setContainer(Collections.singletonList(specimenContainerComponent));
		Resource resource = converter.convertResource(spec);
	}

	
}
