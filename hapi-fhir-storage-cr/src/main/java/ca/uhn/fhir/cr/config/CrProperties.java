package ca.uhn.fhir.cr.config;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hapi.fhir.cr")
public class CrProperties {

	private boolean enabled = true;


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private MeasureProperties measureProperties = null;

	public MeasureProperties getMeasure() {
		return measureProperties;
	}

	public void setMeasure(MeasureProperties measureProperties) {
		this.measureProperties = this.measureProperties;
	}

	private CqlProperties cqlProperties = null;

	public CqlProperties getCql() {
		return cqlProperties;
	}

	public void setCql(CqlProperties cqlProperties) {
		this.cqlProperties = cqlProperties;
	}

	public static class MeasureProperties {


		private boolean threaded_care_gaps_enabled = true;
		private MeasureReportConfiguration measure_report;

		private MeasureEvaluationOptions measure_evaluation = MeasureEvaluationOptions.defaultOptions();


		public boolean getThreadedCareGapsEnabled() {
			return threaded_care_gaps_enabled;
		}

		public void setThreadedCareGapsEnabled(boolean enabled) {
			this.threaded_care_gaps_enabled = enabled;
		}

		public MeasureReportConfiguration getMeasureReport() {
			return this.measure_report;
		}

		public void setMeasureReport(MeasureReportConfiguration measureReport) {
			this.measure_report = measureReport;
		}

		public MeasureEvaluationOptions getMeasureEvaluation() {
			return this.measure_evaluation;
		}

		public void setMeasureEvaluation(MeasureEvaluationOptions measureEvaluation) {
			this.measure_evaluation = measureEvaluation;
		}

		public static class MeasureReportConfiguration {
			/**
			 * Implements the reporter element of the <a href=
			 * "https://www.hl7.org/fhir/measurereport.html">MeasureReport</a> FHIR
			 * Resource.
			 * This is required by the <a href=
			 * "http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/indv-measurereport-deqm">DEQMIndividualMeasureReportProfile</a>
			 * profile found in the
			 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
			 * FHIR Implementation Guide</a>.
			 **/
			private String care_gaps_reporter;
			/**
			 * Implements the author element of the <a href=
			 * "http://www.hl7.org/fhir/composition.html">Composition</a> FHIR
			 * Resource.
			 * This is required by the <a href=
			 * "http://build.fhir.org/ig/HL7/davinci-deqm/StructureDefinition-gaps-composition-deqm.html">DEQMGapsInCareCompositionProfile</a>
			 * profile found in the
			 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
			 * FHIR Implementation Guide</a>.
			 **/
			private String care_gaps_composition_section_author;

			public String getReporter() {
				return care_gaps_reporter;
			}

			public void setCareGapsReporter(String careGapsReporter) {
				this.care_gaps_reporter = null;// ResourceBuilder.ensureOrganizationReference(careGapsReporter);
			}

			public String getCompositionAuthor() {
				return care_gaps_composition_section_author;
			}

			public void setCareGapsCompositionSectionAuthor(String careGapsCompositionSectionAuthor) {
				this.care_gaps_composition_section_author = careGapsCompositionSectionAuthor;
			}
		}

	}


	public static class CqlProperties {

		private boolean useEmbeddedLibraries = true;

		private CqlEngineOptions cqlEngineOptions = CqlEngineOptions.defaultOptions();
		private CqlTranslatorOptions cqlTranslatorOptions = CqlTranslatorOptions.defaultOptions();


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
}
