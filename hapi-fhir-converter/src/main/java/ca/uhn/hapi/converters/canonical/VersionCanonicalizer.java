/*-
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.converters.canonical;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_40;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_14_40;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_14_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_40;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_43_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_14_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_14_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.PackageInformation;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.StructureDefinition;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class converts versions of various resources to/from a canonical version
 * of the resource. The specific version that is considered canonical is arbitrary
 * for historical reasons, generally it will be R4 or R5 but this varies by resource
 * type.
 * <p>
 * This class is an internal HAPI FHIR API and can change without notice at any time.
 * Use with caution!
 * </p>
 */
public class VersionCanonicalizer {

	private static final BaseAdvisor_30_50 ADVISOR_30_50 = new BaseAdvisor_30_50(false);
	private static final BaseAdvisor_30_40 ADVISOR_30_40 = new BaseAdvisor_30_40(false);
	private static final BaseAdvisor_10_40 ADVISOR_10_40 = new BaseAdvisor_10_40(false);
	private static final BaseAdvisor_10_50 ADVISOR_10_50 = new BaseAdvisor_10_50(false);
	private static final BaseAdvisor_40_50 ADVISOR_40_50 = new BaseAdvisor_40_50(false);
	private static final BaseAdvisor_43_50 ADVISOR_43_50 = new BaseAdvisor_43_50(false);
	private static final BaseAdvisor_14_40 ADVISOR_14_40 = new BaseAdvisor_14_40(false);
	private static final BaseAdvisor_14_50 ADVISOR_14_50 = new BaseAdvisor_14_50(false);

	private final IStrategy myStrategy;
	private final FhirContext myContext;

	public VersionCanonicalizer(FhirVersionEnum theTargetVersion) {
		this(theTargetVersion.newContextCached());
	}

	public VersionCanonicalizer(FhirContext theTargetContext) {
		myContext = theTargetContext;
		FhirVersionEnum targetVersion = theTargetContext.getVersion().getVersion();
		switch (targetVersion) {
			case DSTU2:
				myStrategy = new Dstu2Strategy(false);
				break;
			case DSTU2_HL7ORG:
				myStrategy = new Dstu2Strategy(true);
				break;
			case DSTU2_1:
				myStrategy = new Dstu21Strategy();
				break;
			case DSTU3:
				myStrategy = new Dstu3Strategy();
				break;
			case R4:
				myStrategy = new R4Strategy();
				break;
			case R4B:
				myStrategy = new R4BStrategy();
				break;
			case R5:
				myStrategy = new R5Strategy();
				break;
			default:
				throw new IllegalStateException(Msg.code(193) + "Can't handle version: " + targetVersion);
		}
	}

	@SuppressWarnings({"EnhancedSwitchMigration"})

	/**
	 * Canonical version: R5
	 */
	public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
		return myStrategy.capabilityStatementToCanonical(theCapabilityStatement);
	}

	/**
	 * Canonical version: R5
	 */
	public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theCapabilityStatement) {
		return myStrategy.capabilityStatementFromCanonical(theCapabilityStatement);
	}

	/**
	 * Canonical version: R4
	 */
	public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
		if (theCodeableConcept == null) {
			return null;
		}
		return myStrategy.codeableConceptToCanonical(theCodeableConcept);
	}

	/**
	 * Canonical version: R4
	 */
	public Coding codingToCanonical(IBaseCoding theCodingToValidate) {
		if (theCodingToValidate == null) {
			return null;
		}
		return myStrategy.codingToCanonical(theCodingToValidate);
	}

	/**
	 * Canonical version: R4
	 */
	public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
		if (theValueSet == null) {
			return null;
		}
		return myStrategy.valueSetToCanonical(theValueSet);
	}

	/**
	 * Canonical version: R4
	 */
	public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
		return myStrategy.codeSystemToCanonical(theCodeSystem);
	}

	/**
	 * Canonical version: R4
	 */
	public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
		return myStrategy.valueSetFromCanonical(theValueSet);
	}

	/**
	 * Canonical version: R4
	 */
	public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
		return myStrategy.conceptMapToCanonical(theConceptMap);
	}

	/**
	 * Canonical version: R5
	 * <p>
	 * Note that this method will look for any nonstandard resource types specified in
	 * {@literal SearchParameter.base} or {@literal SearchParameter.target} and move them into
	 * extensions with the URLs {@link HapiExtensions#EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE}
	 * and {@link HapiExtensions#EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE} respectively. If any
	 * nonstandard resource types are found, all resource types in the respective list are moved into
	 * the extension (including standard types) and the source list is cleared.
	 */
	public <T extends IBaseResource> SearchParameter searchParameterToCanonical(T theSearchParameter) {

		/*
		 * The R4 model allows custom types to be put into SearchParameter.base and
		 * SearchParameter.target because those fields use a simple CodeType. But
		 * in R5 it uses an Enumeration, so it's not actually possible to put custom
		 * resource types into those fields. This means that the version converter fails
		 * with an exception unless we remove those values from those fields before
		 * conversion. However, we don't want to affect the state of the originally
		 * passed in resource since that may affect other things. So, we clone
		 * it first. This is a pain in the butt, but there doesn't seem to be any
		 * better option.
		 */
		T input = myContext.newTerser().clone(theSearchParameter);

		List<String> baseExtensionValues =
				extractNonStandardSearchParameterListAndClearSourceIfAnyArePresent(input, "base");
		List<String> targetExtensionValues =
				extractNonStandardSearchParameterListAndClearSourceIfAnyArePresent(input, "target");

		SearchParameter retVal = myStrategy.searchParameterToCanonical(input);

		baseExtensionValues.forEach(
				t -> retVal.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE, new CodeType(t)));
		targetExtensionValues.forEach(
				t -> retVal.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE, new CodeType(t)));
		return retVal;
	}

	public IBaseResource searchParameterFromCanonical(SearchParameter theSearchParameter) {
		return myStrategy.searchParameterFromCanonical(theSearchParameter);
	}

	public IBaseParameters parametersFromCanonical(Parameters theParameters) {
		return myStrategy.parametersFromCanonical(theParameters);
	}

	public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
		StructureDefinition retVal = myStrategy.structureDefinitionToCanonical(theResource);
		String packageUserData = (String) theResource.getUserData("package");
		if (packageUserData != null) {
			retVal.setUserData("package", packageUserData);
			retVal.setSourcePackage(new PackageInformation(
					packageUserData, theResource.getStructureFhirVersionEnum().getFhirVersionString(), new Date()));
		}
		return retVal;
	}

	public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
		return myStrategy.structureDefinitionFromCanonical(theResource);
	}

	public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
		return myStrategy.valueSetFromValidatorCanonical(theResource);
	}

	public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
		return myStrategy.resourceToValidatorCanonical(theResource);
	}

	public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
		return myStrategy.valueSetToValidatorCanonical(theResource);
	}

	public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
		return myStrategy.codeSystemToValidatorCanonical(theResource);
	}

	public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
		return myStrategy.auditEventFromCanonical(theResource);
	}

	@Nonnull
	private List<String> extractNonStandardSearchParameterListAndClearSourceIfAnyArePresent(
			IBaseResource theSearchParameter, String theChildName) {

		BaseRuntimeChildDefinition child =
				myContext.getResourceDefinition(theSearchParameter).getChildByName(theChildName);
		List<IBase> baseList = child.getAccessor().getValues(theSearchParameter);

		List<String> baseExtensionValues = baseList.stream()
				.filter(Objects::nonNull)
				.filter(t -> t instanceof IPrimitiveType)
				.map(t -> (IPrimitiveType<?>) t)
				.map(IPrimitiveType::getValueAsString)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
		if (baseExtensionValues.stream().allMatch(Enumerations.VersionIndependentResourceTypesAll::isValidCode)) {
			baseExtensionValues.clear();
		} else {
			baseList.clear();
		}
		return baseExtensionValues;
	}

	private interface IStrategy {

		CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement);

		Coding codingToCanonical(IBaseCoding theCoding);

		CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept);

		ValueSet valueSetToCanonical(IBaseResource theValueSet);

		CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem);

		IBaseResource valueSetFromCanonical(ValueSet theValueSet);

		ConceptMap conceptMapToCanonical(IBaseResource theConceptMap);

		SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter);

		IBaseParameters parametersFromCanonical(Parameters theParameters);

		StructureDefinition structureDefinitionToCanonical(IBaseResource theResource);

		IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource);

		IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource);

		org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource);

		org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource);

		org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource);

		IBaseResource searchParameterFromCanonical(SearchParameter theResource);

		IBaseResource auditEventFromCanonical(AuditEvent theResource);

		IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource);
	}

	private static class Dstu2Strategy implements IStrategy {

		private final FhirContext myDstu2Hl7OrgContext = FhirContext.forDstu2Hl7OrgCached();

		private final FhirContext myDstu2Context = FhirContext.forDstu2Cached();
		private final boolean myHl7OrgStructures;

		public Dstu2Strategy(boolean theHl7OrgStructures) {
			myHl7OrgStructures = theHl7OrgStructures;
		}

		@Override
		public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theCapabilityStatement);
			return (CapabilityStatement) VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
		}

		@Override
		public Coding codingToCanonical(IBaseCoding theCoding) {
			CodingDt coding = (CodingDt) theCoding;
			Coding retVal = new Coding();
			retVal.setCode(coding.getCode());
			retVal.setSystem(coding.getSystem());
			retVal.setDisplay(coding.getDisplay());
			retVal.setVersion(coding.getVersion());
			if (!coding.getUserSelectedElement().isEmpty()) {
				retVal.setUserSelected(coding.getUserSelected());
			}

			return retVal;
		}

		@Override
		public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
			CodeableConceptDt codeableConcept = (CodeableConceptDt) theCodeableConcept;

			CodeableConcept retVal = new CodeableConcept();
			retVal.setText(codeableConcept.getText());
			for (CodingDt next : codeableConcept.getCoding()) {
				retVal.addCoding(codingToCanonical(next));
			}

			return retVal;
		}

		@Override
		public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theValueSet);
			return (ValueSet) VersionConvertorFactory_10_40.convertResource(reencoded, ADVISOR_10_40);
		}

		@Override
		public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
			CodeSystem retVal = new CodeSystem();

			ca.uhn.fhir.model.dstu2.resource.ValueSet input = (ca.uhn.fhir.model.dstu2.resource.ValueSet) theCodeSystem;
			retVal.setUrl(input.getUrl());

			for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept next :
					input.getCodeSystem().getConcept()) {
				translateAndAddConcept(next, retVal.getConcept());
			}

			return retVal;
		}

		private void translateAndAddConcept(
				ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept theSource,
				List<CodeSystem.ConceptDefinitionComponent> theTarget) {
			CodeSystem.ConceptDefinitionComponent targetConcept = new CodeSystem.ConceptDefinitionComponent();
			targetConcept.setCode(theSource.getCode());
			targetConcept.setDisplay(theSource.getDisplay());

			for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConceptDesignation next :
					theSource.getDesignation()) {
				CodeSystem.ConceptDefinitionDesignationComponent targetDesignation = targetConcept.addDesignation();
				targetDesignation.setLanguage(next.getLanguage());
				targetDesignation.setValue(next.getValue());
				if (next.getUse() != null) {
					targetDesignation.setUse(codingToCanonical(next.getUse()));
				}
			}

			for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept nextChild : theSource.getConcept()) {
				translateAndAddConcept(nextChild, targetConcept.getConcept());
			}

			theTarget.add(targetConcept);
		}

		@Override
		public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
			Resource valueSetDstu2Hl7Org = VersionConvertorFactory_10_40.convertResource(theValueSet, ADVISOR_10_40);
			return reencodeFromHl7Org(valueSetDstu2Hl7Org);
		}

		@Override
		public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theConceptMap);
			return (ConceptMap) VersionConvertorFactory_10_40.convertResource(reencoded, ADVISOR_10_40);
		}

		@Override
		public SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter) {
			org.hl7.fhir.dstu2.model.SearchParameter reencoded =
					(org.hl7.fhir.dstu2.model.SearchParameter) reencodeToHl7Org(theSearchParameter);
			SearchParameter retVal =
					(SearchParameter) VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
			if (isBlank(retVal.getExpression())) {
				retVal.setExpression(reencoded.getXpath());
			}
			return retVal;
		}

		@Override
		public IBaseParameters parametersFromCanonical(Parameters theParameters) {
			Resource converted = VersionConvertorFactory_10_40.convertResource(theParameters, ADVISOR_10_40);
			return (IBaseParameters) reencodeFromHl7Org(converted);
		}

		@Override
		public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theResource);
			return (StructureDefinition) VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
		}

		@Override
		public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
			Resource converted = VersionConvertorFactory_10_50.convertResource(theResource, ADVISOR_10_50);
			return reencodeFromHl7Org(converted);
		}

		@Override
		public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
			Resource converted = VersionConvertorFactory_10_50.convertResource(theResource, ADVISOR_10_50);
			return reencodeFromHl7Org(converted);
		}

		@Override
		public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theResource);
			return VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
		}

		@Override
		public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theResource);
			return (org.hl7.fhir.r5.model.ValueSet)
					VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencodeToHl7Org(theResource);
			return (org.hl7.fhir.r5.model.CodeSystem)
					VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
		}

		@Override
		public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
			Resource hl7Org = VersionConvertorFactory_10_40.convertResource(theResource, ADVISOR_10_40);
			return reencodeFromHl7Org(hl7Org);
		}

		@Override
		public IBaseResource searchParameterFromCanonical(SearchParameter theResource) {
			Resource resource = VersionConvertorFactory_10_50.convertResource(theResource, ADVISOR_10_50);
			return reencodeFromHl7Org(resource);
		}

		@Override
		public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource) {
			Resource converted = VersionConvertorFactory_10_50.convertResource(theResource, ADVISOR_10_50);
			return (IBaseConformance) reencodeFromHl7Org(converted);
		}

		private Resource reencodeToHl7Org(IBaseResource theInput) {
			if (myHl7OrgStructures) {
				return (Resource) theInput;
			}
			return (Resource) myDstu2Hl7OrgContext
					.newJsonParser()
					.parseResource(myDstu2Context.newJsonParser().encodeResourceToString(theInput));
		}

		private IBaseResource reencodeFromHl7Org(Resource theInput) {
			if (myHl7OrgStructures) {
				return theInput;
			}
			return myDstu2Context
					.newJsonParser()
					.parseResource(myDstu2Hl7OrgContext.newJsonParser().encodeResourceToString(theInput));
		}
	}

	private static class Dstu21Strategy implements IStrategy {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_14_50.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theCapabilityStatement, ADVISOR_14_50);
		}

		@Override
		public Coding codingToCanonical(IBaseCoding theCoding) {
			return (org.hl7.fhir.r4.model.Coding) VersionConvertorFactory_14_40.convertType(
					(org.hl7.fhir.dstu2016may.model.Coding) theCoding, ADVISOR_14_40);
		}

		@Override
		public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
			return (org.hl7.fhir.r4.model.CodeableConcept) VersionConvertorFactory_14_40.convertType(
					(org.hl7.fhir.dstu2016may.model.CodeableConcept) theCodeableConcept, ADVISOR_14_40);
		}

		@Override
		public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
			return (ValueSet) VersionConvertorFactory_14_40.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theValueSet, ADVISOR_14_40);
		}

		@Override
		public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
			return (CodeSystem) VersionConvertorFactory_14_40.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theCodeSystem, ADVISOR_14_40);
		}

		@Override
		public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
			return VersionConvertorFactory_14_40.convertResource(theValueSet, ADVISOR_14_40);
		}

		@Override
		public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
			return (ConceptMap) VersionConvertorFactory_14_40.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theConceptMap, ADVISOR_14_40);
		}

		@Override
		public SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter) {
			return (SearchParameter) VersionConvertorFactory_14_50.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theSearchParameter, ADVISOR_14_50);
		}

		@Override
		public IBaseParameters parametersFromCanonical(Parameters theParameters) {
			return (IBaseParameters) VersionConvertorFactory_14_40.convertResource(theParameters, ADVISOR_14_40);
		}

		@Override
		public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
			return (StructureDefinition) VersionConvertorFactory_14_50.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theResource, ADVISOR_14_50);
		}

		@Override
		public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
			return VersionConvertorFactory_14_50.convertResource(theResource, ADVISOR_14_50);
		}

		@Override
		public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
			return VersionConvertorFactory_14_50.convertResource(theResource, ADVISOR_14_50);
		}

		@Override
		public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
			return VersionConvertorFactory_14_50.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theResource, ADVISOR_14_50);
		}

		@Override
		public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_14_50.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theResource, ADVISOR_14_50);
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.CodeSystem) VersionConvertorFactory_14_50.convertResource(
					(org.hl7.fhir.dstu2016may.model.Resource) theResource, ADVISOR_14_50);
		}

		@Override
		public IBaseResource searchParameterFromCanonical(SearchParameter theResource) {
			return VersionConvertorFactory_14_50.convertResource(theResource, ADVISOR_14_50);
		}

		@Override
		public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
			return VersionConvertorFactory_14_40.convertResource(theResource, ADVISOR_14_40);
		}

		@Override
		public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource) {
			return (IBaseConformance) VersionConvertorFactory_14_50.convertResource(theResource, ADVISOR_14_50);
		}
	}

	private static class Dstu3Strategy implements IStrategy {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_30_50.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theCapabilityStatement, ADVISOR_30_50);
		}

		@Override
		public Coding codingToCanonical(IBaseCoding theCoding) {
			return (org.hl7.fhir.r4.model.Coding) VersionConvertorFactory_30_40.convertType(
					(org.hl7.fhir.dstu3.model.Coding) theCoding, ADVISOR_30_40);
		}

		@Override
		public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
			return (org.hl7.fhir.r4.model.CodeableConcept) VersionConvertorFactory_30_40.convertType(
					(org.hl7.fhir.dstu3.model.CodeableConcept) theCodeableConcept, ADVISOR_30_40);
		}

		@Override
		public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
			return (ValueSet) VersionConvertorFactory_30_40.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theValueSet, ADVISOR_30_40);
		}

		@Override
		public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
			return (CodeSystem) VersionConvertorFactory_30_40.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theCodeSystem, ADVISOR_30_40);
		}

		@Override
		public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
			return VersionConvertorFactory_30_40.convertResource(theValueSet, ADVISOR_30_40);
		}

		@Override
		public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
			return (ConceptMap) VersionConvertorFactory_30_40.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theConceptMap, ADVISOR_30_40);
		}

		@Override
		public SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter) {
			return (SearchParameter) VersionConvertorFactory_30_50.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theSearchParameter, ADVISOR_30_50);
		}

		@Override
		public IBaseParameters parametersFromCanonical(Parameters theParameters) {
			return (IBaseParameters) VersionConvertorFactory_30_40.convertResource(theParameters, ADVISOR_30_40);
		}

		@Override
		public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
			return (StructureDefinition) VersionConvertorFactory_30_50.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theResource, ADVISOR_30_50);
		}

		@Override
		public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
			return VersionConvertorFactory_30_50.convertResource(theResource, ADVISOR_30_50);
		}

		@Override
		public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
			return VersionConvertorFactory_30_50.convertResource(theResource, ADVISOR_30_50);
		}

		@Override
		public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
			return VersionConvertorFactory_30_50.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theResource, ADVISOR_30_50);
		}

		@Override
		public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_30_50.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theResource, ADVISOR_30_50);
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.CodeSystem) VersionConvertorFactory_30_50.convertResource(
					(org.hl7.fhir.dstu3.model.Resource) theResource, ADVISOR_30_50);
		}

		@Override
		public IBaseResource searchParameterFromCanonical(SearchParameter theResource) {
			return VersionConvertorFactory_30_50.convertResource(theResource, ADVISOR_30_50);
		}

		@Override
		public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
			return VersionConvertorFactory_30_40.convertResource(theResource, ADVISOR_30_40);
		}

		@Override
		public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource) {
			return (IBaseConformance) VersionConvertorFactory_30_50.convertResource(theResource, ADVISOR_30_50);
		}
	}

	private static class R4Strategy implements IStrategy {
		@Override
		public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r4.model.Resource) theCapabilityStatement, ADVISOR_40_50);
		}

		@Override
		public Coding codingToCanonical(IBaseCoding theCoding) {
			return (Coding) theCoding;
		}

		@Override
		public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
			return (CodeableConcept) theCodeableConcept;
		}

		@Override
		public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
			return (ValueSet) theValueSet;
		}

		@Override
		public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
			return (CodeSystem) theCodeSystem;
		}

		@Override
		public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
			return theValueSet;
		}

		@Override
		public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
			return (ConceptMap) theConceptMap;
		}

		@Override
		public SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter) {
			return (SearchParameter) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r4.model.Resource) theSearchParameter, ADVISOR_40_50);
		}

		@Override
		public IBaseParameters parametersFromCanonical(Parameters theParameters) {
			return theParameters;
		}

		@Override
		public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
			return (StructureDefinition) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r4.model.Resource) theResource, ADVISOR_40_50);
		}

		@Override
		public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
			return VersionConvertorFactory_40_50.convertResource(theResource, ADVISOR_40_50);
		}

		@Override
		public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
			return VersionConvertorFactory_40_50.convertResource(theResource, ADVISOR_40_50);
		}

		@Override
		public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
			return VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r4.model.Resource) theResource, ADVISOR_40_50);
		}

		@Override
		public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r4.model.Resource) theResource, ADVISOR_40_50);
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.CodeSystem) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r4.model.Resource) theResource, ADVISOR_40_50);
		}

		@Override
		public IBaseResource searchParameterFromCanonical(SearchParameter theResource) {
			return VersionConvertorFactory_40_50.convertResource(theResource, ADVISOR_40_50);
		}

		@Override
		public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
			return theResource;
		}

		@Override
		public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource) {
			return (IBaseConformance) VersionConvertorFactory_40_50.convertResource(theResource, ADVISOR_40_50);
		}
	}

	private static class R4BStrategy implements IStrategy {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_43_50.convertResource(
					(org.hl7.fhir.r4b.model.Resource) theCapabilityStatement, ADVISOR_43_50);
		}

		@Override
		public Coding codingToCanonical(IBaseCoding theCoding) {
			org.hl7.fhir.r5.model.Coding r5coding = (org.hl7.fhir.r5.model.Coding)
					VersionConvertorFactory_43_50.convertType((org.hl7.fhir.r4b.model.Coding) theCoding, ADVISOR_43_50);
			return (org.hl7.fhir.r4.model.Coding) VersionConvertorFactory_40_50.convertType(r5coding, ADVISOR_40_50);
		}

		@Override
		public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
			org.hl7.fhir.r5.model.CodeableConcept r5coding =
					(org.hl7.fhir.r5.model.CodeableConcept) VersionConvertorFactory_43_50.convertType(
							(org.hl7.fhir.r4b.model.CodeableConcept) theCodeableConcept, ADVISOR_43_50);
			return (org.hl7.fhir.r4.model.CodeableConcept)
					VersionConvertorFactory_40_50.convertType(r5coding, ADVISOR_40_50);
		}

		@Override
		public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
			org.hl7.fhir.r5.model.ValueSet valueSetR5 =
					(org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_43_50.convertResource(
							(org.hl7.fhir.r4b.model.Resource) theValueSet, ADVISOR_43_50);
			return (org.hl7.fhir.r4.model.ValueSet)
					VersionConvertorFactory_40_50.convertResource(valueSetR5, ADVISOR_40_50);
		}

		@Override
		public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
			org.hl7.fhir.r5.model.CodeSystem codeSystemR5 =
					(org.hl7.fhir.r5.model.CodeSystem) VersionConvertorFactory_43_50.convertResource(
							(org.hl7.fhir.r4b.model.Resource) theCodeSystem, ADVISOR_43_50);
			return (org.hl7.fhir.r4.model.CodeSystem)
					VersionConvertorFactory_40_50.convertResource(codeSystemR5, ADVISOR_40_50);
		}

		@Override
		public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
			org.hl7.fhir.r5.model.ValueSet valueSetR5 = (org.hl7.fhir.r5.model.ValueSet)
					VersionConvertorFactory_40_50.convertResource(theValueSet, ADVISOR_40_50);
			return VersionConvertorFactory_43_50.convertResource(valueSetR5, ADVISOR_43_50);
		}

		@Override
		public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
			org.hl7.fhir.r5.model.ConceptMap conceptMapR5 =
					(org.hl7.fhir.r5.model.ConceptMap) VersionConvertorFactory_43_50.convertResource(
							(org.hl7.fhir.r4b.model.Resource) theConceptMap, ADVISOR_43_50);
			return (ConceptMap) VersionConvertorFactory_40_50.convertResource(conceptMapR5, ADVISOR_40_50);
		}

		@Override
		public SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter) {
			return (SearchParameter) VersionConvertorFactory_43_50.convertResource(
					(org.hl7.fhir.r4b.model.Resource) theSearchParameter, ADVISOR_43_50);
		}

		@Override
		public IBaseParameters parametersFromCanonical(Parameters theParameters) {
			org.hl7.fhir.r5.model.Parameters parametersR5 = (org.hl7.fhir.r5.model.Parameters)
					VersionConvertorFactory_40_50.convertResource(theParameters, ADVISOR_40_50);
			return (IBaseParameters) VersionConvertorFactory_43_50.convertResource(parametersR5, ADVISOR_43_50);
		}

		@Override
		public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
			return (StructureDefinition) VersionConvertorFactory_43_50.convertResource(
					(org.hl7.fhir.r4b.model.Resource) theResource, ADVISOR_43_50);
		}

		@Override
		public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
			return VersionConvertorFactory_43_50.convertResource(theResource, ADVISOR_43_50);
		}

		@Override
		public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
			return VersionConvertorFactory_43_50.convertResource(theResource, ADVISOR_43_50);
		}

		@Override
		public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
			return VersionConvertorFactory_43_50.convertResource(
					(org.hl7.fhir.r4b.model.Resource) theResource, ADVISOR_43_50);
		}

		@Override
		public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_43_50.convertResource(
					(org.hl7.fhir.r4b.model.Resource) theResource, ADVISOR_43_50);
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.CodeSystem) VersionConvertorFactory_43_50.convertResource(
					(org.hl7.fhir.r4b.model.Resource) theResource, ADVISOR_43_50);
		}

		@Override
		public IBaseResource searchParameterFromCanonical(SearchParameter theResource) {
			return VersionConvertorFactory_43_50.convertResource(theResource, ADVISOR_43_50);
		}

		@Override
		public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
			org.hl7.fhir.r5.model.AuditEvent r5 = (org.hl7.fhir.r5.model.AuditEvent)
					VersionConvertorFactory_40_50.convertResource(theResource, ADVISOR_40_50);
			return VersionConvertorFactory_43_50.convertResource(r5, ADVISOR_43_50);
		}

		@Override
		public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource) {
			return (IBaseConformance) VersionConvertorFactory_43_50.convertResource(theResource, ADVISOR_43_50);
		}
	}

	private static class R5Strategy implements IStrategy {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
			return (CapabilityStatement) theCapabilityStatement;
		}

		@Override
		public Coding codingToCanonical(IBaseCoding theCoding) {
			return (org.hl7.fhir.r4.model.Coding)
					VersionConvertorFactory_40_50.convertType((org.hl7.fhir.r5.model.Coding) theCoding, ADVISOR_40_50);
		}

		@Override
		public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
			return (org.hl7.fhir.r4.model.CodeableConcept) VersionConvertorFactory_40_50.convertType(
					(org.hl7.fhir.r5.model.CodeableConcept) theCodeableConcept, ADVISOR_40_50);
		}

		@Override
		public ValueSet valueSetToCanonical(IBaseResource theValueSet) {
			return (ValueSet) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r5.model.ValueSet) theValueSet, ADVISOR_40_50);
		}

		@Override
		public CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
			return (CodeSystem) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r5.model.CodeSystem) theCodeSystem, ADVISOR_40_50);
		}

		@Override
		public IBaseResource valueSetFromCanonical(ValueSet theValueSet) {
			return VersionConvertorFactory_40_50.convertResource(theValueSet, ADVISOR_40_50);
		}

		@Override
		public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
			return (ConceptMap) VersionConvertorFactory_40_50.convertResource(
					(org.hl7.fhir.r5.model.ConceptMap) theConceptMap, ADVISOR_40_50);
		}

		@Override
		public SearchParameter searchParameterToCanonical(IBaseResource theSearchParameter) {
			return (SearchParameter) theSearchParameter;
		}

		@Override
		public IBaseParameters parametersFromCanonical(Parameters theParameters) {
			return (IBaseParameters) VersionConvertorFactory_40_50.convertResource(theParameters, ADVISOR_40_50);
		}

		@Override
		public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
			return (StructureDefinition) theResource;
		}

		@Override
		public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
			return theResource;
		}

		@Override
		public IBaseResource valueSetFromValidatorCanonical(org.hl7.fhir.r5.model.ValueSet theResource) {
			return theResource;
		}

		@Override
		public org.hl7.fhir.r5.model.Resource resourceToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.Resource) theResource;
		}

		@Override
		public org.hl7.fhir.r5.model.ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.ValueSet) theResource;
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
			return (org.hl7.fhir.r5.model.CodeSystem) theResource;
		}

		@Override
		public IBaseResource searchParameterFromCanonical(SearchParameter theResource) {
			return theResource;
		}

		@Override
		public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
			return VersionConvertorFactory_40_50.convertResource(theResource, ADVISOR_40_50);
		}

		@Override
		public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theResource) {
			return theResource;
		}
	}
}
