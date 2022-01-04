package ca.uhn.fhir.jpa.bulk.export.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportDaoSvc;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

public class ResourceToFileWriter extends BaseResourceToFileWriter {
	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	public ResourceToFileWriter(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		super(theFhirContext, theDaoRegistry);
	}

	@PostConstruct
	public void start() {
		myBinaryDao = getBinaryDao();
	}

	@Override
	protected Optional<IIdType> flushToFiles() {
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

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}
}
