package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import ca.uhn.fhir.validation.IValidationContext;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.convertors.VersionConvertor_10_50;
import org.hl7.fhir.convertors.VersionConvertor_14_50;
import org.hl7.fhir.convertors.VersionConvertor_30_50;
import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Base;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.TypeDetails;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.utils.FHIRPathEngine;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"PackageAccessibility", "Duplicates"})
public class FhirInstanceValidator extends BaseValidatorBridge implements IInstanceValidatorModule {

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private IValidationSupport myValidationSupport;
	private boolean noTerminologyChecks = false;
	private volatile VersionSpecificWorkerContextWrapper myWrappedWorkerContext;
	private boolean errorForUnknownProfiles;
	private boolean assumeValidRestReferences;
	private List<String> myExtensionDomains = Collections.emptyList();
	private IResourceValidator.IValidatorResourceFetcher validatorResourceFetcher;
	private volatile FhirContext myDstu2Context;
	private volatile FhirContext myHl7OrgDstu2Context;

	/**
	 * Constructor
	 * <p>
	 * Uses {@link DefaultProfileValidationSupport} for {@link IValidationSupport validation support}
	 */
	public FhirInstanceValidator(FhirContext theContext) {
		this(theContext.getValidationSupport());
	}

	/**
	 * Constructor which uses the given validation support
	 *
	 * @param theValidationSupport The validation support
	 */
	public FhirInstanceValidator(IValidationSupport theValidationSupport) {
		if (theValidationSupport.getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			myValidationSupport = new HapiToHl7OrgDstu2ValidatingSupportWrapper(theValidationSupport);
		} else {
			myValidationSupport = theValidationSupport;
		}
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
		this.myExtensionDomains = extensionDomains;
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
		this.myExtensionDomains = Arrays.asList(extensionDomains);
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
	 * Returns the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
	 * DefaultProfileValidationSupport if the no-arguments constructor for this object was used.
	 *
	 * @return
	 */
	public IValidationSupport getValidationSupport() {
		return myValidationSupport;
	}

	/**
	 * Sets the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
	 * DefaultProfileValidationSupport if the no-arguments constructor for this object was used.
	 */
	public void setValidationSupport(IValidationSupport theValidationSupport) {
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

	public List<String> getExtensionDomains() {
		return myExtensionDomains;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theValidationCtx) {
		VersionSpecificWorkerContextWrapper wrappedWorkerContext = myWrappedWorkerContext;
		if (wrappedWorkerContext == null) {
			VersionSpecificWorkerContextWrapper.IVersionTypeConverter converter;

			switch (myValidationSupport.getFhirContext().getVersion().getVersion()) {
				case DSTU2:
				case DSTU2_HL7ORG: {
					converter = new VersionSpecificWorkerContextWrapper.IVersionTypeConverter() {
						@Override
						public Resource toCanonical(IBaseResource theNonCanonical) {
							IBaseResource nonCanonical = theNonCanonical;
							Resource retVal = VersionConvertor_10_50.convertResource((org.hl7.fhir.dstu2.model.Resource) nonCanonical);
							if (nonCanonical instanceof org.hl7.fhir.dstu2.model.ValueSet) {
								org.hl7.fhir.dstu2.model.ValueSet valueSet = (org.hl7.fhir.dstu2.model.ValueSet) nonCanonical;
								if (valueSet.hasCodeSystem() && valueSet.getCodeSystem().hasSystem()) {
									if (!valueSet.hasCompose()) {
										org.hl7.fhir.r5.model.ValueSet valueSetR5 = (org.hl7.fhir.r5.model.ValueSet) retVal;
										valueSetR5.getCompose().addInclude().setSystem(valueSet.getCodeSystem().getSystem());
									}
								}
							}
							return retVal;
						}

						@Override
						public IBaseResource fromCanonical(Resource theCanonical) {
							IBaseResource canonical = VersionConvertor_10_50.convertResource(theCanonical);
							return canonical;
						}
					};
					break;
				}

				case DSTU2_1: {
					converter = new VersionSpecificWorkerContextWrapper.IVersionTypeConverter() {
						@Override
						public org.hl7.fhir.r5.model.Resource toCanonical(IBaseResource theNonCanonical) {
							return VersionConvertor_14_50.convertResource((org.hl7.fhir.dstu2016may.model.Resource) theNonCanonical);
						}

						@Override
						public IBaseResource fromCanonical(org.hl7.fhir.r5.model.Resource theCanonical) {
							return VersionConvertor_14_50.convertResource(theCanonical);
						}
					};
					break;
				}

				case DSTU3: {
					converter = new VersionSpecificWorkerContextWrapper.IVersionTypeConverter() {
						@Override
						public Resource toCanonical(IBaseResource theNonCanonical) {
							return VersionConvertor_30_50.convertResource((org.hl7.fhir.dstu3.model.Resource) theNonCanonical, true);
						}

						@Override
						public IBaseResource fromCanonical(Resource theCanonical) {
							return VersionConvertor_30_50.convertResource(theCanonical, true);
						}
					};
					break;
				}

				case R4: {
					converter = new VersionSpecificWorkerContextWrapper.IVersionTypeConverter() {
						@Override
						public org.hl7.fhir.r5.model.Resource toCanonical(IBaseResource theNonCanonical) {
							return VersionConvertor_40_50.convertResource((org.hl7.fhir.r4.model.Resource) theNonCanonical);
						}

						@Override
						public IBaseResource fromCanonical(org.hl7.fhir.r5.model.Resource theCanonical) {
							return VersionConvertor_40_50.convertResource(theCanonical);
						}
					};
					break;
				}

				case R5: {
					converter = VersionSpecificWorkerContextWrapper.IDENTITY_VERSION_TYPE_CONVERTER;
					break;
				}

				default:
					throw new IllegalStateException();
			}

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

	private FhirContext getDstu2Context() {
		FhirContext dstu2Context = myDstu2Context;
		if (dstu2Context == null) {
			dstu2Context = FhirContext.forDstu2();
			myDstu2Context = dstu2Context;
		}
		return dstu2Context;
	}

	private FhirContext getHl7OrgDstu2Context() {
		FhirContext hl7OrgDstu2Context = myHl7OrgDstu2Context;
		if (hl7OrgDstu2Context == null) {
			hl7OrgDstu2Context = FhirContext.forDstu2Hl7Org();
			myHl7OrgDstu2Context = hl7OrgDstu2Context;
		}
		return hl7OrgDstu2Context;
	}

	public IResourceValidator.IValidatorResourceFetcher getValidatorResourceFetcher() {
		return validatorResourceFetcher;
	}

	public void setValidatorResourceFetcher(IResourceValidator.IValidatorResourceFetcher validatorResourceFetcher) {
		this.validatorResourceFetcher = validatorResourceFetcher;
	}

	public boolean isAssumeValidRestReferences() {
		return assumeValidRestReferences;
	}

	public void setAssumeValidRestReferences(boolean assumeValidRestReferences) {
		this.assumeValidRestReferences = assumeValidRestReferences;
	}

	/**
	 * Clear any cached data held by the validator or any of its internal stores. This is mostly intended
	 * for unit tests, but could be used for production uses too.
	 */
	public void invalidateCaches() {
		myValidationSupport.invalidateCaches();
		myWrappedWorkerContext.invalidateCaches();
	}


	public static class NullEvaluationContext implements FHIRPathEngine.IEvaluationContext {
		@Override
		public Base resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
			return null;
		}

		@Override
		public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
			return null;
		}

		@Override
		public boolean log(String argument, List<Base> focus) {
			return false;
		}

		@Override
		public FunctionDetails resolveFunction(String functionName) {
			return null;
		}

		@Override
		public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
			return null;
		}

		@Override
		public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
			return null;
		}

		@Override
		public Base resolveReference(Object appContext, String url, Base refContext) throws FHIRException {
			return null;
		}

		@Override
		public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
			return false;
		}

		@Override
		public ValueSet resolveValueSet(Object appContext, String url) {
			return null;
		}
	}


}
