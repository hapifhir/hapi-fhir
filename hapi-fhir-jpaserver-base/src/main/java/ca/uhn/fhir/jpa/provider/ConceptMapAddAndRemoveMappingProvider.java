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

public class ConceptMapAddAndRemoveMappingProvider {

	private final IFhirResourceDaoConceptMap<?> myConceptMapDao;

	/**
	 * Constructor
	 */
	public ConceptMapAddAndRemoveMappingProvider(@Nonnull IFhirResourceDaoConceptMap<?> theConceptMapDao) {
		Validate.notNull(theConceptMapDao, "theConceptMapDao must not be null");
		myConceptMapDao = theConceptMapDao;
	}


	@Operation(typeName = "ConceptMap", name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING)
	public IBaseOperationOutcome addMapping(
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_CONCEPTMAP_URL, typeName = "uri") IPrimitiveType<String> theUrl,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_SYSTEM, typeName = "uri") IPrimitiveType<String> theSourceSystem,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_VERSION, typeName = "code") IPrimitiveType<String> theSourceVersion,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_CODE, typeName = "code") IPrimitiveType<String> theSourceCode,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_DISPLAY, typeName = "string") IPrimitiveType<String> theSourceDisplay,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_SYSTEM, typeName = "uri") IPrimitiveType<String> theTargetSystem,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_VERSION, typeName = "code") IPrimitiveType<String> theTargetVersion,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_CODE, typeName = "code") IPrimitiveType<String> theTargetCode,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_DISPLAY, typeName = "string") IPrimitiveType<String> theTargetDisplay,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_EQUIVALENCE, typeName = "code") IPrimitiveType<String> theEquivalence,
		RequestDetails theRequestDetails
	) {

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

	@Operation(typeName = "ConceptMap", name = JpaConstants.OPERATION_CONCEPTMAP_REMOVE_MAPPING)
	public IBaseOperationOutcome removeMapping(
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_CONCEPTMAP_URL, typeName = "uri") IPrimitiveType<String> theUrl,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_SYSTEM, typeName = "uri") IPrimitiveType<String> theSourceSystem,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_VERSION, typeName = "code") IPrimitiveType<String> theSourceVersion,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_CODE, typeName = "code") IPrimitiveType<String> theSourceCode,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_SYSTEM, typeName = "uri") IPrimitiveType<String> theTargetSystem,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_VERSION, typeName = "code") IPrimitiveType<String> theTargetVersion,
		@OperationParam(name = JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_CODE, typeName = "code") IPrimitiveType<String> theTargetCode,
		RequestDetails theRequestDetails
	) {

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
