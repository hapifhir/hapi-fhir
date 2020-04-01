package ca.uhn.fhir.jpa.demo;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

public class ContextPostgreSQLHolder extends ContextHolder {

//	private static String myDbUsername;
//	private static String myDbPassword;
	private static boolean myExternalElasticsearch = false;
//	private static String myElasticsearchHost;
//	private static Integer myElasticsearchPort;
//	private static String myElasticsearchUsername;
//	private static String myElasticsearchPassword;

/*	public static void setDbUsername(String theDbUsername) {
		myDbUsername = theDbUsername;
	}

	public static String getDbUsername() {
		return myDbUsername;
	}

	public static void setDbPassword(String theDbPassword) {
		myDbPassword = theDbPassword;
	}

	public static String getDbPassword() {
		return myDbPassword;
	}
*/
	public static void setExternalElasticsearch(Boolean theExternalElasticsearch) {
		myExternalElasticsearch = theExternalElasticsearch;
	}

	public static Boolean isExternalElasticsearch() {
		return myExternalElasticsearch;
	}
/*
	public static void setElasticsearchHost(String theElasticsearchHost) {
		myElasticsearchHost = theElasticsearchHost;
	}

	public static String getElasticsearchHost() {
		return myElasticsearchHost;
	}

	public static void setElasticsearchPort(Integer theElasticsearchPort) {
		myElasticsearchPort = theElasticsearchPort;
	}

	public static Integer getElasticsearchPort() {
		return myElasticsearchPort;
	}

	public static void setElasticsearchUsername(String theElasticsearchUsername) {
		myElasticsearchUsername = theElasticsearchUsername;
	}

	public static String getElasticsearchUsername() {
		return myElasticsearchUsername;
	}

	public static void setElasticsearchPassword(String theElasticsearchPassword) {
		myElasticsearchPassword = theElasticsearchPassword;
	}

	public static String getElasticsearchPassword() {
		return myElasticsearchPassword;
	}
*/
}
