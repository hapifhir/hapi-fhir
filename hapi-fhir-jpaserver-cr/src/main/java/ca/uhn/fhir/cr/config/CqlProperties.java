package ca.uhn.fhir.cr.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Reasoning
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


import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.engine.CqlEngineOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hapi.fhir.cql")
public class CqlProperties {

	private boolean enabled = true;
	private boolean useEmbeddedLibraries = true;

	private CqlEngineOptions cqlEngineOptions = CqlEngineOptions.defaultOptions();
	private CqlTranslatorOptions cqlTranslatorOptions = CqlTranslatorOptions.defaultOptions();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean useEmbeddedLibraries() {
		return this.useEmbeddedLibraries;
	}

	public void setUseEmbeddedLibraries(boolean useEmbeddedLibraries) {
		this.useEmbeddedLibraries = useEmbeddedLibraries;
	}

	public CqlEngineOptions getEngine() {
		return this.cqlEngineOptions;
	}

	public void setEngine(CqlEngineOptions engine) {
		this.cqlEngineOptions = engine;
	}

	public CqlTranslatorOptions getTranslator() {
		return this.cqlTranslatorOptions;
	}

	public void setTranslator(CqlTranslatorOptions translator) {
		this.cqlTranslatorOptions = translator;
	}

	public CqlOptions getOptions() {
		CqlOptions cqlOptions = new CqlOptions();
		cqlOptions.setUseEmbeddedLibraries(this.useEmbeddedLibraries());
		cqlOptions.setCqlEngineOptions(this.getEngine());
		cqlOptions.setCqlTranslatorOptions(this.getTranslator());
		return cqlOptions;
	}
}
