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

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ITerminologyImportFileHandlerStep<
				PT extends ImportTerminologyJobParameters, IT extends IModelJson, OT extends IModelJson>
		extends IJobStepWorker<PT, IT, OT> {

	@Nonnull
	List<BaseImportTerminologyFileCsvStep.LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<PT, ?> theStepExecutionDetails);

	/**
	 * Returns true if it should be considered an error for this step to not match any files in the
	 * distribution.
	 */
	boolean mustFindFile();

	enum FileHandlingType {
		/**
		 * Comma-Separated Values: Split into chunks of 1000 lines each, with
		 * the header row repeated on each.
		 */
		CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,

		/**
		 * Comma-Separated Values: Split into chunks of 50000 lines each, with
		 * the header row repeated on each.
		 */
		CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,

		/**
		 * Tab-Separated Values: Split into chunks of 50000 lines each, with
		 * the header row repeated on each.
		 */
		TSV_SPLIT_WITH_REPEAT_HEADER_5000_LINE_CHUNKS,

		/**
		 * XML document: Pass the entire file unmodified to the processor step.
		 */
		XML
	}

	// TODO JA: Rename this in the next PR since it's used everywhere now
	record LoincFileNameSpecification(
			FileHandlingType fileHandlingType,
			LoincUploadPropertiesEnum propertyName,
			List<LoincUploadPropertiesEnum> defaultValues,
			Predicate<String> fileNameTester) {

		public LoincFileNameSpecification(
				FileHandlingType theFileHandlingType,
				LoincUploadPropertiesEnum thePropertyName,
				LoincUploadPropertiesEnum... theDefaultValue) {
			this(theFileHandlingType, thePropertyName, Arrays.asList(theDefaultValue), null);
		}

		public LoincFileNameSpecification(FileHandlingType theFileHandlingType, Predicate<String> theFileNameTester) {
			this(theFileHandlingType, null, null, theFileNameTester);
		}

		public boolean matchFileName(Properties theJobProperties, String theFileName) {
			boolean matches = false;
			if (propertyName() != null) {
				String propertyName = propertyName().getCode();
				String fileName = theJobProperties.getProperty(propertyName, null);
				if (isNotBlank(fileName)) {
					matches = theFileName.endsWith(fileName);
				} else {
					for (LoincUploadPropertiesEnum nextDefault : defaultValues()) {
						matches |= theFileName.endsWith(nextDefault.getCode());
					}
				}
			} else if (this.fileNameTester() != null) {
				matches = this.fileNameTester().test(theFileName);
			}
			return matches;
		}
	}
}
