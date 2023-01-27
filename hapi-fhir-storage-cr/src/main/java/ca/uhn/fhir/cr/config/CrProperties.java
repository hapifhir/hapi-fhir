package ca.uhn.fhir.cr.config;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

public class CrProperties {

	private boolean enabled = true;
	private MeasureProperties measureProperties = new MeasureProperties();
	private CqlProperties cqlProperties = new CqlProperties();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public MeasureProperties getMeasure() {
		return measureProperties;
	}

	public void setMeasure(MeasureProperties measureProperties) {
		this.measureProperties = measureProperties;
	}

	public CqlProperties getCql() {
		return cqlProperties;
	}

	public void setCql(CqlProperties cqlProperties) {
		this.cqlProperties = cqlProperties;
	}

	public static class MeasureProperties {


		private boolean threadedCareGapsEnabled = true;
		private MeasureReportConfiguration measureReportConfiguration;

		private MeasureEvaluationOptions measureEvaluationOptions = MeasureEvaluationOptions.defaultOptions();


		public boolean getThreadedCareGapsEnabled() {
			return threadedCareGapsEnabled;
		}

		public void setThreadedCareGapsEnabled(boolean enabled) {
			this.threadedCareGapsEnabled = enabled;
		}

		public MeasureReportConfiguration getMeasureReport() {
			return this.measureReportConfiguration;
		}

		public void setMeasureReport(MeasureReportConfiguration measureReport) {
			this.measureReportConfiguration = measureReport;
		}

		public MeasureEvaluationOptions getMeasureEvaluation() {
			return this.measureEvaluationOptions;
		}

		public void setMeasureEvaluation(MeasureEvaluationOptions measureEvaluation) {
			this.measureEvaluationOptions = measureEvaluation;
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
			private String careGapsReporter;
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
			private String careGapsCompositionSectionAuthor;

			public String getReporter() {
				return careGapsReporter;
			}

			public void setCareGapsReporter(String careGapsReporter) {
				this.careGapsReporter = careGapsReporter;// ResourceBuilder.ensureOrganizationReference(careGapsReporter);
			}

			public String getCompositionAuthor() {
				return careGapsCompositionSectionAuthor;
			}

			public void setCareGapsCompositionSectionAuthor(String careGapsCompositionSectionAuthor) {
				this.careGapsCompositionSectionAuthor = careGapsCompositionSectionAuthor;
			}
		}

	}


	public static class CqlProperties {

		private boolean useEmbeddedLibraries = true;

		private CqlEngineOptions runtimeOptions = CqlEngineOptions.defaultOptions();
		private CqlTranslatorOptions compilerOptions = CqlTranslatorOptions.defaultOptions();


		public boolean useEmbeddedLibraries() {
			return this.useEmbeddedLibraries;
		}

		public void setUseEmbeddedLibraries(boolean useEmbeddedLibraries) {
			this.useEmbeddedLibraries = useEmbeddedLibraries;
		}

		public CqlEngineOptions getRuntime() {
			return this.runtimeOptions;
		}

		public void setRuntime(CqlEngineOptions runtime) {
			this.runtimeOptions = runtime;
		}

		public CqlTranslatorOptions getCompiler() {
			return this.compilerOptions;
		}

		public void setCompiler(CqlTranslatorOptions compiler) {
			this.compilerOptions = compiler;
		}

		public CqlOptions getOptions() {
			CqlOptions cqlOptions = new CqlOptions();
			cqlOptions.setUseEmbeddedLibraries(this.useEmbeddedLibraries());
			cqlOptions.setCqlEngineOptions(this.getRuntime());
			cqlOptions.setCqlTranslatorOptions(this.getCompiler());
			return cqlOptions;
		}
	}
}
