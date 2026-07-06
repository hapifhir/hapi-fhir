/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <ST> The type for the keys on the keyed statistic accumulator. Different job types will use different types of keys for this.
 */
public abstract class BaseFinalizeStep<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson, ST>
		implements IReductionStepWorker<PT, IT, OT> {

	private final TerminologyFileSetJson.RecordsAddedCounter myTotalRecordsAddedCounter =
			new TerminologyFileSetJson.RecordsAddedCounter();
	private final Map<ST, TerminologyFileSetJson.RecordsAddedCounter> myStepToAccumulator = new HashMap<>();

	protected void accumulateStatistics(ST theStep, TerminologyFileSetJson.RecordsAddedCounter theRecordsAddedCounter) {
		myTotalRecordsAddedCounter.addFrom(theRecordsAddedCounter);
		myStepToAccumulator
				.computeIfAbsent(theStep, s -> new TerminologyFileSetJson.RecordsAddedCounter())
				.addFrom(theRecordsAddedCounter);
	}

	protected Map<ST, TerminologyFileSetJson.RecordsAddedCounter> getStepToAccumulator() {
		return myStepToAccumulator;
	}

	protected String createReport(StepExecutionDetails<PT, IT> theStepExecutionDetails) {
		StringBuilder reportBuilder = new StringBuilder();

		for (String titleLine : getReportTitleLines(theStepExecutionDetails)) {
			reportBuilder.append(titleLine);
			reportBuilder.append("\n");
		}
		addDivider(reportBuilder);

		appendCounts(myTotalRecordsAddedCounter, reportBuilder, 0);

		appendAdditionalInfo(theStepExecutionDetails, reportBuilder);

		return reportBuilder.toString();
	}

	@Nonnull
	protected abstract List<String> getReportTitleLines(StepExecutionDetails<PT, IT> theStepExecutionDetails);

	protected abstract void appendAdditionalInfo(
			StepExecutionDetails<PT, IT> theStepExecutionDetails, StringBuilder theReportBuilder);

	protected void addDivider(StringBuilder theReportBuilder) {
		theReportBuilder.append("---------------------------------------------------\n");
	}

	protected void appendCounts(
			TerminologyFileSetJson.RecordsAddedCounter theCounter, StringBuilder theReportBuilder, int theIndent) {
		boolean hasAny = false;
		if (theCounter.getConceptsAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concepts Added               : ")
					.append(theCounter.getConceptsAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getConceptsUpdated() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concepts Updated             : ")
					.append(theCounter.getConceptsUpdated())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getConceptLinksAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concepts Links Added         : ")
					.append(theCounter.getConceptLinksAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getDesignationsAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concept Designations Added   : ")
					.append(theCounter.getDesignationsAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getPropertiesAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concept Properties Added     : ")
					.append(theCounter.getPropertiesAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getConceptsRemoved() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concepts Removed             : ")
					.append(theCounter.getConceptsRemoved())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getConceptLinksRemoved() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concepts Links Removed       : ")
					.append(theCounter.getConceptLinksRemoved())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getDesignationsRemoved() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concept Designations Removed : ")
					.append(theCounter.getDesignationsRemoved())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getPropertiesRemoved() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Concept Properties Removed   : ")
					.append(theCounter.getPropertiesRemoved())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getConceptMapsAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("ConceptMaps Added            : ")
					.append(theCounter.getConceptMapsAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getConceptMapMappingsAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("ConceptMap Mappings Added    : ")
					.append(theCounter.getConceptMapMappingsAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getValueSetsAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("ValueSets Added              : ")
					.append(theCounter.getValueSetsAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getValueSetInclusionsAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("ValueSets Inclusions Added   : ")
					.append(theCounter.getValueSetInclusionsAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getValueSetCodesAdded() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("ValueSets Codes Added        : ")
					.append(theCounter.getValueSetCodesAdded())
					.append("\n");
			hasAny = true;
		}
		if (theCounter.getOtherChanges() > 0) {
			indent(theReportBuilder, theIndent);
			theReportBuilder
					.append("Other Changes Count          : ")
					.append(theCounter.getOtherChanges())
					.append("\n");
			hasAny = true;
		}
		if (!hasAny) {
			indent(theReportBuilder, theIndent);
			appendNoChangesMessage(theReportBuilder);
		}
	}

	protected void appendNoChangesMessage(StringBuilder theReportBuilder) {
		theReportBuilder.append("Nothing changed\n");
	}

	/**
	 * @param theIndent The number of times to indent (Increment by 1)
	 */
	protected void indent(StringBuilder theReportBuilder, int theIndent) {
		theReportBuilder.append(" ".repeat(Math.max(0, theIndent * 3)));
	}
}
