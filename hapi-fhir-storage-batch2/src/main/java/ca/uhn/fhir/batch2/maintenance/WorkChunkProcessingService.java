package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.intellij.lang.annotations.Language;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@Service
public class WorkChunkProcessingService {

	private final HapiTransactionService myTxService;

	public WorkChunkProcessingService(HapiTransactionService theTxService) {
		myTxService = theTxService;
	}
	//	@Language("SQL")
	//	static String WORK_CHUNK_VIEW =
	//		"SELECT e.id as id, "
	//			+ " e.seq as seq, "
	//			+ " e.stat as state, "
	//			+ " e.instance_id as instance_id, "
	//			+ " e.definition_id as job_definition_id, "
	//			+ " e.definition_ver as job_definition_version, "
	//			+ " e.tgt_step_id as target_step_id "
	//			+ " FROM BT2_WORK_CHUNK e "
	//			+ " WHERE e.instance_id = :instanceId AND e.stat = :status "
	//			+ " ORDER BY e.instance_id, e.tgt_step_id, e.stat, e.seq, e.id ASC";
	@Language("SQL")
	static String WORK_CHUNK_VIEW =
			"SELECT e.id, e.seq, e.stat, e.instance_id, e.definition_id, e.definition_ver, e.tgt_step_id "
					+ "FROM BT2_WORK_CHUNK e WHERE e.instance_id = :instanceId AND e.stat = :status";

	public Stream<WorkChunkMetadata> getReadyChunks(String theInstanceId) {
		EntityManager entityManager = getEntityManager();

		Query nativeQuery = entityManager.createNativeQuery(WORK_CHUNK_VIEW);
		nativeQuery.setParameter("instanceId", theInstanceId);
		nativeQuery.setParameter("status", WorkChunkStatusEnum.READY.name());

		return runInTransaction(() -> {
			Stream<Object[]> resultStream = nativeQuery.getResultStream();
			return resultStream
					.map(row -> {
						WorkChunkMetadata metadata = new WorkChunkMetadata();
						metadata.setId((String) row[0]);
						metadata.setSequence((Integer) row[1]);
						metadata.setStatus(WorkChunkStatusEnum.valueOf((String) row[2]));
						metadata.setInstanceId((String) row[3]);
						metadata.setJobDefinitionId((String) row[4]);
						metadata.setJobDefinitionVersion((Integer) row[5]);
						metadata.setTargetStepId((String) row[6]);

						return metadata;
					})
					.onClose(() -> onClose(entityManager));
		});
	}

	public <T> T runInTransaction(Callable<T> theRunnable) {
		return newTxTemplate().execute(t -> {
			try {
				return theRunnable.call();
			} catch (Exception theE) {
				throw new InternalErrorException(theE);
			}
		});
	}

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(myTxService.getTransactionManager());

		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.setReadOnly(true);
		retVal.afterPropertiesSet();
		return retVal;
	}

	@SuppressWarnings("unchecked")
	//	public Stream<WorkChunkMetadata> runQuery(String sql, Map<String, Object> params, EntityManager theEntityManager)
	// {
	//		Query q = theEntityManager.createNativeQuery(sql);
	//		if (!params.isEmpty()) {
	//			params.forEach(q::setParameter);
	//		}
	//		// raw sql to hibernate for compatibility
	//		Query hibernateQuery =  q;
	//
	//		return runInTransaction(() -> {
	//			final Stream<WorkChunkMetadata> resultStream = (Stream<WorkChunkMetadata>) hibernateQuery.getResultStream();
	//			return resultStream.map(row -> {
	//				WorkChunkMetadata metadata = new WorkChunkMetadata();
	//				metadata.setId((String) row[0]);
	//				metadata.setSequence((Integer) row[1]);
	//				metadata.setStatus(WorkChunkStatusEnum.valueOf((String) row[2]));
	//				metadata.setInstanceId((String) row[3]);
	//				metadata.setJobDefinitionId((String) row[4]);
	//				metadata.setJobDefinitionVersion((Integer) row[5]);
	//				metadata.setTargetStepId((String) row[6]);
	//
	//				// ✅ Convert JSON column to IModelJson subclass
	//
	//				if (jsonData != null) {
	//					try {
	//						metadata.setJsonData(objectMapper.readValue(jsonData, jsonClass)); // ✅ Convert JSON to IModelJson
	//					} catch (Exception e) {
	//						throw new RuntimeException("Failed to deserialize JSON data", e);
	//					}
	//				}
	//
	//				return metadata;
	//			}).onClose(() -> onClose(entityManager));
	//		});
	//	}
	// return resultStream.onClose(() -> onClose(theEntityManager));
	//	});
	// }

	private void onClose(EntityManager theEntityManager) {
		theEntityManager.close();
	}

	private EntityManager getEntityManager() {
		JpaTransactionManager jpaTransactionManager = (JpaTransactionManager) myTxService.getTransactionManager();

		return Objects.requireNonNull(jpaTransactionManager.getEntityManagerFactory())
				.createEntityManager();
	}
}
