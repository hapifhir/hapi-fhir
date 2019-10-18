package ca.uhn.fhir.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class BaseTest {

	protected String loadResource(String theClasspath) throws IOException {
		Function<InputStream, InputStream> streamTransform = t->t;
		return loadResource(theClasspath, streamTransform);
	}

	private String loadResource(String theClasspath, Function<InputStream, InputStream> theStreamTransform) throws IOException {
		try (InputStream stream = BaseTest.class.getResourceAsStream(theClasspath)) {
			if (stream == null) {
				throw new IllegalArgumentException("Unable to find resource: " + theClasspath);
			}

			InputStream newStream = theStreamTransform.apply(stream);

			return IOUtils.toString(newStream, Charsets.UTF_8);
		}
	}

	protected String loadCompressedResource(String theClasspath) throws IOException {
		Function<InputStream, InputStream> streamTransform = t-> {
			try {
				return new GZIPInputStream(t);
			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
		};
		return loadResource(theClasspath, streamTransform);
	}

	protected <T extends IBaseResource> T loadResource(FhirContext theCtx, Class<T> theType, String theClasspath) throws IOException {
		String raw = loadResource(theClasspath);
		return EncodingEnum.detectEncodingNoDefault(raw).newParser(theCtx).parseResource(theType, raw);
	}
}
