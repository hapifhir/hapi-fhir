/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	private final FhirContext myCtx;
	private final IValidationSupport myStrategy;
	private final Runnable myFlush;

	/**
	 * Constructor
	 *
	 * @param theFhirContext The context to use
	 */
	public DefaultProfileValidationSupport(FhirContext theFhirContext) {
		myCtx = theFhirContext;
		if (theFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R5)) {
			IValidationSupport strategy = ReflectionUtil.newInstanceOrReturnNull("org.hl7.fhir.common.hapi.validation.support.DefaultProfileValidationSupportNpmStrategy", IValidationSupport.class, new Class[]{FhirContext.class}, new Object[]{theFhirContext});
			Validate.notNull(strategy, "Could not create validation support object. Do you have hapi-fhir-validation library on your classpath?");
			myStrategy = strategy;
			myFlush = ()->{};
		} else {
			DefaultProfileValidationSupportBundleStrategy strategy = new DefaultProfileValidationSupportBundleStrategy(theFhirContext);
			myStrategy = strategy;
			myFlush = strategy::flush;
		}
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		return myStrategy.fetchAllConformanceResources();
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return myStrategy.fetchAllStructureDefinitions();
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		return myStrategy.fetchAllNonBaseStructureDefinitions();
	}


	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return myStrategy.fetchCodeSystem(theSystem);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return myStrategy.fetchStructureDefinition(theUrl);
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		return myStrategy.fetchValueSet(theUrl);
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
		Optional<IBase> urlValue = theFhirContext.getResourceDefinition(theResource).getChildByName("url").getAccessor().getFirstValueOrNull(theResource);
		if (urlValue.isPresent()) {
			IPrimitiveType<?> urlValueType = (IPrimitiveType<?>) urlValue.get();
			urlValueString = urlValueType.getValueAsString();
		}
		return urlValueString;
	}


}
