package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntityPk;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BinaryUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.cache.BasePackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaPackageCache extends BasePackageCacheManager implements IHapiPackageCacheManager {

	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private PlatformTransactionManager myTxManager;

	@Override
	protected NpmPackage loadPackageFromCacheOnly(String theId, @Nullable String theVersion) throws IOException {
		Validate.notBlank(theId, "theId must be populated");

		if (isNotBlank(theVersion)) {
			return myPackageVersionDao.findById(new NpmPackageVersionEntityPk(theId, theVersion)).map(t -> loadPackage(t)).orElse(null);
		}

		Optional<NpmPackageEntity> pkg = myPackageDao.findById(theId);
		if (pkg.isPresent()) {
			return loadPackage(theId, pkg.get().getCurrentVersionId());
		}

		return null;
	}

	private NpmPackage loadPackage(NpmPackageVersionEntity thePackageVersion) {
		IFhirResourceDao<? extends IBaseBinary> binaryDao = getBinaryDao();
		IBaseBinary binary = binaryDao.readByPid(new ResourcePersistentId(thePackageVersion.getPackageBinary().getId()));
		ByteArrayInputStream inputStream = new ByteArrayInputStream(binary.getContent());
		try {
			return NpmPackage.fromPackage(inputStream);
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}

	@Override
	public NpmPackage addPackageToCache(String thePackageId, String thePackageVersion, InputStream thePackageTgzInputStream, String theSourceDesc) throws IOException {
		Validate.notBlank(thePackageId, "thePackageId must not be null");
		Validate.notBlank(thePackageVersion, "thePackageVersion must not be null");
		Validate.notNull(thePackageTgzInputStream, "thePackageTgzInputStream must not be null");

		byte[] bytes = IOUtils.toByteArray(thePackageTgzInputStream);

		NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(bytes));

		IBaseBinary binary = BinaryUtil.newBinary(myCtx);
		BinaryUtil.setData(myCtx, binary, bytes, "application/gzip");

		newTxTemplate().executeWithoutResult(t -> {
			ResourceTable persistedPackage = (ResourceTable) getBinaryDao().create(binary).getEntity();
			NpmPackageEntity pkg = myPackageDao.findById(thePackageId).orElseGet(() -> createPackage(npmPackage));
			NpmPackageVersionEntityPk id = new NpmPackageVersionEntityPk(thePackageId, thePackageVersion);
			NpmPackageVersionEntity packageVersion = myPackageVersionDao.findById(id).orElseGet(() -> new NpmPackageVersionEntity());
			packageVersion.setId(id);
			packageVersion.setPackage(pkg);
			packageVersion.setPackageBinary(persistedPackage);
			packageVersion.setBytes(bytes.length);
			packageVersion.setSavedTime(new Date());
			packageVersion.setFhirVersion(npmPackage.fhirVersion());
			packageVersion.setDescription(npmPackage.description());
			myPackageVersionDao.save(packageVersion);

			NpmPackage.NpmPackageFolder packageFolder = npmPackage.getFolders().get("package");
			packageFolder.

			for (Map.Entry<String, NpmPackage.NpmPackageFolder> nextEntry : npmPackage.getFolders().entrySet()) {

				NpmPackage.NpmPackageFolder packageFolder = nextEntry.getValue();

				for (String nextType : npmPackage.get) {

				}

				for (String nextFile : packageFolder.listFiles()) {

					IBaseResource resource = null;
					try {
						if (nextFile.toLowerCase().endsWith(".xml")) {
							resource = tryToLoadResourceAsXml(packageFolder.fetchFile(nextFile));
						}
					} catch (IOException e) {
						throw new InternalErrorException(e);
					}

				}

			}

		});

		return npmPackage;
	}

	private IBaseResource tryToLoadResourceAsXml(byte[] theFetchFile) {
		String contents = new String(theFetchFile, StandardCharsets.UTF_8);
		return null;
	}

	private NpmPackageEntity createPackage(NpmPackage theNpmPackage) {
		NpmPackageEntity entity = new NpmPackageEntity();
		entity.setPackageId(theNpmPackage.id());
		entity.setCurrentVersionId(theNpmPackage.version());
		return myPackageDao.save(entity);
	}

	@Override
	public NpmPackage loadPackage(String thePackageId, String thePackageVersion) throws FHIRException, IOException {
		NpmPackage cachedPackage = loadPackageFromCacheOnly(thePackageId, thePackageVersion);
		if (cachedPackage != null) {
			return cachedPackage;
		}

		InputStreamWithSrc pkg = super.loadFromPackageServer(thePackageId, thePackageVersion);
		if (pkg == null) {
			throw new ResourceNotFoundException("Unable to locate package " + thePackageId + "#" + thePackageVersion);
		}

		try {
			return addPackageToCache(thePackageId, thePackageVersion == null ? pkg.version : thePackageVersion, pkg.stream, pkg.url);
		} finally {
			pkg.stream.close();
		}

	}

	private TransactionTemplate newTxTemplate() {
		return new TransactionTemplate(myTxManager);
	}

	@Override
	public NpmPackage loadPackage(NpmInstallationSpec theInstallationSpec) throws IOException {

		if (isNotBlank(theInstallationSpec.getPackageId()) && isNotBlank(theInstallationSpec.getPackageVersion())) {
			return loadPackage(theInstallationSpec.getPackageId(), theInstallationSpec.getPackageVersion());
		}

		throw new IllegalArgumentException("Invalid arguments");
	}
}
