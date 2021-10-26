package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.jpa.searchparam.extractor.StringTrimmingTrimmerMatcher;
import ca.uhn.fhir.rest.api.Constants;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.text.StringTokenizer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public enum SubscriptionCriteriaParser {
	;

	public enum TypeEnum {

		/**
		 * Normal search URL expression
		 */
		SEARCH_EXPRESSION,

		/**
		 * Collection of resource types
		 */
		MULTITYPE_EXPRESSION,

		/**
		 * All types
		 */
		STARTYPE_EXPRESSION

	}

	public static class SubscriptionCriteria {

		private final TypeEnum myType;
		private final String myCriteria;
		private final Set<String> myApplicableResourceTypes;

		private SubscriptionCriteria(TypeEnum theType, String theCriteria, Set<String> theApplicableResourceTypes) {
			myType = theType;
			myCriteria = theCriteria;
			myApplicableResourceTypes = theApplicableResourceTypes;
		}

		@Override
		public String toString() {
			ToStringBuilder retVal = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
			retVal.append("type", myType);
			if (isNotBlank(myCriteria)) {
				retVal.append("criteria", myCriteria);
			}
			if (myApplicableResourceTypes != null) {
				retVal.append("applicableResourceTypes", myApplicableResourceTypes);
			}
			return retVal.toString();
		}

		public TypeEnum getType() {
			return myType;
		}

		public String getCriteria() {
			return myCriteria;
		}

		public Set<String> getApplicableResourceTypes() {
			return myApplicableResourceTypes;
		}
	}

	@Nullable
	public static SubscriptionCriteria parse(String theCriteria) {
		String criteria = trim(theCriteria);
		if (isBlank(criteria)) {
			return null;
		}

		if (criteria.startsWith(Constants.SUBSCRIPTION_MULTITYPE_PREFIX)) {
			if (criteria.endsWith(Constants.SUBSCRIPTION_MULTITYPE_SUFFIX)) {
				String multitypeExpression = criteria.substring(1, criteria.length() - 1);
				StringTokenizer tok = new StringTokenizer(multitypeExpression, ",");
				tok.setTrimmerMatcher(new StringTrimmingTrimmerMatcher());
				List<String> types = tok.getTokenList();
				if (types.isEmpty()) {
					return null;
				}
				if (types.contains(Constants.SUBSCRIPTION_MULTITYPE_STAR)) {
					return new SubscriptionCriteria(TypeEnum.STARTYPE_EXPRESSION, null, null);
				}
				Set<String> typesSet = Sets.newHashSet(types);
				return new SubscriptionCriteria(TypeEnum.MULTITYPE_EXPRESSION, null, typesSet);
			}
		}

		if (Character.isLetter(criteria.charAt(0))) {
			String criteriaType = criteria;
			int questionMarkIdx = criteriaType.indexOf('?');
			if (questionMarkIdx > 0) {
				criteriaType = criteriaType.substring(0, questionMarkIdx);
			}
			Set<String> types = Collections.singleton(criteriaType);
			return new SubscriptionCriteria(TypeEnum.SEARCH_EXPRESSION, criteria, types);
		}

		return null;
	}


}
