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
package ca.uhn.fhir.cr.config;

import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.engine.CqlEngineOptions;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;

public class CrProperties {

	private boolean myCqlEnabled = true;
	private MeasureProperties myMeasureProperties;
	private CqlProperties myCqlProperties = new CqlProperties();

	public CrProperties () {
		this.myMeasureProperties = new MeasureProperties();
	};


	public boolean isCqlEnabled() {
		return myCqlEnabled;
	}

	public void setCqlEnabled(boolean theCqlEnabled) {
		this.myCqlEnabled = theCqlEnabled;
	}

	public MeasureProperties getMeasureProperties() {
		return myMeasureProperties;
	}

	public void setMeasureProperties(MeasureProperties theMeasureProperties) {
		this.myMeasureProperties = theMeasureProperties;
	}

	public CqlProperties getCqlProperties() {
		return myCqlProperties;
	}

	public void setCqlProperties(CqlProperties theCqlProperties) {
		this.myCqlProperties = theCqlProperties;
	}

	public static class MeasureProperties {

		private boolean myThreadedCareGapsEnabled = true;
		private MeasureReportConfiguration myMeasureReportConfiguration;
		private MeasureEvaluationOptions myMeasureEvaluationOptions;

		public static final int DEFAULT_THREADS_FOR_MEASURE_EVAL = 4;
		public static final int DEFAULT_THREADS_BATCH_SIZE = 250;
		public static final boolean DEFAULT_THREADS_ENABLED_FOR_MEASURE_EVAL = true;

		public MeasureProperties() {
			myMeasureEvaluationOptions = MeasureEvaluationOptions.defaultOptions();
			myMeasureEvaluationOptions.setNumThreads(DEFAULT_THREADS_FOR_MEASURE_EVAL);
			myMeasureEvaluationOptions.setThreadedBatchSize(DEFAULT_THREADS_BATCH_SIZE);
			myMeasureEvaluationOptions.setThreadedEnabled(DEFAULT_THREADS_ENABLED_FOR_MEASURE_EVAL);
		};


		//care gaps
		public boolean getThreadedCareGapsEnabled() {
			return myThreadedCareGapsEnabled;
		}

		public void setThreadedCareGapsEnabled(boolean theThreadedCareGapsEnabled) {
			myThreadedCareGapsEnabled = theThreadedCareGapsEnabled;
		}
		public boolean isThreadedCareGapsEnabled() {
			return myThreadedCareGapsEnabled;
		}

		//report configuration
		public MeasureReportConfiguration getMeasureReportConfiguration() {
			return myMeasureReportConfiguration;
		}

		public void setMeasureReportConfiguration(MeasureReportConfiguration theMeasureReport) {
			myMeasureReportConfiguration = theMeasureReport;
		}


		//measure evaluations
		public void setMeasureEvaluationOptions(MeasureEvaluationOptions theMeasureEvaluation) {
			myMeasureEvaluationOptions = theMeasureEvaluation;
		}

		public MeasureEvaluationOptions getMeasureEvaluationOptions() {
			return myMeasureEvaluationOptions;
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
			private String myCareGapsReporter;
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
			private String myCareGapsCompositionSectionAuthor;

			public String getCareGapsReporter() {
				return myCareGapsReporter;
			}

			public void setCareGapsReporter(String theCareGapsReporter) {
				myCareGapsReporter = theCareGapsReporter;
			}

			public String getCareGapsCompositionSectionAuthor() {
				return myCareGapsCompositionSectionAuthor;
			}

			public void setCareGapsCompositionSectionAuthor(String theCareGapsCompositionSectionAuthor) {
				myCareGapsCompositionSectionAuthor = theCareGapsCompositionSectionAuthor;
			}
		}

	}


	public static class CqlProperties {

		private boolean myCqlUseOfEmbeddedLibraries = true;

		private CqlEngineOptions myCqlRuntimeOptions = CqlEngineOptions.defaultOptions();
		private CqlTranslatorOptions myCqlTranslatorOptions = CqlTranslatorOptions.defaultOptions();


		public boolean isCqlUseOfEmbeddedLibraries() {
			return myCqlUseOfEmbeddedLibraries;
		}

		public void setCqlUseOfEmbeddedLibraries(boolean theCqlUseOfEmbeddedLibraries) {
			myCqlUseOfEmbeddedLibraries = theCqlUseOfEmbeddedLibraries;
		}

		public CqlEngineOptions getCqlRuntimeOptions() {
			return myCqlRuntimeOptions;
		}

		public void setCqlRuntimeOptions(CqlEngineOptions theRuntime) {
			myCqlRuntimeOptions = theRuntime;
		}

		public CqlTranslatorOptions getCqlTranslatorOptions() {
			return myCqlTranslatorOptions;
		}

		public void setCqlTranslatorOptions(CqlTranslatorOptions theCqlTranslatorOptions) {
			myCqlTranslatorOptions = theCqlTranslatorOptions;
		}

		public CqlOptions getCqlOptions() {
			CqlOptions cqlOptions = new CqlOptions();
			cqlOptions.setUseEmbeddedLibraries(isCqlUseOfEmbeddedLibraries());
			cqlOptions.setCqlEngineOptions(getCqlRuntimeOptions());
			cqlOptions.setCqlTranslatorOptions(getCqlTranslatorOptions());
			return cqlOptions;
		}
	}
}
