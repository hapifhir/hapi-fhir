package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.mdm.provider.BaseLinkR4Test;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearStep;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ca.uhn.fhir.mdm.provider.MdmProviderDstu3Plus.DEFAULT_PAGE_SIZE;
import static ca.uhn.fhir.mdm.provider.MdmProviderDstu3Plus.MAX_PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

public class MdmControllerSvcImplTest extends BaseLinkR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmControllerSvcImplTest.class);

	@Autowired
	IMdmControllerSvc myMdmControllerSvc;

	@SpyBean
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private Batch2JobHelper myBatch2JobHelper;
	@Autowired
	private MdmSettings myMdmSettings;
	private UserRequestRetryVersionConflictsInterceptor myUserRequestRetryVersionConflictsInterceptor;
	private final RequestTenantPartitionInterceptor myPartitionInterceptor = new RequestTenantPartitionInterceptor();

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		myInterceptorService.registerInterceptor(myPartitionInterceptor);
		myMdmSettings.setEnabled(true);

		myUserRequestRetryVersionConflictsInterceptor = new UserRequestRetryVersionConflictsInterceptor();
		myInterceptorService.registerInterceptor(myUserRequestRetryVersionConflictsInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myMdmSettings.setEnabled(false);
		myInterceptorService.unregisterInterceptor(myUserRequestRetryVersionConflictsInterceptor);
		myPartitionSettings.setPartitioningEnabled(false);
		myInterceptorService.unregisterInterceptor(myPartitionInterceptor);
		super.after();
	}

	@Test
	public void testSurvivorshipIsCalledOnMatchingToTheSameGoldenResource() {
		assertLinkCount(1);

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);

		Patient patient = createPatientAndUpdateLinksOnPartition(buildFrankPatient(), requestPartitionId);

		getGoldenResourceFromTargetResource(patient);

		MdmLink link = (MdmLink) myMdmLinkDaoSvc.findMdmLinkBySource(patient).get();
		link.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		saveLink(link);
		assertEquals(MdmLinkSourceEnum.AUTO, link.getLinkSource());
		assertLinkCount(2);

		MdmPageRequest pageRequest = new MdmPageRequest((Integer) null, null, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE);
		MdmQuerySearchParameters params = new MdmQuerySearchParameters(pageRequest)
			.setSourceId(myPatientId.getIdElement().getValue());

		Page<MdmLinkJson> resultPage = myMdmControllerSvc.queryLinks(params,
			new MdmTransactionContext(MdmTransactionContext.OperationType.QUERY_LINKS),
			new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(1)));

		assertEquals(resultPage.getContent().size(), 1);

		assertEquals(resultPage.getContent().get(0).getSourceId(), patient.getIdElement().getResourceType() + "/" + patient.getIdElement().getIdPart());

		Mockito.verify(myRequestPartitionHelperSvc, Mockito.atLeastOnce()).validateHasPartitionPermissions(any(), eq("Patient"), argThat(new PartitionIdMatcher(requestPartitionId)));
	}

	@Test
	public void testMdmDuplicateGoldenResource() {
		assertLinkCount(1);

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);

		Patient patient = createPatientAndUpdateLinksOnPartition(buildFrankPatient(), requestPartitionId);

		getGoldenResourceFromTargetResource(patient);

		MdmLink link = (MdmLink) myMdmLinkDaoSvc.findMdmLinkBySource(patient).get();
		link.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		saveLink(link);
		assertEquals(MdmLinkSourceEnum.AUTO, link.getLinkSource());
		assertLinkCount(2);

		runInTransaction(()->{
			ourLog.info("Links: {}", myMdmLinkDao.findAll().stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));
		});

		myCaptureQueriesListener.clear();
		Page<MdmLinkJson> resultPage = myMdmControllerSvc.getDuplicateGoldenResources(null,
			new MdmPageRequest((Integer) null, null, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE),
			new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(1)), null);
		myCaptureQueriesListener.logSelectQueries();

		assertThat(resultPage.getContent()).hasSize(1);
		assertEquals(patient.getIdElement().getResourceType() + "/" + patient.getIdElement().getIdPart(), resultPage.getContent().get(0).getSourceId());

		Mockito.verify(myRequestPartitionHelperSvc, Mockito.atLeastOnce()).validateHasPartitionPermissions(any(), eq("Patient"), argThat(new PartitionIdMatcher(requestPartitionId)));
	}

	@Test
	public void testMdmClearWithProvidedResources() {
		assertLinkCount(1);

		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		createPractitionerAndUpdateLinksOnPartition(buildJanePractitioner(), requestPartitionId1);
		createPractitionerAndUpdateLinksOnPartition(buildJanePractitioner(), requestPartitionId2);
		assertLinkCount(3);

		List<String> urls = new ArrayList<>();
		urls.add("Practitioner");
		IPrimitiveType<BigDecimal> batchSize = new DecimalType(new BigDecimal(100));
		ServletRequestDetails details = new ServletRequestDetails();
		details.setTenantId(PARTITION_2);
		IBaseParameters clearJob = myMdmControllerSvc.submitMdmClearJob(urls, batchSize, details);
		Parameters.ParametersParameterComponent parameter = ((Parameters) clearJob).getParameter("jobId");
		String jobId = ((StringType) parameter.getValue()).getValueAsString();
		myBatch2JobHelper.awaitJobCompletion(jobId);

		assertLinkCount(2);
	}

	@Test
	public void testMdmClearWithWriteConflict() {
		AtomicBoolean haveFired = new AtomicBoolean(false);
		MdmClearStep.setClearCompletionCallbackForUnitTest(()->{
			if (haveFired.getAndSet(true) == false) {
				throw new ResourceVersionConflictException("Conflict");
			}
		});

		assertLinkCount(1);

		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		createPractitionerAndUpdateLinksOnPartition(buildJanePractitioner(), requestPartitionId1);
		createPractitionerAndUpdateLinksOnPartition(buildJanePractitioner(), requestPartitionId2);
		assertLinkCount(3);

		List<String> urls = new ArrayList<>();
		urls.add("Practitioner");
		IPrimitiveType<BigDecimal> batchSize = new DecimalType(new BigDecimal(100));
		ServletRequestDetails details = new ServletRequestDetails();
		details.setTenantId(PARTITION_2);
		IBaseParameters clearJob = myMdmControllerSvc.submitMdmClearJob(urls, batchSize, details);
		String jobId = ((StringType) ((Parameters) clearJob).getParameterValue("jobId")).getValueAsString();
		myBatch2JobHelper.awaitJobCompletion(jobId);

		assertLinkCount(2);
	}

	private class PartitionIdMatcher implements ArgumentMatcher<RequestPartitionId> {
		private RequestPartitionId myRequestPartitionId;

		PartitionIdMatcher(RequestPartitionId theRequestPartitionId) {
			myRequestPartitionId = theRequestPartitionId;
		}

		@Override
		public boolean matches(RequestPartitionId theRequestPartitionId) {
			return myRequestPartitionId.getPartitionIds().equals(theRequestPartitionId.getPartitionIds());
		}
	}

}
