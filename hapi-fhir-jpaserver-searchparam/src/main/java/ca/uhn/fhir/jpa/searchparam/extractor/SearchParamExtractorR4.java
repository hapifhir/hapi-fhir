package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.utils.FHIRPathEngine;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParamExtractorR4 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	private FHIRPathEngine myFhirPathEngine;

	/**
	 * Constructor
	 */
	public SearchParamExtractorR4() {
		super();
	}

	// This constructor is used by tests
	@VisibleForTesting
	public SearchParamExtractorR4(ModelConfig theModelConfig, FhirContext theCtx, IValidationSupport theValidationSupport, ISearchParamRegistry theSearchParamRegistry) {
		super(theCtx, theSearchParamRegistry);
		initFhirPath(theValidationSupport);
		start();
	}

	@Override
	protected IValueExtractor getPathValueExtractor(IBaseResource theResource, String theSinglePath) {
		return () -> {
			List<Base> allValues = myFhirPathEngine.evaluate((Base) theResource, theSinglePath);
			return (List<IBase>) new ArrayList<IBase>(allValues);
		};
	}






	@Override
	@PostConstruct
	public void start() {
		super.start();
		if (myFhirPathEngine == null) {
			IValidationSupport support = myApplicationContext.getBean(IValidationSupport.class);
			initFhirPath(support);
		}
	}

	public void initFhirPath(IValidationSupport theSupport) {
		IWorkerContext worker = new HapiWorkerContext(getContext(), theSupport);
		myFhirPathEngine = new FHIRPathEngine(worker);
		myFhirPathEngine.setHostServices(new SearchParamExtractorR4HostServices());
	}


	private static class SearchParamExtractorR4HostServices implements FHIRPathEngine.IEvaluationContext {

		private Map<String, Base> myResourceTypeToStub = Collections.synchronizedMap(new HashMap<>());

		@Override
		public Base resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
			return null;
		}

		@Override
		public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
			return null;
		}

		@Override
		public boolean log(String argument, List<Base> focus) {
			return false;
		}

		@Override
		public FunctionDetails resolveFunction(String functionName) {
			return null;
		}

		@Override
		public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
			return null;
		}

		@Override
		public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
			return null;
		}

		@Override
		public Base resolveReference(Object theAppContext, String theUrl) throws FHIRException {

			/*
			 * When we're doing resolution within the SearchParamExtractor, if we want
			 * to do a resolve() it's just to check the type, so there is no point
			 * going through the heavyweight test. We can just return a stub and
			 * that's good enough since we're just doing something like
			 *    Encounter.patient.where(resolve() is Patient)
			 */
			IdType url = new IdType(theUrl);
			Base retVal = null;
			if (isNotBlank(url.getResourceType())) {

				retVal = myResourceTypeToStub.get(url.getResourceType());
				if (retVal != null) {
					return retVal;
				}

				ResourceType resourceType = ResourceType.fromCode(url.getResourceType());
				if (resourceType != null) {
					retVal = new Resource() {
						private static final long serialVersionUID = -5303169871827706447L;

						@Override
						public Resource copy() {
							return this;
						}

						@Override
						public ResourceType getResourceType() {
							return resourceType;
						}

						@Override
						public String fhirType() {
							return url.getResourceType();
						}

					};
					myResourceTypeToStub.put(url.getResourceType(), retVal);
				}
			}
			return retVal;
		}

		@Override
		public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
			return false;
		}

		@Override
		public ValueSet resolveValueSet(Object appContext, String url) {
			return null;
		}
	}

}
