package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;

/**
 * Resource validator, which checks resources for compliance against various validation schemes (schemas, schematrons, etc.)
 * 
 * <p>
 * To obtain a resource validator, call {@link FhirContext#newValidator()}
 * </p>
 */
public class FhirValidator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirValidator.class);

	private static final String I18N_KEY_NO_PHLOC_WARNING = FhirValidator.class.getName()+".noPhlocWarningOnStartup";
	private static final String I18N_KEY_NO_PHLOC_ERROR = FhirValidator.class.getName()+".noPhlocError";

	private FhirContext myContext;
	private List<IValidator> myValidators = new ArrayList<IValidator>();
	private static volatile Boolean ourPhlocPresentOnClasspath;

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

	private void addOrRemoveValidator(boolean theValidateAgainstStandardSchema, Class<? extends IValidator> type, IValidator instance) {
		if (theValidateAgainstStandardSchema) {
			boolean found = haveValidatorOfType(type);
			if (!found) {
				myValidators.add(instance);
			}
		} else {
			for (Iterator<IValidator> iter = myValidators.iterator(); iter.hasNext();) {
				IValidator next = iter.next();
				if (next.getClass().equals(type)) {
					iter.remove();
				}
			}
		}
	}

	private boolean haveValidatorOfType(Class<? extends IValidator> type) {
		boolean found = false;
		for (IValidator next : myValidators) {
			if (next.getClass().equals(type)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR distribution itself)
	 */
	public boolean isValidateAgainstStandardSchema() {
		return haveValidatorOfType(SchemaBaseValidator.class);
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR distribution itself)
	 */
	public boolean isValidateAgainstStandardSchematron() {
		return haveValidatorOfType(SchematronBaseValidator.class);
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR distribution itself)
	 */
	public void setValidateAgainstStandardSchema(boolean theValidateAgainstStandardSchema) {
		addOrRemoveValidator(theValidateAgainstStandardSchema, SchemaBaseValidator.class, new SchemaBaseValidator());
	}

	/**
	 * Should the validator validate the resource against the base schematron (the schematron provided with the FHIR distribution itself)
	 */
	public void setValidateAgainstStandardSchematron(boolean theValidateAgainstStandardSchematron) {
		if (theValidateAgainstStandardSchematron && !ourPhlocPresentOnClasspath) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_KEY_NO_PHLOC_ERROR));
		}
		addOrRemoveValidator(theValidateAgainstStandardSchematron, SchematronBaseValidator.class, new SchematronBaseValidator());
	}

	/**
	 * Validates a bundle instance, throwing a {@link ValidationFailureException} if the validation fails. This validation includes validation of all resources in the bundle.
	 * 
	 * @param theBundle
	 *            The resource to validate
	 * @throws ValidationFailureException
	 *             If the validation fails
     * @deprecated use {@link #validateWithResult(ca.uhn.fhir.model.api.Bundle)} instead
	 */
    @Deprecated
	public void validate(Bundle theBundle) {
		Validate.notNull(theBundle, "theBundle must not be null");

		ValidationContext<Bundle> ctx = ValidationContext.forBundle(myContext, theBundle);

		for (IValidator next : myValidators) {
			next.validateBundle(ctx);
		}

		BaseOperationOutcome oo = ctx.getOperationOutcome();
		if (oo != null && oo.getIssue().size() > 0) {
			throw new ValidationFailureException(oo);
		}

	}

	/**
	 * Validates a resource instance, throwing a {@link ValidationFailureException} if the validation fails
	 * 
	 * @param theResource
	 *            The resource to validate
	 * @throws ValidationFailureException
	 *             If the validation fails
     * @deprecated use {@link #validateWithResult(ca.uhn.fhir.model.api.IResource)} instead
	 */
    @Deprecated
	public void validate(IResource theResource) throws ValidationFailureException {
        ValidationResult validationResult = validateWithResult(theResource);
        if (!validationResult.isSuccessful()) {
            throw new ValidationFailureException(validationResult.getOperationOutcome());
        }
    }

    /**
     * Validates a bundle instance returning a {@link ca.uhn.fhir.validation.ValidationResult} which contains the results.
     * This validation includes validation of all resources in the bundle.
     *
     * @param theBundle the bundle to validate
     * @return the results of validation
     * @since 0.7
     */
    public ValidationResult validateWithResult(Bundle theBundle) {
        Validate.notNull(theBundle, "theBundle must not be null");

        ValidationContext<Bundle> ctx = ValidationContext.forBundle(myContext, theBundle);

		for (IValidator next : myValidators) {
			next.validateBundle(ctx);
		}

        BaseOperationOutcome oo = ctx.getOperationOutcome();
        return ValidationResult.valueOf(oo);
    }

    /**
     * Validates a resource instance returning a {@link ca.uhn.fhir.validation.ValidationResult} which contains the results.
     *
     * @param theResource the resource to validate
     * @return the results of validation
     * @since 0.7
     */
    public ValidationResult validateWithResult(IResource theResource) {
        Validate.notNull(theResource, "theResource must not be null");

        ValidationContext<IResource> ctx = ValidationContext.forResource(myContext, theResource);

        for (IValidator next : myValidators) {
            next.validateResource(ctx);
        }

        BaseOperationOutcome oo = ctx.getOperationOutcome();
        return ValidationResult.valueOf(oo);
    }
}
