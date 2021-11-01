package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Extract search params for advanced lucene indexing.
 *
 * This class re-uses the extracted JPA entities to build an ExtendedLuceneIndexData instance.
 */
public class ExtendedLuceneIndexExtractor {

	private final FhirContext myContext;
	private final Map<String, RuntimeSearchParam> myParams;

	public ExtendedLuceneIndexExtractor(FhirContext theContext, Map<String, RuntimeSearchParam> theActiveParams) {
		myContext = theContext;
		myParams = theActiveParams;
	}

	@NotNull
	public ExtendedLuceneIndexData extract(ResourceIndexedSearchParams theNewParams) {
		// wip mb this is testable now.
		ExtendedLuceneIndexData retVal = new ExtendedLuceneIndexData(myContext);

		theNewParams.myStringParams.forEach(nextParam ->
			retVal.addStringIndexData(nextParam.getParamName(), nextParam.getValueExact()));

		theNewParams.myTokenParams.forEach(nextParam ->
			retVal.addTokenIndexData(nextParam.getParamName(), nextParam.getSystem(), nextParam.getValue()));

		if (!theNewParams.myLinks.isEmpty()) {

			// awkwardly, links are shared between different search params if they use the same path,
			// so we re-build the linkage.
			// WIP MB is this the right design?  Or should we follow JPA and share these?
			Map<String, List<String>> linkPathToParamName = new HashMap<>();
			for (String nextParamName : theNewParams.getPopulatedResourceLinkParameters()) {
				RuntimeSearchParam sp = myParams.get(nextParamName);
				List<String> pathsSplit = sp.getPathsSplit();
				for (String nextPath : pathsSplit) {
					// we want case-insensitive matching
					nextPath = nextPath.toLowerCase(Locale.ROOT);

					linkPathToParamName
						.computeIfAbsent(nextPath, (p) -> new ArrayList<>())
						.add(nextParamName);
				}
			}

			for (ResourceLink nextLink : theNewParams.getResourceLinks()) {
				String insensitivePath = nextLink.getSourcePath().toLowerCase(Locale.ROOT);
				List<String> paramNames = linkPathToParamName.getOrDefault(insensitivePath, Collections.emptyList());
				for (String nextParamName : paramNames) {
					String qualifiedTargetResourceId = nextLink.getTargetResourceType() + "/" + nextLink.getTargetResourceId();
					retVal.addResourceLinkIndexData(nextParamName, qualifiedTargetResourceId);
				}
			}
		}
		return retVal;
	}
}
