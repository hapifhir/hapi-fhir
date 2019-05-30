package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.subscription.module.config.TestSubscriptionDstu3Config;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

@ContextConfiguration(classes = {TestSubscriptionDstu3Config.class})
public abstract class BaseSubscriptionDstu3Test extends BaseSubscriptionTest {

	private SubscriptionTestHelper mySubscriptionTestHelper = new SubscriptionTestHelper();

	public static void waitForSize(int theTarget, List<?> theList) {
		StopWatch sw = new StopWatch();
		while (theList.size() != theTarget && sw.getMillis() <= 16000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= 16000) {
			String describeResults = theList
				.stream()
				.map(t -> {
					if (t == null) {
						return "null";
					}
					if (t instanceof IBaseResource) {
						return ((IBaseResource) t).getIdElement().getValue();
					}
					return t.toString();
				})
				.collect(Collectors.joining(", "));
			fail("Size " + theList.size() + " is != target " + theTarget + " - Got: " + describeResults);
		}
	}

	protected long nextId() {
		return mySubscriptionTestHelper.nextId();
	}

	protected Subscription makeActiveSubscription(String theCriteria, String thePayload, String theEndpoint) {
		return mySubscriptionTestHelper.makeActiveSubscription(theCriteria, thePayload, theEndpoint);
	}

	protected Subscription makeSubscriptionWithStatus(String theCriteria, String thePayload, String theEndpoint, Subscription.SubscriptionStatus status) {
		return mySubscriptionTestHelper.makeSubscriptionWithStatus(theCriteria, thePayload, theEndpoint, status);
	}
}
