package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedType;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;

import java.util.StringJoiner;

@OperationEmbeddedType
public class EvaluateMeasureSingleParams {
	@IdParam
	private final IdType myId;
	@OperationParam(name = "periodStart")
	private final String myPeriodStart;
	@OperationParam(name = "periodEnd")
	private final String myPeriodEnd;
	@OperationParam(name = "reportType")
	private final String myReportType;
	@OperationParam(name = "subject")
	private final String mySubject;
	@OperationParam(name = "practitioner")
	private final String myPractitioner;
	@OperationParam(name = "lastReceivedOn")
	private final String myLastReceivedOn;
	@OperationParam(name = "productLine")
	private final String myProductLine;
	@OperationParam(name = "additionalData")
	private final Bundle myAdditionalData;
	@OperationParam(name = "terminologyEndpoint")
	private final Endpoint myTerminologyEndpoint;
	@OperationParam(name = "parameters")
	private final Parameters myParameters;

	public EvaluateMeasureSingleParams(IdType myId, String myPeriodStart, String myPeriodEnd, String myReportType, String mySubject, String myPractitioner, String myLastReceivedOn, String myProductLine, Bundle myAdditionalData, Endpoint myTerminologyEndpoint, Parameters myParameters) {
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
}
