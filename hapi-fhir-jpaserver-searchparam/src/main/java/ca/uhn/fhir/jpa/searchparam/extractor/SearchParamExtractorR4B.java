/*
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4b.context.IWorkerContext;
import org.hl7.fhir.r4b.fhirpath.ExpressionNode;
import org.hl7.fhir.r4b.fhirpath.FHIRPathEngine;
import org.hl7.fhir.r4b.fhirpath.FHIRPathUtilityClasses.FunctionDetails;
import org.hl7.fhir.r4b.fhirpath.TypeDetails;
import org.hl7.fhir.r4b.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4b.model.Base;
import org.hl7.fhir.r4b.model.IdType;
import org.hl7.fhir.r4b.model.Resource;
import org.hl7.fhir.r4b.model.ResourceType;
import org.hl7.fhir.r4b.model.ValueSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParamExtractorR4B extends BaseSearchParamExtractor implements ISearchParamExtractor {

	private Cache<String, ExpressionNode> myParsedFhirPathCache;
	private FHIRPathEngine myFhirPathEngine;

	/**
	 * Constructor
	 */
	public SearchParamExtractorR4B() {
		super();
	}

	// This constructor is used by tests
	@VisibleForTesting
	public SearchParamExtractorR4B(
			StorageSettings theStorageSettings,
			PartitionSettings thePartitionSettings,
			FhirContext theCtx,
			ISearchParamRegistry theSearchParamRegistry) {
		super(theStorageSettings, thePartitionSettings, theCtx, theSearchParamRegistry);
		initFhirPath();
		start();
	}

	@Override
	public IValueExtractor getPathValueExtractor(IBase theResource, String theSinglePath) {
		return () -> {
			ExpressionNode parsed = myParsedFhirPathCache.get(theSinglePath, path -> myFhirPathEngine.parse(path));
			return myFhirPathEngine.evaluate(
					theResource, (Base) theResource, (Base) theResource, (Base) theResource, parsed);
		};
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
		myFhirPathEngine.setHostServices(new SearchParamExtractorR4BHostServices());

		myParsedFhirPathCache = CacheFactory.build(TimeUnit.MINUTES.toMillis(10));
	}

	private class SearchParamExtractorR4BHostServices implements FHIRPathEngine.IEvaluationContext {

		private final Map<String, Base> myResourceTypeToStub = Collections.synchronizedMap(new HashMap<>());

		@Override
		public List<Base> resolveConstant(
				FHIRPathEngine engine, Object appContext, String name, boolean beforeContext, boolean explicitConstant)
				throws PathEngineException {
			return Collections.emptyList();
		}

		@Override
		public TypeDetails resolveConstantType(
				FHIRPathEngine engine, Object appContext, String name, boolean explicitConstant)
				throws PathEngineException {
			return null;
		}

		@Override
		public boolean log(String argument, List<Base> focus) {
			return false;
		}

		@Override
		public FunctionDetails resolveFunction(FHIRPathEngine engine, String functionName) {
			return null;
		}

		@Override
		public TypeDetails checkFunction(
				FHIRPathEngine engine,
				Object appContext,
				String functionName,
				TypeDetails focus,
				List<TypeDetails> parameters)
				throws PathEngineException {
			return null;
		}

		@Override
		public List<Base> executeFunction(
				FHIRPathEngine engine,
				Object appContext,
				List<Base> focus,
				String functionName,
				List<List<Base>> parameters) {
			return null;
		}

		@Override
		public Base resolveReference(FHIRPathEngine engine, Object theAppContext, String theUrl, Base refContext) {
			Base retVal = (Base) BundleUtil.getReferenceInBundle(getContext(), theUrl, theAppContext);
			if (retVal != null) {
				return retVal;
			}

			/*
			 * When we're doing resolution within the SearchParamExtractor, if we want
			 * to do a resolve() it's just to check the type, so there is no point
			 * going through the heavyweight test. We can just return a stub and
			 * that's good enough since we're just doing something like
			 *    Encounter.patient.where(resolve() is Patient)
			 */
			IdType url = new IdType(theUrl);
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
		public boolean conformsToProfile(FHIRPathEngine engine, Object appContext, Base item, String url)
				throws FHIRException {
			return false;
		}

		@Override
		public ValueSet resolveValueSet(FHIRPathEngine engine, Object appContext, String url) {
			return null;
		}
	}
}
