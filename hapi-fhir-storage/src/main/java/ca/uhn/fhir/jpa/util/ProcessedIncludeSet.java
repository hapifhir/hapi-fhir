package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ProcessedIncludeSet {

	private static final Logger ourLog = LoggerFactory.getLogger(ProcessedIncludeSet.class);
	private final boolean myIncludeStar;
	private final Set<String> myPaths;

	private ProcessedIncludeSet(boolean theIncludeStar, Set<String> thePaths) {
		myIncludeStar = theIncludeStar;
		myPaths = thePaths;
	}

	public static ProcessedIncludeSet create(FhirContext theContext, Collection<Include> theInclude, boolean theReverseMode, ISearchParamRegistry theSearchParamRegistry) {

		Set<String> paths = null;
		Set<String> starResourceTypes = null;
		boolean includeStar = false;

		for (Include nextInclude : theInclude) {
			if (Constants.INCLUDE_STAR.equals(nextInclude.getValue())) {
				includeStar = true;
				continue;
			}
			if (Constants.INCLUDE_STAR.equals(nextInclude.getParamName())) {
//				if (isNotBlank(nextInclude.get))
				includeStar = true;
				continue;
			}

			RuntimeSearchParam param;
			String resType = nextInclude.getParamType();
			if (isBlank(resType)) {
				continue;
			}
			RuntimeResourceDefinition def = theContext.getResourceDefinition(resType);
			if (def == null) {
				ourLog.warn("Unknown resource type in include/revinclude=" + nextInclude.getValue());
				continue;
			}

			String paramName = nextInclude.getParamName();
			if (isNotBlank(paramName)) {
				param = theSearchParamRegistry.getActiveSearchParam(resType, paramName);
			} else {
				param = null;
			}
			if (param == null) {
				ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
				continue;
			}

			// FIXME: why?
//			List<String> nextPaths = theReverseMode ? param.getPathsSplitForResourceType(resType) : param.getPathsSplit();
			List<String> nextPaths = param.getPathsSplitForResourceType(resType);
			if (paths == null) {
				paths = new HashSet<>();
			}
			paths.addAll(nextPaths);

		}

		return new ProcessedIncludeSet(includeStar, paths);
	}

}
