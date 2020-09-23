package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinEnum;
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
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TagPredicateBuilder3 extends BaseIndexTable {
	private static final Logger ourLog = LoggerFactory.getLogger(TagPredicateBuilder3.class);

	private final DbColumn myColumnResId;
	private final DbTable myTagDefinitionTable;
	private final DbColumn myTagDefinitionColumnTagId;
	private final DbColumn myTagDefinitionColumnTagSystem;
	private final DbColumn myTagDefinitionColumnTagCode;
	private final DbColumn myColumnTagId;
	private final DbColumn myTagDefinitionColumnTagType;

	public TagPredicateBuilder3(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_TAG"));

		myColumnResId = getTable().addColumn("RES_ID");
		myColumnTagId = getTable().addColumn("TAG_ID");

		myTagDefinitionTable = theSearchSqlBuilder.addTable("HFJ_TAG_DEF");
		myTagDefinitionColumnTagId = myTagDefinitionTable.addColumn("TAG_ID");
		myTagDefinitionColumnTagSystem = myTagDefinitionTable.addColumn("TAG_SYSTEM");
		myTagDefinitionColumnTagCode = myTagDefinitionTable.addColumn("TAG_CODE");
		myTagDefinitionColumnTagType = myTagDefinitionTable.addColumn("TAG_TYPE");
	}

	public void addPredicateTag(List<List<IQueryParameterType>> theList, String theParamName, RequestPartitionId theRequestPartitionId) {
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

		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			boolean haveTags = false;
			for (IQueryParameterType nextParamUncasted : nextAndParams) {
				if (nextParamUncasted instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					} else if (isNotBlank(nextParam.getSystem())) {
						throw new InvalidRequestException("Invalid " + theParamName + " parameter (must supply a value/code and not just a system): " + nextParam.getValueAsQueryToken(getFhirContext()));
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

//			if (paramInverted) {
//				ourLog.debug("Searching for _tag:not");
//
//				Subquery<Long> subQ = myQueryStack.subqueryForTagNegation();
//				Root<ResourceTag> subQfrom = subQ.from(ResourceTag.class);
//				subQ.select(subQfrom.get("myResourceId").as(Long.class));
//
//				myQueryStack.addPredicate(
//					myCriteriaBuilder.not(
//						myCriteriaBuilder.in(
//							myQueryStack.get("myId")
//						).value(subQ)
//					)
//				);
//
//				Subquery<Long> defJoin = subQ.subquery(Long.class);
//				Root<TagDefinition> defJoinFrom = defJoin.from(TagDefinition.class);
//				defJoin.select(defJoinFrom.get("myId").as(Long.class));
//
//				subQ.where(subQfrom.get("myTagId").as(Long.class).in(defJoin));
//
//				Predicate tagListPredicate = createPredicateTagList(defJoinFrom, myCriteriaBuilder, tagType, tokens);
//				defJoin.where(tagListPredicate);
//
//				continue;
//
//			}

			addJoin(getTable(), myTagDefinitionTable, myColumnTagId, myTagDefinitionColumnTagId);

			Condition tagListPredicate = createPredicateTagList(tagType, tokens);
			if (paramInverted) {
				tagListPredicate = new NotCondition(tagListPredicate);
			}

			// FIXME: needed?
//			if (theRequestPartitionId != null) {
//				addPartitionIdPredicate(theRequestPartitionId, tagJoin, predicates);
//			}

			addCondition(tagListPredicate);
		}

	}

	private Condition createPredicateTagList(TagTypeEnum theTagType, List<Pair<String, String>> theTokens) {
		Condition typePredicate = BinaryCondition.equalTo(myTagDefinitionColumnTagType, generatePlaceholder(theTagType.ordinal()));

		List<Condition> orPredicates = Lists.newArrayList();
		for (Pair<String, String> next : theTokens) {
			Condition codePredicate = BinaryCondition.equalTo(myTagDefinitionColumnTagCode, generatePlaceholder(next.getRight()));
			if (isNotBlank(next.getLeft())) {
				Condition systemPredicate = BinaryCondition.equalTo(myTagDefinitionColumnTagSystem, generatePlaceholder(next.getLeft()));
				orPredicates.add(ComboCondition.and(typePredicate, systemPredicate, codePredicate));
			} else {
				orPredicates.add(ComboCondition.and(typePredicate, codePredicate));
			}
		}

		return ComboCondition.or(orPredicates.toArray(new Condition[0]));
	}

	@Override
	DbColumn getResourceIdColumn() {
		return myColumnResId;
	}
}
