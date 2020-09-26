package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.predicate.SearchFuzzUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.math.MathContext;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class NumberIndexTable extends BaseSearchParamIndexTable {

	private static final Logger ourLog = LoggerFactory.getLogger(NumberIndexTable.class);
	private final DbColumn myColumnValue;

	/**
	 * Constructor
	 */
	public NumberIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_NUMBER"));

		myColumnValue = getTable().addColumn("SP_VALUE");
	}

	public Condition createPredicateNumeric(String theResourceName, String theParamName, NumberIndexTable theJoin, CriteriaBuilder theCriteriaBuilder, IQueryParameterType theNextOr, SearchFilterParser.CompareOperation theOperation, BigDecimal theValue, Object theO, String theInvalidMessageName, RequestPartitionId theRequestPartitionId) {
		Condition numericPredicate = createPredicateNumeric(this, theResourceName, theParamName, theOperation, theValue, theRequestPartitionId, myColumnValue);
		return combineWithHashIdentityPredicate(theResourceName, theParamName, numericPredicate, theRequestPartitionId);
	}


	static Condition createPredicateNumeric(BaseSearchParamIndexTable theIndexTable, String theResourceName, String theParamName, SearchFilterParser.CompareOperation theOperation, BigDecimal theValue, RequestPartitionId theRequestPartitionId, DbColumn theColumn) {
		Condition num;

		// Per discussions with Grahame Grieve and James Agnew on 11/13/19, modified logic for EQUAL and NOT_EQUAL operators below so as to
		//   use exact value matching.  The "fuzz amount" matching is still used with the APPROXIMATE operator.
		SearchFilterParser.CompareOperation operation = defaultIfNull(theOperation, SearchFilterParser.CompareOperation.eq);
		switch (operation) {
			case gt:
				num = BinaryCondition.greaterThan(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case ge:
				num = BinaryCondition.greaterThanOrEq(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case lt:
				num = BinaryCondition.lessThan(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case le:
				num = BinaryCondition.lessThanOrEq(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case eq:
				num = BinaryCondition.equalTo(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case ne:
				num = BinaryCondition.notEqualTo(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case ap:
				BigDecimal mul = SearchFuzzUtil.calculateFuzzAmount(null, theValue);
				BigDecimal low = theValue.subtract(mul, MathContext.DECIMAL64);
				BigDecimal high = theValue.add(mul, MathContext.DECIMAL64);
				Condition lowPred = BinaryCondition.greaterThanOrEq(theColumn, low);
				Condition highPred = BinaryCondition.lessThanOrEq(theColumn, high);
				num = ComboCondition.and(lowPred, highPred);
				ourLog.trace("Searching for {} <= val <= {}", low, high);
				break;
			default:
				// FIXME: does this happen in tests?
				String msg = theIndexTable.getFhirContext().getLocalizer().getMessage(SearchBuilder.class, "invalidNumberPrefix", operation, "AAAAAAAA");
				throw new InvalidRequestException(msg);
		}

		return num;
	}
}
