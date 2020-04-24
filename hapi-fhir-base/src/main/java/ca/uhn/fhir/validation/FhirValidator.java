package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import java.util.*;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.schematron.SchematronProvider;

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

	private static final String I18N_KEY_NO_PH_ERROR = FhirValidator.class.getName() + ".noPhError";

	private static volatile Boolean ourPhPresentOnClasspath;
	private final FhirContext myContext;
	private List<IValidatorModule> myValidators = new ArrayList<>();

	/**
	 * Constructor (this should not be called directly, but rather {@link FhirContext#newValidator()} should be called to obtain an instance of {@link FhirValidator})
	 */
	public FhirValidator(FhirContext theFhirContext) {
		myContext = theFhirContext;

		if (ourPhPresentOnClasspath == null) {
			ourPhPresentOnClasspath = SchematronProvider.isSchematronAvailable(theFhirContext);
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
		if (!ourPhPresentOnClasspath) {
			// No need to ask since we dont have Ph-Schematron. Also Class.forname will complain
			// about missing ph-schematron import.
			return false;
		}
		Class<? extends IValidatorModule> cls = SchematronProvider.getSchematronValidatorClass();
		return haveValidatorOfType(cls);
	}

	/**
	 * Add a new validator module to this validator. You may register as many modules as you like at any time.
	 * 
	 * @param theValidator
	 *           The validator module. Must not be null.
	 * @return Returns a reference to <code>this</code> for easy method chaining.
	 */
	public synchronized FhirValidator registerValidatorModule(IValidatorModule theValidator) {
		Validate.notNull(theValidator, "theValidator must not be null");
		ArrayList<IValidatorModule> newValidators = new ArrayList<IValidatorModule>(myValidators.size() + 1);
		newValidators.addAll(myValidators);
		newValidators.add(theValidator);

		myValidators = newValidators;
		return this;
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
		if (theValidateAgainstStandardSchematron && !ourPhPresentOnClasspath) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_KEY_NO_PH_ERROR));
		}
		if (!theValidateAgainstStandardSchematron && !ourPhPresentOnClasspath) {
			return this;
		}
		Class<? extends IValidatorModule> cls = SchematronProvider.getSchematronValidatorClass();
		IValidatorModule instance = SchematronProvider.getSchematronValidatorInstance(myContext);
		addOrRemoveValidator(theValidateAgainstStandardSchematron, cls, instance);
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


	private void applyDefaultValidators() {
		if (myValidators.isEmpty()) {
			setValidateAgainstStandardSchema(true);
			if (ourPhPresentOnClasspath) {
				setValidateAgainstStandardSchematron(true);
			}
		}
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
		return validateWithResult(theResource, null);
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
		return validateWithResult(theResource, null);
	}

	/**
	 * Validates a resource instance returning a {@link ca.uhn.fhir.validation.ValidationResult} which contains the results.
	 *
	 * @param theResource
	 *           the resource to validate
	 * @param theOptions
	 *       Optionally provides options to the validator
	 * @return the results of validation
	 * @since 4.0.0
	 */
	public ValidationResult validateWithResult(IBaseResource theResource, ValidationOptions theOptions) {
		Validate.notNull(theResource, "theResource must not be null");

		applyDefaultValidators();

		IValidationContext<IBaseResource> ctx = ValidationContext.forResource(myContext, theResource, theOptions);

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
	 * @param theOptions
	 *       Optionally provides options to the validator
	 * @return the results of validation
	 * @since 4.0.0
	 */
	public ValidationResult validateWithResult(String theResource, ValidationOptions theOptions) {
		Validate.notNull(theResource, "theResource must not be null");

		applyDefaultValidators();

		IValidationContext<IBaseResource> ctx = ValidationContext.forText(myContext, theResource, theOptions);

		for (IValidatorModule next : myValidators) {
			next.validateResource(ctx);
		}

		return ctx.toResult();
	}
}
