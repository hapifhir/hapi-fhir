package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderDstu2Test;
import ca.uhn.fhir.jpa.subscription.email.JavaMailEmailSender;
import ca.uhn.fhir.jpa.subscription.email.SubscriptionEmailInterceptor;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class EmailSubscriptionDstu2Test extends BaseResourceProviderDstu2Test {

	private static final Logger ourLog = LoggerFactory.getLogger(EmailSubscriptionDstu2Test.class);
	private static GreenMail ourTestSmtp;
	private static int ourListenerPort;
	private SubscriptionEmailInterceptor mySubscriber;
	private List<IIdType> mySubscriptionIds = new ArrayList<>();

	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;
	@Autowired
	private AsyncTaskExecutor myAsyncTaskExecutor;

	@After
	public void after() throws Exception {
		ourLog.info("** AFTER **");
		super.after();

		for (IIdType next : mySubscriptionIds) {
			ourClient.delete().resourceById(next).execute();
		}
		mySubscriptionIds.clear();

		ourRestServer.unregisterInterceptor(mySubscriber);
	}

	@Before
	public void before() throws Exception {
		super.before();

		JavaMailEmailSender emailSender = new JavaMailEmailSender();
		emailSender.setSmtpServerHostname("localhost");
		emailSender.setSmtpServerPort(ourListenerPort);
		emailSender.start();

		mySubscriber = new SubscriptionEmailInterceptor();
		mySubscriber.setEmailSender(emailSender);
		mySubscriber.setResourceDaos(myResourceDaos);
		mySubscriber.setFhirContext(myFhirCtx);
		mySubscriber.setTxManager(ourTxManager);
		mySubscriber.setAsyncTaskExecutorForUnitTest(myAsyncTaskExecutor);
		mySubscriber.start();
		ourRestServer.registerInterceptor(mySubscriber);

//		ourLog.info("Sending test email to warm up the server");
//		EmailDetails details = new EmailDetails();
//		details.setFrom("a@a.com");
//		details.setTo(Arrays.asList("b@b.com"));
//		details.setSubjectTemplate("SUBJ");
//		details.setBodyTemplate("BODY");
//		emailSender.send(details);
//		ourLog.info("Done sending test email to warm up the server");
//		Store store = ourTestSmtp.getManagers().getImapHostManager().getStore();
//		MailFolder mailbox = store.getMailbox(ImapConstants.USER_NAMESPACE);
//		mailbox.deleteAllMessages();
	}

	private Subscription createSubscription(String criteria, String payload, String endpoint) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(SubscriptionStatusEnum.REQUESTED);
		subscription.setCriteria(criteria);

		Subscription.Channel channel = new Subscription.Channel();
		channel.setType(SubscriptionChannelTypeEnum.EMAIL);
		channel.setPayload(payload);
		channel.setEndpoint(endpoint);
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

		RestHookTestDstu2Test.waitForQueueToDrain(ourRestHookSubscriptionInterceptor);

		return subscription;
	}

	private Observation sendObservation(String code, String system) {
		Observation observation = new Observation();
		CodeableConceptDt codeableConcept = new CodeableConceptDt();
		observation.setCode(codeableConcept);
		CodingDt coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(ObservationStatusEnum.FINAL);

		MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();

		String observationId = methodOutcome.getId().getIdPart();
		observation.setId(observationId);

		return observation;
	}

	@Test
	public void testSubscribeAndDeliver() throws Exception {
		String payload = "A subscription update has been received";
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, "to1@example.com,to2@example.com");
		RestHookTestDstu2Test.waitForQueueToDrain(ourRestHookSubscriptionInterceptor);

		assertEquals(0, Arrays.asList(ourTestSmtp.getReceivedMessages()).size());

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		waitForSize(2, 60000, new Callable<Number>(){
			@Override
			public Number call() {
				int length = ourTestSmtp.getReceivedMessages().length;
				ourLog.trace("Have received {}", length);
				return length;
			}
		});

		MimeMessage[] messages = ourTestSmtp.getReceivedMessages();
		assertEquals(2, messages.length);
		int msgIdx = 0;
		ourLog.info("Received: " + GreenMailUtil.getWholeMessage(messages[msgIdx]));
		assertEquals("HAPI FHIR Subscriptions", messages[msgIdx].getSubject());
		assertEquals(1, messages[msgIdx].getFrom().length);
		assertEquals("noreply@unknown.com", ((InternetAddress) messages[msgIdx].getFrom()[0]).getAddress());
		assertEquals(2, messages[msgIdx].getAllRecipients().length);
		assertEquals("to1@example.com", ((InternetAddress) messages[msgIdx].getAllRecipients()[0]).getAddress());
		assertEquals("to2@example.com", ((InternetAddress) messages[msgIdx].getAllRecipients()[1]).getAddress());
		assertEquals(1, messages[msgIdx].getHeader("Content-Type").length);
		assertEquals("text/plain; charset=us-ascii", messages[msgIdx].getHeader("Content-Type")[0]);
		String foundBody = GreenMailUtil.getBody(messages[msgIdx]);
		assertEquals("A subscription update has been received", foundBody);

	}


	@AfterClass
	public static void afterClass() {
		ourTestSmtp.stop();
	}

	@BeforeClass
	public static void beforeClass() {
		ourListenerPort = RandomServerPortProvider.findFreePort();
		ServerSetup smtp = new ServerSetup(ourListenerPort, null, ServerSetup.PROTOCOL_SMTP);
		smtp.setServerStartupTimeout(2000);
		ourTestSmtp = new GreenMail(smtp);
		ourTestSmtp.start();
	}


}
