package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class TransactionCapturingProviderExtension<T extends IBaseBundle> implements BeforeEachCallback, AfterEachCallback {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionCapturingProviderExtension.class);
	private final RestfulServerExtension myRestfulServerExtension;
	private final List<T> myInputBundles = Collections.synchronizedList(new ArrayList<>());
	private PlainProvider myProvider;

	/**
	 * Constructor
	 */
	public TransactionCapturingProviderExtension(RestfulServerExtension theRestfulServerExtension, Class<T> theBundleType) {
		myRestfulServerExtension = theRestfulServerExtension;
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myProvider = new PlainProvider();
		myRestfulServerExtension.getRestfulServer().unregisterProvider(myProvider);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		myRestfulServerExtension.getRestfulServer().registerProvider(myProvider);
		myInputBundles.clear();
	}

	public void waitForTransactionCount(int theCount) {
		assertThat(theCount, greaterThanOrEqualTo(myInputBundles.size()));
		await().until(()->myInputBundles.size(), equalTo(theCount));
	}

	public List<T> getTransactions() {
		return Collections.unmodifiableList(myInputBundles);
	}

	private class PlainProvider {

		@Transaction
		public T transaction(@TransactionParam T theInput) {
			ourLog.info("Received transaction update");
			myInputBundles.add(theInput);
			return theInput;
		}

	}


}
