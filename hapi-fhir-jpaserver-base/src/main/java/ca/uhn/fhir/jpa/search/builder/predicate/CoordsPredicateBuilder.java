/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.CoordCalculator;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu2.resource.Location;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hibernate.search.engine.spatial.GeoBoundingBox;

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

	public DbColumn getColumnLatitude() {
		return myColumnLatitude;
	}

	public DbColumn getColumnLongitude() {
		return myColumnLongitude;
	}

	public Condition createPredicateCoords(
			SearchParameterMap theParams,
			IQueryParameterType theParam,
			String theResourceName,
			RuntimeSearchParam theSearchParam,
			CoordsPredicateBuilder theFrom,
			RequestPartitionId theRequestPartitionId) {

		ParsedLocationParam params = ParsedLocationParam.from(theParams, theParam);
		double distanceKm = params.getDistanceKm();
		double latitudeValue = params.getLatitudeValue();
		double longitudeValue = params.getLongitudeValue();

		Condition latitudePredicate;
		Condition longitudePredicate;
		if (distanceKm == 0.0) {
			latitudePredicate = theFrom.createPredicateLatitudeExact(latitudeValue);
			longitudePredicate = theFrom.createPredicateLongitudeExact(longitudeValue);
		} else if (distanceKm < 0.0) {
			throw new IllegalArgumentException(Msg.code(1233) + "Invalid " + Location.SP_NEAR_DISTANCE + " parameter '"
					+ distanceKm + "' must be >= 0.0");
		} else if (distanceKm > CoordCalculator.MAX_SUPPORTED_DISTANCE_KM) {
			throw new IllegalArgumentException(Msg.code(1234) + "Invalid " + Location.SP_NEAR_DISTANCE + " parameter '"
					+ distanceKm + "' must be <= " + CoordCalculator.MAX_SUPPORTED_DISTANCE_KM);
		} else {
			GeoBoundingBox box = CoordCalculator.getBox(latitudeValue, longitudeValue, distanceKm);
			latitudePredicate = theFrom.createLatitudePredicateFromBox(box);
			longitudePredicate = theFrom.createLongitudePredicateFromBox(box);
		}
		ComboCondition singleCode = ComboCondition.and(latitudePredicate, longitudePredicate);
		return combineWithHashIdentityPredicate(theResourceName, theSearchParam.getName(), singleCode);
	}

	public Condition createPredicateLatitudeExact(double theLatitudeValue) {
		return BinaryCondition.equalTo(myColumnLatitude, generatePlaceholder(theLatitudeValue));
	}

	public Condition createPredicateLongitudeExact(double theLongitudeValue) {
		return BinaryCondition.equalTo(myColumnLongitude, generatePlaceholder(theLongitudeValue));
	}

	public Condition createLatitudePredicateFromBox(GeoBoundingBox theBox) {
		return ComboCondition.and(
				BinaryCondition.greaterThanOrEq(
						myColumnLatitude,
						generatePlaceholder(theBox.bottomRight().latitude())),
				BinaryCondition.lessThanOrEq(
						myColumnLatitude, generatePlaceholder(theBox.topLeft().latitude())));
	}

	public Condition createLongitudePredicateFromBox(GeoBoundingBox theBox) {
		if (theBox.bottomRight().longitude() < theBox.topLeft().longitude()) {
			return ComboCondition.or(
					BinaryCondition.greaterThanOrEq(
							myColumnLongitude,
							generatePlaceholder(theBox.bottomRight().longitude())),
					BinaryCondition.lessThanOrEq(
							myColumnLongitude,
							generatePlaceholder(theBox.topLeft().longitude())));
		} else {
			return ComboCondition.and(
					BinaryCondition.greaterThanOrEq(
							myColumnLongitude,
							generatePlaceholder(theBox.topLeft().longitude())),
					BinaryCondition.lessThanOrEq(
							myColumnLongitude,
							generatePlaceholder(theBox.bottomRight().longitude())));
		}
	}
}
