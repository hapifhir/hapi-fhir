package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.IValueSetConceptAccumulator;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.validation.ValidationOptions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * #%L
 * HAPI FHIR JPA Server
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

/**
 * This interface is the "read" interface for the terminology service. It handles things like
 * lookups, code validations, expansions, concept mappings, etc.
 * <p>
 * It is intended to only handle read operations, leaving various write operations to
 * other services within the terminology service APIs.
 * (Note that at present, a few write operations remain here- they should be moved but haven't
 * been moved yet)
 * </p>
 */
public interface ITermReadSvc extends IValidationSupport {

	ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand);

	void expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator);

	/**
	 * Version independent
	 */
	IBaseResource expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand);

	void expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator);

	List<VersionIndependentConcept> expandValueSet(ValueSetExpansionOptions theExpansionOptions, String theValueSet);

	Optional<TermConcept> findCode(String theCodeSystem, String theCode);

	Set<TermConcept> findCodesAbove(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersionPid, String theCode);

	List<VersionIndependentConcept> findCodesAbove(String theSystem, String theCode);

	List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode);

	Set<TermConcept> findCodesBelow(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersionPid, String theCode);

	List<VersionIndependentConcept> findCodesBelow(String theSystem, String theCode);

	List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode);

	CodeSystem fetchCanonicalCodeSystemFromCompleteContext(String theSystem);

	void deleteConceptMapAndChildren(ResourceTable theResourceTable);

	void deleteValueSetAndChildren(ResourceTable theResourceTable);

	void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap);

	void storeTermValueSet(ResourceTable theResourceTable, ValueSet theValueSet);

	boolean supportsSystem(String theCodeSystem);

	List<TermConceptMapGroupElementTarget> translate(TranslationRequest theTranslationRequest);

	List<TermConceptMapGroupElement> translateWithReverse(TranslationRequest theTranslationRequest);

	IFhirResourceDaoCodeSystem.SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB, IPrimitiveType<String> theSystem, IBaseCoding theCodingA, IBaseCoding theCodingB);

	void preExpandDeferredValueSetsToTerminologyTables();

	/**
	 * Version independent
	 */
	IFhirResourceDaoValueSet.ValidateCodeResult validateCodeIsInPreExpandedValueSet(ValidationOptions theOptions, IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept);

	boolean isValueSetPreExpandedForCodeValidation(ValueSet theValueSet);

	/**
	 * Version independent
	 */
	boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet);


}
