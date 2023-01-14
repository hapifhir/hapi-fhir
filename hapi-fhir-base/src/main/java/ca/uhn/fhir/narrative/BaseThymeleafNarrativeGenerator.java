package ca.uhn.fhir.narrative;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.narrative2.BaseNarrativeGenerator;
import ca.uhn.fhir.narrative2.INarrativeTemplate;
import ca.uhn.fhir.narrative2.NarrativeTemplateManifest;
import ca.uhn.fhir.narrative2.ThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public abstract class BaseThymeleafNarrativeGenerator extends ThymeleafNarrativeGenerator {

	private volatile boolean myInitialized;

	/**
	 * Constructor
	 */
	protected BaseThymeleafNarrativeGenerator() {
		super();
	}

	@Override
	public boolean populateResourceNarrative(FhirContext theFhirContext, IBaseResource theResource) {
		initializeIfNecessary();
		super.populateResourceNarrative(theFhirContext, theResource);
		return false;
	}

	@Override
	public String generateResourceNarrative(FhirContext theFhirContext, IBaseResource theResource) {
		initializeIfNecessary();
		return super.generateResourceNarrative(theFhirContext, theResource);
	}

	protected void initializeIfNecessary() {
		if (!myInitialized) {
			initialize();
		}
	}

	protected abstract List<String> getPropertyFile();

	private synchronized void initialize() {
		if (myInitialized) {
			return;
		}

		List<String> propFileName = getPropertyFile();
		try {
			NarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation(propFileName);
			setManifest(manifest);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(1808) + e);
		}

		myInitialized = true;
	}


}
