package ca.uhn.fhir.models;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This is a test object that can be used to wrap a string
 * that could be injected as a spring-resource at bootup.
 */
public class TestResource extends AbstractResource {

	public static Resource createFromString(String theTxt) {
		TestResource resource = new TestResource(new ByteArrayResource(theTxt.getBytes(StandardCharsets.UTF_8)));

		return resource;
	}

	private Resource myResource;

	private TestResource(Resource theResource) {
		myResource = theResource;
	}

	@Override
	public String getDescription() {
		return getClass().getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return myResource.getInputStream();
	}

	@Override
	public URL getURL() throws IOException {
		return new URL("http://example.com/test");
	}
}
