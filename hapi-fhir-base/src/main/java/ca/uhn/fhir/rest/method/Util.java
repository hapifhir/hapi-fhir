package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dsotnikov on 2/25/2014.
 */
class Util {
//	public static Integer findCountParameterIndex(Method theMethod) {
//		return findParamIndex(theMethod, Count.class);
//	}


	public static Map<String, String> getQueryParams(String query) {
		try {

			Map<String, String> params = new HashMap<String, String>();
			for (String param : query.split("&")) {
				String[] pair = param.split("=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = URLDecoder.decode(pair[1], "UTF-8");

				params.put(key, value);
			}
			return params;
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
	}

}
