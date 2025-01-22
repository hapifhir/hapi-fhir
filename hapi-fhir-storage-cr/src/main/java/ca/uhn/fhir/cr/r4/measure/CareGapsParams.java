package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Non-RequestDetails parameters for the <a href=
 * "http://build.fhir.org/ig/HL7/davinci-deqm/OperationDefinition-care-gaps.html">$care-gaps</a>
 * operation found in the
 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
 * FHIR Implementation Guide</a> that overrides the <a href=
 * "http://build.fhir.org/operation-measure-care-gaps.html">$care-gaps</a>
 * operation found in the
 * <a href="http://hl7.org/fhir/R4/clinicalreasoning-module.html">FHIR Clinical
 * Reasoning Module</a>.
 * <p/>
 * The operation calculates measures describing gaps in care. For more details,
 * reference the <a href=
 * "http://build.fhir.org/ig/HL7/davinci-deqm/gaps-in-care-reporting.html">Gaps
 * in Care Reporting</a> section of the
 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
 * FHIR Implementation Guide</a>.
 * <p/>
 * A Parameters resource that includes zero to many document bundles that
 * include Care Gap Measure Reports will be returned.
 * <p/>
 * Usage:
 * URL: [base]/Measure/$care-gaps
 * <p/>
 * myRequestDetails generally auto-populated by the HAPI server
 *                          framework.
 * myPeriodStart       the start of the gaps through period
 * myPeriodEnd         the end of the gaps through period
 * mySubject           a reference to either a Patient or Group for which
 *                          the gaps in care report(s) will be generated
 * myStatus            the status code of gaps in care reports that will be
 *                          included in the result
 * myMeasureId         the id of Measure(s) for which the gaps in care
 *                          report(s) will be calculated
 * myMeasureIdentifier the identifier of Measure(s) for which the gaps in
 *                          care report(s) will be calculated
 * myMeasureUrl        the canonical URL of Measure(s) for which the gaps
 *                          in care report(s) will be calculated
 * myNonDocument    defaults to 'false' which returns standard 'document' bundle for `$care-gaps`.
 *                  If 'true', this will return summarized subject bundle with only detectedIssue resource.
 */
public class CareGapsParams {
	@OperationEmbeddedParam(name = "periodStart")
	private final String myPeriodStart;

	@OperationEmbeddedParam(name = "periodEnd")
	private final String myPeriodEnd;

	@OperationEmbeddedParam(name = "subject")
	private final String mySubject;

	@OperationEmbeddedParam(name = "status")
	private final List<String> myStatus;

	@OperationEmbeddedParam(name = "measureId")
	private final List<String> myMeasureId;

	@OperationEmbeddedParam(name = "measureIdentifier")
	private final List<String> myMeasureIdentifier;

	@OperationEmbeddedParam(name = "measureUrl")
	private final List<CanonicalType> myMeasureUrl;

	@OperationEmbeddedParam(name = "nonDocument")
	private final BooleanType myNonDocument;

	public CareGapsParams(
			String thePeriodStart,
			String thePeriodEnd,
			String theSubject,
			List<String> theStatus,
			List<String> theMeasureId,
			List<String> theMeasureIdentifier,
			List<CanonicalType> theMeasureUrl,
			BooleanType theNonDocument) {
		myPeriodStart = thePeriodStart;
		myPeriodEnd = thePeriodEnd;
		mySubject = theSubject;
		myStatus = theStatus;
		myMeasureId = theMeasureId;
		myMeasureIdentifier = theMeasureIdentifier;
		myMeasureUrl = theMeasureUrl;
		myNonDocument = theNonDocument;
	}

	private CareGapsParams(Builder builder) {
		this.myPeriodStart = builder.myPeriodStart;
		this.myPeriodEnd = builder.myPeriodEnd;
		this.mySubject = builder.mySubject;
		this.myStatus = builder.myStatus;
		this.myMeasureId = builder.myMeasureId;
		this.myMeasureIdentifier = builder.myMeasureIdentifier;
		this.myMeasureUrl = builder.myMeasureUrl;
		this.myNonDocument = builder.myNonDocument;
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
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		CareGapsParams that = (CareGapsParams) o;
		return Objects.equals(myPeriodStart, that.myPeriodStart)
				&& Objects.equals(myPeriodEnd, that.myPeriodEnd)
				&& Objects.equals(mySubject, that.mySubject)
				&& Objects.equals(myStatus, that.myStatus)
				&& Objects.equals(myMeasureId, that.myMeasureId)
				&& Objects.equals(myMeasureIdentifier, that.myMeasureIdentifier)
				&& Objects.equals(myMeasureUrl, that.myMeasureUrl)
				&& Objects.equals(myNonDocument, that.myNonDocument);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				myPeriodStart,
				myPeriodEnd,
				mySubject,
				myStatus,
				myMeasureId,
				myMeasureIdentifier,
				myMeasureUrl,
				myNonDocument);
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String myPeriodStart;
		private String myPeriodEnd;
		private String mySubject;
		private List<String> myStatus;
		private List<String> myMeasureId;
		private List<String> myMeasureIdentifier;
		private List<CanonicalType> myMeasureUrl;
		private BooleanType myNonDocument;

		public Builder setPeriodStart(String myPeriodStart) {
			this.myPeriodStart = myPeriodStart;
			return this;
		}

		public Builder setPeriodEnd(String myPeriodEnd) {
			this.myPeriodEnd = myPeriodEnd;
			return this;
		}

		public Builder setSubject(String mySubject) {
			this.mySubject = mySubject;
			return this;
		}

		public Builder setStatus(List<String> myStatus) {
			this.myStatus = myStatus;
			return this;
		}

		public Builder setMeasureId(List<String> myMeasureId) {
			this.myMeasureId = myMeasureId;
			return this;
		}

		public Builder setMeasureIdentifier(List<String> myMeasureIdentifier) {
			this.myMeasureIdentifier = myMeasureIdentifier;
			return this;
		}

		public Builder setMeasureUrl(List<CanonicalType> myMeasureUrl) {
			this.myMeasureUrl = myMeasureUrl;
			return this;
		}

		public Builder setNonDocument(BooleanType myNonDocument) {
			this.myNonDocument = myNonDocument;
			return this;
		}

		public CareGapsParams build() {
			return new CareGapsParams(this);
		}
	}
}
