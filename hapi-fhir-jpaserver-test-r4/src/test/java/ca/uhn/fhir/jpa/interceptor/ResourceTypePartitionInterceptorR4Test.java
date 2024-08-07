package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceTypePartitionInterceptorR4Test extends BaseResourceProviderR4Test {
	private final MyPartitionSelectorInterceptor myPartitionInterceptor = new MyPartitionSelectorInterceptor();

	@Override
	@BeforeEach
	public void before() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(3).setName("PART-3"), null);
	}

	@AfterEach
	public void after() {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());
		myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);
	}

	@ParameterizedTest
	@CsvSource(value = {"Patient?, 1", "Observation?, 1", ",3"})
	public void reindex_withUrl_completesSuccessfully(String theUrl, int theExpectedIndexedResourceCount) {
		IIdType patientId = createPatient(withGiven("John"));
		createObservation(withSubject(patientId));
		createEncounter();

		Parameters input = new Parameters();
		input.setParameter(ProviderConstants.OPERATION_REINDEX_PARAM_URL, theUrl);
		Parameters response = myClient
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_REINDEX)
				.withParameters(input)
				.execute();

		String jobId = ((StringType)response.getParameterValue(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID)).getValue();
		myBatch2JobHelper.awaitJobHasStatus(jobId, StatusEnum.COMPLETED);
		assertThat(myBatch2JobHelper.getCombinedRecordsProcessed(jobId)).isEqualTo(theExpectedIndexedResourceCount);
	}

	public class MyPartitionSelectorInterceptor {
		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId selectPartitionCreate(IBaseResource theResource) {
			return selectPartition(myFhirContext.getResourceType(theResource));
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId selectPartitionRead(ReadPartitionIdRequestDetails theDetails) {
			return selectPartition(theDetails.getResourceType());
		}

		@Nonnull
		private static RequestPartitionId selectPartition(String resourceType) {
			return switch (resourceType) {
				case "Patient" -> RequestPartitionId.fromPartitionId(1);
				case "Observation" -> RequestPartitionId.fromPartitionId(2);
				default -> RequestPartitionId.fromPartitionId(3);
			};
		}

	}
}
