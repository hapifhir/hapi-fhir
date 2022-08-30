package ca.uhn.fhir.jpa.search.builder.helpers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.search.builder.models.LeafNodeDefinition;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.split;

public class ReferenceChainExtractor {
	private static final Logger ourLog = LoggerFactory.getLogger(ReferenceChainExtractor.class);

	private final FhirContext myFhirContext;
	private final ISearchParamRegistry mySearchParamRegistry;

	private final Map<List<ChainElement>, Set<LeafNodeDefinition>> myChains = Maps.newHashMap();

	public Map<List<ChainElement>,Set<LeafNodeDefinition>> getChains() { return myChains; }

	public ReferenceChainExtractor(FhirContext theFhirContext,
											 ISearchParamRegistry theSearchParamRegistry) {
		myFhirContext = theFhirContext;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	private boolean isReferenceParamValid(ReferenceParam theReferenceParam) {
		return split(theReferenceParam.getChain(), '.').length <= 3;
	}

	private List<String> extractPaths(String theResourceType, RuntimeSearchParam theSearchParam) {
		List<String> pathsForType = theSearchParam.getPathsSplit().stream()
			.map(String::trim)
			.filter(t -> t.startsWith(theResourceType))
			.collect(Collectors.toList());
		if (pathsForType.isEmpty()) {
			ourLog.warn("Search parameter {} does not have a path for resource type {}.", theSearchParam.getName(), theResourceType);
		}

		return pathsForType;
	}

	public void deriveChains(String theResourceType, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList) {
		List<String> paths = extractPaths(theResourceType, theSearchParam);
		for (String path : paths) {
			List<ChainElement> searchParams = Lists.newArrayList();
			searchParams.add(new ChainElement(theResourceType, theSearchParam.getName(), path));
			for (IQueryParameterType nextOr : theList) {
				String targetValue = nextOr.getValueAsQueryToken(myFhirContext);
				if (nextOr instanceof ReferenceParam) {
					ReferenceParam referenceParam = (ReferenceParam) nextOr;

					if (!isReferenceParamValid(referenceParam)) {
						throw new InvalidRequestException(Msg.code(2007) +
							"The search chain " + theSearchParam.getName() + "." + referenceParam.getChain() +
							" is too long. Only chains up to three references are supported.");
					}

					String targetChain = referenceParam.getChain();
					List<String> qualifiers = Lists.newArrayList(referenceParam.getResourceType());

					processNextLinkInChain(searchParams, theSearchParam, targetChain, targetValue, qualifiers, referenceParam.getResourceType());

				}
			}
		}
	}

	private void processNextLinkInChain(List<ChainElement> theSearchParams, RuntimeSearchParam thePreviousSearchParam, String theChain, String theTargetValue, List<String> theQualifiers, String theResourceType) {

		String nextParamName = theChain;
		String nextChain = null;
		String nextQualifier = null;
		int linkIndex = theChain.indexOf('.');
		if (linkIndex != -1) {
			nextParamName = theChain.substring(0, linkIndex);
			nextChain = theChain.substring(linkIndex+1);
		}

		int qualifierIndex = nextParamName.indexOf(':');
		if (qualifierIndex != -1) {
			nextParamName = nextParamName.substring(0, qualifierIndex);
			nextQualifier = nextParamName.substring(qualifierIndex);
		}

		List<String> qualifiersBranch = Lists.newArrayList();
		qualifiersBranch.addAll(theQualifiers);
		qualifiersBranch.add(nextQualifier);

		boolean searchParamFound = false;
		for (String nextTarget : thePreviousSearchParam.getTargets()) {
			RuntimeSearchParam nextSearchParam = null;
			if (StringUtils.isBlank(theResourceType) || theResourceType.equals(nextTarget)) {
				nextSearchParam = mySearchParamRegistry.getActiveSearchParam(nextTarget, nextParamName);
			}
			if (nextSearchParam != null) {
				searchParamFound = true;
				// If we find a search param on this resource type for this parameter name, keep iterating
				//  Otherwise, abandon this branch and carry on to the next one
				if (StringUtils.isEmpty(nextChain)) {
					// We've reached the end of the chain
					ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

					if (RestSearchParameterTypeEnum.REFERENCE.equals(nextSearchParam.getParamType())) {
						orValues.add(new ReferenceParam(nextQualifier, "", theTargetValue));
					} else {
						IQueryParameterType qp = QueryParameterUtils.toParameterType(nextSearchParam, mySearchParamRegistry);
						qp.setValueAsQueryToken(myFhirContext, nextSearchParam.getName(), null, theTargetValue);
						orValues.add(qp);
					}

					Set<LeafNodeDefinition> leafNodes = myChains.get(theSearchParams);
					if (leafNodes == null) {
						leafNodes = Sets.newHashSet();
						myChains.put(theSearchParams, leafNodes);
					}
					leafNodes.add(new LeafNodeDefinition(nextSearchParam, orValues, nextTarget, nextParamName, "", qualifiersBranch));
				} else {
					List<String> nextPaths = extractPaths(nextTarget, nextSearchParam);
					for (String nextPath : nextPaths) {
						List<ChainElement> searchParamBranch = Lists.newArrayList();
						searchParamBranch.addAll(theSearchParams);

						searchParamBranch.add(new ChainElement(nextTarget, nextSearchParam.getName(), nextPath));
						processNextLinkInChain(searchParamBranch, nextSearchParam, nextChain, theTargetValue, qualifiersBranch, nextQualifier);
					}
				}
			}
		}
		if (!searchParamFound) {
			throw new InvalidRequestException(Msg.code(1214) + myFhirContext.getLocalizer().getMessage(BaseStorageDao.class, "invalidParameterChain", thePreviousSearchParam.getName() + '.' + theChain));
		}
	}
}
