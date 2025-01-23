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

import ca.uhn.fhir.rest.annotation.EmbeddedParameterRangeType;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;

import java.time.ZonedDateTime;
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
	@OperationEmbeddedParam(
			name = "periodStart",
			sourceType = String.class,
			rangeType = EmbeddedParameterRangeType.START)
	private final ZonedDateTime myPeriodStart;

	@OperationEmbeddedParam(name = "periodEnd", sourceType = String.class, rangeType = EmbeddedParameterRangeType.END)
	private final ZonedDateTime myPeriodEnd;

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
			ZonedDateTime thePeriodStart,
			ZonedDateTime thePeriodEnd,
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
		myPeriodStart = builder.myPeriodStart;
		myPeriodEnd = builder.myPeriodEnd;
		mySubject = builder.mySubject;
		myStatus = builder.myStatus;
		myMeasureId = builder.myMeasureId;
		myMeasureIdentifier = builder.myMeasureIdentifier;
		myMeasureUrl = builder.myMeasureUrl;
		myNonDocument = builder.myNonDocument;
	}

	public ZonedDateTime getPeriodStart() {
		return myPeriodStart;
	}

	public ZonedDateTime getPeriodEnd() {
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
		private ZonedDateTime myPeriodStart;
		private ZonedDateTime myPeriodEnd;
		private String mySubject;
		private List<String> myStatus;
		private List<String> myMeasureId;
		private List<String> myMeasureIdentifier;
		private List<CanonicalType> myMeasureUrl;
		private BooleanType myNonDocument;

		public Builder setPeriodStart(ZonedDateTime thePeriodStart) {
			myPeriodStart = thePeriodStart;
			return this;
		}

		public Builder setPeriodEnd(ZonedDateTime thePeriodEnd) {
			myPeriodEnd = thePeriodEnd;
			return this;
		}

		public Builder setSubject(String theSubject) {
			mySubject = theSubject;
			return this;
		}

		public Builder setStatus(List<String> theStatus) {
			myStatus = theStatus;
			return this;
		}

		public Builder setMeasureId(List<String> theMeasureId) {
			myMeasureId = theMeasureId;
			return this;
		}

		public Builder setMeasureIdentifier(List<String> theMeasureIdentifier) {
			myMeasureIdentifier = theMeasureIdentifier;
			return this;
		}

		public Builder setMeasureUrl(List<CanonicalType> theMeasureUrl) {
			myMeasureUrl = theMeasureUrl;
			return this;
		}

		public Builder setNonDocument(BooleanType theNonDocument) {
			myNonDocument = theNonDocument;
			return this;
		}

		public CareGapsParams build() {
			return new CareGapsParams(this);
		}
	}
}
