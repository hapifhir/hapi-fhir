package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public interface IValueSetExpansionIT {
	static final String CODE_SYSTEM_CODE = "PRODUCT-MULTI-SOURCE";
	static final String PROPERTY_NAME = "ACTIVE";

	static final String CODE_SYSTEM_STR_BASE =
			"""
								{
								"resourceType": "CodeSystem",
								"id": "4fb48e4e-57a4-4844-be74-d93707bdf9a1",
								"meta": {
									"versionId": "4",
									"lastUpdated": "2024-01-16T19:10:18.370+00:00",
									"source": "#c8957026d46dfab5"
								},
								"url": "https://health.gov.on.ca/idms/fhir/CodeSystem/Internal-Product-Types",
								"version": "1.0.0",
								"name": "IDMS-Internal-Product-Types",
								"status": "active",
								"date": "2024-01-10",
								"publisher": "IDMS",
								"description": "This contains a lists of Product Type codes.",
								"content": "complete",
								"property": [{
										"code": "ACTIVE",
										"type": "boolean"
								}],
								"concept": [
									{
										"code": "PRODUCT-MULTI-SOURCE",
										"display": "Multi source drug product streamlined or Multi source drug product non- streamlined",
										"property": [
											{
												"code": "ACTIVE",
												"valueBoolean": true
											}
										]
									}
								]
							}
					""";

	static final String VALUE_SET_STR_BASE =
			"""
						{
							"resourceType": "ValueSet",
							"id": "e0324e95-6d5c-4b08-8832-d5f5cd00a29a",
							"meta": {
								"versionId": "7",
								"lastUpdated": "2024-01-16T19:03:43.313+00:00",
								"source": "#1f91b035f91cd290"
							},
							"url": "https://health.gov.on.ca/idms/fhir/ValueSet/IDMS-Product-Types",
							"version": "1.0.0",
							"name": "IDMS-Product-Types",
							"title": "IDMS Product Types",
							"status": "active",
							"experimental": false,
							"date": "2024-01-16",
							"publisher": "IDMS",
							"description": "List of Product Types",
							"compose": {
								"include": [
									{
										"system": "https://health.gov.on.ca/idms/fhir/CodeSystem/Internal-Product-Types",
										"filter": [
											{
												"property": "ACTIVE",
												"op": "=",
												"value": "true"
											}
										]
									}
								]
							}
						}
					""";

	FhirContext getFhirContext();

	ITermDeferredStorageSvc getTerminologyDefferedStorageService();

	ITermReadSvc getTerminologyReadSvc();

	DaoRegistry getDaoRegistry();

	IFhirResourceDaoValueSet<ValueSet> getValueSetDao();

	JpaStorageSettings getJpaStorageSettings();

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withFiltersThatShouldNotMatchInInclude_addsNoNewCodes(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();

		// setup codesystem
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(new IntegerType(1));
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EXISTS:
				filterComponent.setProperty(PROPERTY_NAME + "-not");
				filterComponent.setValue(null);
				break;
			case IN:
				filterComponent.setValue("2,3,4");
				break;
			case NOTIN:
				filterComponent.setValue("1,2,3");
				break;
			case EQUAL:
				filterComponent.setValue("2");
				break;
			default:
				// just in case
				fail(theOperator.getDisplay() + " is not added for testing");
				break;
		}
		conceptSetComponent.setFilter(List.of(filterComponent));

		// test
		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();
		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doFailedValueSetExpansionTest(codeSystem, valueSet);
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withBooleanFilteredValuesInInclude_addsMatchingValues(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();
		// setup codesystem (nothing to do - base is already boolean friendly)
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue("true");
			case NOTIN -> filterComponent.setValue("false");
		}
		conceptSetComponent.setFilter(List.of(filterComponent)); // overwrite the filter

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doSuccessfulValueSetExpansionTest(codeSystem, valueSet);
			assertTrue(expanded.getExpansion().getContains().stream()
					.anyMatch(c -> c.getCode().equals(CODE_SYSTEM_CODE)));
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withIntegerFilteredValuesInInclude_addsMatchingValues(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();

		// setup codesystem
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(new IntegerType(1));
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue("1");
			case NOTIN -> filterComponent.setValue("2,3,4");
		}
		conceptSetComponent.setFilter(List.of(filterComponent));

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doSuccessfulValueSetExpansionTest(codeSystem, valueSet);

			assertTrue(expanded.getExpansion().getContains().stream()
					.anyMatch(c -> c.getCode().equals(CODE_SYSTEM_CODE)));
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withDecimalFilteredValuesInInclude_addsMatchingValues(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();

		// setup code system
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(new DecimalType(1.1));
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue("1.1");
			case NOTIN -> filterComponent.setValue("2.1,3.2,4.3");
		}
		conceptSetComponent.setFilter(List.of(filterComponent));

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doSuccessfulValueSetExpansionTest(codeSystem, valueSet);

			assertTrue(expanded.getExpansion().getContains().stream()
					.anyMatch(c -> c.getCode().equals(CODE_SYSTEM_CODE)));
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withDateTimeFilteredValuesInInclude_addsMatchingValues(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();
		Date now = new Date();
		DateTimeType dt = new DateTimeType(now);

		// setup codesystem
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(dt);
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);

		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue(dt.getValueAsString());
			case NOTIN -> {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < 3; i++) {
					DateTimeType arbitraryDateTime =
							new DateTimeType(Date.from(Instant.now().minus(i, ChronoUnit.SECONDS)));
					if (!sb.isEmpty()) {
						sb.append(",");
					}
					sb.append(arbitraryDateTime.getValueAsString());
				}
				filterComponent.setValue(sb.toString());
			}
		}
		conceptSetComponent.setFilter(List.of(filterComponent));

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doSuccessfulValueSetExpansionTest(codeSystem, valueSet);

			assertTrue(expanded.getExpansion().getContains().stream()
					.anyMatch(c -> c.getCode().equals(CODE_SYSTEM_CODE)));
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withBooleanFilterInExclude_doesNotAddMatchingCode(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();
		// setup codesystem (nothing to do - base is already boolean friendly)
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ValueSetComposeComponent composeComponent = valueSet.getCompose();
		ValueSet.ConceptSetComponent exclude = composeComponent.getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue("true");
			case NOTIN -> filterComponent.setValue("false");
		}
		exclude.setFilter(List.of(filterComponent));
		composeComponent.setExclude(List.of(exclude));
		composeComponent.setInclude(null);

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doFailedValueSetExpansionTest(codeSystem, valueSet);
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withIntegerFilteredValuesInExclude_doesNotAddMatchingCode(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();

		// setup codesystem
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(new IntegerType(1));
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue("1");
			case NOTIN -> filterComponent.setValue("2,3,4");
		}
		conceptSetComponent.setFilter(List.of(filterComponent));
		valueSet.getCompose().setExclude(List.of(conceptSetComponent));
		valueSet.getCompose().setInclude(null);

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doFailedValueSetExpansionTest(codeSystem, valueSet);
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withDecimalFilteredValuesInExclude_doesNotAddMatchingCode(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();

		// setup code system
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(new DecimalType(1.1));
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue("1.1");
			case NOTIN -> filterComponent.setValue("2.1,3.2,4.3");
		}
		conceptSetComponent.setFilter(List.of(filterComponent));
		valueSet.getCompose().setExclude(List.of(conceptSetComponent));
		valueSet.getCompose().setInclude(null);

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doFailedValueSetExpansionTest(codeSystem, valueSet);
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	@ParameterizedTest
	@EnumSource(
			value = ValueSet.FilterOperator.class,
			mode = EnumSource.Mode.INCLUDE,
			names = {"EQUAL", "EXISTS", "IN", "NOTIN"})
	default void expandByIdentifier_withDateTimeFilteredValuesInExclude_doesNotAddMatchingCode(
			ValueSet.FilterOperator theOperator) {
		// setup
		IParser parser = getFhirContext().newJsonParser();
		Date now = new Date();
		DateTimeType dt = new DateTimeType(now);

		// setup codesystem
		CodeSystem codeSystem = parser.parseResource(CodeSystem.class, CODE_SYSTEM_STR_BASE);
		CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent =
				codeSystem.getConcept().get(0);
		CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent();
		propertyComponent.setCode(PROPERTY_NAME);
		propertyComponent.setValue(dt);
		conceptDefinitionComponent.setProperty(List.of(propertyComponent));

		// setup valueset
		ValueSet valueSet = parser.parseResource(ValueSet.class, VALUE_SET_STR_BASE);
		ValueSet.ConceptSetComponent conceptSetComponent =
				valueSet.getCompose().getInclude().get(0);
		ValueSet.ConceptSetFilterComponent filterComponent = new ValueSet.ConceptSetFilterComponent();
		filterComponent.setProperty(PROPERTY_NAME);
		filterComponent.setOp(theOperator);
		switch (theOperator) {
			case EQUAL, EXISTS, IN -> filterComponent.setValue(dt.getValueAsString());
			case NOTIN -> {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < 3; i++) {
					DateTimeType arbitraryDateTime =
							new DateTimeType(Date.from(Instant.now().minus(i, ChronoUnit.SECONDS)));
					if (!sb.isEmpty()) {
						sb.append(",");
					}
					sb.append(arbitraryDateTime.getValueAsString());
				}
				filterComponent.setValue(sb.toString());
			}
		}
		conceptSetComponent.setFilter(List.of(filterComponent));
		valueSet.getCompose().setExclude(List.of(conceptSetComponent));
		valueSet.getCompose().setInclude(null);

		boolean preExpand = getJpaStorageSettings().isPreExpandValueSets();

		getJpaStorageSettings().setPreExpandValueSets(true);
		try {
			ValueSet expanded = doFailedValueSetExpansionTest(codeSystem, valueSet);
		} finally {
			getJpaStorageSettings().setPreExpandValueSets(preExpand);
		}
	}

	/**
	 * Runs the test for value set expansion that will find no new codes to add
	 * @param theCodeSystem
	 * @param theValueSet
	 * @return
	 */
	private ValueSet doFailedValueSetExpansionTest(CodeSystem theCodeSystem, ValueSet theValueSet) {
		ValueSet expandedValueSet = createCodeSystemAndValueSetAndReturnExpandedValueSet(theCodeSystem, theValueSet);

		// validate
		assertNotNull(expandedValueSet);
		assertNotNull(expandedValueSet.getExpansion());
		assertTrue(expandedValueSet.getExpansion().getContains().isEmpty());

		// pass back for additional validation
		return expandedValueSet;
	}

	/**
	 * Runs the test for value set expansion that will find codes to add
	 * @param theCodeSystem
	 * @param theValueSet
	 * @return
	 */
	private ValueSet doSuccessfulValueSetExpansionTest(CodeSystem theCodeSystem, ValueSet theValueSet) {
		ValueSet expandedValueSet = createCodeSystemAndValueSetAndReturnExpandedValueSet(theCodeSystem, theValueSet);

		// validate
		assertNotNull(expandedValueSet);
		assertNotNull(expandedValueSet.getExpansion());
		assertFalse(expandedValueSet.getExpansion().getContains().isEmpty());

		// pass back for additional validation
		return expandedValueSet;
	}

	private ValueSet createCodeSystemAndValueSetAndReturnExpandedValueSet(
			CodeSystem theCodeSystem, ValueSet theValueSet) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		String url = "https://health.gov.on.ca/idms/fhir/ValueSet/IDMS-Product-Types";

		// create the code system
		{
			@SuppressWarnings("unchecked")
			IFhirResourceDao<CodeSystem> codeSystemDao = getDaoRegistry().getResourceDao("CodeSystem");
			DaoMethodOutcome outcome = codeSystemDao.create(theCodeSystem, requestDetails);
			theCodeSystem.setId(outcome.getId());
			getTerminologyDefferedStorageService().saveAllDeferred();
		}
		// create the value set
		{
			@SuppressWarnings("unchecked")
			IFhirResourceDao<ValueSet> valueSetDao = getDaoRegistry().getResourceDao("ValueSet");
			DaoMethodOutcome outcome = valueSetDao.create(theValueSet, requestDetails);
			theValueSet.setId(outcome.getId());
			getTerminologyReadSvc().preExpandDeferredValueSetsToTerminologyTables();
		}

		// test
		ValueSetExpansionOptions options = new ValueSetExpansionOptions();
		ValueSet expandedValueSet = getValueSetDao().expandByIdentifier(url, options);
		return expandedValueSet;
	}
}
