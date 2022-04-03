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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.StringReader;

public class XmlDetectionUtil {

	private static final Logger ourLog = LoggerFactory.getLogger(XmlDetectionUtil.class);
	private static Boolean ourStaxPresent;

	/**
	 * This method will return <code>true</code> if a StAX XML parsing library is present
	 * on the classpath
	 */
	public static boolean isStaxPresent() {
		Boolean retVal = ourStaxPresent;
		if (retVal == null) {
			try {
				Class.forName("javax.xml.stream.events.XMLEvent");
				Class<?> xmlUtilClazz = Class.forName("ca.uhn.fhir.util.XmlUtil");
				xmlUtilClazz.getMethod("createXmlReader", Reader.class).invoke(xmlUtilClazz, new StringReader(""));
				ourStaxPresent = Boolean.TRUE;
				retVal = Boolean.TRUE;
			} catch (Throwable t) {
				ourLog.info("StAX not detected on classpath, XML processing will be disabled");
				ourStaxPresent = Boolean.FALSE;
				retVal = Boolean.FALSE;
			}
		}
		return retVal;
	}

	
}
