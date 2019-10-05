package ca.uhn.fhir.jpa;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class BaseTest {

	protected String loadResource(String theClasspath) throws IOException {
		InputStream stream = BaseTest.class.getResourceAsStream(theClasspath);
		if (stream==null) {
			throw new IllegalArgumentException("Unable to find resource: " + theClasspath);
		}
		return IOUtils.toString(stream, Charsets.UTF_8);
	}

}
