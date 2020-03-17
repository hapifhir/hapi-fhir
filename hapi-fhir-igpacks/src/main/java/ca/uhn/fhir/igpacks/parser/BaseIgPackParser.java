package ca.uhn.fhir.igpacks.parser;

/*-
 * #%L
 * hapi-fhir-igpacks
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ImplementationGuide;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseIgPackParser<T> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseIgPackParser.class);
	private final FhirContext myCtx;

	public BaseIgPackParser(FhirContext theCtx) {
		FhirVersionEnum expectedVersion = provideExpectedVersion();
		Validate.isTrue(theCtx.getVersion().getVersion() == expectedVersion, "theCtx is not for the correct version, expecting " + expectedVersion);

		myCtx = theCtx;
	}

	public FhirContext getCtx() {
		return myCtx;
	}

	protected abstract T createValidationSupport(Map<IIdType, IBaseResource> theIgResources);

	private IBaseResource findResource(Map<String, IBaseResource> theCandidateResources, IIdType theId) {
		IBaseResource retVal = theCandidateResources.get(theId.toUnqualifiedVersionless().getValue());
		if (retVal == null) {
			throw new InternalErrorException("Unknown reference in ImplementationGuide: " + theId);
		}
		return retVal;
	}

	/**
	 * @param theIgInputStream The "validator.pack" ZIP file
	 * @param theDescription   A description (just used for logs)
	 */
	public T parseIg(InputStream theIgInputStream, String theDescription) {
		Validate.notNull(theIgInputStream, "theIdInputStream must not be null");
		String igResourceName = "ImplementationGuide-ig.json";

		ourLog.info("Parsing IGPack: {}", theDescription);
		StopWatch sw = new StopWatch();

		ZipInputStream zipInputStream = new ZipInputStream(theIgInputStream);
		ZipEntry entry;
		try {

			Map<String, IBaseResource> candidateResources = new HashMap<>();
			Map<IIdType, IBaseResource> igResources = new HashMap<>();

			while ((entry = zipInputStream.getNextEntry()) != null) {
				if (entry.getName().endsWith(".json")) {

					IBaseResource parsed;
					InputStreamReader nextReader = new InputStreamReader(zipInputStream, Constants.CHARSET_UTF8);

					if (entry.getName().equals(igResourceName)) {
						parsed = FhirContext.forDstu3().newJsonParser().parseResource(ImplementationGuide.class, nextReader);
					} else {
						LenientErrorHandler errorHandler = new LenientErrorHandler();
						errorHandler.setErrorOnInvalidValue(false);
						parsed = myCtx.newJsonParser().setParserErrorHandler(errorHandler).parseResource(nextReader);
					}

					candidateResources.put(entry.getName(), parsed);
				}
			}

			ourLog.info("Parsed {} candidateResources in {}ms", candidateResources.size(), sw.getMillis());

			ImplementationGuide ig = (ImplementationGuide) candidateResources.get(igResourceName);

			if (ig == null) {
				throw new InternalErrorException("IG Pack '" + theDescription + "' does not contain a resource named: " + igResourceName);
			}

			HashMap<String, IBaseResource> newCandidateResources = new HashMap<>();
			for (IBaseResource next : candidateResources.values()) {
				newCandidateResources.put(next.getIdElement().toUnqualifiedVersionless().getValue(), next);
			}
			candidateResources = newCandidateResources;

			for (ImplementationGuide.ImplementationGuidePackageComponent nextPackage : ig.getPackage()) {
				ourLog.info("Processing package {}", nextPackage.getName());

				for (ImplementationGuide.ImplementationGuidePackageResourceComponent nextResource : nextPackage.getResource()) {
					if (isNotBlank(nextResource.getSourceReference().getReference())) {
						IdType id = new IdType(nextResource.getSourceReference().getReference());
						if (isNotBlank(id.getResourceType())) {
							switch (id.getResourceType()) {
								case "CodeSystem":
								case "ConceptMap":
								case "StructureDefinition":
								case "ValueSet":
									IBaseResource resource = findResource(candidateResources, id);
									igResources.put(id.toUnqualifiedVersionless(), resource);
									break;
							}
						}
					}
				}

			}

			ourLog.info("IG contains {} resources", igResources.size());
			return createValidationSupport(igResources);

		} catch (Exception e) {
			throw new InternalErrorException("Failure while parsing IG: " + e, e);
		}


	}

	/**
	 * @param theIgPack      The "validator.pack" ZIP file
	 * @param theDescription A description (just used for logs)
	 */
	public T parseIg(byte[] theIgPack, String theDescription) {
		return parseIg(new ByteArrayInputStream(theIgPack), theDescription);
	}

	protected abstract FhirVersionEnum provideExpectedVersion();

}
