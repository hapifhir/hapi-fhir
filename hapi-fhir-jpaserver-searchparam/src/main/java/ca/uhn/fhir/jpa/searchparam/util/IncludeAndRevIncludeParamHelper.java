package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.UrlUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class IncludeAndRevIncludeParamHelper {

	private final FhirContext myContext;
	private final ISearchParamRegistry mySearchParamRegistry;

	public IncludeAndRevIncludeParamHelper(FhirContext theFhirContext, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theFhirContext;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public void validateIncludeAndRevIncludeParams(SearchParameterMap theParams) {
		validateIncludes(theParams.getIncludes(), Constants.PARAM_INCLUDE);
		validateIncludes(theParams.getRevIncludes(), Constants.PARAM_REVINCLUDE);
	}

	private void validateIncludes(Set<Include> includes, String name) {
		for (Include next : includes) {
			String value = next.getValue();
			if (value.equals(Constants.INCLUDE_STAR) || isBlank(value)) {
				continue;
			}

			String paramType = next.getParamType();
			String paramName = next.getParamName();

			if (isBlank(paramType) || isBlank(paramName)) {
				String msg = myContext
					.getLocalizer()
					.getMessageSanitized(IncludeAndRevIncludeParamHelper.class, "invalidInclude", name, value, "");
				throw new InvalidRequestException(Msg.code(2018) + msg);
			}

			if (!Constants.INCLUDE_STAR.equals(paramName)
				&& mySearchParamRegistry.getActiveSearchParam(paramType, paramName) == null) {
				List<String> validNames = mySearchParamRegistry.getActiveSearchParams(paramType).values().stream()
					.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
					.map(t -> UrlUtil.sanitizeUrlPart(t.getName()))
					.sorted()
					.collect(Collectors.toList());
				String searchParamMessage = myContext
					.getLocalizer()
					.getMessage(
						IncludeAndRevIncludeParamHelper.class,
						"invalidSearchParameter",
						UrlUtil.sanitizeUrlPart(paramName),
						UrlUtil.sanitizeUrlPart(paramType),
						validNames);
				String msg = myContext
					.getLocalizer()
					.getMessage(
						IncludeAndRevIncludeParamHelper.class,
						"invalidInclude",
						UrlUtil.sanitizeUrlPart(name),
						UrlUtil.sanitizeUrlPart(value),
						searchParamMessage); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2015) + msg);
			}
		}
	}

}
