package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class SearchParamExtractorDstu3 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	private FHIRPathEngine myFhirPathEngine;

	/**
	 * Constructor
	 */
	public SearchParamExtractorDstu3() {
		super();
	}

	// This constructor is used by tests
	@VisibleForTesting
	public SearchParamExtractorDstu3(ModelConfig theModelConfig, PartitionSettings thePartitionSettings, FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		super(theModelConfig, thePartitionSettings, theCtx, theSearchParamRegistry);
		initFhirPathEngine();
		start();
	}

	@Override
	public IValueExtractor getPathValueExtractor(IBaseResource theResource, String theSinglePath) {
		return () -> {
			List<IBase> values = new ArrayList<>();
			List<Base> allValues = myFhirPathEngine.evaluate((Base) theResource, theSinglePath);
			if (allValues.isEmpty() == false) {
				values.addAll(allValues);
			}

			return values;
		};
	}


	@Override
	@PostConstruct
	public void start() {
		super.start();
		if (myFhirPathEngine == null) {
			initFhirPathEngine();
		}
	}

	public void initFhirPathEngine() {
		IWorkerContext worker = new HapiWorkerContext(getContext(), getContext().getValidationSupport());
		myFhirPathEngine = new FHIRPathEngine(worker);
	}

}
