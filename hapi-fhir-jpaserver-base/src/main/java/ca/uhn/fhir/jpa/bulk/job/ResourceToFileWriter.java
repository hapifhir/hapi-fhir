package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
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
	private FhirContext myContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	private ByteArrayOutputStream myOutputStream;
	private OutputStreamWriter myWriter;
	private IParser myParser;

	@Value("#{stepExecutionContext['bulkExportCollectionEntityId']}")
	private Long myBulkExportCollectionEntityId;

	private IFhirResourceDao<IBaseBinary> myBinaryDao;


	public ResourceToFileWriter() {
		myOutputStream = new ByteArrayOutputStream();
		myWriter = new OutputStreamWriter(myOutputStream, Constants.CHARSET_UTF8);
	}

	@PostConstruct
	public void start() {
		myParser = myContext.newJsonParser().setPrettyPrint(false);
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
		IBaseBinary binary = BinaryUtil.newBinary(myContext);
		binary.setContentType(Constants.CT_FHIR_NDJSON);
		binary.setContent(myOutputStream.toByteArray());

		return myBinaryDao.create(binary).getResource().getIdElement();
	}


	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}

	@Override
	public void write(List<? extends List<IBaseResource>> theList) throws Exception {

		for (List<IBaseResource> resourceList : theList) {
			for (IBaseResource nextFileResource : resourceList) {
				myParser.encodeResourceToWriter(nextFileResource, myWriter);
				myWriter.append("\n");
			}
		}

		Optional<IIdType> createdId = flushToFiles();
		createdId.ifPresent(theIIdType -> ourLog.warn("Created resources for bulk export file containing {} resources of type ", theIIdType.toUnqualifiedVersionless().getValue()));
	}
}
