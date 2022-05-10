package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import java.text.DecimalFormat;

public class FileUtil {
	// Use "bytes" instead of just "b" because it reads easier in logs
	private static final String[] UNITS = new String[]{"Bytes", "kB", "MB", "GB", "TB"};

	public static String formatFileSize(long theBytes) {
		if (theBytes <= 0) {
			return "0 " + UNITS[0];
		}
		int digitGroups = (int) (Math.log10(theBytes) / Math.log10(1024));
		digitGroups = Math.min(digitGroups, UNITS.length - 1);
		return new DecimalFormat("###0.#").format(theBytes / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
	}

}
