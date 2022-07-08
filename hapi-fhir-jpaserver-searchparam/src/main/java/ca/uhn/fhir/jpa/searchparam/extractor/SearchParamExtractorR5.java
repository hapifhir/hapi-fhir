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
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.model.Base;
import org.hl7.fhir.r5.model.ExpressionNode;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ResourceType;
import org.hl7.fhir.r5.model.TypeDetails;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.utils.FHIRPathEngine;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParamExtractorR5 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	private FHIRPathEngine myFhirPathEngine;
	private Cache<String, ExpressionNode> myParsedFhirPathCache;

	public SearchParamExtractorR5() {
		super();
	}

	/**
	 * Constructor for unit tests
	 */
	public SearchParamExtractorR5(ModelConfig theModelConfig, PartitionSettings thePartitionSettings, FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		super(theModelConfig, thePartitionSettings, theCtx, theSearchParamRegistry);
		initFhirPath();
		start();
	}

	@Override
	@PostConstruct
	public void start() {
		super.start();
		if (myFhirPathEngine == null) {
			initFhirPath();
		}
	}

	public void initFhirPath() {
		IWorkerContext worker = new HapiWorkerContext(getContext(), getContext().getValidationSupport());
		myFhirPathEngine = new FHIRPathEngine(worker);
		myFhirPathEngine.setHostServices(new SearchParamExtractorR5HostServices());

		myParsedFhirPathCache = Caffeine
			.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();
	}

	@Override
	public IValueExtractor getPathValueExtractor(IBaseResource theResource, String theSinglePath) {
		return () -> {
			ExpressionNode parsed = myParsedFhirPathCache.get(theSinglePath, path -> myFhirPathEngine.parse(path));
			return myFhirPathEngine.evaluate((Base) theResource, parsed);
		};
	}


	private static class SearchParamExtractorR5HostServices implements FHIRPathEngine.IEvaluationContext {

		private final Map<String, Base> myResourceTypeToStub = Collections.synchronizedMap(new HashMap<>());

		@Override
		public List<Base> resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
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
		public List<Base> executeFunction(Object appContext, List<Base> focus, String functionName, List<List<Base>> parameters) {
			return null;
		}

		@Override
		public Base resolveReference(Object appContext, String theUrl, Base refContext) throws FHIRException {

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
						private static final long serialVersionUID = 2368522971330181178L;

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
		public ValueSet resolveValueSet(Object theO, String theS) {
			return null;
		}

	}


}
