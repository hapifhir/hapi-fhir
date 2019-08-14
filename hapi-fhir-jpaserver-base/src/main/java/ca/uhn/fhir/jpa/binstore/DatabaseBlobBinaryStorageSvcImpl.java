package ca.uhn.fhir.jpa.binstore;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.dao.data.IBinaryStorageEntityDao;
import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import com.google.common.hash.HashingInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

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

	@Override
	@Transactional(Transactional.TxType.SUPPORTS)
	public StoredDetails storeBlob(IIdType theResourceId, String theContentType, InputStream theInputStream) {
		Date publishedDate = new Date();

		HashingInputStream hashingInputStream = createHashingInputStream(theInputStream);
		CountingInputStream countingInputStream = createCountingInputStream(hashingInputStream);

		String id = newRandomId();

		BinaryStorageEntity entity = new BinaryStorageEntity();
		entity.setResourceId(theResourceId.toUnqualifiedVersionless().getValue());
		entity.setBlobId(id);
		entity.setBlobContentType(theContentType);
		entity.setPublished(publishedDate);

		Session session = (Session) myEntityManager.getDelegate();
		LobHelper lobHelper = session.getLobHelper();
		Blob dataBlob = lobHelper.createBlob(countingInputStream, 0);
		entity.setBlob(dataBlob);

		// Save the entity

		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(t->{
			myEntityManager.persist(entity);
			return null;
		});

		// Update the entity with the final byte count and hash
		long bytes = countingInputStream.getCount();
		String hash = hashingInputStream.hash().toString();
		txTemplate.execute(t-> {
			myBinaryStorageEntityDao.setSize(id, (int) bytes);
			myBinaryStorageEntityDao.setHash(id, hash);
			return null;
		});

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

		try {
			InputStream inputStream = entityOpt.get().getBlob().getBinaryStream();
			IOUtils.copy(inputStream, theOutputStream);
		} catch (SQLException e) {
			throw new IOException(e);
		}

		return true;
	}

	@Override
	public void expungeBlob(IIdType theResourceId, String theBlobId) {
		Optional<BinaryStorageEntity> entityOpt = myBinaryStorageEntityDao.findByIdAndResourceId(theBlobId, theResourceId.toUnqualifiedVersionless().getValue());
		entityOpt.ifPresent(theBinaryStorageEntity -> myBinaryStorageEntityDao.delete(theBinaryStorageEntity));
	}
}
