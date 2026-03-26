package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComboUniqueSearchParameterDateOffsetR4Test extends BaseResourceProviderR4Test {

	private static final String IDENTIFIER_SYSTEM = "http://example.org/fhir/NamingSystem/pharmacy-rx-number";
	private static final String IDENTIFIER_VALUE = "36055285";
	private static final String WHEN_PREPARED_OFFSET = "2026-03-24T12:17:47-04:00";
	private static final String WHEN_PREPARED_UTC = "2026-03-24T16:17:47Z";

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
		myStorageSettings.setUniqueIndexesEnabled(true);

		createComboUniqueSearchParamWithCompositionAndDate();
	}

	@Test
	void testSearch_withDottedSpCodeAndDateOffset_returnsResource() {
		Bundle document = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		 myClient.create().resource(document).execute();

		// Search with date offset -04:00 — should return the resource
		Bundle offsetResult = myClient.search()
			.byUrl("Bundle?composition.medicationdispense.currentrx.identifier="
				+ UrlUtil.escapeUrlParam(IDENTIFIER_SYSTEM + "|" + IDENTIFIER_VALUE)
				+ "&composition.medicationdispense.whenprepared="
				+ UrlUtil.escapeUrlParam(WHEN_PREPARED_OFFSET))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(offsetResult.getEntry())
			.as("Search with date offset -04:00 should return the resource")
			.hasSize(1);

		// Search with normalized date Z — should also return the resource
		Bundle utcResult = myClient.search()
			.byUrl("Bundle?composition.medicationdispense.currentrx.identifier="
				+ UrlUtil.escapeUrlParam(IDENTIFIER_SYSTEM + "|" + IDENTIFIER_VALUE)
				+ "&composition.medicationdispense.whenprepared="
				+ UrlUtil.escapeUrlParam(WHEN_PREPARED_UTC))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(utcResult.getEntry())
			.as("Search with normalized UTC date should return the resource")
			.hasSize(1);
	}

	@Test
	void testConditionalUpdate_withDottedSpCodeAndDateOffset_updatesExistingResource() {
		Bundle document = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		String bundleId = myClient.create().resource(document).execute().getId().toUnqualifiedVersionless().getValue();

		// Conditional update with date offset — should find and update, not 409
		Bundle updatedDocument = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		((Composition) updatedDocument.getEntry().get(0).getResource()).setTitle("Updated medication dispensed");
		String conditionalUrl = "Bundle?composition.medicationdispense.currentrx.identifier="
			+ UrlUtil.escapeUrlParam(IDENTIFIER_SYSTEM + "|" + IDENTIFIER_VALUE)
			+ "&composition.medicationdispense.whenprepared="
			+ UrlUtil.escapeUrlParam(WHEN_PREPARED_OFFSET);
		MethodOutcome updateOutcome = myClient.update().resource(updatedDocument)
			.conditionalByUrl(conditionalUrl)
			.execute();
		assertEquals(bundleId, updateOutcome.getId().toUnqualifiedVersionless().getValue());
		assertEquals("2", updateOutcome.getId().getVersionIdPart());
	}

	/**
	 * Verifies that the composite unique constraint prevents creating a duplicate
	 * resource with the same identifier + whenPrepared values.
	 */
	@Test
	void testCreate_duplicateWithSameComboValues_isRejected() {
		Bundle document = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		myClient.create().resource(document).execute();

		// Second create with identical values should be rejected by the unique constraint
		Bundle duplicate = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		assertThatThrownBy(() -> myClient.create().resource(duplicate).execute())
			.isInstanceOf(ResourceVersionConflictException.class);

		// Verify only the original resource exists
		Bundle searchResult = myClient.search()
			.forResource(Bundle.class)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(searchResult.getEntry()).hasSize(1);
	}

	/**
	 * Verifies that the composite unique constraint detects duplicates even when
	 * the same date-time is represented with a different timezone offset.
	 * Both -04:00 and the equivalent UTC (Z) should normalize to the same index key.
	 */
	@Test
	void testCreate_duplicateWithEquivalentUtcDate_isRejected() {
		Bundle document = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		myClient.create().resource(document).execute();

		// Second create with equivalent UTC time should also be rejected
		Bundle duplicate = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_UTC);
		assertThatThrownBy(() -> myClient.create().resource(duplicate).execute())
			.isInstanceOf(ResourceVersionConflictException.class);

		// Verify only the original resource exists
		Bundle searchResult = myClient.search()
			.forResource(Bundle.class)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(searchResult.getEntry()).hasSize(1);
	}

	/**
	 * Verifies that a search using ONLY the dotted date SP (no combo involved)
	 * works correctly with a timezone offset.
	 */
	@Test
	void testSearch_withDottedDateSpCodeOnly_returnsResource() {
		Bundle document = makeMedicationDispenseBundle(IDENTIFIER_SYSTEM, IDENTIFIER_VALUE, WHEN_PREPARED_OFFSET);
		myClient.create().resource(document).execute();

		// Search with only the dotted date SP and timezone offset
		Bundle offsetResult = myClient.search()
			.byUrl("Bundle?composition.medicationdispense.whenprepared="
				+ UrlUtil.escapeUrlParam(WHEN_PREPARED_OFFSET))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(offsetResult.getEntry())
			.as("Individual search with dotted date SP and offset should return the resource")
			.hasSize(1);

		// Same search with UTC
		Bundle utcResult = myClient.search()
			.byUrl("Bundle?composition.medicationdispense.whenprepared="
				+ UrlUtil.escapeUrlParam(WHEN_PREPARED_UTC))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(utcResult.getEntry())
			.as("Individual search with dotted date SP and UTC should return the resource")
			.hasSize(1);
	}

	private void createComboUniqueSearchParamWithCompositionAndDate() {
		// SP 1: Token SP with dotted code
		SearchParameter identifierSp = new SearchParameter();
		identifierSp.setId("bundle-composition-medicationdispense-currentrx-identifier");
		identifierSp.setUrl("http://example.org/SearchParameter/bundle-composition-medicationdispense-currentrx-identifier");
		identifierSp.setName("composition.medicationdispense.currentrx.identifier");
		identifierSp.setCode("composition.medicationdispense.currentrx.identifier");
		identifierSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		identifierSp.addBase("Bundle");
		identifierSp.setType(Enumerations.SearchParamType.TOKEN);
		identifierSp.setExpression("Bundle.entry.resource.ofType(MedicationDispense).identifier");
		myClient.update().resource(identifierSp).execute();

		// SP 2: Date SP with dotted code
		SearchParameter whenPreparedSp = new SearchParameter();
		whenPreparedSp.setId("bundle-composition-medicationdispense-whenprepared");
		whenPreparedSp.setUrl("http://example.org/SearchParameter/bundle-composition-medicationdispense-whenprepared");
		whenPreparedSp.setName("composition.medicationdispense.whenprepared");
		whenPreparedSp.setCode("composition.medicationdispense.whenprepared");
		whenPreparedSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		whenPreparedSp.addBase("Bundle");
		whenPreparedSp.setType(Enumerations.SearchParamType.DATE);
		whenPreparedSp.setExpression("Bundle.entry.resource.ofType(MedicationDispense).whenPrepared");
		myClient.update().resource(whenPreparedSp).execute();

		// SP 3: Composite unique SP with dotted code
		SearchParameter compositeUniqueSp = new SearchParameter();
		compositeUniqueSp.setId("bundle-composition-medicationDispense-composite");
		compositeUniqueSp.setUrl("http://example.org/SearchParameter/bundle-composition-medicationDispense-composite");
		compositeUniqueSp.setName("composition.medicationDispense.currentrx.identifier.and.whenPrepared");
		compositeUniqueSp.setCode("composition.medicationDispense.currentrx.identifier.and.whenPrepared");
		compositeUniqueSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		compositeUniqueSp.addBase("Bundle");
		compositeUniqueSp.setType(Enumerations.SearchParamType.COMPOSITE);
		compositeUniqueSp.setExpression("Bundle");
		compositeUniqueSp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		compositeUniqueSp.addComponent()
			.setExpression("Bundle.entry.resource.ofType(MedicationDispense).identifier")
			.setDefinition("SearchParameter/bundle-composition-medicationdispense-currentrx-identifier");
		compositeUniqueSp.addComponent()
			.setExpression("Bundle.entry.resource.ofType(MedicationDispense).whenPrepared")
			.setDefinition("SearchParameter/bundle-composition-medicationdispense-whenprepared");
		myClient.update().resource(compositeUniqueSp).execute();
	}

	private static Bundle makeMedicationDispenseBundle(String theIdentifierSystem,
													   String theIdentifierValue, String theWhenPrepared) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);

		Composition composition = new Composition();
		composition.setStatus(Composition.CompositionStatus.FINAL);
		composition.getType().addCoding().setSystem("http://loinc.org").setCode("29550-1");
		composition.setTitle("Medication dispensed");
		bundle.addEntry().setFullUrl("urn:uuid:" + UUID.randomUUID()).setResource(composition);

		MedicationDispense dispense = new MedicationDispense();
		dispense.addIdentifier().setSystem(theIdentifierSystem).setValue(theIdentifierValue);
		dispense.setWhenPreparedElement(new DateTimeType(theWhenPrepared));
		dispense.setStatus(MedicationDispense.MedicationDispenseStatus.COMPLETED);
		bundle.addEntry().setFullUrl("urn:uuid:" + UUID.randomUUID()).setResource(dispense);

		return bundle;
	}
}
