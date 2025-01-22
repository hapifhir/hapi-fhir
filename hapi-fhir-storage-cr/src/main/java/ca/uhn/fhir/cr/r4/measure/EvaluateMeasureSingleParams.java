package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;

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
public class EvaluateMeasureSingleParams {
	// LUKETODO:  should we defined a new @IdEmbeddedParam annotation?
	@IdParam
	private final IdType myId;

	@OperationEmbeddedParam(name = "periodStart")
	private final String myPeriodStart;

	@OperationEmbeddedParam(name = "periodEnd")
	private final String myPeriodEnd;

	@OperationEmbeddedParam(name = "reportType")
	private final String myReportType;

	@OperationEmbeddedParam(name = "subject")
	private final String mySubject;

	@OperationEmbeddedParam(name = "practitioner")
	private final String myPractitioner;

	@OperationEmbeddedParam(name = "lastReceivedOn")
	private final String myLastReceivedOn;

	@OperationEmbeddedParam(name = "productLine")
	private final String myProductLine;

	@OperationEmbeddedParam(name = "additionalData")
	private final Bundle myAdditionalData;

	@OperationEmbeddedParam(name = "terminologyEndpoint")
	private final Endpoint myTerminologyEndpoint;

	@OperationEmbeddedParam(name = "parameters")
	private final Parameters myParameters;

	public EvaluateMeasureSingleParams(
			IdType myId,
			String myPeriodStart,
			String myPeriodEnd,
			String myReportType,
			String mySubject,
			String myPractitioner,
			String myLastReceivedOn,
			String myProductLine,
			Bundle myAdditionalData,
			Endpoint myTerminologyEndpoint,
			Parameters myParameters) {
		this.myId = myId;
		this.myPeriodStart = myPeriodStart;
		this.myPeriodEnd = myPeriodEnd;
		this.myReportType = myReportType;
		this.mySubject = mySubject;
		this.myPractitioner = myPractitioner;
		this.myLastReceivedOn = myLastReceivedOn;
		this.myProductLine = myProductLine;
		this.myAdditionalData = myAdditionalData;
		this.myTerminologyEndpoint = myTerminologyEndpoint;
		this.myParameters = myParameters;
	}

	private EvaluateMeasureSingleParams(Builder builder) {
		this.myId = builder.myId;
		this.myPeriodStart = builder.myPeriodStart;
		this.myPeriodEnd = builder.myPeriodEnd;
		this.myReportType = builder.myReportType;
		this.mySubject = builder.mySubject;
		this.myPractitioner = builder.myPractitioner;
		this.myLastReceivedOn = builder.myLastReceivedOn;
		this.myProductLine = builder.myProductLine;
		this.myAdditionalData = builder.myAdditionalData;
		this.myTerminologyEndpoint = builder.myTerminologyEndpoint;
		this.myParameters = builder.myParameters;
	}

	public IdType getId() {
		return myId;
	}

	public String getPeriodStart() {
		return myPeriodStart;
	}

	public String getPeriodEnd() {
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
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EvaluateMeasureSingleParams that = (EvaluateMeasureSingleParams) o;
		return Objects.equals(myId, that.myId)
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
				myId,
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
				.add("myId=" + myId)
				.add("myPeriodStart='" + myPeriodStart + "'")
				.add("myPeriodEnd='" + myPeriodEnd + "'")
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
		private String myPeriodStart;
		private String myPeriodEnd;
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

		public Builder setPeriodStart(String myPeriodStart) {
			this.myPeriodStart = myPeriodStart;
			return this;
		}

		public Builder setPeriodEnd(String myPeriodEnd) {
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
