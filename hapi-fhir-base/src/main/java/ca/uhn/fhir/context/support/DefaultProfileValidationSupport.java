/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.util.ILockable;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class returns the vocabulary that is shipped with the base FHIR
 * specification.
 *
 * Note that this class is version aware. For example, a request for
 * <code>http://foo-codesystem|123</code> will only return a value if
 * the built in resource if the version matches. Unversioned URLs
 * should generally be used, and will return whatever version is
 * present.
 */
public class DefaultProfileValidationSupport implements IValidationSupport {

	private static final Map<FhirVersionEnum, IValidationSupport> ourImplementations =
			Collections.synchronizedMap(new HashMap<>());
	private final FhirContext myCtx;
	/**
	 * This module just delegates all calls to a concrete implementation which will
	 * be in this field. Which implementation gets used depends on the FHIR version.
	 */
	private final IValidationSupport myDelegate;

	private final Runnable myFlush;

	/**
	 * Constructor
	 *
	 * @param theFhirContext The context to use
	 */
	public DefaultProfileValidationSupport(FhirContext theFhirContext) {
		myCtx = theFhirContext;

		IValidationSupport strategy;
		synchronized (ourImplementations) {
			strategy = ourImplementations.get(theFhirContext.getVersion().getVersion());

			if (strategy == null) {
				if (theFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R5)) {
					/*
					 * I don't love that we use reflection here, but this class is in
					 * hapi-fhir-base, and the class we're creating is in
					 * hapi-fhir-validation. There are complicated dependency chains that
					 * make this hard to clean up. At some point it'd be nice to figure out
					 * a cleaner solution though.
					 */
					strategy = ReflectionUtil.newInstance(
							"org.hl7.fhir.common.hapi.validation.support.DefaultProfileValidationSupportNpmStrategy",
							IValidationSupport.class,
							new Class[] {FhirContext.class},
							new Object[] {theFhirContext});
					((ILockable) strategy).lock();
				} else {
					strategy = new DefaultProfileValidationSupportBundleStrategy(theFhirContext);
				}
				ourImplementations.put(theFhirContext.getVersion().getVersion(), strategy);
			}
		}

		myDelegate = strategy;
		if (myDelegate instanceof DefaultProfileValidationSupportBundleStrategy) {
			myFlush = () -> ((DefaultProfileValidationSupportBundleStrategy) myDelegate).flush();
		} else {
			myFlush = () -> {};
		}
	}

	@Override
	public String getName() {
		return myCtx.getVersion().getVersion() + " FHIR Standard Profile Validation Support";
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		return myDelegate.fetchAllConformanceResources();
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return myDelegate.fetchAllStructureDefinitions();
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		return myDelegate.fetchAllNonBaseStructureDefinitions();
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return myDelegate.fetchCodeSystem(theSystem);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return myDelegate.fetchStructureDefinition(theUrl);
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		return myDelegate.fetchValueSet(theUrl);
	}

	public void flush() {
		myFlush.run();
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

	@Nullable
	public static String getConformanceResourceUrl(FhirContext theFhirContext, IBaseResource theResource) {
		String urlValueString = null;
		Optional<IBase> urlValue = theFhirContext
				.getResourceDefinition(theResource)
				.getChildByName("url")
				.getAccessor()
				.getFirstValueOrNull(theResource);
		if (urlValue.isPresent()) {
			IPrimitiveType<?> urlValueType = (IPrimitiveType<?>) urlValue.get();
			urlValueString = urlValueType.getValueAsString();
		}
		return urlValueString;
	}
}
