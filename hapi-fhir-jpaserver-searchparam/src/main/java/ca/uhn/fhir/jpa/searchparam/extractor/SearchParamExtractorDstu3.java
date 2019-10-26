package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class SearchParamExtractorDstu3 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	@Autowired
	private org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport myValidationSupport;

	private HapiWorkerContext myWorkerContext;
	private FHIRPathEngine myFhirPathEngine;

	/**
	 * Constructor
	 */
	public SearchParamExtractorDstu3() {
		super();
	}

	// This constructor is used by tests
	@VisibleForTesting
	public SearchParamExtractorDstu3(ModelConfig theModelConfig, FhirContext theCtx, IValidationSupport theValidationSupport, ISearchParamRegistry theSearchParamRegistry) {
		super(theCtx, theSearchParamRegistry);
		myValidationSupport = theValidationSupport;
	}

	@Override
	protected IValueExtractor getPathValueExtractor(IBaseResource theResource, String thePaths) {
		return () -> {
			List<IBase> values = new ArrayList<>();
			String[] nextPathsSplit = split(thePaths);
			for (String nextPath : nextPathsSplit) {
				List<Base> allValues = myFhirPathEngine.evaluate((Base) theResource, trim(nextPath));
				if (allValues.isEmpty() == false) {
					values.addAll(allValues);
				}
			}

			return values;
		};
	}

	@Override
	@PostConstruct
	public void start() {
		myWorkerContext = new HapiWorkerContext(getContext(), myValidationSupport);
		myFhirPathEngine = new FHIRPathEngine(myWorkerContext);
	}

}
