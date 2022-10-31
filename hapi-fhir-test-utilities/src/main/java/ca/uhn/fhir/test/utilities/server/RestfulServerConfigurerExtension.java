package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This JUnit extension can be used to perform configuration of the
 * {@link RestfulServerExtension}, where non-static fields are available. This
 * is primarily useful for accessing Spring Test beans.
 */
public class RestfulServerConfigurerExtension implements BeforeEachCallback {

	private final RestfulServerExtension myRestfulServerExtension;
	private final List<Consumer<RestfulServer>> myConsumers = new ArrayList<>();

	/**
	 * Constructor
	 */
	public RestfulServerConfigurerExtension(RestfulServerExtension theRestfulServerExtension) {
		Assert.notNull(theRestfulServerExtension, "theRestfulServerExtension must not be null");
		myRestfulServerExtension = theRestfulServerExtension;
	}

	public RestfulServerConfigurerExtension withServer(Consumer<RestfulServer> theServer) {
		Assert.notNull(theServer, "theServer must not be null");
		myConsumers.add(theServer);
		return this;
	}

	@Override
	public void beforeEach(ExtensionContext theExtensionContext) throws Exception {
		for (Consumer<RestfulServer> next : myConsumers) {
			myRestfulServerExtension.withServer(next::accept);
		}
	}
}
