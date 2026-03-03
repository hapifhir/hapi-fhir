package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.interceptor.ex.MockPartitioningInterceptor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.SqlParsingUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirPatchBuilder;
import net.sf.jsqlparser.JSQLParserException;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(properties = {
	JpaConstants.HAPI_DATABASE_PARTITION_MODE + "=true"
})
public class FhirSystemDaoTransactionExplicitPartitionR5Test extends BaseJpaR5Test {

	public static final RequestPartitionId PARTITION_1 = RequestPartitionId.fromPartitionId(1);
	private final MockPartitioningInterceptor myPartitionInterceptor = new MockPartitioningInterceptor();

	@BeforeEach
	public void before() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDatabasePartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(0);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(true);

		registerInterceptor(myPartitionInterceptor);

		// Just to warm up caches

		myPartitionInterceptor.setAlwaysReturnPartition(RequestPartitionId.fromPartitionId(99999));
		createPatient(withActiveFalse());
		myPartitionInterceptor.clearPartitions();
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		# Conditional , AlreadyExists
		  true        , false
		  false       , true
		""")
	void testDelete(boolean theConditional, boolean theAlreadyExists) {
		/*
		 * Setup
		 */

		myPartitionInterceptor.setAlwaysReturnPartition(PARTITION_1);
		String id;
		if (theAlreadyExists) {
			id = createPatient(withActiveTrue(), withIdentifier("http://foo", "bar")).getIdPart();
		} else {
			id = "999999";
		}

		/*
		 * Test
		 */

		// Create a bundle with an explicit partition on the entry
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		if (theConditional) {
			bb.addTransactionDeleteEntry("Patient?identifier=http://foo|bar");
		} else {
			bb.addTransactionDeleteEntry(new IdType("Patient/" + id));
		}
		Bundle requestBundle = bb.getBundleTyped();
		requestBundle.getEntry().get(0).setUserData(Constants.RESOURCE_PARTITION_ID, PARTITION_1);

		myPartitionInterceptor.clearPartitions();

		// Execute transaction
		myCaptureQueriesListener.clear();
		if (theConditional) {
			/*
			 * We don't currently support an explicit partition stored on the Bundle.entry userdata map for conditional
			 * deletes. The reason is that there's no easy way to pass the selected partition into the DAO from here.
			 * If any use cases come up which require this, we could consider finding a way to smuggle it in the
			 * TransactionDetails, but for now we'll leave this use case unaddressed and just throw an error.
			 */
			assertThatThrownBy(()->mySystemDao.transaction(newSrd(), requestBundle))
				.isInstanceOf(InternalErrorException.class)
				.hasMessageContaining("Can not specify explicit partition for conditional delete");
			return;
		} else {
			mySystemDao.transaction(newSrd(), requestBundle);
		}

		/*
		 * Verify
		 */

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		int queryIndex = 0;
		assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rht1_0.RES_ID,rht1_0.PARTITION_ID)=('" + id + "','1') and rht1_0.RES_VER='1'");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).contains(" where (rl1_0.TARGET_RESOURCE_ID,rl1_0.TARGET_RES_PARTITION_ID)=('" + id + "','1') ");
		assertEquals(queryIndex, myCaptureQueriesListener.countSelectQueries());

	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		# Conditional , AlreadyExists
		  false       , false
		  true        , false
		  true        , true
		""")
	void testCreate(boolean theConditional, boolean theAlreadyExists) throws JSQLParserException {
		/*
		 * Test
		 */
		String existingId = null;
		if (theAlreadyExists) {
			Patient patient = (Patient) buildPatient(withIdentifier("http://foo", "bar"));
			patient.setUserData(Constants.RESOURCE_PARTITION_ID, PARTITION_1);
			existingId = doCreateResource(patient).getIdPart();
		}

		// Create a bundle with an explicit partition on the entry
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		BundleBuilder.CreateBuilder entry = bb.addTransactionCreateEntry(buildPatient(withId(IdType.newRandomUuid()), withIdentifier("http://foo", "bar")));
		if (theConditional) {
			entry.conditional("Patient?identifier=http://foo|bar");
		}
		Bundle requestBundle = bb.getBundleTyped();
		requestBundle.getEntry().get(0).setUserData(Constants.RESOURCE_PARTITION_ID, PARTITION_1);

		myPartitionInterceptor.clearPartitions();

		// Execute transaction
		myCaptureQueriesListener.clear();
		Bundle responseBundle = mySystemDao.transaction(newSrd(), requestBundle);

		/*
		 * Verify
		 */

		TransactionUtil.TransactionResponse outcome = TransactionUtil.parseTransactionResponse(myFhirContext, requestBundle, responseBundle);
		String id = outcome.getStorageOutcomes().get(0).getTargetId().getIdPart();
		if (theAlreadyExists) {
			assertEquals(200, outcome.getStorageOutcomes().get(0).getStatusCode());
			assertEquals(existingId, id);
		} else {
			assertEquals(201, outcome.getStorageOutcomes().get(0).getStatusCode());
		}

		// Verify Select Queries
		int queryIndex = 0;
		if (theConditional) {
			assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).contains("rispt1_0.PARTITION_ID in ('1')");
		}
		if (theAlreadyExists) {
			assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		}
		assertEquals(queryIndex, myCaptureQueriesListener.logSelectQueries().size());

		// Verify Insert Queries
		myCaptureQueriesListener.logInsertQueries();
		if (theAlreadyExists) {
			assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		} else {
			assertEquals("HFJ_RESOURCE", SqlParsingUtil.parseInsertStatementTableName(myCaptureQueriesListener.getInsertQueries().get(0).getSql(true, false)));
			Map<String, String> parsedInsert = SqlParsingUtil.parseInsertStatementParams(myCaptureQueriesListener.getInsertQueries().get(0).getSql(true, false));
			assertEquals("'" + id + "'", parsedInsert.get("RES_ID"));
			assertEquals("'1'", parsedInsert.get("PARTITION_ID"));
		}
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		# Conditional , AlreadyExists
		  true        , true
		  true        , false
		  false       , true
		  false       , false
		""")
	void testPatch(boolean theConditional, boolean theAlreadyExists) {
		/*
		 * Setup
		 */

		myPartitionInterceptor.setAlwaysReturnPartition(PARTITION_1);
		String id;
		if (theAlreadyExists) {
			id = createPatient(withActiveTrue(), withIdentifier("http://foo", "bar")).getIdPart();
		} else {
			id = "A";
		}

		/*
		 * Test
		 */

		// Create a bundle with an explicit partition on the entry
		FhirPatchBuilder patchBuilder = new FhirPatchBuilder(myFhirContext);
		patchBuilder
			.replace()
			.path("Patient.active")
			.value(new BooleanType(false));
		Parameters patch = (Parameters) patchBuilder.build();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		BundleBuilder.PatchBuilder entry = bb.addTransactionFhirPatchEntry(new IdType("Patient/" + id), patch);
		if (theConditional) {
			entry.conditional("Patient?identifier=http://foo|bar");
		}
		Bundle requestBundle = bb.getBundleTyped();
		requestBundle.getEntry().get(0).setUserData(Constants.RESOURCE_PARTITION_ID, PARTITION_1);

		myPartitionInterceptor.clearPartitions();

		// Execute transaction
		myCaptureQueriesListener.clear();
		if (!theAlreadyExists) {
			assertThatThrownBy(()->mySystemDao.transaction(newSrd(), requestBundle))
				.isInstanceOf(ResourceNotFoundException.class);
			return;
		}
		mySystemDao.transaction(newSrd(), requestBundle);

		/*
		 * Verify
		 */

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		int queryIndex = 0;
		if (theConditional) {
			assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).contains("rispt1_0.PARTITION_ID in ('1')");
		}
		assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertEquals(queryIndex, myCaptureQueriesListener.countSelectQueries());

	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		# Conditional , AlreadyExists
		  true        , true
		  true        , false
		  false       , true
		  false       , false
		""")
	void testUpdate(boolean theConditional, boolean theAlreadyExists) {
		/*
		 * Setup
		 */

		myPartitionInterceptor.setAlwaysReturnPartition(PARTITION_1);
		String id;
		if (theAlreadyExists) {
			id = createPatient(withActiveTrue(), withIdentifier("http://foo", "bar")).getIdPart();
		} else {
			id = "A";
		}

		/*
		 * Test
		 */

		// Create a bundle with an explicit partition on the entry
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		BundleBuilder.UpdateBuilder entry = bb.addTransactionUpdateEntry(buildPatient(withId(id), withActiveFalse(), withIdentifier("http://foo", "bar")));
		if (theConditional) {
			entry.conditional("Patient?identifier=http://foo|bar");
		}
		Bundle requestBundle = bb.getBundleTyped();
		requestBundle.getEntry().get(0).setUserData(Constants.RESOURCE_PARTITION_ID, PARTITION_1);

		myPartitionInterceptor.clearPartitions();

		// Execute transaction
		myCaptureQueriesListener.clear();
		Bundle responseBundle = mySystemDao.transaction(newSrd(), requestBundle);

		/*
		 * Verify
		 */
		int queryIndex = 0;
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		if (theConditional) {
			assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).contains("rispt1_0.PARTITION_ID in ('1')");
		}
		if (!theAlreadyExists) {
			if (!theConditional) {
				assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where rt1_0.PARTITION_ID='1' and (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A')");
			}
		} else {
			assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
			assertThat(myCaptureQueriesListener.getSelectQueries().get(queryIndex++).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		}

		assertEquals(queryIndex, myCaptureQueriesListener.countSelectQueries());
	}




}


