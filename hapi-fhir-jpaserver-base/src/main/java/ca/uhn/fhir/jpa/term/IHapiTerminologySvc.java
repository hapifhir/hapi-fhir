package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public interface IHapiTerminologySvc {

	void deleteCodeSystem(TermCodeSystem thePersCs);

	ValueSet expandValueSet(ValueSet theValueSetToExpand);

	ValueSet expandValueSet(ValueSet theValueSetToExpand, int theOffset, int theCount);

	void expandValueSet(ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator);

	/**
	 * Version independent
	 */
	IBaseResource expandValueSet(IBaseResource theValueSetToExpand);

	/**
	 * Version independent
	 */
	IBaseResource expandValueSet(IBaseResource theValueSetToExpand, int theOffset, int theCount);

	void expandValueSet(IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator);

	List<VersionIndependentConcept> expandValueSet(String theValueSet);

	Optional<TermConcept> findCode(String theCodeSystem, String theCode);

	List<TermConcept> findCodes(String theSystem);

	Set<TermConcept> findCodesAbove(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersionPid, String theCode);

	List<VersionIndependentConcept> findCodesAbove(String theSystem, String theCode);

	List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode);

	Set<TermConcept> findCodesBelow(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersionPid, String theCode);

	List<VersionIndependentConcept> findCodesBelow(String theSystem, String theCode);

	List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode);

	void saveDeferred();

	/**
	 * This is mostly for unit tests - we can disable processing of deferred concepts
	 * by changing this flag
	 */
	void setProcessDeferred(boolean theProcessDeferred);

	void storeNewCodeSystemVersion(Long theCodeSystemResourcePid, String theSystemUri, String theSystemName, String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion);

	/**
	 * @return Returns the ID of the created/updated code system
	 */
	IIdType storeNewCodeSystemVersion(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails, List<org.hl7.fhir.r4.model.ValueSet> theValueSets, List<org.hl7.fhir.r4.model.ConceptMap> theConceptMaps);

	void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity);

	void deleteConceptMapAndChildren(ResourceTable theResourceTable);

	void deleteValueSetAndChildren(ResourceTable theResourceTable);

	void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap);

	void storeTermValueSet(ResourceTable theResourceTable, ValueSet theValueSet);

	boolean supportsSystem(String theCodeSystem);

	List<TermConceptMapGroupElementTarget> translate(TranslationRequest theTranslationRequest);

	List<TermConceptMapGroupElement> translateWithReverse(TranslationRequest theTranslationRequest);

	IFhirResourceDaoCodeSystem.SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB, IPrimitiveType<String> theSystem, IBaseCoding theCodingA, IBaseCoding theCodingB);

	AtomicInteger applyDeltaCodesystemsAdd(String theSystem, @Nullable String theParent, CodeSystem theValue);

	AtomicInteger applyDeltaCodesystemsRemove(String theSystem, CodeSystem theDelta);

	void preExpandDeferredValueSetsToTerminologyTables();

	/**
	 * Version independent
	 */
	ValidateCodeResult validateCodeIsInPreExpandedValueSet(IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept);

	boolean isValueSetPreExpandedForCodeValidation(ValueSet theValueSet);

	/**
	 * Version independent
	 */
	boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet);
}
