package ca.uhn.fhir.rest.server.interceptor.validation.address;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ConfigLoader;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.interceptor.validation.helpers.ExtensionHelper;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AddressValidatingInterceptor extends ServerOperationInterceptorAdapter {

	private static final Logger ourLog = LoggerFactory.getLogger(AddressValidatingInterceptor.class);

	public static final String ADDRESS_TYPE_NAME = "Address";
	public static final String PROPERTY_VALIDATOR_CLASS = "validator.class";

	public static final String ADDRESS_VALIDATION_DISABLED_HEADER = "HAPI-Address-Validation-Disabled";

	private ExtensionHelper myExtensionHelper = new ExtensionHelper();

	private IAddressValidator myAddressValidator;

	private Properties myProperties;


	public AddressValidatingInterceptor() {
		super();

		ourLog.info("Starting AddressValidatingInterceptor {}", this);
		myProperties = ConfigLoader.loadProperties("classpath:address-validation.properties");
		start(myProperties);
	}

	public AddressValidatingInterceptor(Properties theProperties) {
		super();
		start(theProperties);
	}

	public void start(Properties theProperties) {
		if (!theProperties.containsKey(PROPERTY_VALIDATOR_CLASS)) {
			ourLog.info("Address validator class is not defined. Validation is disabled");
			return;
		}

		String validatorClassName = theProperties.getProperty(PROPERTY_VALIDATOR_CLASS);
		Validate.notBlank(validatorClassName, "%s property can not be blank", PROPERTY_VALIDATOR_CLASS);

		ourLog.info("Using address validator {}", validatorClassName);
		try {
			Class validatorClass = Class.forName(validatorClassName);
			IAddressValidator addressValidator;
			try {
				addressValidator = (IAddressValidator) validatorClass
					.getDeclaredConstructor(Properties.class).newInstance(theProperties);
			} catch (Exception e) {
				addressValidator = (IAddressValidator) validatorClass.getDeclaredConstructor().newInstance();
			}
			setAddressValidator(addressValidator);
		} catch (Exception e) {
			throw new RuntimeException("Unable to create validator", e);
		}
	}

	@Override
	public void resourcePreCreate(RequestDetails theRequest, IBaseResource theResource) {
		ourLog.debug("Validating address on for create {}, {}", theResource, theRequest);
		handleRequest(theRequest, theResource);
	}

	@Override
	public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		ourLog.debug("Validating address on for update {}, {}, {}", theOldResource, theNewResource, theRequest);
		handleRequest(theRequest, theNewResource);
	}

	protected void handleRequest(RequestDetails theRequest, IBaseResource theResource) {
		if (getAddressValidator() == null) {
			return;
		}

		if (!theRequest.getHeaders(ADDRESS_VALIDATION_DISABLED_HEADER).isEmpty()) {
			ourLog.debug("Address validation is disabled for this request via header");
		}

		FhirContext ctx = theRequest.getFhirContext();
		getAddresses(theResource, ctx)
			.stream()
			.filter(a -> {
				return !myExtensionHelper.hasExtension(a, IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL) ||
					myExtensionHelper.hasExtension(a, IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL, IAddressValidator.EXT_UNABLE_TO_VALIDATE);
			})
			.forEach(a -> validateAddress(a, ctx));
	}

	protected void validateAddress(IBase theAddress, FhirContext theFhirContext) {
		try {
			AddressValidationResult validationResult = getAddressValidator().isValid(theAddress, theFhirContext);
			myExtensionHelper.setValue(theAddress, IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL,
				validationResult.isValid() ? IAddressValidator.EXT_VALUE_VALID : IAddressValidator.EXT_VALUE_INVALID, theFhirContext);
		} catch (Exception ex) {
			myExtensionHelper.setValue(theAddress, IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL, IAddressValidator.EXT_UNABLE_TO_VALIDATE, theFhirContext);
		}
	}

	protected List<IBase> getAddresses(IBaseResource theResource, final FhirContext theFhirContext) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theResource);

		List<IBase> retVal = new ArrayList<>();
		for (BaseRuntimeChildDefinition c : definition.getChildren()) {
			Class childClass = c.getClass();
			List<IBase> allValues = c.getAccessor()
				.getValues(theResource)
				.stream()
				.filter(v -> ADDRESS_TYPE_NAME.equals(v.getClass().getSimpleName()))
				.collect(Collectors.toList());

			retVal.addAll(allValues);
		}

		return (List<IBase>) retVal;
	}

	public IAddressValidator getAddressValidator() {
		return myAddressValidator;
	}

	public void setAddressValidator(IAddressValidator theAddressValidator) {
		this.myAddressValidator = theAddressValidator;
	}

	public Properties getProperties() {
		return myProperties;
	}

	public void setProperties(Properties theProperties) {
		myProperties = theProperties;
	}
}
