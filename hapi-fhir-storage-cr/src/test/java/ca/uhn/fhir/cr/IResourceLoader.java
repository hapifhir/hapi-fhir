package ca.uhn.fhir.cr;

import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * This is a utility interface that allows a class that has a DaoRegistry to load Bundles and read Resources.
 * This is used primarily to set up integration tests for clinical reasoning operations since they often
 * require big bundles of content, such as Libraries, ValueSets, Measures, and so on.
 */
public interface IResourceLoader extends IDaoRegistryUser {
	/**
	 * Method to load bundles
	 * @param theType, resource type
	 * @param theLocation, location of the resource
	 * @return of type bundle
	 * @param <T>
	 */
	default <T extends IBaseBundle> T loadBundle(Class<T> theType, String theLocation) {
		var bundle = readResource(theType, theLocation);
		getDaoRegistry().getSystemDao().transaction(new SystemRequestDetails(), bundle);

		return bundle;
	}

	/**
	 * Method to read resource
	 * @param theType, resource type
	 * @param theLocation, location of the resource
	 * @return of type resource
	 * @param <T>
	 */
	default <T extends IBaseResource> T readResource(Class<T> theType, String theLocation) {
		return ClasspathUtil.loadResource(getFhirContext(), theType, theLocation);
	}

	/**
	 * Method to load resource
	 * @param theType, resource type
	 * @param theLocation, location of the resource
	 * @return of type resource
	 * @param <T>
	 */
	default <T extends IBaseResource> T loadResource(Class<T> theType, String theLocation, RequestDetails theRequestDetails) {
		var resource = readResource(theType, theLocation);
		getDaoRegistry().getResourceDao(theType).update(resource, theRequestDetails);

		return resource;
	}

	default public <T extends IBaseBundle> T loadBundle(String theLocation) {
		return loadBundle(theLocation);
	}

	default public IBaseResource readResource(String theLocation) {
		String resourceString = stringFromResource(theLocation);
		if (theLocation.endsWith("json")) {
			return loadResource("json", resourceString);
		} else {
			return loadResource("xml", resourceString);
		}
	}

	default public IBaseResource loadResource(String encoding, String resourceString) {
		IBaseResource resource = parseResource(encoding, resourceString);
		if (getDaoRegistry() == null) {
			return resource;
		}

		update(resource);
		return resource;
	}

	default public IBaseResource parseResource(String encoding, String resourceString) {
		IParser parser;
		switch (encoding.toLowerCase()) {
			case "json":
				parser = getFhirContext().newJsonParser();
				break;
			case "xml":
				parser = getFhirContext().newXmlParser();
				break;
			default:
				throw new IllegalArgumentException(
					String.format("Expected encoding xml, or json.  %s is not a valid encoding", encoding));
		}

		return parser.parseResource(resourceString);
	}

	default public String stringFromResource(String theLocation) {
		InputStream is = null;
		try {
			if (theLocation.startsWith(File.separator)) {
				is = new FileInputStream(theLocation);
			} else {
				DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
				org.springframework.core.io.Resource resource = resourceLoader.getResource(theLocation);
				is = resource.getInputStream();
			}
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error loading resource from %s", theLocation), e);
		}
	}
}
