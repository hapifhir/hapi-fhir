package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.OperationEmbeddedType;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;

import java.util.List;
import java.util.StringJoiner;

@OperationEmbeddedType
public class CareGapsParams {
	@OperationParam(name = "periodStart")
	private final String myPeriodStart;
	@OperationParam(name = "periodEnd")
	private final String myPeriodEnd;
	@OperationParam(name = "subject")
	private final String mySubject;
	@OperationParam(name = "status")
	private final List<String> myStatus;
	@OperationParam(name = "measureId")
	private final List<String> myMeasureId;
	@OperationParam(name = "measureIdentifier")
	private final List<String> myMeasureIdentifier;
	@OperationParam(name = "measureUrl")
	private final List<CanonicalType> myMeasureUrl;
	@OperationParam(name = "nonDocument")
	private final BooleanType myNonDocument;

	public CareGapsParams(
			String myPeriodStart,
			String myPeriodEnd,
			String mySubject,
			List<String> myStatus,
			List<String> myMeasureId,
			List<String> myMeasureIdentifier,
			List<CanonicalType> myMeasureUrl,
			BooleanType myNonDocument) {
		this.myPeriodStart = myPeriodStart;
		this.myPeriodEnd = myPeriodEnd;
		this.mySubject = mySubject;
		this.myStatus = myStatus;
		this.myMeasureId = myMeasureId;
		this.myMeasureIdentifier = myMeasureIdentifier;
		this.myMeasureUrl = myMeasureUrl;
		this.myNonDocument = myNonDocument;
	}

	public String getPeriodStart() {
		return myPeriodStart;
	}

	public String getPeriodEnd() {
		return myPeriodEnd;
	}

	public String getSubject() {
		return mySubject;
	}

	public List<String> getStatus() {
		return myStatus;
	}

	public List<String> getMeasureId() {
		return myMeasureId;
	}

	public List<String> getMeasureIdentifier() {
		return myMeasureIdentifier;
	}

	public List<CanonicalType> getMeasureUrl() {
		return myMeasureUrl;
	}

	public BooleanType getNonDocument() {
		return myNonDocument;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", CareGapsParams.class.getSimpleName() + "[", "]")
			.add("myPeriodStart='" + myPeriodStart + "'")
			.add("myPeriodEnd='" + myPeriodEnd + "'")
			.add("mySubject='" + mySubject + "'")
			.add("myStatus=" + myStatus)
			.add("myMeasureId=" + myMeasureId)
			.add("myMeasureIdentifier=" + myMeasureIdentifier)
			.add("myMeasureUrl=" + myMeasureUrl)
			.add("myNonDocument=" + myNonDocument)
			.toString();
	}
}
