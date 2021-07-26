package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReindexJobTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexJobTest.class);
	public static final String ALLELE_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/observation-geneticsAlleleName";
	public static final String ALLELE_SP = "alleleName";
	private static final String TEST_ALLELE_VALUE = "HERC";
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.REINDEX_JOB_NAME)
	private Job myReindexJob;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Test
	public void testReindexJob() throws Exception {
		// setup

		IIdType obsFinalId = createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		IIdType obsCancelledId = createObservationWithAlleleExtension(Observation.ObservationStatus.CANCELLED);

		createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The searchparam value is on the observation, but it hasn't been indexed yet
		assertEquals(0, countAlleleObservations());

		JobParameters jobParameters = DeleteExpungeJobParameterUtil.buildJobParameters("Observation?status=final");

		// execute
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myReindexJob, jobParameters);

		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// validate
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// Now one of them should be indexed
		assertEquals(1, countAlleleObservations());
		// FIXME KHS verify it is the right one (see IT)
	}


	private void createAlleleSearchParameter() {
		SearchParameter alleleName = new SearchParameter();
		alleleName.setId("SearchParameter/alleleName");
		alleleName.setStatus(Enumerations.PublicationStatus.ACTIVE);
		alleleName.addBase("Observation");
		alleleName.setCode(ALLELE_SP);
		alleleName.setType(Enumerations.SearchParamType.TOKEN);
		alleleName.setTitle("AlleleName");
		alleleName.setExpression("Observation.extension('" + ALLELE_EXTENSION_URL + "')");
		alleleName.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		mySearchParameterDao.create(alleleName);
	}

	private IIdType createObservationWithAlleleExtension(Observation.ObservationStatus theStatus) {
		Observation observation = new Observation();
		observation.addExtension(ALLELE_EXTENSION_URL, new StringType(TEST_ALLELE_VALUE));
		observation.setStatus(theStatus);
		return myObservationDao.create(observation).getId();
	}

	private int countAlleleObservations() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(ALLELE_SP, new TokenParam(TEST_ALLELE_VALUE));
		map.setSummaryMode(SummaryEnum.COUNT);
		ourLog.info("Searching with url {}", map.toNormalizedQueryString(myFhirCtx));
		IBundleProvider result = myObservationDao.search(map);
		return result.size();
	}
}
