package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.util.OperationOutcomeUtil;

/**
 * Resource validator, which checks resources for compliance against various validation schemes (schemas, schematrons, profiles, etc.)
 * 
 * <p>
 * To obtain a resource validator, call {@link FhirContext#newValidator()}
 * </p>
 * 
 * <p>
 * <b>Thread safety note:</b> This class is thread safe, so you may register or unregister validator modules at any time. Individual modules are not guaranteed to be thread safe however. Reconfigure
 * them with caution.
 * </p>
 */
public class FhirValidator {

	private static final String I18N_KEY_NO_PHLOC_ERROR = FhirValidator.class.getName() + ".noPhlocError";

	private static final String I18N_KEY_NO_PHLOC_WARNING = FhirValidator.class.getName() + ".noPhlocWarningOnStartup";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirValidator.class);

	private static volatile Boolean ourPhlocPresentOnClasspath;
	private final FhirContext myContext;
	private volatile List<IValidatorModule> myValidators = new ArrayList<IValidatorModule>();

	/**
	 * Constructor (this should not be called directly, but rather {@link FhirContext#newValidator()} should be called to obtain an instance of {@link FhirValidator})
	 */
	public FhirValidator(FhirContext theFhirContext) {
		myContext = theFhirContext;
		setValidateAgainstStandardSchema(true);

		if (ourPhlocPresentOnClasspath == null) {
			try {
				Class.forName("com.phloc.schematron.ISchematronResource");
				ourPhlocPresentOnClasspath = true;
			} catch (ClassNotFoundException e) {
				ourLog.info(theFhirContext.getLocalizer().getMessage(I18N_KEY_NO_PHLOC_WARNING));
				ourPhlocPresentOnClasspath = false;
			}
		}
		if (ourPhlocPresentOnClasspath) {
			setValidateAgainstStandardSchematron(true);
		}

	}

	private void addOrRemoveValidator(boolean theValidateAgainstStandardSchema, Class<? extends IValidatorModule> type, IValidatorModule theInstance) {
		if (theValidateAgainstStandardSchema) {
			boolean found = haveValidatorOfType(type);
			if (!found) {
				registerValidatorModule(theInstance);
			}
		} else {
			for (Iterator<IValidatorModule> iter = myValidators.iterator(); iter.hasNext();) {
				IValidatorModule next = iter.next();
				if (next.getClass().equals(type)) {
					unregisterValidatorModule(next);
				}
			}
		}
	}

	private boolean haveValidatorOfType(Class<? extends IValidatorModule> type) {
		boolean found = false;
		for (IValidatorModule next : myValidators) {
			if (next.getClass().equals(type)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR distribution itself)
	 */
	public synchronized boolean isValidateAgainstStandardSchema() {
		return haveValidatorOfType(SchemaBaseValidator.class);
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR distribution itself)
	 */
	public synchronized boolean isValidateAgainstStandardSchematron() {
		return haveValidatorOfType(SchematronBaseValidator.class);
	}

	/**
	 * Add a new validator module to this validator. You may register as many modules as you like at any time.
	 * 
	 * @param theValidator
	 *           The validator module. Must not be null.
	 */
	public synchronized void registerValidatorModule(IValidatorModule theValidator) {
		Validate.notNull(theValidator, "theValidator must not be null");
		ArrayList<IValidatorModule> newValidators = new ArrayList<IValidatorModule>(myValidators.size() + 1);
		newValidators.addAll(myValidators);
		newValidators.add(theValidator);

		myValidators = newValidators;
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR distribution itself)
	 * 
	 * @return Returns a referens to <code>this<code> for method chaining
	 */
	public synchronized  FhirValidator setValidateAgainstStandardSchema(boolean theValidateAgainstStandardSchema) {
		addOrRemoveValidator(theValidateAgainstStandardSchema, SchemaBaseValidator.class, new SchemaBaseValidator(myContext));
		return this;
	}

	/**
	 * Should the validator validate the resource against the base schematron (the schematron provided with the FHIR distribution itself)
	 * 
	 * @return Returns a referens to <code>this<code> for method chaining
	 */
	public synchronized FhirValidator setValidateAgainstStandardSchematron(boolean theValidateAgainstStandardSchematron) {
		if (theValidateAgainstStandardSchematron && !ourPhlocPresentOnClasspath) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_KEY_NO_PHLOC_ERROR));
		}
		addOrRemoveValidator(theValidateAgainstStandardSchematron, SchematronBaseValidator.class, new SchematronBaseValidator(myContext));
		return this;
	}

	/**
	 * Removes a validator module from this validator. You may register as many modules as you like, and remove them at any time.
	 * 
	 * @param theValidator
	 *           The validator module. Must not be null.
	 */
	public synchronized void unregisterValidatorModule(IValidatorModule theValidator) {
		Validate.notNull(theValidator, "theValidator must not be null");
		ArrayList<IValidatorModule> newValidators = new ArrayList<IValidatorModule>(myValidators.size() + 1);
		newValidators.addAll(myValidators);
		newValidators.remove(theValidator);

		myValidators = newValidators;
	}

	/**
	 * Validates a bundle instance, throwing a {@link ValidationFailureException} if the validation fails. This validation includes validation of all resources in the bundle.
	 * 
	 * @param theBundle
	 *           The resource to validate
	 * @throws ValidationFailureException
	 *            If the validation fails
	 * @deprecated use {@link #validateWithResult(ca.uhn.fhir.model.api.Bundle)} instead
	 */
	@Deprecated
	public void validate(Bundle theBundle) {
		Validate.notNull(theBundle, "theBundle must not be null");

		IValidationContext<Bundle> ctx = ValidationContext.forBundle(myContext, theBundle);

		for (IValidatorModule next : myValidators) {
			next.validateBundle(ctx);
		}

		IBaseOperationOutcome oo = ctx.toResult().toOperationOutcome();
		if (oo != null && OperationOutcomeUtil.hasIssues(myContext, oo)) {
			throw new ValidationFailureException(myContext, oo);
		}

	}

	/**
	 * Validates a resource instance, throwing a {@link ValidationFailureException} if the validation fails
	 * 
	 * @param theResource
	 *           The resource to validate
	 * @throws ValidationFailureException
	 *            If the validation fails
	 * @deprecated use {@link #validateWithResult(IBaseResource)} instead
	 */
	@Deprecated
	public void validate(IResource theResource) throws ValidationFailureException {
		ValidationResult validationResult = validateWithResult(theResource);
		if (!validationResult.isSuccessful()) {
			throw new ValidationFailureException(myContext, validationResult.toOperationOutcome());
		}
	}

	/**
	 * Validates a bundle instance returning a {@link ca.uhn.fhir.validation.ValidationResult} which contains the results. This validation includes validation of all resources in the bundle.
	 *
	 * @param theBundle
	 *           the bundle to validate
	 * @return the results of validation
	 * @since 0.7
	 */
	public ValidationResult validateWithResult(Bundle theBundle) {
		Validate.notNull(theBundle, "theBundle must not be null");

		IValidationContext<Bundle> ctx = ValidationContext.forBundle(myContext, theBundle);

		for (IValidatorModule next : myValidators) {
			next.validateBundle(ctx);
		}

		return ctx.toResult();
	}

	/**
	 * Validates a resource instance returning a {@link ca.uhn.fhir.validation.ValidationResult} which contains the results.
	 *
	 * @param theResource
	 *           the resource to validate
	 * @return the results of validation
	 * @since 0.7
	 */
	public ValidationResult validateWithResult(IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");

		IValidationContext<IBaseResource> ctx = ValidationContext.forResource(myContext, theResource);

		for (IValidatorModule next : myValidators) {
			next.validateResource(ctx);
		}

		return ctx.toResult();
	}

	/**
	 * Validates a resource instance returning a {@link ca.uhn.fhir.validation.ValidationResult} which contains the results.
	 *
	 * @param theResource
	 *           the resource to validate
	 * @return the results of validation
	 * @since 1.1
	 */
	public ValidationResult validateWithResult(String theResource) {
		Validate.notNull(theResource, "theResource must not be null");

		IValidationContext<IBaseResource> ctx = ValidationContext.forText(myContext, theResource);

		for (IValidatorModule next : myValidators) {
			next.validateResource(ctx);
		}

		return ctx.toResult();
	}

}
