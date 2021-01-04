package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.bulk.svc.BulkExportDaoSvc;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BinaryUtil;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Optional;

public class ResourceToFileWriter implements ItemWriter<List<IBaseResource>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	private ByteArrayOutputStream myOutputStream;
	private OutputStreamWriter myWriter;
	private IParser myParser;

	@Value("#{stepExecutionContext['bulkExportCollectionEntityId']}")
	private Long myBulkExportCollectionEntityId;

	@Value("#{stepExecutionContext['resourceType']}")
	private String myReosurceType;

	private IFhirResourceDao<IBaseBinary> myBinaryDao;


	public ResourceToFileWriter() {
		myOutputStream = new ByteArrayOutputStream();
		myWriter = new OutputStreamWriter(myOutputStream, Constants.CHARSET_UTF8);
	}

	@PostConstruct
	public void start() {
		myParser = myFhirContext.newJsonParser().setPrettyPrint(false);
		myBinaryDao = getBinaryDao();
	}

	private Optional<IIdType> flushToFiles() {
		if (myOutputStream.size() > 0) {
			IIdType createdId = createBinaryFromOutputStream();
			BulkExportCollectionFileEntity file = new BulkExportCollectionFileEntity();
			file.setResource(createdId.getIdPart());

			myBulkExportDaoSvc.addFileToCollectionWithId(myBulkExportCollectionEntityId, file);

			myOutputStream.reset();

			return Optional.of(createdId);
		}

		return Optional.empty();
	}

	private IIdType createBinaryFromOutputStream() {
		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);
		binary.setContentType(Constants.CT_FHIR_NDJSON);
		binary.setContent(myOutputStream.toByteArray());

		DaoMethodOutcome outcome = myBinaryDao.create(binary);
		return outcome.getResource().getIdElement();
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
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
			ourLog.info("Created {} resources for bulk export file containing {} resources of type {} ", count, createdId.get().toUnqualifiedVersionless().getValue(), myReosurceType);
		}
	}
}
