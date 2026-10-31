package org.hl7.fhir.r4.validation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemHierarchyMeaning;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.DiscriminatorType;
import org.hl7.fhir.r4.model.ElementDefinition.SlicingRules;
import org.hl7.fhir.r4.model.Enumerations.BindingStrength;
import org.hl7.fhir.r4.model.Enumerations.FHIRVersion;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionDifferentialComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.ParameterComponent;
import org.hl7.fhir.r4.model.Task.TaskIntent;
import org.hl7.fhir.r4.model.Task.TaskStatus;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

public class TaskValidationTest
{
	private static final Logger logger = LoggerFactory.getLogger(TaskValidationTest.class);

	private FhirValidator validator;

	@BeforeEach
	public void before() throws Exception
	{
		FhirContext context = FhirContext.forR4();
		HapiLocalizer localizer = new HapiLocalizer()
		{
			@Override
			public Locale getLocale()
			{
				return Locale.ROOT;
			}
		};
		context.setLocalizer(localizer);

		validator = createValidator(context);
	}

	private FhirValidator createValidator(FhirContext context)
	{
		PrePopulatedValidationSupport testResources = new PrePopulatedValidationSupport(context);
		testResources.addCodeSystem(createCodeSystem());
		testResources.addValueSet(createValueSet());
		testResources.addStructureDefinition(createStructureDefinition());

		IValidationSupport validationSupport = new ValidationSupportChain(
				new InMemoryTerminologyServerValidationSupport(context),
				new SnapshotGeneratingValidationSupport(context), testResources,
				new DefaultProfileValidationSupport(context), new CommonCodeSystemsTerminologyService(context));

		FhirValidator validator = context.newValidator();
		validator.registerValidatorModule(new FhirInstanceValidator(validationSupport));

		return validator;
	}

	private CodeSystem createCodeSystem()
	{
		CodeSystem c = new CodeSystem();
		c.setUrl("http://test.com/fhir/CodeSystem/test");
		c.setVersion("1.0.0");
		c.setName("Test_CodeSystem");
		c.setStatus(PublicationStatus.ACTIVE);
		c.setExperimental(true);
		c.setDate(new Date());
		c.setCaseSensitive(true);
		c.setHierarchyMeaning(CodeSystemHierarchyMeaning.GROUPEDBY);
		c.setVersionNeeded(false);
		c.setContent(CodeSystemContentMode.COMPLETE);
		c.addConcept().setCode("foo").setDisplay("Foo Display").setDefinition("Foo Definition");
		c.addConcept().setCode("bar").setDisplay("Bar Display").setDefinition("Bar Definition");
		c.addConcept().setCode("baz").setDisplay("Baz Display").setDefinition("Baz Definition");

		return c;
	}

	private ValueSet createValueSet()
	{
		ValueSet v = new ValueSet();
		v.setUrl("http://test.com/fhir/ValueSet/test");
		v.setVersion("1.0.0");
		v.setName("Test_ValueSet");
		v.setStatus(PublicationStatus.ACTIVE);
		v.setExperimental(true);
		v.setDate(new Date());
		v.setImmutable(true);
		v.getCompose().addInclude().setSystem("http://test.com/fhir/CodeSystem/test");

		return v;
	}

	private StructureDefinition createStructureDefinition()
	{
		StructureDefinition s = new StructureDefinition();
		s.setUrl("http://test.com/fhir/StructureDefinition/TestTask");
		s.setName("TestTask");
		s.setStatus(PublicationStatus.ACTIVE);
		s.setExperimental(true);
		s.setFhirVersion(FHIRVersion._4_0_1);
		s.setKind(StructureDefinitionKind.RESOURCE);
		s.setAbstract(false);
		s.setType("Task");
		s.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Task");
		s.setDerivation(TypeDerivationRule.CONSTRAINT);

		StructureDefinitionDifferentialComponent d = s.getDifferential();
		ElementDefinition e1 = d.addElement();
		e1.setId("Task.input");
		e1.setPath("Task.input");
		e1.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("type.coding.system");
		e1.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("type.coding.code");
		e1.getSlicing().setRules(SlicingRules.CLOSED);
		e1.setMin(3);
		e1.setMax("3");

		ElementDefinition e2 = d.addElement();
		e2.setId("Task.input:stringSlice1");
		e2.setPath("Task.input");
		e2.setSliceName("stringSlice1");
		e2.setMin(1);
		e2.setMax("1");

		ElementDefinition e3 = d.addElement();
		e3.setId("Task.input:stringSlice1.type");
		e3.setPath("Task.input.type");
		e3.getBinding().setStrength(BindingStrength.REQUIRED).setValueSet("http://test.com/fhir/ValueSet/test");

		ElementDefinition e4 = d.addElement();
		e4.setId("Task.input:stringSlice1.type.coding");
		e4.setPath("Task.input.type.coding");
		e4.setMin(1);
		e4.setMax("1");

		ElementDefinition e5 = d.addElement();
		e5.setId("Task.input:stringSlice1.type.coding.system");
		e5.setPath("Task.input.type.coding.system");
		e5.setMin(1);
		e5.setFixed(new UriType("http://test.com/fhir/CodeSystem/test"));

		ElementDefinition e6 = d.addElement();
		e6.setId("Task.input:stringSlice1.type.coding.code");
		e6.setPath("Task.input.type.coding.code");
		e6.setMin(1);
		e6.setFixed(new CodeType("foo"));

		ElementDefinition e7 = d.addElement();
		e7.setId("Task.input:stringSlice1.value[x]");
		e7.setPath("Task.input.value[x]");
		e7.setFixed(new StringType("foo-bar-baz"));

		ElementDefinition e8 = d.addElement();
		e8.setId("Task.input:booleanSlice");
		e8.setPath("Task.input");
		e8.setSliceName("booleanSlice");
		e8.setMin(1);
		e8.setMax("1");

		ElementDefinition e9 = d.addElement();
		e9.setId("Task.input:booleanSlice.type");
		e9.setPath("Task.input.type");
		e9.getBinding().setStrength(BindingStrength.REQUIRED).setValueSet("http://test.com/fhir/ValueSet/test");

		ElementDefinition e10 = d.addElement();
		e10.setId("Task.input:booleanSlice.type.coding");
		e10.setPath("Task.input.type.coding");
		e10.setMin(1);
		e10.setMax("1");

		ElementDefinition e11 = d.addElement();
		e11.setId("Task.input:booleanSlice.type.coding.system");
		e11.setPath("Task.input.type.coding.system");
		e11.setMin(1);
		e11.setFixed(new UriType("http://test.com/fhir/CodeSystem/test"));

		ElementDefinition e12 = d.addElement();
		e12.setId("Task.input:booleanSlice.type.coding.code");
		e12.setPath("Task.input.type.coding.code");
		e12.setMin(1);
		e12.setFixed(new CodeType("bar"));

		ElementDefinition e13 = d.addElement();
		e13.setId("Task.input:booleanSlice.value[x]");
		e13.setPath("Task.input.value[x]");
		e13.addType().setCode("boolean");

		ElementDefinition e14 = d.addElement();
		e14.setId("Task.input:stringSlice2");
		e14.setPath("Task.input");
		e14.setSliceName("stringSlice2");
		e14.setMin(1);
		e14.setMax("1");

		ElementDefinition e15 = d.addElement();
		e15.setId("Task.input:stringSlice2.type");
		e15.setPath("Task.input.type");
		e15.getBinding().setStrength(BindingStrength.REQUIRED).setValueSet("http://test.com/fhir/ValueSet/test");

		ElementDefinition e16 = d.addElement();
		e16.setId("Task.input:stringSlice2.type.coding");
		e16.setPath("Task.input.type.coding");
		e16.setMin(1);
		e16.setMax("1");

		ElementDefinition e17 = d.addElement();
		e17.setId("Task.input:stringSlice2.type.coding.system");
		e17.setPath("Task.input.type.coding.system");
		e17.setMin(1);
		e17.setFixed(new UriType("http://test.com/fhir/CodeSystem/test"));

		ElementDefinition e18 = d.addElement();
		e18.setId("Task.input:stringSlice2.type.coding.code");
		e18.setPath("Task.input.type.coding.code");
		e18.setMin(1);
		e18.setFixed(new CodeType("baz"));

		ElementDefinition e19 = d.addElement();
		e19.setId("Task.input:stringSlice2.value[x]");
		e19.setPath("Task.input.value[x]");
		e19.addType().setCode("string");

		return s;
	}

	@Test
	public void testTaskValidation() throws Exception
	{
		ValidationResult result = validator.validateWithResult(createValidTask());
		assertNotNull(result);
		assertNotNull(result.getMessages());

		result.getMessages().stream()
				.forEach(m -> logger.warn("{} - {}: {}", m.getSeverity(), m.getLocationString(), m.getMessage()));

		assertEquals(0, result.getMessages().size(),
				() -> result.getMessages().stream().map(SingleValidationMessage::getMessage)
						.collect(Collectors.joining("; ", "Validation messages: [", "]")));
	}

	private static Task createValidTask()
	{
		Task t = new Task();
		t.getMeta().addProfile("http://test.com/fhir/StructureDefinition/TestTask");
		t.setStatus(TaskStatus.REQUESTED);
		t.setIntent(TaskIntent.ORDER);

		ParameterComponent i1 = t.addInput();
		i1.getType().getCodingFirstRep().setSystem("http://test.com/fhir/CodeSystem/test").setCode("foo");
		i1.setValue(new StringType("foo-bar-baz"));

		ParameterComponent i2 = t.addInput();
		i2.getType().getCodingFirstRep().setSystem("http://test.com/fhir/CodeSystem/test").setCode("bar");
		i2.setValue(new BooleanType(true));

		ParameterComponent i3 = t.addInput();
		i3.getType().getCodingFirstRep().setSystem("http://test.com/fhir/CodeSystem/test").setCode("baz");
		i3.setValue(new StringType(UUID.randomUUID().toString()));

		return t;
	}
}
