package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class IncludeProcessor {

		private static final Logger ourLog = LoggerFactory.getLogger(IncludeProcessor.class);

	public static List<String> convertIncludesTo(FhirContext theContext, Include theInclude, boolean theReverseMode, ISearchParamRegistry theSearchParamRegistry) {

		List<String> paths;
		RuntimeSearchParam param;
		String resType = theInclude.getParamType();
		if (isBlank(resType)) {
			return Collections.emptyList();
		}
		RuntimeResourceDefinition def = theContext.getResourceDefinition(resType);
		if (def == null) {
			ourLog.warn("Unknown resource type in include/revinclude=" + theInclude.getValue());
			return Collections.emptyList();
		}

		String paramName = theInclude.getParamName();
		if (isNotBlank(paramName)) {
			param = theSearchParamRegistry.getActiveSearchParam(resType, paramName);
		} else {
			param = null;
		}
		if (param == null) {
			ourLog.warn("Unknown param name in include/revinclude=" + theInclude.getValue());
			return Collections.emptyList();
		}

		return theReverseMode ? param.getPathsSplitForResourceType(resType) : param.getPathsSplit();


	}

}
