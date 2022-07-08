package ca.uhn.fhir.jpa.binstore;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.binary.svc.BaseBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.dao.data.IBinaryStorageEntityDao;
import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.hash.HashingInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@Transactional
public class DatabaseBlobBinaryStorageSvcImpl extends BaseBinaryStorageSvcImpl {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired
	private IBinaryStorageEntityDao myBinaryStorageEntityDao;
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private DaoConfig myDaoConfig;

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public StoredDetails storeBlob(IIdType theResourceId, String theBlobIdOrNull, String theContentType, InputStream theInputStream) throws IOException {

		/*
		 * Note on transactionality: This method used to have a propagation value of SUPPORTS and then do the actual
		 * write in a new transaction.. I don't actually get why that was the original design, but it causes
		 * connection pool deadlocks under load!
		 */

		Date publishedDate = new Date();

		HashingInputStream hashingInputStream = createHashingInputStream(theInputStream);
		CountingInputStream countingInputStream = createCountingInputStream(hashingInputStream);

		String id = super.provideIdForNewBlob(theBlobIdOrNull);

		BinaryStorageEntity entity = new BinaryStorageEntity();
		entity.setResourceId(theResourceId.toUnqualifiedVersionless().getValue());
		entity.setBlobId(id);
		entity.setBlobContentType(theContentType);
		entity.setPublished(publishedDate);

		Session session = (Session) myEntityManager.getDelegate();
		LobHelper lobHelper = session.getLobHelper();
		byte[] loadedStream = IOUtils.toByteArray(countingInputStream);
		Blob dataBlob = lobHelper.createBlob(loadedStream);
		entity.setBlob(dataBlob);

		// Update the entity with the final byte count and hash
		long bytes = countingInputStream.getCount();
		String hash = hashingInputStream.hash().toString();
		entity.setSize((int) bytes);
		entity.setHash(hash);

		// Save the entity
		myEntityManager.persist(entity);

		return new StoredDetails()
			.setBlobId(id)
			.setBytes(bytes)
			.setPublished(publishedDate)
			.setHash(hash)
			.setContentType(theContentType);
	}

	@Override
	public StoredDetails fetchBlobDetails(IIdType theResourceId, String theBlobId) {

		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(theBlobId, theResourceId.toUnqualifiedVersionless().getValue());
		if (entityOpt.isPresent() == false) {
			return null;
		}

		BinaryStorageEntity entity = entityOpt.get();
		return new StoredDetails()
			.setBlobId(theBlobId)
			.setContentType(entity.getBlobContentType())
			.setHash(entity.getHash())
			.setPublished(entity.getPublished())
			.setBytes(entity.getSize());
	}

	@Override
	public boolean writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) throws IOException {
		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(theBlobId, theResourceId.toUnqualifiedVersionless().getValue());
		if (entityOpt.isPresent() == false) {
			return false;
		}

		copyBlobToOutputStream(theOutputStream, entityOpt.get());

		return true;
	}

	@Override
	public void expungeBlob(IIdType theResourceId, String theBlobId) {
		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(theBlobId, theResourceId.toUnqualifiedVersionless().getValue());
		entityOpt.ifPresent(theBinaryStorageEntity -> myBinaryStorageEntityDao.deleteByPid(theBinaryStorageEntity.getBlobId()));
	}

	@Override
	public byte[] fetchBlob(IIdType theResourceId, String theBlobId) throws IOException {
		BinaryStorageEntity entityOpt = myBinaryStorageEntityDao
			.findByIdAndResourceId(theBlobId, theResourceId.toUnqualifiedVersionless().getValue())
			.orElseThrow(() -> new ResourceNotFoundException("Unknown blob ID: " + theBlobId + " for resource ID " + theResourceId));

		return copyBlobToByteArray(entityOpt);
	}

	void copyBlobToOutputStream(OutputStream theOutputStream, BinaryStorageEntity theEntity) throws IOException {
		try (InputStream inputStream = theEntity.getBlob().getBinaryStream()) {
			IOUtils.copy(inputStream, theOutputStream);
		} catch (SQLException e) {
			throw new IOException(Msg.code(1341) + e);
		}
	}

	byte[] copyBlobToByteArray(BinaryStorageEntity theEntity) throws IOException {
		int size = theEntity.getSize();
		try {
			return IOUtils.toByteArray(theEntity.getBlob().getBinaryStream(), size);
		} catch (SQLException e) {
			throw new IOException(Msg.code(1342) + e);
		}
	}
}
