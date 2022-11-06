package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This JUnit extension can be used to perform configuration of the
 * {@link RestfulServerExtension}, where non-static fields are available. This
 * is primarily useful for accessing Spring Test beans.
 */
public class RestfulServerConfigurerExtension implements BeforeEachCallback {

	private Supplier<RestfulServerExtension> myRestfulServerExtensionSupplier;
	private RestfulServerExtension myRestfulServerExtension;
	private final List<Consumer<RestfulServer>> myBeforeEachConsumers = new ArrayList<>();
	private final List<Consumer<RestfulServer>> myBeforeAllConsumers = new ArrayList<>();

	/**
	 * Constructor
	 */
	public RestfulServerConfigurerExtension(RestfulServerExtension theRestfulServerExtension) {
		Assert.notNull(theRestfulServerExtension, "theRestfulServerExtension must not be null");
		myRestfulServerExtension = theRestfulServerExtension;
	}

	/**
	 * Constructor - Use this if the server is dependency injected
	 */
	public RestfulServerConfigurerExtension(Supplier<RestfulServerExtension> theRestfulServerExtension) {
		Assert.notNull(theRestfulServerExtension, "theRestfulServerExtension must not be null");
		myRestfulServerExtensionSupplier = theRestfulServerExtension;
	}

	/**
	 * This callback will be invoked once after the server has started
	 */
	public RestfulServerConfigurerExtension withServerBeforeEach(Consumer<RestfulServer> theServer) {
		Assert.notNull(theServer, "theServer must not be null");
		myBeforeEachConsumers.add(theServer);
		return this;
	}

	/**
	 * This callback will be invoked before each test but after the server has started
	 */
	public RestfulServerConfigurerExtension withServerBeforeAll(Consumer<RestfulServer> theServer) {
		Assert.notNull(theServer, "theServer must not be null");
		myBeforeAllConsumers.add(theServer);
		return this;
	}

	@Override
	public void beforeEach(ExtensionContext theExtensionContext) throws Exception {
		ensureServerIsPresentAndRunning();

		String key = getClass().getName();

		// One time
		if (!myRestfulServerExtension.getRunningServerUserData().containsKey(key)) {
			myRestfulServerExtension.getRunningServerUserData().put(key, key);

			for (Consumer<RestfulServer> next : myBeforeEachConsumers) {
				myRestfulServerExtension.withServer(next::accept);
			}
		}

		// Every time
		for (Consumer<RestfulServer> next : myBeforeAllConsumers) {
			myRestfulServerExtension.withServer(next::accept);
		}
	}

	private void ensureServerIsPresentAndRunning() {
		if (myRestfulServerExtension == null) {
			myRestfulServerExtension = myRestfulServerExtensionSupplier.get();
		}
		Assert.isTrue(myRestfulServerExtension.isRunning(), "Server is not running");
	}
}
