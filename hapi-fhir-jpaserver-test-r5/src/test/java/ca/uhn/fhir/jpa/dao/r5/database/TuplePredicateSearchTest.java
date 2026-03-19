package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.SearchTestUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_EVERYTHING;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shared search test cases that verify FHIR searches work across all database
 * vendors. These tests exercise code paths that generate tuple predicates in
 * database partition mode, and standard predicates in non-partition mode.
 */
interface TuplePredicateSearchTest extends ITestDataBuilder {

	record Context(
		JpaStorageSettings storageSettings,
		RestfulServerExtension server
	) {}

	Context getTuplePredicateSearchTestContext();

	@Test
	default void testSecuritySearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType taggedId = createPatient(withActiveTrue(), withSecurity("http://sys", "code"));
		createPatient(withActiveTrue());

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?_security=http://sys|code")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(taggedId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testSecurityNotSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		createPatient(withActiveTrue(), withSecurity("http://sys", "code"));
		IIdType untaggedId = createPatient(withActiveTrue());

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?_security:not=http://sys|code")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(untaggedId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testTagSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType taggedId = createPatient(withActiveTrue(), withTag("http://sys", "tag1"));
		createPatient(withActiveTrue());

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?_tag=http://sys|tag1")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(taggedId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testTagNotSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		createPatient(withActiveTrue(), withTag("http://sys", "tag1"));
		IIdType untaggedId = createPatient(withActiveTrue());

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?_tag:not=http://sys|tag1")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(untaggedId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testTokenSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType maleId = createPatient(withActiveTrue(), withGender("male"));
		createPatient(withActiveTrue(), withGender("female"));

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?gender=male")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(maleId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testTokenNotSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		createPatient(withActiveTrue(), withGender("male"));
		IIdType femaleId = createPatient(withActiveTrue(), withGender("female"));

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?gender:not=male")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(femaleId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testEverythingOperation() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType patientId = createPatient(withActiveTrue());
		IIdType obsId = createObservation(withSubject(patientId));

		Bundle outcome = ctx.server().getFhirClient()
			.operation()
			.onInstanceVersion(new IdType(patientId.toUnqualifiedVersionless().getValue()))
			.named(OPERATION_EVERYTHING)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).containsExactlyInAnyOrder(
			patientId.toUnqualifiedVersionless().getValue(),
			obsId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testChainedReferenceSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		boolean previousIndexContained = ctx.storageSettings().isIndexOnContainedResources();
		ctx.storageSettings().setIndexOnContainedResources(true);
		try {
			IIdType patientId = createPatient(withActiveTrue(), withFamily("Smith"));
			createObservation(withSubject(patientId));

			Bundle results = ctx.server().getFhirClient()
				.search()
				.byUrl("Observation?subject.name=Smith")
				.returnBundle(Bundle.class)
				.execute();

			assertThat(results.getEntry()).hasSize(1);
		} finally {
			ctx.storageSettings().setIndexOnContainedResources(previousIndexContained);
		}
	}

	@Test
	default void testChainedIdSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType patientId = createPatient(withActiveTrue(), withFamily("Johnson"));
		IIdType obsId = createObservation(withSubject(patientId));

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Observation?subject._id=" + patientId.toUnqualifiedVersionless().getValue())
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(obsId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testChainedTokenSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType malePatientId = createPatient(withActiveTrue(), withGender("male"));
		IIdType femalePatientId = createPatient(withActiveTrue(), withGender("female"));
		IIdType maleObsId = createObservation(withSubject(malePatientId));
		createObservation(withSubject(femalePatientId));

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Observation?subject.gender=male")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(maleObsId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testCombinedTagNotAndSecurityNotSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		// Patient with excluded tag — should be filtered out by _tag:not
		createPatient(withActiveTrue(), withTag("http://sys", "exclude-tag"));
		// Patient with excluded security label — should be filtered out by _security:not
		createPatient(withActiveTrue(), withSecurity("http://sys", "exclude-sec"));
		// Patient with neither — should be the only result
		IIdType cleanId = createPatient(withActiveTrue());

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Patient?_tag:not=http://sys|exclude-tag&_security:not=http://sys|exclude-sec")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(cleanId.toUnqualifiedVersionless().getValue());
	}

	@Test
	default void testChainedTokenNotSearch() {
		Context ctx = getTuplePredicateSearchTestContext();
		IIdType malePatientId = createPatient(withActiveTrue(), withGender("male"));
		IIdType femalePatientId = createPatient(withActiveTrue(), withGender("female"));
		createObservation(withSubject(malePatientId));
		IIdType femaleObsId = createObservation(withSubject(femalePatientId));

		Bundle results = ctx.server().getFhirClient()
			.search()
			.byUrl("Observation?subject.gender:not=male")
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = SearchTestUtil.toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactly(femaleObsId.toUnqualifiedVersionless().getValue());
	}
}
