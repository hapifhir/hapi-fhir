/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import org.hibernate.search.engine.spatial.GeoBoundingBox;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.slf4j.Logger;

import static ca.uhn.fhir.jpa.searchparam.extractor.GeopointNormalizer.normalizeLatitude;
import static ca.uhn.fhir.jpa.searchparam.extractor.GeopointNormalizer.normalizeLongitude;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Utility for calculating a symmetric GeoBoundingBox around a center point,
 * ensuring each edge is at least the given distance in kilometers away.
 */
public class CoordCalculator {
	private static final Logger ourLog = getLogger(CoordCalculator.class);
	public static final double MAX_SUPPORTED_DISTANCE_KM =
			10000.0; // Slightly less than a quarter of the earth's circumference
	private static final double RADIUS_EARTH_KM = 6378.1;

	/**
	 * Computes a symmetric bounding box around the given latitude/longitude center,
	 * with each edge being at least the given distance in km away.
	 *
	 * @param lat center latitude
	 * @param lon center longitude
	 * @param distanceKm distance in kilometers from center to each edge
	 * @return GeoBoundingBox centered around the given coordinates
	 */
	public static GeoBoundingBox getBox(double lat, double lon, double distanceKm) {
		// Approximate 1 degree latitude as 111 km
		double deltaLat = distanceKm / 111.0;

		// Longitude varies with latitude
		double deltaLon = distanceKm / (111.320 * Math.cos(Math.toRadians(lat)));

		double minLat = normalizeLatitude(lat - deltaLat);
		double maxLat = normalizeLatitude(lat + deltaLat);
		double minLon = normalizeLongitude(lon - deltaLon);
		double maxLon = normalizeLongitude(lon + deltaLon);

		GeoPoint topLeft = GeoPoint.of(maxLat, minLon);
		GeoPoint bottomRight = GeoPoint.of(minLat, maxLon);

		return GeoBoundingBox.of(topLeft, bottomRight);
	}
}
