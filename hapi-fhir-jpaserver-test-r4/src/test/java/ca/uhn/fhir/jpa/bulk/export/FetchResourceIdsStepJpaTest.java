package ca.uhn.fhir.jpa.bulk.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.FetchResourceIdsStep;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TagsTest;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.OrganizationAffiliation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

public class FetchResourceIdsStepJpaTest  extends BaseJpaR4Test {

	@Autowired
	private FetchResourceIdsStep myFetchResourceIdsStep;

	@Mock
	private IJobDataSink<ResourceIdList> mySink;
	@Captor
	private ArgumentCaptor<ResourceIdList> myResourceIdListCaptor;

	@Override
	public void afterCleanupDao() {
		super.afterCleanupDao();

		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setTagStorageMode(defaults.getTagStorageMode());
		myStorageSettings.setBulkExportFileMaximumSize(defaults.getBulkExportFileMaximumSize());
	}

	@Test
	public void testSystemBulkExportWithManyTags() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);

		mySearchParameterDao.create(FhirResourceDaoR4TagsTest.createSecuritySearchParameter(myFhirContext), mySrd);

		for (int i = 0; i < 10; i++) {
			OrganizationAffiliation orgAff = new OrganizationAffiliation();
			orgAff.getMeta().addSecurity().setSystem("http://foo").setCode("01B0");
			orgAff.setActive(true);
			myOrganizationAffiliationDao.create(orgAff, mySrd);
		}

		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setResourceTypes(List.of("OrganizationAffiliation"));
		params.setSince(new DateTimeType("2023-01-01").getValue());
		params.setFilters(List.of("OrganizationAffiliation?_security=01B0,01G0,01B0,01C0,17D0,02I0,02I0,02E0,03J0,03A0,03A0,03D0,03K0,05C0,04B0,04P0,05H0,05C0,04B0,06S0,06B0,06E0,07B0,07B0,07D0,08B0,08N0,08B0,08D0,09B0,09B0,09D0,10G0,10P0,10P0,10E0,11B0,11M0,11D0,12B0,12B0,12H0,13B0,13C0,14B0,15B0,14B0,14D0,15E0,16B0,16B0,16M0,16D0,18M0,17B0,20B0,20D0,22N0,22P0,22Q0,22S0,22B0,22B0,22B0,23B0,23E0,25E0,25B0,26B0,26B0,26H0,27B0,27B0,27B0,27Q2,28J0,28G0,29M0,28G0,28F0,30B0,30B0,31H0,31H0,31B0,32S0,32Q0,32Q0,32Y0,32R0,33B0,33B0,33D0,34P0,34V0,34P0,34U0,34P0,35B0,35E0,36P0,36B0,36K0,36F0,37B0,37B0,37D0,38B0,38F0,39D0,42B0,39B0,71A0,72A0,39W0,42B0,39W0,39F0,42F0,71B0,72B0,46B0,46K0,46B0,46P0,46E0,47B0,47F0,35A0,29A0,49A0,50I0,52S0,50I0,52S0,51P0,49A0,49B0,52G0,50J0,52V0,54A0,54B0,55A0,55A0,55D0,56B0,56B0,56D0,57A0,57B0,58B0,58A0,58B0,58D0,59H0,59H0,59C0,60B0,60M0,60F0,61B0,61S0,61F0,62A0,63B0,63B0,63B0,65B0,67B0,65R0,65Q0,65E0,67E0,68P0,68Q0,69B0,69B0,69C0,70J0,70G0,70B0"));
		VoidModel data = new VoidModel();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("instance-id");
		String chunkId = "chunk-id";
		StepExecutionDetails<BulkExportJobParameters, VoidModel> executionDetails = new StepExecutionDetails<>(params, data, instance, new WorkChunk().setId(chunkId));
		myCaptureQueriesListener.clear();

		// Test
		myFetchResourceIdsStep.run(executionDetails, mySink);

		// Verify
		verify(mySink, times(1)).accept(myResourceIdListCaptor.capture());
		ResourceIdList idList = myResourceIdListCaptor.getAllValues().get(0);
		assertThat(idList.getIds()).hasSize(10);
	}

	@Test
	public void testChunkMaximumSize() {
        myStorageSettings.setBulkExportFileMaximumSize(500);

		for (int i = 0; i < 100; i++) {
			OrganizationAffiliation orgAff = new OrganizationAffiliation();
			orgAff.setActive(true);
			myOrganizationAffiliationDao.create(orgAff, mySrd);
		}

		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setResourceTypes(List.of("OrganizationAffiliation"));
		VoidModel data = new VoidModel();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("instance-id");
		String chunkId = "chunk-id";
		StepExecutionDetails<BulkExportJobParameters, VoidModel> executionDetails = new StepExecutionDetails<>(params, data, instance, new WorkChunk().setId(chunkId));

		// Test
		myFetchResourceIdsStep.run(executionDetails, mySink);

		// Verify
		verify(mySink, Mockito.atLeast(1)).accept(myResourceIdListCaptor.capture());
		List<ResourceIdList> idLists = myResourceIdListCaptor.getAllValues();
		for (var next : idLists) {
			String serialized = JsonUtil.serialize(next, false);

			// Note that the 600 is a bit higher than the configured maximum of 500 above,
			// because our chunk size estimate is not totally accurate, but it's not
			// going to be way off, less than 100 regardless of how big the maximum is
			assertThat(serialized.length()).as(serialized).isLessThanOrEqualTo(600);
		}

	}

}
