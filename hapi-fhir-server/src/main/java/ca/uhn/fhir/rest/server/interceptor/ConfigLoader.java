package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.util.ClasspathUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class ConfigLoader {

	private static final Logger ourLog = LoggerFactory.getLogger(ConfigLoader.class);
	public static final String CLASSPATH = "classpath:";

	public static String loadResourceContent(String theResourcePath) {
		if(theResourcePath.startsWith(CLASSPATH)) {
			theResourcePath = theResourcePath.substring(CLASSPATH.length());
		}
		return ClasspathUtil.loadResource(theResourcePath);
	}

	public static Properties loadProperties(String theResourcePath) {
		String propsString = loadResourceContent(theResourcePath);
		Properties props = new Properties();
		try {
			props.load(new StringReader(propsString));
		} catch (IOException e) {
			throw new RuntimeException(Msg.code(324) + String.format("Unable to load properties at %s", theResourcePath), e);
		}
		return props;
	}

	public static <T> T loadJson(String theResourcePath, Class<T> theModelClass) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(loadResourceContent(theResourcePath), theModelClass);
		} catch (Exception e) {
			throw new RuntimeException(Msg.code(325) + String.format("Unable to parse resource at %s", theResourcePath), e);
		}
	}

}
