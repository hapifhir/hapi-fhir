/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.cr;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.opencds.cqf.fhir.utility.Ids;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.google.common.base.Preconditions.checkNotNull;

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
	default <T extends IBaseResource> T loadResource(
		Class<T> theType, String theLocation, RequestDetails theRequestDetails) {
		var resource = readResource(theType, theLocation);
		getDaoRegistry().getResourceDao(theType).update(resource, theRequestDetails);

		return resource;
	}

	public default IBaseResource readResource(String theLocation) {
		String resourceString = stringFromResource(theLocation);
		return EncodingEnum.detectEncoding(resourceString)
			.newParser(getFhirContext())
			.parseResource(resourceString);
	}

	public default IBaseResource readAndLoadResource(String theLocation) {
		String resourceString = stringFromResource(theLocation);
		if (theLocation.endsWith("json")) {
			return loadResource(parseResource("json", resourceString));
		} else {
			return loadResource(parseResource("xml", resourceString));
		}
	}

	public default IBaseResource loadResource(IBaseResource theResource) {
		if (getDaoRegistry() == null) {
			return theResource;
		}

		update(theResource);
		return theResource;
	}

	public default IBaseResource parseResource(String theEncoding, String theResourceString) {
		IParser parser;
		switch (theEncoding.toLowerCase()) {
			case "json":
				parser = getFhirContext().newJsonParser();
				break;
			case "xml":
				parser = getFhirContext().newXmlParser();
				break;
			default:
				throw new IllegalArgumentException(
					String.format("Expected encoding xml, or json.  %s is not a valid encoding", theEncoding));
		}

		return parser.parseResource(theResourceString);
	}

	public default String stringFromResource(String theLocation) {
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

	default Object loadTransaction(String theLocation) {
		IBaseBundle resource = (IBaseBundle) readResource(theLocation);
		return transaction(resource, new SystemRequestDetails());
	}

	private Bundle.BundleEntryComponent createEntry(IBaseResource theResource) {
		return new Bundle.BundleEntryComponent()
			.setResource((Resource) theResource)
			.setRequest(createRequest(theResource));
	}

	private Bundle.BundleEntryRequestComponent createRequest(IBaseResource theResource) {
		Bundle.BundleEntryRequestComponent request = new Bundle.BundleEntryRequestComponent();
		if (theResource.getIdElement().hasValue()) {
			request.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl(theResource.getIdElement().getValue());
		} else {
			request.setMethod(Bundle.HTTPVerb.POST).setUrl(theResource.fhirType());
		}

		return request;
	}

	default <T extends IBaseResource> T newResource(Class<T> theResourceClass, String theIdPart) {
		checkNotNull(theResourceClass);
		checkNotNull(theIdPart);

		T newResource = newResource(theResourceClass);
		newResource.setId((IIdType) Ids.newId(getFhirContext(), newResource.fhirType(), theIdPart));

		return newResource;
	}

	@SuppressWarnings("unchecked")
	default <T extends IBaseResource> T newResource(Class<T> theResourceClass) {
		checkNotNull(theResourceClass);

		return (T) this.getFhirContext().getResourceDefinition(theResourceClass).newInstance();
	}
}
