package org.hl7.fhir.r4.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import ca.uhn.fhir.validation.IValidationContext;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.ValidatorWrapper;
import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.dstu3.hapi.validation.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"PackageAccessibility", "Duplicates"})
public class FhirInstanceValidator extends org.hl7.fhir.r4.hapi.validation.BaseValidatorBridge implements IInstanceValidatorModule {

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private IContextValidationSupport myValidationSupport;
	private boolean noTerminologyChecks = false;
	private volatile VersionSpecificWorkerContextWrapper myWrappedWorkerContext;
	private IResourceValidator.IValidatorResourceFetcher validatorResourceFetcher;
	private boolean assumeValidRestReferences;
	private boolean errorForUnknownProfiles;
	private List<String> extensionDomains = Collections.emptyList();

	/**
	 * Constructor
	 * <p>
	 * Uses {@link DefaultProfileValidationSupport} for {@link IContextValidationSupport validation support}
	 */
	public FhirInstanceValidator(FhirContext theContext) {
		this(theContext.getValidationSupport());
	}

	/**
	 * Constructor which uses the given validation support
	 *
	 * @param theValidationSupport The validation support
	 */
	public FhirInstanceValidator(IContextValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
	}

	/**
	 * Every element in a resource or data type includes an optional <it>extension</it> child element
	 * which is identified by it's {@code url attribute}. There exists a number of predefined
	 * extension urls or extension domains:<ul>
	 * <li>any url which contains {@code example.org}, {@code nema.org}, or {@code acme.com}.</li>
	 * <li>any url which starts with {@code http://hl7.org/fhir/StructureDefinition/}.</li>
	 * </ul>
	 * It is possible to extend this list of known extension by defining custom extensions:
	 * Any url which starts which one of the elements in the list of custom extension domains is
	 * considered as known.
	 * <p>
	 * Any unknown extension domain will result in an information message when validating a resource.
	 * </p>
	 */
	public FhirInstanceValidator setCustomExtensionDomains(List<String> extensionDomains) {
		this.extensionDomains = extensionDomains;
		return this;
	}

	/**
	 * Every element in a resource or data type includes an optional <it>extension</it> child element
	 * which is identified by it's {@code url attribute}. There exists a number of predefined
	 * extension urls or extension domains:<ul>
	 * <li>any url which contains {@code example.org}, {@code nema.org}, or {@code acme.com}.</li>
	 * <li>any url which starts with {@code http://hl7.org/fhir/StructureDefinition/}.</li>
	 * </ul>
	 * It is possible to extend this list of known extension by defining custom extensions:
	 * Any url which starts which one of the elements in the list of custom extension domains is
	 * considered as known.
	 * <p>
	 * Any unknown extension domain will result in an information message when validating a resource.
	 * </p>
	 */
	public FhirInstanceValidator setCustomExtensionDomains(String... extensionDomains) {
		setCustomExtensionDomains(Arrays.asList(extensionDomains));
		return this;
	}


	/**
	 * Returns the "best practice" warning level (default is {@link BestPracticeWarningLevel#Hint}).
	 * <p>
	 * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
	 * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
	 * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
	 * guielines will be ignored.
	 * </p>
	 *
	 * @see #setBestPracticeWarningLevel(BestPracticeWarningLevel)
	 */
	public BestPracticeWarningLevel getBestPracticeWarningLevel() {
		return myBestPracticeWarningLevel;
	}

	/**
	 * Sets the "best practice warning level". When validating, any deviations from best practices will be reported at
	 * this level.
	 * <p>
	 * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
	 * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
	 * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
	 * guielines will be ignored.
	 * </p>
	 *
	 * @param theBestPracticeWarningLevel The level, must not be <code>null</code>
	 */
	public void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		Validate.notNull(theBestPracticeWarningLevel);
		myBestPracticeWarningLevel = theBestPracticeWarningLevel;
	}

	/**
	 * Returns the {@link IContextValidationSupport validation support} in use by this validator. Default is an instance of
	 * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
	 * @return
	 */
	public IContextValidationSupport getValidationSupport() {
		return myValidationSupport;
	}

	/**
	 * Sets the {@link IContextValidationSupport validation support} in use by this validator. Default is an instance of
	 * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
	 */
	public void setValidationSupport(IContextValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
		myWrappedWorkerContext = null;
	}

	/**
	 * If set to {@literal true} (default is true) extensions which are not known to the
	 * validator (e.g. because they have not been explicitly declared in a profile) will
	 * be validated but will not cause an error.
	 */
	public boolean isAnyExtensionsAllowed() {
		return myAnyExtensionsAllowed;
	}

	/**
	 * If set to {@literal true} (default is true) extensions which are not known to the
	 * validator (e.g. because they have not been explicitly declared in a profile) will
	 * be validated but will not cause an error.
	 */
	public void setAnyExtensionsAllowed(boolean theAnyExtensionsAllowed) {
		myAnyExtensionsAllowed = theAnyExtensionsAllowed;
	}

	public boolean isErrorForUnknownProfiles() {
		return errorForUnknownProfiles;
	}

	public void setErrorForUnknownProfiles(boolean errorForUnknownProfiles) {
		this.errorForUnknownProfiles = errorForUnknownProfiles;
	}

	/**
	 * If set to {@literal true} (default is false) the valueSet will not be validate
	 */
	public boolean isNoTerminologyChecks() {
		return noTerminologyChecks;
	}

	/**
	 * If set to {@literal true} (default is false) the valueSet will not be validate
	 */
	public void setNoTerminologyChecks(final boolean theNoTerminologyChecks) {
		noTerminologyChecks = theNoTerminologyChecks;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theValidationCtx) {

		VersionSpecificWorkerContextWrapper wrappedWorkerContext = myWrappedWorkerContext;
		if (wrappedWorkerContext == null) {
			VersionSpecificWorkerContextWrapper.IVersionTypeConverter converter = new VersionSpecificWorkerContextWrapper.IVersionTypeConverter() {
				@Override
				public org.hl7.fhir.r5.model.Resource toCanonical(IBaseResource theNonCanonical) {
					return VersionConvertor_40_50.convertResource((org.hl7.fhir.r4.model.Resource)theNonCanonical);
				}

				@Override
				public IBaseResource fromCanonical(org.hl7.fhir.r5.model.Resource theCanonical) {
					return VersionConvertor_40_50.convertResource(theCanonical);
				}
			};
			wrappedWorkerContext = new VersionSpecificWorkerContextWrapper(myValidationSupport, converter);
		}
		myWrappedWorkerContext = wrappedWorkerContext;

		return new ValidatorWrapper()
			.setAnyExtensionsAllowed(isAnyExtensionsAllowed())
			.setBestPracticeWarningLevel(getBestPracticeWarningLevel())
			.setErrorForUnknownProfiles(isErrorForUnknownProfiles())
			.setExtensionDomains(getExtensionDomains())
			.setNoTerminologyChecks(isNoTerminologyChecks())
			.setValidatorResourceFetcher(getValidatorResourceFetcher())
			.setAssumeValidRestReferences(isAssumeValidRestReferences())
			.validate(wrappedWorkerContext, theValidationCtx);
	}

	public IResourceValidator.IValidatorResourceFetcher getValidatorResourceFetcher() {
		return validatorResourceFetcher;
	}

	public void setValidatorResourceFetcher(IResourceValidator.IValidatorResourceFetcher validatorResourceFetcher) {
		this.validatorResourceFetcher = validatorResourceFetcher;
	}


	private List<String> getExtensionDomains() {
		return extensionDomains;
	}

	public boolean isAssumeValidRestReferences() {
		return assumeValidRestReferences;
	}

	public void setAssumeValidRestReferences(boolean assumeValidRestReferences) {
		this.assumeValidRestReferences = assumeValidRestReferences;
	}

}
