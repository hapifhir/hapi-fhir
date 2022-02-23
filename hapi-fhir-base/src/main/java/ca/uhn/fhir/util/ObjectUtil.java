package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.StringUtils;

/*
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

public class ObjectUtil {

	public static boolean equals(Object object1, Object object2) {
		if (object1 == object2) {
			return true;
		}
		if ((object1 == null) || (object2 == null)) {
			return false;
		}
		return object1.equals(object2);
	}
	
	public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(Msg.code(1776) + message);
        return obj;
    }

	public static void requireNotEmpty(String str, String message) {
		if (StringUtils.isBlank(str)) {
			throw new IllegalArgumentException(Msg.code(1777) + message);
		}
	}
	
}
