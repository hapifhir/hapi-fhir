/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
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

	/**
	 * Userdata key indicating the source package ID for this package
	 */
	public static final String SOURCE_PACKAGE_ID =
			DefaultProfileValidationSupport.class.getName() + "_SOURCE_PACKAGE_ID";

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
	public DefaultProfileValidationSupport(@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "FhirContext must not be null");
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
		List<IBaseResource> retVal = myDelegate.fetchAllConformanceResources();
		addPackageInformation(retVal);
		return retVal;
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		List<T> retVal = myDelegate.fetchAllStructureDefinitions();
		addPackageInformation(retVal);
		return retVal;
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		List<T> retVal = myDelegate.fetchAllNonBaseStructureDefinitions();
		addPackageInformation(retVal);
		return retVal;
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		IBaseResource retVal = myDelegate.fetchCodeSystem(theSystem);
		addPackageInformation(retVal);
		return retVal;
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		IBaseResource retVal = myDelegate.fetchStructureDefinition(theUrl);
		addPackageInformation(retVal);
		return retVal;
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		IBaseResource retVal = myDelegate.fetchValueSet(theUrl);
		addPackageInformation(retVal);
		return retVal;
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

	private <T extends IBaseResource> void addPackageInformation(List<T> theResources) {
		if (theResources != null) {
			theResources.forEach(this::addPackageInformation);
		}
	}

	private void addPackageInformation(IBaseResource theResource) {
		if (theResource != null) {
			String sourcePackageId = null;
			switch (myCtx.getVersion().getVersion()) {
				case DSTU2:
				case DSTU2_HL7ORG:
					sourcePackageId = "hl7.fhir.r2.core";
					break;
				case DSTU2_1:
					return;
				case DSTU3:
					sourcePackageId = "hl7.fhir.r3.core";
					break;
				case R4:
					sourcePackageId = "hl7.fhir.r4.core";
					break;
				case R4B:
					sourcePackageId = "hl7.fhir.r4b.core";
					break;
				case R5:
					sourcePackageId = "hl7.fhir.r5.core";
					break;
			}

			Validate.notNull(
					sourcePackageId,
					"Don't know how to handle package ID: %s",
					myCtx.getVersion().getVersion());

			theResource.setUserData(SOURCE_PACKAGE_ID, sourcePackageId);
		}
	}
}
