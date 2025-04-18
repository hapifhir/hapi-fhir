/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.util.TransactionSemanticsHeader;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.LineIterator;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNullElseGet;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ConsumeFilesStepV2
		implements IJobStepWorker<BulkImportJobParameters, NdJsonFileJson, ConsumeFilesOutcomeJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(ConsumeFilesStepV2.class);

	@Autowired
	private FhirContext myCtx;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IFhirSystemDao mySystemDao;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkImportJobParameters, NdJsonFileJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ConsumeFilesOutcomeJson> theDataSink) {

		RequestPartitionId partitionId = theStepExecutionDetails.getParameters().getPartitionId();
		String ndjson = theStepExecutionDetails.getData().getNdJsonText();
		String sourceName = theStepExecutionDetails.getData().getSourceName();

		IParser jsonParser = myCtx.newJsonParser();
		LineIterator lineIter = new LineIterator(new StringReader(ndjson));
		List<IBaseResource> resources = new ArrayList<>();
		while (lineIter.hasNext()) {
			String next = lineIter.next();
			if (isNotBlank(next)) {
				IBaseResource parsed;
				try {
					parsed = jsonParser.parseResource(next);
				} catch (DataFormatException e) {
					throw new JobExecutionFailedException(Msg.code(2052) + "Failed to parse resource: " + e, e);
				}
				resources.add(parsed);
			}
		}

		ourLog.info("Bulk loading {} resources from source {}", resources.size(), sourceName);

		TransactionUtil.TransactionResponse response = storeResources(resources, partitionId);

		ConsumeFilesOutcomeJson outcome = new ConsumeFilesOutcomeJson();
		outcome.setSourceName(sourceName);
		for (TransactionUtil.StorageOutcome entry : response.getStorageOutcomes()) {
			if (entry.getStorageResponseCode() != null) {
				outcome.addOutcome(entry.getStorageResponseCode());
			}
			if (isNotBlank(entry.getErrorMessage())) {
				outcome.addError(entry.getErrorMessage());
			}
		}
		theDataSink.accept(outcome);

		return new RunOutcome(resources.size());
	}

	public TransactionUtil.TransactionResponse storeResources(
			List<IBaseResource> resources, RequestPartitionId thePartitionId) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(
				requireNonNullElseGet(thePartitionId, RequestPartitionId::defaultPartition));

		TransactionSemanticsHeader transactionSemantics = TransactionSemanticsHeader.newBuilder()
				.withTryBatchAsTransactionFirst(true)
				.withRetryCount(3)
				.withMinRetryDelay(500)
				.withMaxRetryDelay(1000)
				.build();
		requestDetails.addHeader(TransactionSemanticsHeader.HEADER_NAME, transactionSemantics.toHeaderValue());

		BundleBuilder bb = new BundleBuilder(myCtx);
		bb.setType("batch");

		for (var resource : resources) {
			if (resource.getIdElement().hasIdPart()) {
				bb.addTransactionUpdateEntry(resource);
			} else {
				bb.addTransactionCreateEntry(resource);
			}
		}

		IBaseBundle requestBundle = bb.getBundleTyped();
		IBaseBundle responseBundle = (IBaseBundle) mySystemDao.transaction(requestDetails, requestBundle);

		return TransactionUtil.parseTransactionResponse(myCtx, requestBundle, responseBundle);
	}
}
