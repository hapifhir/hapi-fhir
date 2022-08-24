package ca.uhn.fhir.jpa.search.builder.utils;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class QueryParameterUtils {

	private static final BidiMap<SearchFilterParser.CompareOperation, ParamPrefixEnum> ourCompareOperationToParamPrefix;

	static {
		DualHashBidiMap<SearchFilterParser.CompareOperation, ParamPrefixEnum> compareOperationToParamPrefix = new DualHashBidiMap<>();
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ap, ParamPrefixEnum.APPROXIMATE);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.eq, ParamPrefixEnum.EQUAL);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.gt, ParamPrefixEnum.GREATERTHAN);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ge, ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.lt, ParamPrefixEnum.LESSTHAN);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.le, ParamPrefixEnum.LESSTHAN_OR_EQUALS);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ne, ParamPrefixEnum.NOT_EQUAL);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.eb, ParamPrefixEnum.ENDS_BEFORE);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.sa, ParamPrefixEnum.STARTS_AFTER);
		ourCompareOperationToParamPrefix = UnmodifiableBidiMap.unmodifiableBidiMap(compareOperationToParamPrefix);
	}

	public static IQueryParameterType toParameterType(RuntimeSearchParam theParam, ISearchParamRegistry theRegistry) {
		IQueryParameterType qp;
		switch (theParam.getParamType()) {
			case DATE:
				qp = new DateParam();
				break;
			case NUMBER:
				qp = new NumberParam();
				break;
			case QUANTITY:
				qp = new QuantityParam();
				break;
			case STRING:
				qp = new StringParam();
				break;
			case TOKEN:
				qp = new TokenParam();
				break;
			case COMPOSITE:
				List<RuntimeSearchParam> compositeOf = JpaParamUtil.resolveComponentParameters(theRegistry, theParam);
				if (compositeOf.size() != 2) {
					throw new InternalErrorException(Msg.code(1224) + "Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
				}
				IQueryParameterType leftParam = toParameterType(compositeOf.get(0), theRegistry);
				IQueryParameterType rightParam = toParameterType(compositeOf.get(1), theRegistry);
				qp = new CompositeParam<>(leftParam, rightParam);
				break;
			case URI:
				qp = new UriParam();
				break;
			case HAS:
			case REFERENCE:
			case SPECIAL:
			default:
				throw new InvalidRequestException(Msg.code(1225) + "The search type: " + theParam.getParamType() + " is not supported.");
		}
		return qp;
	}

	@Nullable
	public static Condition toAndPredicate(List<Condition> theAndPredicates) {
		List<Condition> andPredicates = theAndPredicates.stream().filter(t -> t != null).collect(Collectors.toList());
		if (andPredicates.size() == 0) {
			return null;
		} else if (andPredicates.size() == 1) {
			return andPredicates.get(0);
		} else {
			return ComboCondition.and(andPredicates.toArray(new Condition[0]));
		}
	}

	@Nullable
	public static Condition toOrPredicate(List<Condition> theOrPredicates) {
		List<Condition> orPredicates = theOrPredicates.stream().filter(t -> t != null).collect(Collectors.toList());
		if (orPredicates.size() == 0) {
			return null;
		} else if (orPredicates.size() == 1) {
			return orPredicates.get(0);
		} else {
			return ComboCondition.or(orPredicates.toArray(new Condition[0]));
		}
	}

	@Nullable
	public static Condition toOrPredicate(Condition... theOrPredicates) {
		return toOrPredicate(Arrays.asList(theOrPredicates));
	}

	@Nullable
	public static Condition toAndPredicate(Condition... theAndPredicates) {
		return toAndPredicate(Arrays.asList(theAndPredicates));
	}

	@Nonnull
	public static Condition toEqualToOrInPredicate(DbColumn theColumn, List<String> theValuePlaceholders, boolean theInverse) {
		if (theInverse) {
			return toNotEqualToOrNotInPredicate(theColumn, theValuePlaceholders);
		} else {
			return toEqualToOrInPredicate(theColumn, theValuePlaceholders);
		}
	}

	@Nonnull
	public static Condition toEqualToOrInPredicate(DbColumn theColumn, List<String> theValuePlaceholders) {
		if (theValuePlaceholders.size() == 1) {
			return BinaryCondition.equalTo(theColumn, theValuePlaceholders.get(0));
		}
		return new InCondition(theColumn, theValuePlaceholders);
	}

	@Nonnull
	public static Condition toNotEqualToOrNotInPredicate(DbColumn theColumn, List<String> theValuePlaceholders) {
		if (theValuePlaceholders.size() == 1) {
			return BinaryCondition.notEqualTo(theColumn, theValuePlaceholders.get(0));
		}
		return new InCondition(theColumn, theValuePlaceholders).setNegate(true);
	}

	public static SearchFilterParser.CompareOperation toOperation(ParamPrefixEnum thePrefix) {
		SearchFilterParser.CompareOperation retVal = null;
		if (thePrefix != null && ourCompareOperationToParamPrefix.containsValue(thePrefix)) {
			retVal = ourCompareOperationToParamPrefix.getKey(thePrefix);
		}
		return defaultIfNull(retVal, SearchFilterParser.CompareOperation.eq);
	}

	public static ParamPrefixEnum fromOperation(SearchFilterParser.CompareOperation thePrefix) {
		ParamPrefixEnum retVal = null;
		if (thePrefix != null && ourCompareOperationToParamPrefix.containsKey(thePrefix)) {
			retVal = ourCompareOperationToParamPrefix.get(thePrefix);
		}
		return defaultIfNull(retVal, ParamPrefixEnum.EQUAL);
	}

	public static String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}

	public static String getParamNameWithPrefix(String theSpnamePrefix, String theParamName) {
		if (isBlank(theSpnamePrefix))
			return theParamName;

		return theSpnamePrefix + "." + theParamName;
	}

	private QueryParameterUtils() {}
}
