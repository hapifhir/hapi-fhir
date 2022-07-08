package ca.uhn.fhir.cql.common.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface CqlProviderTestBase {

	FhirContext getTestFhirContext();

	DaoRegistry getDaoRegistry();

	default IBaseResource loadResource(String theLocation, RequestDetails theRequestDetails) throws IOException {
		String json = stringFromResource(theLocation);
		IBaseResource resource = getTestFhirContext().newJsonParser().parseResource(json);
		IFhirResourceDao<IBaseResource> dao = getDaoRegistry().getResourceDao(resource.getIdElement().getResourceType());
		if (dao == null) {
			return null;
		} else {
			dao.update(resource, theRequestDetails);
			return resource;
		}
	}

	default String stringFromResource(String theLocation) throws IOException {
		InputStream is = null;
		if (theLocation.startsWith(File.separator)) {
			is = new FileInputStream(theLocation);
		} else {
			DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource(theLocation);
			is = resource.getInputStream();
		}
		return IOUtils.toString(is, Charsets.UTF_8);
	}
}
