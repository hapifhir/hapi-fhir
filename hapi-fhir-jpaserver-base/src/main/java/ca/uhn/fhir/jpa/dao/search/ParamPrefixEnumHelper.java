package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ParamPrefixEnumHelper implements IHSearchParameterClauseBuilder {

	private final Map<ParamPrefixEnum, IHSearchParameterClauseBuilder> OPERATION_MAP;


//		OPERATION_MAP.put(ParamPrefixEnum.APPROXIMATE, this::addPredicateForApproximate),
//		OPERATION_MAP.put(ParamPrefixEnum.ENDS_BEFORE,
//		OPERATION_MAP.put(ParamPrefixEnum.EQUAL,
//		OPERATION_MAP.put(ParamPrefixEnum.GREATERTHAN,
//		OPERATION_MAP.put(ParamPrefixEnum.GREATERTHAN_OR_EQUALS,
//		OPERATION_MAP.put(ParamPrefixEnum.LESSTHAN,
//		OPERATION_MAP.put(ParamPrefixEnum.LESSTHAN_OR_EQUALS,
//		OPERATION_MAP.put(ParamPrefixEnum.NOT_EQUAL,
//		OPERATION_MAP.put(ParamPrefixEnum.STARTS_AFTER,
//

	public ParamPrefixEnumHelper() {
		OPERATION_MAP = new EnumMap<>(ParamPrefixEnum.class);

//		OPERATION_MAP.put(ParamPrefixEnum.APPROXIMATE, this::addPredicate),
//			OPERATION_MAP.put(ParamPrefixEnum.ENDS_BEFORE,
//				OPERATION_MAP.put(ParamPrefixEnum.EQUAL,
//					OPERATION_MAP.put(ParamPrefixEnum.GREATERTHAN,
//						OPERATION_MAP.put(ParamPrefixEnum.GREATERTHAN_OR_EQUALS,
//							OPERATION_MAP.put(ParamPrefixEnum.LESSTHAN,
//								OPERATION_MAP.put(ParamPrefixEnum.LESSTHAN_OR_EQUALS,
//									OPERATION_MAP.put(ParamPrefixEnum.NOT_EQUAL,
//										OPERATION_MAP.put(ParamPrefixEnum.STARTS_AFTER,

		if (Arrays.stream(ParamPrefixEnum.values()).anyMatch(it -> !OPERATION_MAP.containsKey(it))) {
			throw new IllegalStateException("Unmapped enum constant found!");
		}
	}


	@Override
	public void addPredicate(ParamPrefixEnum thePrefix, BooleanPredicateClausesStep<?> thePredicateStep, String theFieldPath, double theValue) {

	}

}
