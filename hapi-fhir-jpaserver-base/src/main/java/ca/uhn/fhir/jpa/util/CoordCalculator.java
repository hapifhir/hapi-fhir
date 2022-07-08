package ca.uhn.fhir.jpa.util;

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


import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.engine.spatial.GeoBoundingBox;
import org.slf4j.Logger;

import static ca.uhn.fhir.jpa.searchparam.extractor.GeopointNormalizer.normalizeLatitude;
import static ca.uhn.fhir.jpa.searchparam.extractor.GeopointNormalizer.normalizeLongitude;
import static org.slf4j.LoggerFactory.getLogger;

public class CoordCalculator {
	private static final Logger ourLog = getLogger(CoordCalculator.class);
	public static final double MAX_SUPPORTED_DISTANCE_KM = 10000.0; // Slightly less than a quarter of the earth's circumference
	private static final double RADIUS_EARTH_KM = 6378.1;

	// Source: https://stackoverflow.com/questions/7222382/get-lat-long-given-current-point-distance-and-bearing
	static GeoPoint findTarget(double theLatitudeDegrees, double theLongitudeDegrees, double theBearingDegrees, double theDistanceKm) {

		double latitudeRadians = Math.toRadians(normalizeLatitude(theLatitudeDegrees));
		double longitudeRadians = Math.toRadians(normalizeLongitude(theLongitudeDegrees));
		double bearingRadians = Math.toRadians(theBearingDegrees);
		double distanceRadians = theDistanceKm / RADIUS_EARTH_KM;

		double targetLatitude = Math.asin( Math.sin(latitudeRadians) * Math.cos(distanceRadians) +
			Math.cos(latitudeRadians) * Math.sin(distanceRadians) * Math.cos(bearingRadians));

		double targetLongitude = longitudeRadians + Math.atan2(Math.sin(bearingRadians) * Math.sin(distanceRadians) * Math.cos(latitudeRadians),
			Math.cos(distanceRadians)-Math.sin(latitudeRadians) * Math.sin(targetLatitude));

		double latitude = Math.toDegrees(targetLatitude);
		double longitude = Math.toDegrees(targetLongitude);

		GeoPoint of = GeoPoint.of(normalizeLatitude(latitude), normalizeLongitude(longitude));
		return of;
	}

	/**
	 * Find a box around my coordinates such that the closest distance to each edge is the provided distance
	 * @return
	 */
	public static GeoBoundingBox getBox(double theLatitudeDegrees, double theLongitudeDegrees, Double theDistanceKm) {
		double diagonalDistanceKm = theDistanceKm * Math.sqrt(2.0);

		GeoPoint topLeft = CoordCalculator.findTarget(theLatitudeDegrees, theLongitudeDegrees, 315.0, diagonalDistanceKm);
		GeoPoint bottomRight = CoordCalculator.findTarget(theLatitudeDegrees, theLongitudeDegrees, 135.0, diagonalDistanceKm);

		return GeoBoundingBox.of(topLeft, bottomRight);
	}
}
