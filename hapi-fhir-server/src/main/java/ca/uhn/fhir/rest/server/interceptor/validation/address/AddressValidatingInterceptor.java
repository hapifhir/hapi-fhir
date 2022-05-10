package ca.uhn.fhir.rest.server.interceptor.validation.address;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ConfigLoader;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor2;
import ca.uhn.fhir.util.TerserUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Interceptor
public class AddressValidatingInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(AddressValidatingInterceptor.class);

	public static final String ADDRESS_TYPE_NAME = "Address";
	public static final String PROPERTY_VALIDATOR_CLASS = "validator.class";
	public static final String PROPERTY_EXTENSION_URL = "extension.url";

	public static final String ADDRESS_VALIDATION_DISABLED_HEADER = "HAPI-Address-Validation-Disabled";

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
		myProperties = theProperties;
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
			throw new RuntimeException(Msg.code(344) + "Unable to create validator", e);
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(RequestDetails theRequest, IBaseResource theResource) {
		ourLog.debug("Validating address on for create {}, {}", theResource, theRequest);
		handleRequest(theRequest, theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		ourLog.debug("Validating address on for update {}, {}, {}", theOldResource, theNewResource, theRequest);
		handleRequest(theRequest, theNewResource);
	}

	protected void handleRequest(RequestDetails theRequest, IBaseResource theResource) {
		if (getAddressValidator() == null) {
			ourLog.debug("Address validator is not provided - validation disabled");
			return;
		}

		if (theRequest == null) {
			ourLog.debug("RequestDetails is null - unable to validate address for {}", theResource);
			return;
		}

		if (!theRequest.getHeaders(ADDRESS_VALIDATION_DISABLED_HEADER).isEmpty()) {
			ourLog.debug("Address validation is disabled for this request via header");
			return;
		}

		FhirContext ctx = theRequest.getFhirContext();
		List<IBase> addresses = getAddresses(theResource, ctx)
			.stream()
			.filter(this::isValidating)
			.collect(Collectors.toList());

		if (!addresses.isEmpty()) {
			validateAddresses(theRequest, theResource, addresses);
		}
	}

	/**
	 * Validates specified child addresses for the resource
	 *
	 * @return Returns true if all addresses are valid, or false if there is at least one invalid address
	 */
	protected boolean validateAddresses(RequestDetails theRequest, IBaseResource theResource, List<IBase> theAddresses) {
		boolean retVal = true;
		for (IBase address : theAddresses) {
			retVal &= validateAddress(address, theRequest.getFhirContext());
		}
		return retVal;
	}

	private boolean isValidating(IBase theAddress) {
		IBaseExtension ext = ExtensionUtil.getExtensionByUrl(theAddress, getExtensionUrl());
		if (ext == null) {
			return true;
		}
		if (ext.getValue() == null || ext.getValue().isEmpty()) {
			return true;
		}
		return !"false".equals(ext.getValue().toString());
	}

	protected boolean validateAddress(IBase theAddress, FhirContext theFhirContext) {
		ExtensionUtil.clearExtensionsByUrl(theAddress, getExtensionUrl());

		try {
			AddressValidationResult validationResult = getAddressValidator().isValid(theAddress, theFhirContext);
			ourLog.debug("Validated address {}", validationResult);

			clearPossibleDuplicatesDueToTerserCloning(theAddress, theFhirContext);
			ExtensionUtil.setExtension(theFhirContext, theAddress, getExtensionUrl(), "boolean", !validationResult.isValid());
			if (validationResult.getValidatedAddress() != null) {
				theFhirContext.newTerser().cloneInto(validationResult.getValidatedAddress(), theAddress, true);
			} else {
				ourLog.info("Validated address is not provided - skipping update on the target address instance");
			}
			return validationResult.isValid();
		} catch (Exception ex) {
			ourLog.warn("Unable to validate address", ex);
			IBaseExtension extension = ExtensionUtil.getOrCreateExtension(theAddress, getExtensionUrl());
			IBaseExtension errorValue = ExtensionUtil.getOrCreateExtension(extension, "error");
			errorValue.setValue(TerserUtil.newElement(theFhirContext, "string", ex.getMessage()));
			return false;
		}
	}

	private void clearPossibleDuplicatesDueToTerserCloning(IBase theAddress, FhirContext theFhirContext) {
		TerserUtil.clearField(theFhirContext, "line", theAddress);
		ExtensionUtil.clearExtensionsByUrl(theAddress, getExtensionUrl());
	}

	protected String getExtensionUrl() {
		if (getProperties().containsKey(PROPERTY_EXTENSION_URL)) {
			return getProperties().getProperty(PROPERTY_EXTENSION_URL);
		} else {
			return IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL;
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
