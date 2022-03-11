package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.CoordCalculator;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hibernate.search.engine.spatial.GeoBoundingBox;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CoordsPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnLatitude;
	private final DbColumn myColumnLongitude;

	/**
	 * Constructor
	 */
	public CoordsPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_COORDS"));

		myColumnLatitude = getTable().addColumn("SP_LATITUDE");
		myColumnLongitude = getTable().addColumn("SP_LONGITUDE");
	}


	public Condition createPredicateCoords(SearchParameterMap theParams,
														IQueryParameterType theParam,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														CoordsPredicateBuilder theFrom,
														RequestPartitionId theRequestPartitionId) {
		String latitudeValue;
		String longitudeValue;
		double distanceKm = 0.0;

		if (theParam instanceof TokenParam) { // DSTU3
			TokenParam param = (TokenParam) theParam;
			String value = param.getValue();
			String[] parts = value.split(":");
			if (parts.length != 2) {
				throw new IllegalArgumentException(Msg.code(1228) + "Invalid position format '" + value + "'.  Required format is 'latitude:longitude'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException(Msg.code(1229) + "Invalid position format '" + value + "'.  Both latitude and longitude must be provided.");
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
				throw new IllegalArgumentException(Msg.code(1230) + "Invalid position format '" + value + "'.  Required format is 'latitude|longitude' or 'latitude|longitude|distance' or 'latitude|longitude|distance|units'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException(Msg.code(1231) + "Invalid position format '" + value + "'.  Both latitude and longitude must be provided.");
			}
			if (parts.length >= 3) {
				String distanceString = parts[2];
				if (!isBlank(distanceString)) {
					distanceKm = Double.parseDouble(distanceString);
				}
			}
		} else {
			throw new IllegalArgumentException(Msg.code(1232) + "Invalid position type: " + theParam.getClass());
		}

		Condition latitudePredicate;
		Condition longitudePredicate;
		if (distanceKm == 0.0) {
			latitudePredicate = theFrom.createPredicateLatitudeExact(latitudeValue);
			longitudePredicate = theFrom.createPredicateLongitudeExact(longitudeValue);
		} else if (distanceKm < 0.0) {
			throw new IllegalArgumentException(Msg.code(1233) + "Invalid " + Location.SP_NEAR_DISTANCE + " parameter '" + distanceKm + "' must be >= 0.0");
		} else if (distanceKm > CoordCalculator.MAX_SUPPORTED_DISTANCE_KM) {
			throw new IllegalArgumentException(Msg.code(1234) + "Invalid " + Location.SP_NEAR_DISTANCE + " parameter '" + distanceKm + "' must be <= " + CoordCalculator.MAX_SUPPORTED_DISTANCE_KM);
		} else {
			double latitudeDegrees = Double.parseDouble(latitudeValue);
			double longitudeDegrees = Double.parseDouble(longitudeValue);

			GeoBoundingBox box = CoordCalculator.getBox(latitudeDegrees, longitudeDegrees, distanceKm);
			latitudePredicate = theFrom.createLatitudePredicateFromBox(box);
			longitudePredicate = theFrom.createLongitudePredicateFromBox(box);
		}
		ComboCondition singleCode = ComboCondition.and(latitudePredicate, longitudePredicate);
		return combineWithHashIdentityPredicate(theResourceName, theSearchParam.getName(), singleCode);
	}


	public Condition createPredicateLatitudeExact(String theLatitudeValue) {
		return BinaryCondition.equalTo(myColumnLatitude, generatePlaceholder(theLatitudeValue));
	}

	public Condition createPredicateLongitudeExact(String theLongitudeValue) {
		return BinaryCondition.equalTo(myColumnLongitude, generatePlaceholder(theLongitudeValue));
	}

	public Condition createLatitudePredicateFromBox(GeoBoundingBox theBox) {
		return ComboCondition.and(
			BinaryCondition.greaterThanOrEq(myColumnLatitude, generatePlaceholder(theBox.bottomRight().latitude())),
			BinaryCondition.lessThanOrEq(myColumnLatitude, generatePlaceholder(theBox.topLeft().latitude()))
		);
	}

	public Condition createLongitudePredicateFromBox(GeoBoundingBox theBox) {
		if (theBox.bottomRight().longitude() < theBox.topLeft().longitude()) {
			return ComboCondition.or(
				BinaryCondition.greaterThanOrEq(myColumnLongitude, generatePlaceholder(theBox.bottomRight().longitude())),
				BinaryCondition.lessThanOrEq(myColumnLongitude, generatePlaceholder(theBox.topLeft().longitude()))
			);
		}
		return ComboCondition.and(
			BinaryCondition.greaterThanOrEq(myColumnLongitude, generatePlaceholder(theBox.topLeft().longitude())),
			BinaryCondition.lessThanOrEq(myColumnLongitude, generatePlaceholder(theBox.bottomRight().longitude()))
		);
	}
}
