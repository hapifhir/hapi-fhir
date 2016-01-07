package ca.uhn.fhir.util.jar;

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

public class DependencyLogFactory {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DependencyLogFactory.class);
	
	@SuppressWarnings("unchecked")
	public static IDependencyLog createJarLogger() {
		try {
			Class<IDependencyLog> clas = (Class<IDependencyLog>) Class.forName("ca.uhn.fhir.util.jar.DependencyLogImpl");
			return clas.newInstance();
		} catch (Exception e) {
			ourLog.info("Could not log dependency.");
			return null;
		}
	}
}
