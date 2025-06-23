package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.svcs.ISearchLimiterSvc;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchLimiterSvc implements ISearchLimiterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ISearchLimiterSvc.class);

	private final Multimap<String, String> myOperationToOmittedResourceTypes = HashMultimap.create();

	public void addOmittedResourceType(@Nonnull String theOperationName, @Nonnull String theResourceType) {
		if (ourLog.isDebugEnabled()) {
			ourLog.debug(
					"Filtering operation {} to prevent including resources of type {}",
					theOperationName,
					theResourceType);
		}
		myOperationToOmittedResourceTypes.put(theOperationName, theResourceType);
	}

	@Override
	public Set<String> getResourcesToOmitForOperationSearches(@Nonnull String theOperationName) {
		Collection<String> omittedResourceNames = myOperationToOmittedResourceTypes.get(theOperationName);
		return Set.copyOf(omittedResourceNames);
	}

	@Override
	public void removeOmittedResourceType(@Nonnull String theOperationName, String theResourceType) {
		if (isBlank(theResourceType)) {
			ourLog.debug("Removing any filtering of resources for {} operations", theOperationName);
			myOperationToOmittedResourceTypes.removeAll(theOperationName);
		} else {
			ourLog.debug("Removing any filtering of {} for {} operation", theResourceType, theOperationName);
			myOperationToOmittedResourceTypes.remove(theOperationName, theResourceType);
		}
	}
}
