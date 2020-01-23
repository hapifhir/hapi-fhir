package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PredicateBuilderTag extends BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderTag.class);

	PredicateBuilderTag(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	public void addPredicateTag(List<List<IQueryParameterType>> theList, String theParamName) {
		TagTypeEnum tagType;
		if (Constants.PARAM_TAG.equals(theParamName)) {
			tagType = TagTypeEnum.TAG;
		} else if (Constants.PARAM_PROFILE.equals(theParamName)) {
			tagType = TagTypeEnum.PROFILE;
		} else if (Constants.PARAM_SECURITY.equals(theParamName)) {
			tagType = TagTypeEnum.SECURITY_LABEL;
		} else {
			throw new IllegalArgumentException("Param name: " + theParamName); // shouldn't happen
		}

		List<Pair<String, String>> notTags = Lists.newArrayList();
		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			for (IQueryParameterType nextOrParams : nextAndParams) {
				if (nextOrParams instanceof TokenParam) {
					TokenParam param = (TokenParam) nextOrParams;
					if (param.getModifier() == TokenParamModifier.NOT) {
						if (isNotBlank(param.getSystem()) || isNotBlank(param.getValue())) {
							notTags.add(Pair.of(param.getSystem(), param.getValue()));
						}
					}
				}
			}
		}

		/*
		 * We have a parameter of ResourceType?_tag:not=foo This means match resources that don't have the given tag(s)
		 */
		if (notTags.isEmpty() == false) {
			// CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			// CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			// Root<ResourceTable> from = cq.from(ResourceTable.class);
			// cq.select(from.get("myId").as(Long.class));
			//
			// Subquery<Long> subQ = cq.subquery(Long.class);
			// Root<ResourceTag> subQfrom = subQ.from(ResourceTag.class);
			// subQ.select(subQfrom.get("myResourceId").as(Long.class));
			// Predicate subQname = builder.equal(subQfrom.get("myParamName"), theParamName);
			// Predicate subQtype = builder.equal(subQfrom.get("myResourceType"), myResourceName);
			// subQ.where(builder.and(subQtype, subQname));
			//
			// List<Predicate> predicates = new ArrayList<Predicate>();
			// predicates.add(builder.not(builder.in(from.get("myId")).value(subQ)));
			// predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
			// predicates.add(builder.isNull(from.get("myDeleted")));
			// createPredicateResourceId(builder, cq, predicates, from.get("myId").as(Long.class));
		}

		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			boolean haveTags = false;
			for (IQueryParameterType nextParamUncasted : nextAndParams) {
				if (nextParamUncasted instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					} else if (isNotBlank(nextParam.getSystem())) {
						throw new InvalidRequestException("Invalid " + theParamName + " parameter (must supply a value/code and not just a system): " + nextParam.getValueAsQueryToken(myContext));
					}
				} else {
					UriParam nextParam = (UriParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					}
				}
			}
			if (!haveTags) {
				continue;
			}

			boolean paramInverted = false;
			List<Pair<String, String>> tokens = Lists.newArrayList();
			for (IQueryParameterType nextOrParams : nextAndParams) {
				String code;
				String system;
				if (nextOrParams instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextOrParams;
					code = nextParam.getValue();
					system = nextParam.getSystem();
					if (nextParam.getModifier() == TokenParamModifier.NOT) {
						paramInverted = true;
					}
				} else {
					UriParam nextParam = (UriParam) nextOrParams;
					code = nextParam.getValue();
					system = null;
				}

				if (isNotBlank(code)) {
					tokens.add(Pair.of(system, code));
				}
			}

			if (tokens.isEmpty()) {
				continue;
			}

			if (paramInverted) {
				ourLog.debug("Searching for _tag:not");

				Subquery<Long> subQ = myResourceTableQuery.subquery(Long.class);
				Root<ResourceTag> subQfrom = subQ.from(ResourceTag.class);
				subQ.select(subQfrom.get("myResourceId").as(Long.class));

				myQueryRoot.addPredicate(
					myBuilder.not(
						myBuilder.in(
							myQueryRoot.get("myId")
						).value(subQ)
					)
				);

				Subquery<Long> defJoin = subQ.subquery(Long.class);
				Root<TagDefinition> defJoinFrom = defJoin.from(TagDefinition.class);
				defJoin.select(defJoinFrom.get("myId").as(Long.class));

				subQ.where(subQfrom.get("myTagId").as(Long.class).in(defJoin));

				Predicate tagListPredicate = createPredicateTagList(defJoinFrom, myBuilder, tagType, tokens);
				defJoin.where(tagListPredicate);

				continue;
			}

			Join<ResourceTable, ResourceTag> tagJoin = myQueryRoot.join("myTags", JoinType.LEFT);
			From<ResourceTag, TagDefinition> defJoin = tagJoin.join("myTag");

			Predicate tagListPredicate = createPredicateTagList(defJoin, myBuilder, tagType, tokens);
			myQueryRoot.addPredicate(tagListPredicate);

		}

	}

	private Predicate createPredicateTagList(Path<TagDefinition> theDefJoin, CriteriaBuilder theBuilder, TagTypeEnum theTagType, List<Pair<String, String>> theTokens) {
		Predicate typePredicate = theBuilder.equal(theDefJoin.get("myTagType"), theTagType);

		List<Predicate> orPredicates = Lists.newArrayList();
		for (Pair<String, String> next : theTokens) {
			Predicate codePredicate = theBuilder.equal(theDefJoin.get("myCode"), next.getRight());
			if (isNotBlank(next.getLeft())) {
				Predicate systemPredicate = theBuilder.equal(theDefJoin.get("mySystem"), next.getLeft());
				orPredicates.add(theBuilder.and(typePredicate, systemPredicate, codePredicate));
			} else {
				orPredicates.add(theBuilder.and(typePredicate, codePredicate));
			}
		}

		return theBuilder.or(toArray(orPredicates));
	}

}
