package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.validation.schematron.SchematronProvider;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;


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
	private static final Logger ourLog = LoggerFactory.getLogger(FhirValidator.class);

	private static final String I18N_KEY_NO_PH_ERROR = FhirValidator.class.getName() + ".noPhError";

	private static volatile Boolean ourPhPresentOnClasspath;
	private final FhirContext myFhirContext;
	private List<IValidatorModule> myValidators = new ArrayList<>();
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	private boolean myConcurrentBundleValidation;

	private ExecutorService myExecutorService;

	/**
	 * Constructor (this should not be called directly, but rather {@link FhirContext#newValidator()} should be called to obtain an instance of {@link FhirValidator})
	 */
	public FhirValidator(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;

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
			for (IValidatorModule next : myValidators) {
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
				break;
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
	 *
	 * @return Returns a referens to <code>this<code> for method chaining
	 */
	public synchronized FhirValidator setValidateAgainstStandardSchema(boolean theValidateAgainstStandardSchema) {
		addOrRemoveValidator(theValidateAgainstStandardSchema, SchemaBaseValidator.class, new SchemaBaseValidator(myFhirContext));
		return this;
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
	 * Should the validator validate the resource against the base schematron (the schematron provided with the FHIR distribution itself)
	 *
	 * @return Returns a referens to <code>this<code> for method chaining
	 */
	public synchronized FhirValidator setValidateAgainstStandardSchematron(boolean theValidateAgainstStandardSchematron) {
		if (theValidateAgainstStandardSchematron && !ourPhPresentOnClasspath) {
			throw new IllegalArgumentException(myFhirContext.getLocalizer().getMessage(I18N_KEY_NO_PH_ERROR));
		}
		if (!theValidateAgainstStandardSchematron && !ourPhPresentOnClasspath) {
			return this;
		}
		Class<? extends IValidatorModule> cls = SchematronProvider.getSchematronValidatorClass();
		IValidatorModule instance = SchematronProvider.getSchematronValidatorInstance(myFhirContext);
		addOrRemoveValidator(theValidateAgainstStandardSchematron, cls, instance);
		return this;
	}

	/**
	 * Add a new validator module to this validator. You may register as many modules as you like at any time.
	 *
	 * @param theValidator The validator module. Must not be null.
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
	 * Removes a validator module from this validator. You may register as many modules as you like, and remove them at any time.
	 *
	 * @param theValidator The validator module. Must not be null.
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
	 * Validates a resource instance returning a {@link ValidationResult} which contains the results.
	 *
	 * @param theResource the resource to validate
	 * @return the results of validation
	 * @since 0.7
	 */
	public ValidationResult validateWithResult(IBaseResource theResource) {
		return validateWithResult(theResource, null);
	}

	/**
	 * Validates a resource instance returning a {@link ValidationResult} which contains the results.
	 *
	 * @param theResource the resource to validate
	 * @return the results of validation
	 * @since 1.1
	 */
	public ValidationResult validateWithResult(String theResource) {
		return validateWithResult(theResource, null);
	}

	/**
	 * Validates a resource instance returning a {@link ValidationResult} which contains the results.
	 *
	 * @param theResource the resource to validate
	 * @param theOptions  Optionally provides options to the validator
	 * @return the results of validation
	 * @since 4.0.0
	 */
	public ValidationResult validateWithResult(String theResource, ValidationOptions theOptions) {
		Validate.notNull(theResource, "theResource must not be null");
		IValidationContext<IBaseResource> validationContext = ValidationContext.forText(myFhirContext, theResource, theOptions);
		Function<ValidationResult, ValidationResult> callback = result -> invokeValidationCompletedHooks(null, theResource, result);
		return doValidate(validationContext, theOptions, callback);
	}

	/**
	 * Validates a resource instance returning a {@link ValidationResult} which contains the results.
	 *
	 * @param theResource the resource to validate
	 * @param theOptions  Optionally provides options to the validator
	 * @return the results of validation
	 * @since 4.0.0
	 */
	public ValidationResult validateWithResult(IBaseResource theResource, ValidationOptions theOptions) {
		Validate.notNull(theResource, "theResource must not be null");
		IValidationContext<IBaseResource> validationContext = ValidationContext.forResource(myFhirContext, theResource, theOptions);
		Function<ValidationResult, ValidationResult> callback = result -> invokeValidationCompletedHooks(theResource, null, result);
		return doValidate(validationContext, theOptions, callback);
	}

	private ValidationResult doValidate(IValidationContext<IBaseResource> theValidationContext, ValidationOptions theOptions,
													Function<ValidationResult, ValidationResult> theValidationCompletionCallback) {
		applyDefaultValidators();

		ValidationResult result;
		if (myConcurrentBundleValidation && theValidationContext.getResource() instanceof IBaseBundle
			&& myExecutorService != null) {
			result = validateBundleEntriesConcurrently(theValidationContext, theOptions);
		} else {
			result = validateResource(theValidationContext);
		}

		return theValidationCompletionCallback.apply(result);
	}

	private ValidationResult validateBundleEntriesConcurrently(IValidationContext<IBaseResource> theValidationContext, ValidationOptions theOptions) {
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(myFhirContext, (IBaseBundle) theValidationContext.getResource());
		// Async validation tasks
		List<ConcurrentValidationTask> validationTasks = new ArrayList<>();
		for (int index = 0; index < entries.size(); ++index) {
			BundleEntryParts entry = entries.get(index);
			if (entry.getResource() == null) {
				continue;
			}
			String originalPathPrefix = String.format("Bundle.entry[%d].resource.ofType(%s)", index, entry.getResource().fhirType());
			String validatedPathPrefix = String.format("Bundle.entry[0].resource.ofType(%s)", entry.getResource().fhirType());

			Future<ValidationResult> future = myExecutorService.submit(() -> {
				IBaseBundle singleEntryBundle = bundleFromEntry(entry);
				IValidationContext<IBaseResource> entryValidationContext = ValidationContext.forResource(theValidationContext.getFhirContext(), singleEntryBundle, theOptions);
				return validateResource(entryValidationContext);
			});
			validationTasks.add(new ConcurrentValidationTask(originalPathPrefix, validatedPathPrefix, future));
		}

		List<SingleValidationMessage> validationMessages = buildValidationMessages(validationTasks);
		return new ValidationResult(myFhirContext, validationMessages);
	}

	private IBaseBundle bundleFromEntry(BundleEntryParts theEntry) {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IBaseResource resource = theEntry.getResource();

		// For validation purposes, it doesn't matter too much what kind of bundle entry we use
		// Ideally we would use the same bundle entry from the source bundle, but we don't have
		// access to the structure libraries here
		if (resource != null) {
			if (resource.getIdElement().getIdPart() != null) {
				builder.addTransactionUpdateEntry(theEntry.getResource());
			} else {
				builder.addTransactionCreateEntry(theEntry.getResource());
			}
		}
		IBaseBundle retval = builder.getBundle();
		// Add a fullUrl so the bundle itself is valid
		BundleUtil.processEntries(myFhirContext, retval, entry -> entry.setFullUrl("urn:uuid:61ebe359-bfdc-4613-8bf2-c5e300945f0a"));
		return retval;
	}

	static List<SingleValidationMessage> buildValidationMessages(List<ConcurrentValidationTask> validationTasks) {
		List<SingleValidationMessage> retval = new ArrayList<>();
		try {
			for (ConcurrentValidationTask validationTask : validationTasks) {
				ValidationResult result = validationTask.getFuture().get();
				final String originalEntryPathPrefix = validationTask.getOriginalPathPrefix();
				final String validatedEntryPathPrefix = validationTask.getValidatedPathPrefix();
				result.getMessages().stream()
					.filter(message -> message.getLocationString() != null)
					.forEach(message ->
						message.setLocationString(message.getLocationString().replace(validatedEntryPathPrefix, originalEntryPathPrefix)));
				retval.addAll(result.getMessages());
			}
		} catch (InterruptedException | ExecutionException exp) {
			throw new InternalErrorException(exp);
		}
		return retval;
	}

	private ValidationResult validateResource(IValidationContext<IBaseResource> theValidationContext) {
		for (IValidatorModule next : myValidators) {
			next.validateResource(theValidationContext);
		}
		return theValidationContext.toResult();
	}

	private ValidationResult invokeValidationCompletedHooks(IBaseResource theResourceParsed, String theResourceRaw, ValidationResult theValidationResult) {
		if (myInterceptorBroadcaster != null) {
			if (myInterceptorBroadcaster.hasHooks(Pointcut.VALIDATION_COMPLETED)) {
				HookParams params = new HookParams()
					.add(IBaseResource.class, theResourceParsed)
					.add(String.class, theResourceRaw)
					.add(ValidationResult.class, theValidationResult);
				Object newResult = myInterceptorBroadcaster.callHooksAndReturnObject(Pointcut.VALIDATION_COMPLETED, params);
				if (newResult != null) {
					theValidationResult = (ValidationResult) newResult;
				}
			}
		}
		return theValidationResult;
	}

	/**
	 * Optionally supplies an interceptor broadcaster that will be used to invoke validation related Pointcut events
	 *
	 * @since 5.5.0
	 */
	public void setInterceptorBroadcaster(IInterceptorBroadcaster theInterceptorBraodcaster) {
		myInterceptorBroadcaster = theInterceptorBraodcaster;
	}

	public FhirValidator setExecutorService(ExecutorService theExecutorService) {
		myExecutorService = theExecutorService;
		return this;
	}

	/**
	 * If this is true, bundles will be validated in parallel threads.  The bundle structure itself will not be validated,
	 * only the resources in its entries.
	 */

	public boolean isConcurrentBundleValidation() {
		return myConcurrentBundleValidation;
	}

	/**
	 * If this is true, bundles will be validated in parallel threads.  The bundle structure itself will not be validated,
	 * only the resources in its entries.
	 */
	public FhirValidator setConcurrentBundleValidation(boolean theConcurrentBundleValidation) {
		myConcurrentBundleValidation = theConcurrentBundleValidation;
		return this;
	}

	static class ConcurrentValidationTask {
		private final String myOriginalPathPrefix;
		private final String myValidatedPathPrefix;
		private final Future<ValidationResult> myFuture;

		ConcurrentValidationTask(String theOriginalPathPrefix, String theValidatedPathPrefix, Future<ValidationResult> theFuture) {
			myOriginalPathPrefix = theOriginalPathPrefix;
			myValidatedPathPrefix = theValidatedPathPrefix;
			myFuture = theFuture;
		}

		public String getOriginalPathPrefix() {
			return myOriginalPathPrefix;
		}

		public String getValidatedPathPrefix() {
			return myValidatedPathPrefix;
		}

		public Future<ValidationResult> getFuture() {
			return myFuture;
		}
	}

}
