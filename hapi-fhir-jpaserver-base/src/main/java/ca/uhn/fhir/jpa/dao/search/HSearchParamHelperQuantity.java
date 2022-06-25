package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE_NORM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_PARAM_NAME;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE_NORM;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HSearchParamHelperQuantity extends HSearchParamHelper<QuantityParam> {

	private static final String SYSTEM_PATH 		= String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", QTY_PARAM_NAME, QTY_SYSTEM );
	private static final String CODE_PATH   		= String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", QTY_PARAM_NAME, QTY_CODE );
	private static final String VALUE_PATH   		= String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", QTY_PARAM_NAME, QTY_VALUE );
	private static final String CODE_NORM_PATH 	= String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", QTY_PARAM_NAME, QTY_CODE_NORM );
	private static final String VALUE_NORM_PATH 	= String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", QTY_PARAM_NAME, QTY_VALUE_NORM );

	private static final List<String> mySearchCanonicalQtyProperties = List.of( CODE_NORM_PATH, VALUE_NORM_PATH );
	private static final List<String> mySearchQtyProperties = List.of( SYSTEM_PATH, CODE_PATH, VALUE_PATH );

	private final ModelConfig myModelConfig;
	private final IPrefixedNumberPredicateHelper myPrefixedNumberPredicateHelper;


	public HSearchParamHelperQuantity(IPrefixedNumberPredicateHelper theIPrefixedNumberPredicateHelper, ModelConfig theModelConfig) {
		myPrefixedNumberPredicateHelper = theIPrefixedNumberPredicateHelper;
		myModelConfig = theModelConfig;
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.QUANTITY; }

	@Override
	public boolean isNested() { return SYSTEM_PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }


	@Override
	public Optional<Object> getParamPropertyValue(IQueryParameterType theParam, String thePropName) {
		QuantityParam qtyParam = (QuantityParam) theParam;

		if (thePropName.endsWith(QTY_SYSTEM)) 		{ return Optional.of( qtyParam.getSystem() ); }
		if (thePropName.endsWith(QTY_CODE)) 		{ return Optional.of( qtyParam.getUnits() ); }
		if (thePropName.endsWith(QTY_VALUE)) 		{ return Optional.of( qtyParam.getValue().doubleValue() ); }
		if (thePropName.endsWith(QTY_CODE_NORM)) 	{ return Optional.of( qtyParam.getSystem() ); }

		// no check for canonical config or possibility because that was already done in getParamProperties method
		if (thePropName.endsWith(QTY_VALUE_NORM)) {
			QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull( qtyParam );
			return Optional.of( canonicalQty == null ? qtyParam.getValue().doubleValue() : canonicalQty.getValue().doubleValue() );
		}

		return Optional.empty();
	}


	/**
	 * Selects which properties to return based on the configuration and the parameter value. If supported in the
	 * configuration and a canonical quantity can be obtained from the parameter values, return the canonical properties.
	 * Otherwise, return the non-canonical properties.
	 */
	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) {
		if (myModelConfig.getNormalizedQuantitySearchLevel() == NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED) {
			QuantityParam qtyParam = (QuantityParam) theParam;
			QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull( qtyParam );
			if (canonicalQty != null) {
				return mySearchCanonicalQtyProperties;
			}
		}

		return mySearchQtyProperties;
	}


	@Override
	public <P extends IQueryParameterType> void addOrClauses(SearchPredicateFactory theFactory,
			BooleanPredicateClausesStep<?> theBool, String theParamName, P theParam) {

		List<String> propertyPaths = getParamPropertiesForParameter(theParamName, theParam);

		QuantityParam qtyParam = (QuantityParam) theParam;
		ParamPrefixEnum activePrefix = qtyParam.getPrefix() == null ? ParamPrefixEnum.EQUAL : qtyParam.getPrefix();

		if (propertyPaths.contains(CODE_NORM_PATH)) {
			QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull(qtyParam);
			if (canonicalQty == null) {
//				fixme jm: code
				throw new InternalErrorException(Msg.code(0) + "Couldn't extract canonical value for parameter: " + theParam);
			}

			String valueNormPathForParam = mergeParamIntoProperty(VALUE_NORM_PATH, theParamName);
			myPrefixedNumberPredicateHelper.addPredicate(theFactory, theBool, activePrefix, canonicalQty.getValue().doubleValue(),  valueNormPathForParam);

			String codeNormPathForParam= mergeParamIntoProperty(CODE_NORM_PATH, theParamName);
			theBool.must( theFactory.match().field( codeNormPathForParam ) .matching(canonicalQty.getUnits()) );
			return;
		}

		// adding not-normalized properties clauses
		String valuePathForParam = mergeParamIntoProperty(VALUE_PATH, theParamName);
		myPrefixedNumberPredicateHelper.addPredicate(theFactory, theBool, activePrefix, qtyParam.getValue().doubleValue(), valuePathForParam);

		if ( isNotBlank(qtyParam.getSystem()) ) {
			String systemPathForParam = mergeParamIntoProperty(SYSTEM_PATH, theParamName);
			theBool.must( theFactory.match().field(systemPathForParam).matching(qtyParam.getSystem()) );
		}

		if ( isNotBlank(qtyParam.getUnits()) ) {
			String codePathForParam = mergeParamIntoProperty(CODE_PATH, theParamName);
			theBool.must(theFactory.match().field(codePathForParam) .matching(qtyParam.getUnits()) );
		}
	}


}
