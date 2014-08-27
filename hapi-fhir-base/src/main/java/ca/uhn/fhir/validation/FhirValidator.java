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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;

/**
 * Resource validator, which checks resources for compliance against various
 * validation schemes (schemas, schematrons, etc.)
 * 
 * <p>
 * To obtain a resource validator, call {@link FhirContext#newValidator()}
 * </p>
 */
public class FhirValidator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirValidator.class);

	private FhirContext myContext;
	private List<IValidator> myValidators = new ArrayList<IValidator>();

	/**
	 * Constructor (this should not be called directly, but rather {@link FhirContext#newValidator()} should be called
	 * to obtain an instance of {@link FhirValidator})
	 */
	public FhirValidator(FhirContext theFhirContext) {
		myContext = theFhirContext;
		setValidateBaseSchema(true);
		setValidateBaseSchematron(true);
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR
	 * distribution itself)
	 */
	public boolean isValidateBaseSchema() {
		return haveValidatorOfType(SchemaBaseValidator.class);
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR
	 * distribution itself)
	 */
	public void setValidateBaseSchema(boolean theValidateBaseSchema) {
		addOrRemoveValidator(theValidateBaseSchema, SchemaBaseValidator.class, new SchemaBaseValidator());
	}

	/**
	 * Should the validator validate the resource against the base schema (the schema provided with the FHIR
	 * distribution itself)
	 */
	public boolean isValidateBaseSchematron() {
		return haveValidatorOfType(SchematronBaseValidator.class);
	}

	/**
	 * Should the validator validate the resource against the base schematron (the schematron provided with the FHIR
	 * distribution itself)
	 */
	public void setValidateBaseSchematron(boolean theValidateBaseSchematron) {
		addOrRemoveValidator(theValidateBaseSchematron, SchematronBaseValidator.class, new SchematronBaseValidator());
	}

	
	public void validate(IResource theResource) throws ValidationFailureException {
		Validate.notNull(theResource, "theResource must not be null");

		ValidationContext ctx = new ValidationContext(myContext, theResource);

		for (IValidator next : myValidators) {
			next.validate(ctx);
		}

		OperationOutcome oo = ctx.getOperationOutcome();
		if (oo != null && oo.getIssue().size() > 0) {
			throw new ValidationFailureException(oo.getIssueFirstRep().getDetails().getValue(), oo);
		}

	}

	private void addOrRemoveValidator(boolean theValidateBaseSchema, Class<? extends IValidator> type, IValidator instance) {
		if (theValidateBaseSchema) {
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

}
