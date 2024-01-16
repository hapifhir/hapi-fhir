/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.util;

public final class CoordCalculatorTestUtil {
	// CHIN and UHN coordinates from Google Maps
	// Distance and bearing from https://www.movable-type.co.uk/scripts/latlong.html
	public static final double LATITUDE_CHIN = 43.65513;
	public static final double LONGITUDE_CHIN = -79.4170007;
	public static final double LATITUDE_UHN = 43.656765;
	public static final double LONGITUDE_UHN = -79.3987645;
	public static final double DISTANCE_KM_CHIN_TO_UHN = 1.478;
	public static final double BEARING_CHIN_TO_UHN = 82 + (55.0 / 60) + (46.0 / 3600);
	// A Fiji island near the anti-meridian
	public static final double LATITUDE_TAVEUNI = -16.8488893;
	public static final double LONGITIDE_TAVEUNI = 179.889793;
	// enough distance from point to cross anti-meridian
	public static final double DISTANCE_TAVEUNI = 100.0;
	public static final double LATITUDE_TORONTO = 43.741667;
	public static final double LONGITUDE_TORONTO = -79.373333;
	public static final double LATITUDE_BELLEVILLE = 44.166667;
	public static final double LONGITUDE_BELLEVILLE = -77.383333;
	public static final double LATITUDE_KINGSTON = 44.234722;
	public static final double LONGITUDE_KINGSTON = -76.510833;
	public static final double LATITUDE_OTTAWA = 45.424722;
	public static final double LONGITUDE_OTTAWA = -75.695;

	private CoordCalculatorTestUtil() {}
}
