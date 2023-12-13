package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiscoveryValidationSupportTest extends BaseJpaR4Test {
   private static final FhirContext ourCtx = FhirContext.forR4();
   private static FhirValidator myFhirValidator;

   private static PrePopulatedValidationSupport ourValidationSupport;

   @BeforeAll
   public static void setup() {
      myFhirValidator = ourCtx.newValidator();
      myFhirValidator.setValidateAgainstStandardSchema(false);
      myFhirValidator.setValidateAgainstStandardSchematron(false);

      ourValidationSupport = new PrePopulatedValidationSupport(ourCtx);

      ValidationSupportChain chain = new ValidationSupportChain(
            new DefaultProfileValidationSupport(ourCtx),
		    new SnapshotGeneratingValidationSupport(ourCtx),
		    new CommonCodeSystemsTerminologyService(ourCtx),
		    new InMemoryTerminologyServerValidationSupport(ourCtx),
            ourValidationSupport);
      CachingValidationSupport myValidationSupport = new CachingValidationSupport(chain, true);
      FhirInstanceValidator myInstanceVal = new FhirInstanceValidator(myValidationSupport);
      myFhirValidator.registerValidatorModule(myInstanceVal);
   }

   @Test
   public void test() {
	   ValueSet vs = new ValueSet();
	   vs.setUrl("http://vs");
	   vs.getCompose().addInclude().setSystem("http://cs")
		   .addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")))
		   .addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code2")));
	   myValueSetDao.create(vs);

      StructureDefinition sd1 = new StructureDefinition()
		  .setUrl("http://example.org/fhir/StructureDefinition/TestObservation").setVersion("v1")
		  .setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation")
		  .setType("Observation")
		  .setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
	   sd1.getDifferential()
		   .addElement()
		   .setPath("Observation.value[x]")
		   .addType(new ElementDefinition.TypeRefComponent(new UriType("Quantity")))
		   .setBinding(new ElementDefinition.ElementDefinitionBindingComponent().setStrength(Enumerations.BindingStrength.REQUIRED).setValueSet("http://vs1"))
		   .setId("Observation.value[x]");
      myStructureDefinitionDao.create(sd1, mySrd);

      StructureDefinition sd2 = new StructureDefinition()
		  .setUrl("http://example.org/fhir/StructureDefinition/TestObservation").setVersion("v2")
		  .setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation")
		  .setType("Observation")
		  .setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
	   sd2.getDifferential()
		   .addElement()
		   .setPath("Observation.value[x]")
		   .addType(new ElementDefinition.TypeRefComponent(new UriType("Quantity")))
		   .setBinding(new ElementDefinition.ElementDefinitionBindingComponent().setStrength(Enumerations.BindingStrength.REQUIRED).setValueSet("http://vs"))
		   .setId("Observation.value[x]");
      myStructureDefinitionDao.create(sd2, mySrd);

      ourValidationSupport.addStructureDefinition(sd1);
      ourValidationSupport.addStructureDefinition(sd2);

      Observation observation = new Observation();
      observation.getMeta().addProfile("http://example.org/fhir/StructureDefinition/TestObservation");
      observation.setStatus(Observation.ObservationStatus.REGISTERED);
      observation.getCode().setText("new-visit");
	  observation.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));

      ValidationResult validationResult = myFhirValidator.validateWithResult(observation);
	  for (SingleValidationMessage message : validationResult.getMessages()) {
		  ourLog.info(message.toString());
	  }
      assertTrue(validationResult.isSuccessful());

   }

   @Test
   public void test_simple() {
	   StructureDefinition sdIdentifier = new StructureDefinition()
		   .setUrl("http://example.org/fhir/StructureDefinition/TestPatient").setVersion("1")
		   .setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient-identifier")
		   .setType("Patient")
		   .setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
	   sdIdentifier.getDifferential().addElement()
		   .setPath("Patient.identifier")
		   .setMin(1)
		   .setId("Patient.identifier");
	   myStructureDefinitionDao.create(sdIdentifier, mySrd);

	   StructureDefinition sdName = new StructureDefinition()
		   .setUrl("http://example.org/fhir/StructureDefinition/TestPatient").setVersion("2")
		   .setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient-name")
		   .setType("Patient")
		   .setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
	   sdName.getDifferential().addElement()
		   .setPath("Patient.name")
		   .setMin(1)
		   .setMustSupport(true)
		   .setId("Patient.name");
	   myStructureDefinitionDao.create(sdName, mySrd);

	   StructureDefinition sdBirthDate = new StructureDefinition()
		   .setUrl("http://example.org/fhir/StructureDefinition/TestPatient").setVersion("3")
		   .setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient")
		   .setType("Patient")
		   .setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
	   sdBirthDate.getDifferential().addElement()
		   .setPath("Patient.birthDate")
		   .setMin(1)
		   .setMustSupport(true)
		   .setId("Patient.birthDate");
	   myStructureDefinitionDao.create(sdBirthDate, mySrd);

	   ourValidationSupport.addStructureDefinition(sdIdentifier);
	   ourValidationSupport.addStructureDefinition(sdName);
	   ourValidationSupport.addStructureDefinition(sdBirthDate);

	   Patient patient = new Patient();
	   patient.getMeta()
		   .addProfile("http://example.org/fhir/StructureDefinition/TestPatient-name|1")
		   .addProfile("http://example.org/fhir/StructureDefinition/TestPatient-identifier|2");
	   myPatientDao.create(patient, mySrd);

	   ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
	   for (SingleValidationMessage message : validationResult.getMessages()) {
		   ourLog.info(message.toString());
	   }
	   assertTrue(validationResult.isSuccessful());
   }
}
