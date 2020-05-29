package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BinaryUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.cache.BasePackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaPackageCache extends BasePackageCacheManager implements IHapiPackageCacheManager {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaPackageCache.class);
	private final Map<FhirVersionEnum, FhirContext> myVersionToContext = Collections.synchronizedMap(new HashMap<>());
	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private INpmPackageVersionResourceDao myPackageVersionResourceDao;
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
			return myPackageVersionDao.findByPackageIdAndVersion(theId, theVersion).map(t -> loadPackage(t)).orElse(null);
		}

		Optional<NpmPackageEntity> pkg = myPackageDao.findByPackageId(theId);
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
		if (!npmPackage.id().equals(thePackageId)) {
			throw new InvalidRequestException("Package ID " + npmPackage.id() + " doesn't match expected: " + thePackageId);
		}
		if (!npmPackage.version().equals(thePackageVersion)) {
			throw new InvalidRequestException("Package ID " + npmPackage.version() + " doesn't match expected: " + thePackageVersion);
		}

		FhirVersionEnum fhirVersion = FhirVersionEnum.forVersionString(npmPackage.fhirVersion());
		if (fhirVersion == null) {
			throw new InvalidRequestException("Unknown FHIR version: " + npmPackage.fhirVersion());
		}
		FhirContext packageContext = getFhirContext(fhirVersion);

		IBaseBinary binary = createPackageBinary(bytes);

		newTxTemplate().executeWithoutResult(tx -> {
			ResourceTable persistedPackage = (ResourceTable) getBinaryDao().create(binary).getEntity();
			NpmPackageEntity pkg = myPackageDao.findByPackageId(thePackageId).orElseGet(() -> createPackage(npmPackage));
			NpmPackageVersionEntity packageVersion = myPackageVersionDao.findByPackageIdAndVersion(thePackageId, thePackageVersion).orElse(null);
			if (packageVersion != null) {
				ourLog.info("Package version already exists, no action taken: {}#{}", thePackageId, thePackageVersion);
				return;
			}

			packageVersion = new NpmPackageVersionEntity();
			packageVersion.setPackageId(thePackageId);
			packageVersion.setVersionId(thePackageVersion);
			packageVersion.setPackage(pkg);
			packageVersion.setPackageBinary(persistedPackage);
			packageVersion.setSavedTime(new Date());
			packageVersion.setDescription(npmPackage.description());
			packageVersion.setFhirVersionId(npmPackage.fhirVersion());
			packageVersion.setFhirVersionName(fhirVersion);
			packageVersion.setCurrentVersion(true);
			packageVersion.setPackageSizeBytes(bytes.length);
			packageVersion = myPackageVersionDao.save(packageVersion);

			String dirName = "package";
			NpmPackage.NpmPackageFolder packageFolder = npmPackage.getFolders().get(dirName);
			for (Map.Entry<String, List<String>> nextTypeToFiles : packageFolder.getTypes().entrySet()) {
				String nextType = nextTypeToFiles.getKey();
				for (String nextFile : nextTypeToFiles.getValue()) {

					byte[] contents;
					String contentsString;
					try {
						contents = packageFolder.fetchFile(nextFile);
						contentsString = new String(contents, Charsets.UTF_8);
					} catch (IOException e) {
						throw new InternalErrorException(e);
					}

					String contentType;
					IBaseResource resource;
					if (nextFile.toLowerCase().endsWith(".xml")) {
						contentType = Constants.CT_FHIR_XML_NEW;
						resource = packageContext.newXmlParser().parseResource(contentsString);
					} else if (nextFile.toLowerCase().endsWith(".json")) {
						contentType = Constants.CT_FHIR_JSON_NEW;
						resource = packageContext.newJsonParser().parseResource(contentsString);
					} else {
						return;
					}

					IBaseBinary resourceBinary = createPackageResourceBinary(nextFile, contents, contentType);
					ResourceTable persistedResource = (ResourceTable) getBinaryDao().create(resourceBinary).getEntity();

					NpmPackageVersionResourceEntity resourceEntity = new NpmPackageVersionResourceEntity();
					resourceEntity.setPackageVersion(packageVersion);
					resourceEntity.setResourceBinary(persistedResource);
					resourceEntity.setDirectory(dirName);
					resourceEntity.setFhirVersionId(npmPackage.fhirVersion());
					resourceEntity.setFhirVersionName(fhirVersion);
					resourceEntity.setFilename(nextFile);
					resourceEntity.setResourceType(nextType);
					resourceEntity.setResSizeBytes(contents.length);
					BaseRuntimeChildDefinition urlChild = packageContext.getResourceDefinition(nextType).getChildByName("url");
					BaseRuntimeChildDefinition versionChild = packageContext.getResourceDefinition(nextType).getChildByName("version");
					String url = null;
					String version = null;
					if (urlChild != null) {
						url = urlChild.getAccessor().getFirstValueOrNull(resource).map(t -> ((IPrimitiveType<?>) t).getValueAsString()).orElse(null);
						resourceEntity.setCanonicalUrl(url);
						version = versionChild.getAccessor().getFirstValueOrNull(resource).map(t -> ((IPrimitiveType<?>) t).getValueAsString()).orElse(null);
						resourceEntity.setCanonicalVersion(version);
					}
					myPackageVersionResourceDao.save(resourceEntity);

					ourLog.info("Package[{}#{}] Installing Resource[{}] with URL: {}|{}", thePackageId, thePackageVersion, dirName + '/' + nextFile, defaultString(url), defaultString(version));
				}
			}

		});

		return npmPackage;
	}

	@Nonnull
	public FhirContext getFhirContext(FhirVersionEnum theFhirVersion) {
		return myVersionToContext.computeIfAbsent(theFhirVersion, v -> new FhirContext(v));
	}

	private IBaseBinary createPackageBinary(byte[] theBytes) {
		IBaseBinary binary = BinaryUtil.newBinary(myCtx);
		BinaryUtil.setData(myCtx, binary, theBytes, "application/gzip");
		return binary;
	}

	private IBaseBinary createPackageResourceBinary(String theFileName, byte[] theBytes, String theContentType) {
		IBaseBinary binary = BinaryUtil.newBinary(myCtx);
		BinaryUtil.setData(myCtx, binary, theBytes, theContentType);
		return binary;
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

	@Override
	@Transactional
	public IBaseResource loadPackageAssetByUrl(FhirVersionEnum theFhirVersion, String theCanonicalUrl) {

		String canonicalUrl = theCanonicalUrl;

		int versionSeparator = canonicalUrl.lastIndexOf('|');
		Slice<NpmPackageVersionResourceEntity> slice;
		if (versionSeparator != -1) {
			String canonicalVersion = canonicalUrl.substring(versionSeparator+1);
			canonicalUrl = canonicalUrl.substring(0, versionSeparator);
			slice = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrlAndVersion(PageRequest.of(0, 1), theFhirVersion, canonicalUrl, canonicalVersion);
		} else {
			slice = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrl(PageRequest.of(0, 1), theFhirVersion, canonicalUrl);
		}

		if (slice.isEmpty()) {
			return null;
		} else {
			NpmPackageVersionResourceEntity contents = slice.getContent().get(0);
			ResourcePersistentId binaryPid = new ResourcePersistentId(contents.getResourceBinary().getId());
			IBaseBinary binary = getBinaryDao().readByPid(binaryPid);
			byte[] resourceContentsBytes = BinaryUtil.getOrCreateData(myCtx, binary).getValue();
			String resourceContents = new String(resourceContentsBytes, StandardCharsets.UTF_8);

			FhirContext packageContext = getFhirContext(contents.getFhirVersionName());
			return EncodingEnum.detectEncoding(resourceContents).newParser(packageContext).parseResource(resourceContents);
		}
	}
}
