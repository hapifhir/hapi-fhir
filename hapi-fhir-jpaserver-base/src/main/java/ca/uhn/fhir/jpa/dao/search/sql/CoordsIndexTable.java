package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.util.SearchBox;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordsIndexTable extends BaseSearchParamIndexTable {

	private static final Logger ourLog = LoggerFactory.getLogger(CoordsIndexTable.class);
	private final DbColumn myColumnLatitude;
	private final DbColumn myColumnLongitude;

	/**
	 * Constructor
	 */
	public CoordsIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_COORDS"));

		myColumnLatitude = getTable().addColumn("SP_LATITUDE");
		myColumnLongitude = getTable().addColumn("SP_LONGITUDE");
	}

	public Condition createPredicateLatitudeExact(String theLatitudeValue) {
		return BinaryCondition.equalTo(myColumnLatitude, theLatitudeValue);
	}

	public Condition createPredicateLongitudeExact(String theLongitudeValue) {
		return BinaryCondition.equalTo(myColumnLongitude, theLongitudeValue);
	}

	public Condition createLatitudePredicateFromBox(SearchBox theBox) {
		return ComboCondition.and(
			BinaryCondition.greaterThanOrEq(myColumnLatitude, theBox.getSouthWest().getLatitude()),
			BinaryCondition.lessThanOrEq(myColumnLatitude, theBox.getNorthEast().getLatitude())
		);
	}

	public Condition createLongitudePredicateFromBox(SearchBox theBox) {
		if (theBox.crossesAntiMeridian()) {
			return ComboCondition.or(
				BinaryCondition.greaterThanOrEq(myColumnLongitude, theBox.getNorthEast().getLongitude()),
				BinaryCondition.lessThanOrEq(myColumnLongitude, theBox.getSouthWest().getLongitude())
			);
		}
		return ComboCondition.and(
			BinaryCondition.greaterThanOrEq(myColumnLongitude, theBox.getSouthWest().getLongitude()),
			BinaryCondition.lessThanOrEq(myColumnLongitude, theBox.getNorthEast().getLongitude())
		);
	}
}
