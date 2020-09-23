package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.CoordCalculator;
import ca.uhn.fhir.jpa.util.SearchBox;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;

import static org.apache.commons.lang3.StringUtils.isBlank;

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


	public Condition createPredicateCoords(SearchParameterMap theParams,
														IQueryParameterType theParam,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														CriteriaBuilder theBuilder,
														CoordsIndexTable theFrom,
														RequestPartitionId theRequestPartitionId) {
		String latitudeValue;
		String longitudeValue;
		Double distanceKm = 0.0;

		if (theParam instanceof TokenParam) { // DSTU3
			TokenParam param = (TokenParam) theParam;
			String value = param.getValue();
			String[] parts = value.split(":");
			if (parts.length != 2) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Required format is 'latitude:longitude'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Both latitude and longitude must be provided.");
			}
			QuantityParam distanceParam = theParams.getNearDistanceParam();
			if (distanceParam != null) {
				distanceKm = distanceParam.getValue().doubleValue();
			}
		} else if (theParam instanceof SpecialParam) { // R4
			SpecialParam param = (SpecialParam) theParam;
			String value = param.getValue();
			String[] parts = value.split("\\|");
			if (parts.length < 2 || parts.length > 4) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Required format is 'latitude|longitude' or 'latitude|longitude|distance' or 'latitude|longitude|distance|units'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Both latitude and longitude must be provided.");
			}
			if (parts.length >= 3) {
				String distanceString = parts[2];
				if (!isBlank(distanceString)) {
					distanceKm = Double.valueOf(distanceString);
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid position type: " + theParam.getClass());
		}

		Condition latitudePredicate;
		Condition longitudePredicate;
		if (distanceKm == 0.0) {
			latitudePredicate = theFrom.createPredicateLatitudeExact(latitudeValue);
			longitudePredicate = theFrom.createPredicateLongitudeExact(longitudeValue);
		} else if (distanceKm < 0.0) {
			throw new IllegalArgumentException("Invalid " + Location.SP_NEAR_DISTANCE + " parameter '" + distanceKm + "' must be >= 0.0");
		} else if (distanceKm > CoordCalculator.MAX_SUPPORTED_DISTANCE_KM) {
			throw new IllegalArgumentException("Invalid " + Location.SP_NEAR_DISTANCE + " parameter '" + distanceKm + "' must be <= " + CoordCalculator.MAX_SUPPORTED_DISTANCE_KM);
		} else {
			double latitudeDegrees = Double.parseDouble(latitudeValue);
			double longitudeDegrees = Double.parseDouble(longitudeValue);

			SearchBox box = CoordCalculator.getBox(latitudeDegrees, longitudeDegrees, distanceKm);
			latitudePredicate = theFrom.createLatitudePredicateFromBox(box);
			longitudePredicate = theFrom.createLongitudePredicateFromBox(box);
		}
		ComboCondition singleCode = ComboCondition.and(latitudePredicate, longitudePredicate);
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theSearchParam.getName(), singleCode, theRequestPartitionId);
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
