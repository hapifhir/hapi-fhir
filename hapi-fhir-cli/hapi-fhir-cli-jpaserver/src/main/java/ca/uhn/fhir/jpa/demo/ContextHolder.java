package ca.uhn.fhir.jpa.demo;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.Validate;

public class ContextHolder {

	private static boolean ourAllowExternalRefs;
	private static FhirContext ourCtx;
	private static boolean ourDisableReferentialIntegrity;
	private static String ourPath;
	private static Long ourReuseSearchResultsMillis;
	private static String ourDatabaseUrl;

	static {
		ourReuseSearchResultsMillis = DaoConfig.DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS;
	}

	public static FhirContext getCtx() {
		Validate.notNull(ourPath, "Context not set");
		return ourCtx;
	}

	public static void setCtx(FhirContext theCtx) throws ParseException {
		switch (theCtx.getVersion().getVersion()) {
			case DSTU2:
				ourPath = "/baseDstu2/";
				break;
			case DSTU2_1:
				break;
			case DSTU3:
				ourPath = "/baseDstu3/";
				break;
			case R4:
				ourPath = "/baseR4/";
				break;
			case DSTU2_HL7ORG:
			default:
				throw new ParseException(Msg.code(1531) + "FHIR version not supported by this command: " + theCtx.getVersion().getVersion());
		}

		ourCtx = theCtx;
	}

	public static String getPath() {
		Validate.notNull(ourPath, "Context not set");
		return ourPath;
	}

	public static Long getReuseCachedSearchResultsForMillis() {
		return ourReuseSearchResultsMillis;
	}

	public static void setReuseCachedSearchResultsForMillis(Long reuseSearchResultsMillis) {
		ourReuseSearchResultsMillis = reuseSearchResultsMillis;
	}

	public static boolean isAllowExternalRefs() {
		return ourAllowExternalRefs;
	}

	public static void setAllowExternalRefs(boolean theAllowExternalRefs) {
		ourAllowExternalRefs = theAllowExternalRefs;
	}

	public static boolean isDisableReferentialIntegrity() {
		return ourDisableReferentialIntegrity;
	}

	public static void setDisableReferentialIntegrity(boolean theDisableReferentialIntegrity) {
		ourDisableReferentialIntegrity = theDisableReferentialIntegrity;
	}

	public static String getDatabaseUrl() {
		return ourDatabaseUrl;
	}

	public static void setDatabaseUrl(String theDatabaseUrl) {
		ourDatabaseUrl = theDatabaseUrl;
	}
}
