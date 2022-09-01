package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QueryParameterUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(QueryParameterUtils.class);

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
        return ObjectUtils.defaultIfNull(retVal, SearchFilterParser.CompareOperation.eq);
    }

    public static ParamPrefixEnum fromOperation(SearchFilterParser.CompareOperation thePrefix) {
        ParamPrefixEnum retVal = null;
        if (thePrefix != null && ourCompareOperationToParamPrefix.containsKey(thePrefix)) {
            retVal = ourCompareOperationToParamPrefix.get(thePrefix);
        }
        return ObjectUtils.defaultIfNull(retVal, ParamPrefixEnum.EQUAL);
    }

    public static String getChainedPart(String parameter) {
        return parameter.substring(parameter.indexOf(".") + 1);
    }

    public static String getParamNameWithPrefix(String theSpnamePrefix, String theParamName) {

        if (StringUtils.isBlank(theSpnamePrefix))
            return theParamName;

        return theSpnamePrefix + "." + theParamName;
    }

    public static Predicate[] toPredicateArray(List<Predicate> thePredicates) {
        return thePredicates.toArray(new Predicate[0]);
    }

	private static List<Predicate> createLastUpdatedPredicates(final DateRangeParam theLastUpdated, CriteriaBuilder builder, From<?, ResourceTable> from) {
		List<Predicate> lastUpdatedPredicates = new ArrayList<>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
				ourLog.debug("LastUpdated lower bound: {}", new InstantDt(theLastUpdated.getLowerBoundAsInstant()));
				Predicate predicateLower = builder.greaterThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getLowerBoundAsInstant());
				lastUpdatedPredicates.add(predicateLower);
			}
			if (theLastUpdated.getUpperBoundAsInstant() != null) {
				Predicate predicateUpper = builder.lessThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getUpperBoundAsInstant());
				lastUpdatedPredicates.add(predicateUpper);
			}
		}
		return lastUpdatedPredicates;
	}

	public static List<ResourcePersistentId> filterResourceIdsByLastUpdated(EntityManager theEntityManager, final DateRangeParam theLastUpdated, Collection<ResourcePersistentId> thePids) {
		if (thePids.isEmpty()) {
			return Collections.emptyList();
		}
		CriteriaBuilder builder = theEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);
		lastUpdatedPredicates.add(from.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(thePids)));

		cq.where(toPredicateArray(lastUpdatedPredicates));
		TypedQuery<Long> query = theEntityManager.createQuery(cq);

		return ResourcePersistentId.fromLongList(query.getResultList());
	}
}
