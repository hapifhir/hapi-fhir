package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.interceptor.ex.MockPartitioningInterceptor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.SqlParsingUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirPatchBuilder;
import net.sf.jsqlparser.JSQLParserException;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
		false, false
		true,  false
		true,  true
		""")
	void testCreate(boolean theConditional, boolean theAlreadyExists) throws JSQLParserException {
		/*
		 * Test
		 */
		String existingId = null;
		if (theAlreadyExists) {
			existingId = createPatient(withIdentifier("http://foo", "bar")).getIdPart();
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
			assertEquals("", outcome.getStorageOutcomes().get(0).getStatusCode());
			assertEquals(existingId, id);
		} else {
			assertEquals(201, outcome.getStorageOutcomes().get(0).getStatusCode());
		}

		assertEquals(0, myCaptureQueriesListener.logSelectQueries().size());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals("HFJ_RESOURCE", SqlParsingUtil.parseInsertStatementTableName(myCaptureQueriesListener.getInsertQueries().get(0).getSql(true, false)));
		Map<String, String> parsedInsert = SqlParsingUtil.parseInsertStatementParams(myCaptureQueriesListener.getInsertQueries().get(0).getSql(true, false));
		assertEquals("'" + id + "'", parsedInsert.get("RES_ID"));
		assertEquals("'1'", parsedInsert.get("PARTITION_ID"));

	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		false, false
		true,  false
		true,  true
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
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		# Conditional , AlreadyExists
		  false       , false
		  false       , true
		  true        , false
		  true        , true
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
		mySystemDao.transaction(newSrd(), requestBundle);

		/*
		 * Verify
		 */

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

	}




}


