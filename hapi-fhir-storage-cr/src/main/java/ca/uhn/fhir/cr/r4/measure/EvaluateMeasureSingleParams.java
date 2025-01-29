/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.EmbeddableOperationParams;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Parameters;
import org.opencds.cqf.fhir.utility.monad.Either3;
import org.opencds.cqf.fhir.utility.monad.Eithers;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Non-RequestDetails parameters for the <a href=
 * "https://www.hl7.org/fhir/operation-measure-evaluate-measure.html">$evaluate-measure</a>
 * operation found in the
 * <a href="http://www.hl7.org/fhir/clinicalreasoning-module.html">FHIR Clinical
 * Reasoning Module</a>. This implementation aims to be compatible with the CQF
 * IG.
 * <p/>
 * myeId             the id of the Measure to evaluate
 * myPeriodStart     The start of the reporting period
 * myPeriodEnd       The end of the reporting period
 * myReportType      The type of MeasureReport to generate
 * mySubject         the subject to use for the evaluation
 * myPractitioner    the practitioner to use for the evaluation
 * myLastReceivedOn  the date the results of this measure were last
 *                          received.
 * myProductLine     the productLine (e.g. Medicare, Medicaid, etc) to use
 *                         for the evaluation. This is a non-standard parameter.
 * myAdditionalData  the data bundle containing additional data
 */
// LUKETODO:  start to integrate this with a clinical reasoning branch
@EmbeddableOperationParams
public class EvaluateMeasureSingleParams {
	private final Either3<CanonicalType, IdType, Measure> myMeasure;

	private final ZonedDateTime myPeriodStart;

	private final ZonedDateTime myPeriodEnd;

	private final String myReportType;

	private final String mySubject;

	private final String myPractitioner;

	private final String myLastReceivedOn;

	private final String myProductLine;

	private final Bundle myAdditionalData;

	private final Endpoint myTerminologyEndpoint;

	private final Parameters myParameters;

	// LUKETODO:  embedded factory constructor annoation
	// LUKETODO:  annotations on constructor parameters instead
	public EvaluateMeasureSingleParams(
			@IdParam IdType theId,
			@OperationParam(
							name = "periodStart",
							sourceType = String.class,
							rangeType = OperationParameterRangeType.START)
					ZonedDateTime thePeriodStart,
			@OperationParam(name = "periodEnd", sourceType = String.class, rangeType = OperationParameterRangeType.END)
					ZonedDateTime thePeriodEnd,
			@OperationParam(name = "reportType") String theReportType,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "practitioner") String thePractitioner,
			@OperationParam(name = "lastReceivedOn") String theLastReceivedOn,
			@OperationParam(name = "productLine") String theProductLine,
			@OperationParam(name = "additionalData") Bundle theAdditionalData,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			@OperationParam(name = "parameters") Parameters theParameters) {
		myMeasure = Eithers.forMiddle3(theId);
		myPeriodStart = thePeriodStart;
		myPeriodEnd = thePeriodEnd;
		myReportType = theReportType;
		mySubject = theSubject;
		myPractitioner = thePractitioner;
		myLastReceivedOn = theLastReceivedOn;
		myProductLine = theProductLine;
		myAdditionalData = theAdditionalData;
		myTerminologyEndpoint = theTerminologyEndpoint;
		myParameters = theParameters;
	}

	private EvaluateMeasureSingleParams(Builder builder) {
		this(
				builder.myId,
				builder.myPeriodStart,
				builder.myPeriodEnd,
				builder.myReportType,
				builder.mySubject,
				builder.myPractitioner,
				builder.myLastReceivedOn,
				builder.myProductLine,
				builder.myAdditionalData,
				builder.myTerminologyEndpoint,
				builder.myParameters);
	}

	public Either3<CanonicalType, IdType, Measure> getMeasure() {
		return myMeasure;
	}

	public ZonedDateTime getPeriodStart() {
		return myPeriodStart;
	}

	public ZonedDateTime getPeriodEnd() {
		return myPeriodEnd;
	}

	public String getReportType() {
		return myReportType;
	}

	public String getSubject() {
		return mySubject;
	}

	public String getPractitioner() {
		return myPractitioner;
	}

	public String getLastReceivedOn() {
		return myLastReceivedOn;
	}

	public String getProductLine() {
		return myProductLine;
	}

	public Bundle getAdditionalData() {
		return myAdditionalData;
	}

	public Endpoint getTerminologyEndpoint() {
		return myTerminologyEndpoint;
	}

	public Parameters getParameters() {
		return myParameters;
	}

	@Override
	public boolean equals(Object theO) {
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		EvaluateMeasureSingleParams that = (EvaluateMeasureSingleParams) theO;
		return Objects.equals(myMeasure, that.myMeasure)
				&& Objects.equals(myPeriodStart, that.myPeriodStart)
				&& Objects.equals(myPeriodEnd, that.myPeriodEnd)
				&& Objects.equals(myReportType, that.myReportType)
				&& Objects.equals(mySubject, that.mySubject)
				&& Objects.equals(myPractitioner, that.myPractitioner)
				&& Objects.equals(myLastReceivedOn, that.myLastReceivedOn)
				&& Objects.equals(myProductLine, that.myProductLine)
				&& Objects.equals(myAdditionalData, that.myAdditionalData)
				&& Objects.equals(myTerminologyEndpoint, that.myTerminologyEndpoint)
				&& Objects.equals(myParameters, that.myParameters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				myMeasure,
				myPeriodStart,
				myPeriodEnd,
				myReportType,
				mySubject,
				myPractitioner,
				myLastReceivedOn,
				myProductLine,
				myAdditionalData,
				myTerminologyEndpoint,
				myParameters);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", EvaluateMeasureSingleParams.class.getSimpleName() + "[", "]")
				.add("myMeasure=" + myMeasure)
				.add("myPeriodStart=" + myPeriodStart)
				.add("myPeriodEnd=" + myPeriodEnd)
				.add("myReportType='" + myReportType + "'")
				.add("mySubject='" + mySubject + "'")
				.add("myPractitioner='" + myPractitioner + "'")
				.add("myLastReceivedOn='" + myLastReceivedOn + "'")
				.add("myProductLine='" + myProductLine + "'")
				.add("myAdditionalData=" + myAdditionalData)
				.add("myTerminologyEndpoint=" + myTerminologyEndpoint)
				.add("myParameters=" + myParameters)
				.toString();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private IdType myId;
		private ZonedDateTime myPeriodStart;
		private ZonedDateTime myPeriodEnd;
		private String myReportType;
		private String mySubject;
		private String myPractitioner;
		private String myLastReceivedOn;
		private String myProductLine;
		private Bundle myAdditionalData;
		private Endpoint myTerminologyEndpoint;
		private Parameters myParameters;

		public Builder setId(IdType myId) {
			this.myId = myId;
			return this;
		}

		public Builder setPeriodStart(ZonedDateTime myPeriodStart) {
			this.myPeriodStart = myPeriodStart;
			return this;
		}

		public Builder setPeriodEnd(ZonedDateTime myPeriodEnd) {
			this.myPeriodEnd = myPeriodEnd;
			return this;
		}

		public Builder setReportType(String myReportType) {
			this.myReportType = myReportType;
			return this;
		}

		public Builder setSubject(String mySubject) {
			this.mySubject = mySubject;
			return this;
		}

		public Builder setPractitioner(String myPractitioner) {
			this.myPractitioner = myPractitioner;
			return this;
		}

		public Builder setLastReceivedOn(String myLastReceivedOn) {
			this.myLastReceivedOn = myLastReceivedOn;
			return this;
		}

		public Builder setProductLine(String myProductLine) {
			this.myProductLine = myProductLine;
			return this;
		}

		public Builder setAdditionalData(Bundle myAdditionalData) {
			this.myAdditionalData = myAdditionalData;
			return this;
		}

		public Builder setTerminologyEndpoint(Endpoint myTerminologyEndpoint) {
			this.myTerminologyEndpoint = myTerminologyEndpoint;
			return this;
		}

		public Builder setParameters(Parameters myParameters) {
			this.myParameters = myParameters;
			return this;
		}

		public EvaluateMeasureSingleParams build() {
			return new EvaluateMeasureSingleParams(this);
		}
	}
}
