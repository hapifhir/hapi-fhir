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
	private MeasureProperties myMeasureProperties = new MeasureProperties();
	private CqlProperties myCqlProperties = new CqlProperties();

	public boolean isCqlEnabled() {
		return myCqlEnabled;
	}

	public void setCqlEnabled(boolean theCqlEnabled) {
		this.myCqlEnabled = theCqlEnabled;
	}

	public MeasureProperties getMeasure() {
		return myMeasureProperties;
	}

	public void setMeasure(MeasureProperties theMeasureProperties) {
		this.myMeasureProperties = theMeasureProperties;
	}

	public CqlProperties getCql() {
		return myCqlProperties;
	}

	public void setCql(CqlProperties theCqlProperties) {
		this.myCqlProperties = theCqlProperties;
	}

	public static class MeasureProperties {


		private boolean myThreadedCareGapsEnabled = true;
		private MeasureReportConfiguration myMeasureReportConfiguration;

		private MeasureEvaluationOptions myMeasureEvaluationOptions = MeasureEvaluationOptions.defaultOptions();


		public boolean getMyThreadedCareGapsEnabled() {
			return myThreadedCareGapsEnabled;
		}

		public void setMyThreadedCareGapsEnabled(boolean theThreadedCareGapsEnabled) {
			this.myThreadedCareGapsEnabled = theThreadedCareGapsEnabled;
		}

		public MeasureReportConfiguration getMeasureReport() {
			return this.myMeasureReportConfiguration;
		}

		public void setMeasureReport(MeasureReportConfiguration theMeasureReport) {
			this.myMeasureReportConfiguration = theMeasureReport;
		}

		public MeasureEvaluationOptions getMeasureEvaluation() {
			return this.myMeasureEvaluationOptions;
		}

		public void setMeasureEvaluation(MeasureEvaluationOptions theMeasureEvaluation) {
			this.myMeasureEvaluationOptions = theMeasureEvaluation;
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

			public String getReporter() {
				return myCareGapsReporter;
			}

			public void setMyCareGapsReporter(String theCareGapsReporter) {
				this.myCareGapsReporter = theCareGapsReporter;
			}

			public String getCompositionAuthor() {
				return myCareGapsCompositionSectionAuthor;
			}

			public void setMyCareGapsCompositionSectionAuthor(String theCareGapsCompositionSectionAuthor) {
				this.myCareGapsCompositionSectionAuthor = theCareGapsCompositionSectionAuthor;
			}
		}

	}


	public static class CqlProperties {

		private boolean myCqlUseOfEmbeddedLibraries = true;

		private CqlEngineOptions myCqlRuntimeOptions = CqlEngineOptions.defaultOptions();
		private CqlTranslatorOptions myCqlTranslatorOptions = CqlTranslatorOptions.defaultOptions();


		public boolean useEmbeddedLibraries() {
			return this.myCqlUseOfEmbeddedLibraries;
		}

		public void setUseEmbeddedLibraries(boolean theCqlUseOfEmbeddedLibraries) {
			this.myCqlUseOfEmbeddedLibraries = theCqlUseOfEmbeddedLibraries;
		}

		public CqlEngineOptions getRuntime() {
			return this.myCqlRuntimeOptions;
		}

		public void setRuntime(CqlEngineOptions theRuntime) {
			this.myCqlRuntimeOptions = theRuntime;
		}

		public CqlTranslatorOptions getCompiler() {
			return this.myCqlTranslatorOptions;
		}

		public void setCompiler(CqlTranslatorOptions theCqlTranslatorOptions) {
			this.myCqlTranslatorOptions = theCqlTranslatorOptions;
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
