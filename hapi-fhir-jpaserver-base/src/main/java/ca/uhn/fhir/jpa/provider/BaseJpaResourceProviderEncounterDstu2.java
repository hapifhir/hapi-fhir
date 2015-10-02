package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoEncounter;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.Constants;

public class BaseJpaResourceProviderEncounterDstu2 extends JpaResourceProviderDstu2<Encounter> {

	/**
	 * Encounter/123/$everything
	 */
	//@formatter:off
	@Operation(name = "everything", idempotent = true)
	public ca.uhn.fhir.rest.server.IBundleProvider EncounterInstanceEverything(

			javax.servlet.http.HttpServletRequest theServletRequest,

			@IdParam 
			ca.uhn.fhir.model.primitive.IdDt theId,
			
			@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the size of those pages.") 
			@OperationParam(name = Constants.PARAM_COUNT) 
			ca.uhn.fhir.model.primitive.UnsignedIntDt theCount,
			
			@Description(shortDefinition="Only return resources which were last updated as specified by the given range")
			@OperationParam(name = Constants.PARAM_LASTUPDATED, min=0, max=1) 
			DateRangeParam theLastUpdated,
			
			@Sort
			SortSpec theSortSpec
			) {
		//@formatter:on

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoEncounter<Encounter>)getDao()).encounterInstanceEverything(theServletRequest, theId, theCount, theLastUpdated, theSortSpec);
		} finally {
			endRequest(theServletRequest);
		}}

		/**
		 * /Encounter/$everything
		 */
		//@formatter:off
		@Operation(name = "everything", idempotent = true)
		public ca.uhn.fhir.rest.server.IBundleProvider EncounterTypeEverything(

				javax.servlet.http.HttpServletRequest theServletRequest,

				@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the size of those pages.") 
				@OperationParam(name = Constants.PARAM_COUNT) 
				ca.uhn.fhir.model.primitive.UnsignedIntDt theCount,
				
				@Description(shortDefinition="Only return resources which were last updated as specified by the given range")
				@OperationParam(name = Constants.PARAM_LASTUPDATED, min=0, max=1) 
				DateRangeParam theLastUpdated,
				
				@Sort
				SortSpec theSortSpec
				) {
			//@formatter:on

			startRequest(theServletRequest);
			try {
				return ((IFhirResourceDaoEncounter<Encounter>)getDao()).encounterTypeEverything(theServletRequest, theCount, theLastUpdated, theSortSpec);
			} finally {
				endRequest(theServletRequest);
			}

	}

}
