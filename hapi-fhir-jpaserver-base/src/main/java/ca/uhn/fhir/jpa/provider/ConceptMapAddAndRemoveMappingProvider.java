/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.DatatypeUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

/**
 * This resource provider implements the <code>ConceptMap/$hapi.fhir.add-mapping</code>
 * and <code>ConceptMap/$hapi.fhir.remove-mapping</code> operations.
 */
@SuppressWarnings("ClassCanBeRecord")
public class ConceptMapAddAndRemoveMappingProvider {

	private final IFhirResourceDaoConceptMap<?> myConceptMapDao;

	/**
	 * Constructor
	 */
	public ConceptMapAddAndRemoveMappingProvider(@Nonnull IFhirResourceDaoConceptMap<?> theConceptMapDao) {
		Validate.notNull(theConceptMapDao, "theConceptMapDao must not be null");
		myConceptMapDao = theConceptMapDao;
	}

	/**
	 * Operation: <code>ConceptMap/$hapi.fhir.add-mapping</code>
	 */
	@Operation(typeName = "ConceptMap", name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING)
	public IBaseOperationOutcome addMapping(
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_CONCEPTMAP_URL, typeName = "uri")
					IPrimitiveType<String> theUrl,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_SYSTEM, typeName = "uri")
					IPrimitiveType<String> theSourceSystem,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_VERSION, typeName = "code")
					IPrimitiveType<String> theSourceVersion,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_CODE, typeName = "code")
					IPrimitiveType<String> theSourceCode,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_DISPLAY, typeName = "string")
					IPrimitiveType<String> theSourceDisplay,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_SYSTEM, typeName = "uri")
					IPrimitiveType<String> theTargetSystem,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_VERSION, typeName = "code")
					IPrimitiveType<String> theTargetVersion,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_CODE, typeName = "code")
					IPrimitiveType<String> theTargetCode,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_DISPLAY, typeName = "string")
					IPrimitiveType<String> theTargetDisplay,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_EQUIVALENCE, typeName = "code")
					IPrimitiveType<String> theEquivalence,
			RequestDetails theRequestDetails) {

		IFhirResourceDaoConceptMap.AddMappingRequest request = new IFhirResourceDaoConceptMap.AddMappingRequest();
		request.setConceptMapUri(DatatypeUtil.toStringValue(theUrl));
		request.setSourceSystem(DatatypeUtil.toStringValue(theSourceSystem));
		request.setSourceSystemVersion(DatatypeUtil.toStringValue(theSourceVersion));
		request.setSourceCode(DatatypeUtil.toStringValue(theSourceCode));
		request.setSourceDisplay(DatatypeUtil.toStringValue(theSourceDisplay));
		request.setTargetSystem(DatatypeUtil.toStringValue(theTargetSystem));
		request.setTargetSystemVersion(DatatypeUtil.toStringValue(theTargetVersion));
		request.setTargetCode(DatatypeUtil.toStringValue(theTargetCode));
		request.setTargetDisplay(DatatypeUtil.toStringValue(theTargetDisplay));
		request.setEquivalence(DatatypeUtil.toStringValue(theEquivalence));

		return myConceptMapDao.addMapping(request, theRequestDetails);
	}

	/**
	 * Operation: <code>ConceptMap/$hapi.fhir.remove-mapping</code>
	 */
	@Operation(typeName = "ConceptMap", name = JpaConstants.OPERATION_CONCEPTMAP_REMOVE_MAPPING)
	public IBaseOperationOutcome removeMapping(
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_CONCEPTMAP_URL, typeName = "uri")
					IPrimitiveType<String> theUrl,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_SYSTEM, typeName = "uri")
					IPrimitiveType<String> theSourceSystem,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_VERSION, typeName = "code")
					IPrimitiveType<String> theSourceVersion,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_CODE, typeName = "code")
					IPrimitiveType<String> theSourceCode,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_SYSTEM, typeName = "uri")
					IPrimitiveType<String> theTargetSystem,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_VERSION, typeName = "code")
					IPrimitiveType<String> theTargetVersion,
			@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_CODE, typeName = "code")
					IPrimitiveType<String> theTargetCode,
			RequestDetails theRequestDetails) {

		IFhirResourceDaoConceptMap.RemoveMappingRequest request = new IFhirResourceDaoConceptMap.RemoveMappingRequest();
		request.setConceptMapUri(DatatypeUtil.toStringValue(theUrl));
		request.setSourceSystem(DatatypeUtil.toStringValue(theSourceSystem));
		request.setSourceSystemVersion(DatatypeUtil.toStringValue(theSourceVersion));
		request.setSourceCode(DatatypeUtil.toStringValue(theSourceCode));
		request.setTargetSystem(DatatypeUtil.toStringValue(theTargetSystem));
		request.setTargetSystemVersion(DatatypeUtil.toStringValue(theTargetVersion));
		request.setTargetCode(DatatypeUtil.toStringValue(theTargetCode));

		return myConceptMapDao.removeMapping(request, theRequestDetails);
	}
}
