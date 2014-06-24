package example;

import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;

@SuppressWarnings("unused")
//START SNIPPET: provider
public class PagingPatientProvider implements IResourceProvider {

	/**
	 * Search for Patient resources matching a given family name 
	 */
	@Search
	public IBundleProvider search(@RequiredParam(name = Patient.SP_FAMILY) StringParam theFamily) {
		final InstantDt searchTime = InstantDt.withCurrentTime();
		final List<String> matchingResourceIds = findIdsByFamily(theFamily);

		return new IBundleProvider() {

			@Override
			public int size() {
				return matchingResourceIds.size();
			}

			@Override
			public List<IResource> getResources(int theFromIndex, int theToIndex) {
				int end = Math.max(theToIndex, matchingResourceIds.size() - 1);
				List<String> idsToReturn = matchingResourceIds.subList(theFromIndex, end);
				return loadResourcesByIds(idsToReturn);
			}

			@Override
			public InstantDt getPublished() {
				return searchTime;
			}
		};
	}

	/**
	 * Get a list of resource IDs which match a given family name
	 */
	private List<String> findIdsByFamily(StringParam theFamily) {
		// .. implement this search against the database ..
		return null;
	}

	/**
	 * Load a list of patient resources given their IDs
	 */
	private List<IResource> loadResourcesByIds(List<String> theFamily) {
		// .. implement this search against the database ..
		return null;
	}

	@Override
	public Class<? extends IResource> getResourceType() {
		return Patient.class;
	}

}
//END SNIPPET: provider
