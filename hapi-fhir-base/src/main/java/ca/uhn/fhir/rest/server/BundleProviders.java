package ca.uhn.fhir.rest.server;

import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.InstantDt;

/**
 * Utility methods for working with {@link IBundleProvider}
 */
public class BundleProviders {

	/** Non instantiable */
	private BundleProviders() {
		//nothing
	}

	/**
	 * Create a new unmodifiable empty resource list with the current time as the publish date.
	 */
	public static IBundleProvider newEmptyList() {
		final InstantDt published = InstantDt.withCurrentTime();
		return new IBundleProvider() {
			@Override
			public List<IResource> getResources(int theFromIndex, int theToIndex) {
				return Collections.emptyList();
			}

			@Override
			public int size() {
				return 0;
			}

			@Override
			public InstantDt getPublished() {
				return published;
			}
		};
	}

	public static IBundleProvider newList(IResource theResource) {
		return new SimpleBundleProvider(theResource);
	}

	public static IBundleProvider newList(List<IResource> theResources) {
		return new SimpleBundleProvider(theResources);
	}
}
