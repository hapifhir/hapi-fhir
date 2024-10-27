/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.binary.svc.BaseBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.dao.data.IBinaryStorageEntityDao;
import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteStreams;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@Transactional
public class DatabaseBinaryContentStorageSvcImpl extends BaseBinaryStorageSvcImpl {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Autowired
	private IBinaryStorageEntityDao myBinaryStorageEntityDao;

	private boolean mySupportLegacyLobServer = false;

	@Nonnull
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public StoredDetails storeBinaryContent(
			IIdType theResourceId,
			String theBinaryContentIdOrNull,
			String theContentType,
			InputStream theInputStream,
			RequestDetails theRequestDetails)
			throws IOException {

		/*
		 * Note on transactionality: This method used to have a propagation value of SUPPORTS and then do the actual
		 * write in a new transaction.. I don't actually get why that was the original design, but it causes
		 * connection pool deadlocks under load!
		 */

		Date publishedDate = new Date();

		HashingInputStream hashingInputStream = createHashingInputStream(theInputStream);
		CountingInputStream countingInputStream = createCountingInputStream(hashingInputStream);

		BinaryStorageEntity entity = new BinaryStorageEntity();
		entity.setResourceId(theResourceId.toUnqualifiedVersionless().getValue());
		entity.setContentType(theContentType);
		entity.setPublished(publishedDate);

		Session session = (Session) myEntityManager.getDelegate();
		LobHelper lobHelper = session.getLobHelper();

		byte[] loadedStream = IOUtils.toByteArray(countingInputStream);
		String id = super.provideIdForNewBinaryContent(
				theBinaryContentIdOrNull, loadedStream, theRequestDetails, theContentType);

		entity.setContentId(id);
		entity.setStorageContentBin(loadedStream);

		if (mySupportLegacyLobServer) {
			Blob dataBlob = lobHelper.createBlob(loadedStream);
			entity.setBlob(dataBlob);
		}

		// Update the entity with the final byte count and hash
		long bytes = countingInputStream.getByteCount();
		String hash = hashingInputStream.hash().toString();
		entity.setSize(bytes);
		entity.setHash(hash);

		// Save the entity
		myEntityManager.persist(entity);

		return new StoredDetails()
				.setBinaryContentId(id)
				.setBytes(bytes)
				.setPublished(publishedDate)
				.setHash(hash)
				.setContentType(theContentType);
	}

	@Override
	public StoredDetails fetchBinaryContentDetails(IIdType theResourceId, String theBinaryContentId) {

		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(
				theBinaryContentId, theResourceId.toUnqualifiedVersionless().getValue());
		if (entityOpt.isEmpty()) {
			return null;
		}

		BinaryStorageEntity entity = entityOpt.get();
		return new StoredDetails()
				.setBinaryContentId(theBinaryContentId)
				.setContentType(entity.getContentType())
				.setHash(entity.getHash())
				.setPublished(entity.getPublished())
				.setBytes(entity.getSize());
	}

	@Override
	public boolean writeBinaryContent(IIdType theResourceId, String theBinaryContentId, OutputStream theOutputStream)
			throws IOException {
		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(
				theBinaryContentId, theResourceId.toUnqualifiedVersionless().getValue());
		if (entityOpt.isEmpty()) {
			return false;
		}

		copyBinaryContentToOutputStream(theOutputStream, entityOpt.get());

		return true;
	}

	@Override
	public void expungeBinaryContent(IIdType theResourceId, String theBinaryContentId) {
		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(
				theBinaryContentId, theResourceId.toUnqualifiedVersionless().getValue());
		entityOpt.ifPresent(
				theBinaryStorageEntity -> myBinaryStorageEntityDao.deleteByPid(theBinaryStorageEntity.getContentId()));
	}

	@Override
	public byte[] fetchBinaryContent(IIdType theResourceId, String theBinaryContentId) throws IOException {
		BinaryStorageEntity entityOpt = myBinaryStorageEntityDao
				.findByIdAndResourceId(
						theBinaryContentId,
						theResourceId.toUnqualifiedVersionless().getValue())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Unknown BinaryContent ID: " + theBinaryContentId + " for resource ID " + theResourceId));

		return copyBinaryContentToByteArray(entityOpt);
	}

	public DatabaseBinaryContentStorageSvcImpl setSupportLegacyLobServer(boolean theSupportLegacyLobServer) {
		mySupportLegacyLobServer = theSupportLegacyLobServer;
		return this;
	}

	void copyBinaryContentToOutputStream(OutputStream theOutputStream, BinaryStorageEntity theEntity)
			throws IOException {

		try (InputStream inputStream = getBinaryContent(theEntity)) {
			IOUtils.copy(inputStream, theOutputStream);
		} catch (SQLException e) {
			throw new IOException(Msg.code(1341) + e);
		}
	}

	byte[] copyBinaryContentToByteArray(BinaryStorageEntity theEntity) throws IOException {
		byte[] retVal;

		try (InputStream inputStream = getBinaryContent(theEntity)) {
			retVal = ByteStreams.toByteArray(inputStream);
		} catch (SQLException e) {
			throw new IOException(Msg.code(1342) + e);
		}

		return retVal;
	}

	/**
	 *
	 * The caller is responsible for closing the returned stream.
	 *
	 * @param theEntity
	 * @return
	 * @throws SQLException
	 */
	private InputStream getBinaryContent(BinaryStorageEntity theEntity) throws SQLException {
		InputStream retVal;

		if (theEntity.hasStorageContent()) {
			retVal = new ByteArrayInputStream(theEntity.getStorageContentBin());
		} else if (theEntity.hasBlob()) {
			retVal = theEntity.getBlob().getBinaryStream();
		} else {
			retVal = new ByteArrayInputStream(new byte[0]);
		}

		return retVal;
	}

	@VisibleForTesting
	public DatabaseBinaryContentStorageSvcImpl setEntityManagerForTesting(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
		return this;
	}
}
