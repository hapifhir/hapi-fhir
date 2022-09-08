package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageInstallerSvcImplTest {
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private PackageInstallerSvcImpl mySvc;


	@BeforeEach
	public void before() {
		mySvc = new PackageInstallerSvcImpl();
		mySvc.setFhirContextForUnitTest(myCtx);
	}

	@Test
	public void testValidForUpload_SearchParameterWithMetaParam() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("_id");
		assertFalse(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_SearchParameterWithNoBase() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.setExpression("Patient.name");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_SearchParameterWithNoExpression() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(sp));
	}


	@Test
	public void testValidForUpload_GoodSearchParameter() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.addBase("Patient");
		sp.setExpression("Patient.name");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertTrue(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_RequestedSubscription() {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		assertTrue(mySvc.validForUpload(subscription));
	}

	@Test
	public void testValidForUpload_ErrorSubscription() {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(Subscription.SubscriptionStatus.ERROR);
		assertFalse(mySvc.validForUpload(subscription));
	}

	@Test
	public void testValidForUpload_ActiveSubscription() {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(subscription));
	}

	@Test
	public void testValidForUpload_DocumentRefStatusValuePresent() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setStatus(Enumerations.DocumentReferenceStatus.ENTEREDINERROR);
		assertTrue(mySvc.validForUpload(documentReference));
	}

	@Test
	public void testValidForUpload_DocumentRefStatusValueNull() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setStatus(Enumerations.DocumentReferenceStatus.NULL);
		assertFalse(mySvc.validForUpload(documentReference));
		documentReference.setStatus(null);
		assertFalse(mySvc.validForUpload(documentReference));
	}

	@Test
	public void testValidForUpload_CommunicationStatusValuePresent() {
		Communication communication = new Communication();
		communication.setStatus(Communication.CommunicationStatus.NOTDONE);
		assertTrue(mySvc.validForUpload(communication));
	}

	@Test
	public void testValidForUpload_CommunicationStatusValueNull() {
		Communication communication = new Communication();
		communication.setStatus(Communication.CommunicationStatus.NULL);
		assertFalse(mySvc.validForUpload(communication));
		communication.setStatus(null);
		assertFalse(mySvc.validForUpload(communication));
	}
}
