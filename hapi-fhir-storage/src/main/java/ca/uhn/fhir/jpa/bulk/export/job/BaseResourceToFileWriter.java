package ca.uhn.fhir.jpa.bulk.export.job;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BinaryUtil;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Optional;

public abstract class BaseResourceToFileWriter implements ItemWriter<List<IBaseResource>> {
	protected static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	protected FhirContext myFhirContext;

	protected DaoRegistry myDaoRegistry;

	protected ByteArrayOutputStream myOutputStream;

	@Value("#{stepExecutionContext['" + BatchConstants.JOB_COLLECTION_ENTITY_ID + "']}")
	protected Long myBulkExportCollectionEntityId;

	@Value("#{stepExecutionContext['" + BatchConstants.JOB_EXECUTION_RESOURCE_TYPE + "']}")
	protected String myResourceType;

	protected IFhirResourceDao myBinaryDao;
	private final OutputStreamWriter myWriter;
	private final IParser myParser;

	protected BaseResourceToFileWriter(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myParser = myFhirContext.newJsonParser().setPrettyPrint(false);
		myOutputStream = new ByteArrayOutputStream();
		myWriter = new OutputStreamWriter(myOutputStream, Constants.CHARSET_UTF8);
	}


	protected IIdType createBinaryFromOutputStream() {
		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);
		binary.setContentType(Constants.CT_FHIR_NDJSON);
		binary.setContent(myOutputStream.toByteArray());
		DaoMethodOutcome outcome = myBinaryDao.create(binary, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
		return outcome.getResource().getIdElement();
	}

	@Override
	public void write(List<? extends List<IBaseResource>> theList) throws Exception {

		int count = 0;
		for (List<IBaseResource> resourceList : theList) {
			for (IBaseResource nextFileResource : resourceList) {
				myParser.encodeResourceToWriter(nextFileResource, myWriter);
				myWriter.append("\n");
				count++;
			}
		}

		Optional<IIdType> createdId = flushToFiles();
		if (createdId.isPresent()) {
			ourLog.info("Created {} resources for bulk export file containing {} resources of type {} ", count, createdId.get().toUnqualifiedVersionless().getValue(), myResourceType);
		}
	}

	protected abstract Optional<IIdType> flushToFiles();

}
