package ca.uhn.fhir.jpa.packages;

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

import java.math.BigDecimal;
import java.util.Comparator;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class PackageVersionComparator implements Comparator<String> {
	public static final PackageVersionComparator INSTANCE = new PackageVersionComparator();

	@Override
	public int compare(String o1, String o2) {

		String[] o1parts = o1.split("\\.");
		String[] o2parts = o2.split("\\.");

		for (int i = 0; i < o1parts.length && i < o2parts.length; i++) {
			String i1part = o1parts[i];
			String i2part = o2parts[i];

			if (isNumeric(i1part)) {
				if (isNumeric(i2part)) {
					int cmp = new BigDecimal(i1part).compareTo(new BigDecimal(i2part));
					if (cmp != 0) {
						return cmp;
					}
				}
			}

			int cmp = i1part.compareTo(i2part);
			if (cmp != 0) {
				return cmp;
			}
		}

		return o1parts.length - o2parts.length;
	}

	public static boolean isEquivalent(String theSpec, String thePackageVersion) {
		String[] o1parts = theSpec.split("\\.");
		String[] o2parts = thePackageVersion.split("\\.");

		for (int i = 0; i < o1parts.length; i++ ) {
			if (!o1parts[i].equals("x")) {
				if (!o1parts[i].equals(o2parts[i])) {
					return false;
				}
			}
		}

		return true;
	}
}
