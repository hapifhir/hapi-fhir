package ca.uhn.fhir.jpa.search.lastn;

public class IndexConstants {

	// TODO: These should all be moved into ElasticSearchSvcImpl.
	public static final String OBSERVATION_INDEX = "observation_index";
	public static final String CODE_INDEX = "code_index";
	public static final String OBSERVATION_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedSearchParamLastNEntity";
	public static final String CODE_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedCodeCodeableConceptEntity";

	public static final String SUBJECT_SEARCH_PARAM = "subject";
	public static final String PATIENT_SEARCH_PARAM = "patient";
	public static final String CODE_SEARCH_PARAM = "code";
	public static final String CATEGORY_SEARCH_PARAM = "category";

}
