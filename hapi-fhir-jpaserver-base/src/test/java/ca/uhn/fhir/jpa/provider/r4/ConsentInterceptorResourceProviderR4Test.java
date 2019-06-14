package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class ConsentInterceptorResourceProviderR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ConsentInterceptorResourceProviderR4Test.class);
	private List<String> myObservationIds;
	private List<String> myPatientIds;
	private List<String> myObservationIdsOddOnly;
	private List<String> myObservationIdsEvenOnly;
	private List<String> myObservationIdsEvenOnlyBackwards;
	private List<String> myObservationIdsBackwards;
	private ConsentInterceptor myConsentInterceptor;

	@Override
	@After
	public void after() throws Exception {
		super.after();
		Validate.notNull(myConsentInterceptor);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		ourRestServer.getInterceptorService().unregisterInterceptor(myConsentInterceptor);
	}

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		myDaoConfig.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));
	}

	@Test
	public void testSearchAndBlockSomeWithReject() {
		create50Observations();

		IConsentService consentService = new ConsentSvcCantSeeOddNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService);
		ourRestServer.getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search
		Bundle result = ourClient
			.search()
			.forResource("Observation")
			.sort()
			.ascending(Observation.SP_IDENTIFIER)
			.returnBundle(Bundle.class)
			.count(15)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, result);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnly.subList(0, 15), returnedIdValues);

		// Fetch the next page
		result = ourClient
			.loadPage()
			.next(result)
			.execute();
		resources = BundleUtil.toListOfResources(myFhirCtx, result);
		returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnly.subList(15, 25), returnedIdValues);
	}


	@Test
	public void testSearchMaskSubject() {
		create50Observations();

		IConsentService consentService = new ConsentSvcMaskObservationSubjects();
		myConsentInterceptor = new ConsentInterceptor(consentService);
		ourRestServer.getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search
		Bundle result = ourClient
			.search()
			.forResource("Observation")
			.sort()
			.ascending(Observation.SP_IDENTIFIER)
			.returnBundle(Bundle.class)
			.count(15)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, result);
		assertEquals(15, resources.size());
		resources.forEach(t -> {
			assertEquals(null, ((Observation) t).getSubject().getReference());
		});

		// Fetch the next page
		result = ourClient
			.loadPage()
			.next(result)
			.execute();
		resources = BundleUtil.toListOfResources(myFhirCtx, result);
		assertEquals(15, resources.size());
		resources.forEach(t -> {
			assertEquals(null, ((Observation) t).getSubject().getReference());
		});
	}


	@Test
	public void testHistoryAndBlockSome() {
		create50Observations();

		IConsentService consentService = new ConsentSvcCantSeeOddNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService);
		ourRestServer.getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search
		Bundle result = ourClient
			.history()
			.onServer()
			.returnBundle(Bundle.class)
			.count(10)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, result);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnlyBackwards.subList(0, 5), returnedIdValues);

	}


	@Test
	public void testReadAndBlockSome() {
		create50Observations();

		IConsentService consentService = new ConsentSvcCantSeeOddNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService);
		ourRestServer.getInterceptorService().registerInterceptor(myConsentInterceptor);

		myObservationDao.read(new IdType(myObservationIdsEvenOnly.get(0)), mySrd);
		myObservationDao.read(new IdType(myObservationIdsEvenOnly.get(1)), mySrd);

		try {
			myObservationDao.read(new IdType(myObservationIdsOddOnly.get(0)), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		try {
			myObservationDao.read(new IdType(myObservationIdsOddOnly.get(1)), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}


	private void create50Observations() {
		myPatientIds = new ArrayList<>();
		myObservationIds = new ArrayList<>();

		Patient p = new Patient();
		p.setActive(true);
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();
		myPatientIds.add(pid);

		for (int i = 0; i < 50; i++) {
			final Observation obs1 = new Observation();
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.addIdentifier().setSystem("urn:system").setValue("I" + leftPad("" + i, 5, '0'));
			obs1.getSubject().setReference(pid);
			IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();
			myObservationIds.add(obs1id.toUnqualifiedVersionless().getValue());
		}

		myObservationIdsEvenOnly =
			myObservationIds
				.stream()
				.filter(t -> Long.parseLong(t.substring(t.indexOf('/') + 1)) % 2 == 0)
				.collect(Collectors.toList());

		myObservationIdsOddOnly = ListUtils.removeAll(myObservationIds, myObservationIdsEvenOnly);
		myObservationIdsBackwards = Lists.reverse(myObservationIds);
		myObservationIdsEvenOnlyBackwards = Lists.reverse(myObservationIdsEvenOnly);
	}

	private class ConsentSvcMaskObservationSubjects implements IConsentService {
		@Override
		public ConsentOutcome startOperation(RestOperationTypeEnum theOperationTypeEnum, IServerInterceptor.ActionRequestDetails theRequestDetails) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome seeResource(RequestDetails theRequestDetails, IBaseResource theResource) {
			if (theResource instanceof Observation) {
				((Observation) theResource).getSubject().setReference("");
				((Observation) theResource).getSubject().setResource(null);
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, theResource);
			}
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException) {
			// nothing
		}

	}

	private static class ConsentSvcCantSeeOddNumbered implements IConsentService {
		@Override
		public ConsentOutcome startOperation(RestOperationTypeEnum theOperationTypeEnum, IServerInterceptor.ActionRequestDetails theRequestDetails) {
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource) {
			Long resIdLong = theResource.getIdElement().getIdPartAsLong();
			if (resIdLong % 2 == 1) {
				return new ConsentOutcome(ConsentOperationStatusEnum.REJECT);
			}
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome seeResource(RequestDetails theRequestDetails, IBaseResource theResource) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException) {
			// nothing
		}

	}
}
