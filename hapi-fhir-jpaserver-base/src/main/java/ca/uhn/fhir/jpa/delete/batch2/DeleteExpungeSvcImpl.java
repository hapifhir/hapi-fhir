package ca.uhn.fhir.jpa.delete.batch2;

import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
public class DeleteExpungeSvcImpl implements IDeleteExpungeSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeSvcImpl.class);

	private final EntityManager myEntityManager;
	private final DeleteExpungeSqlBuilder myDeleteExpungeSqlBuilder;

	public DeleteExpungeSvcImpl(EntityManager theEntityManager, DeleteExpungeSqlBuilder theDeleteExpungeSqlBuilder) {
		myEntityManager = theEntityManager;
		myDeleteExpungeSqlBuilder = theDeleteExpungeSqlBuilder;
	}

	@Override
	public void deleteExpunge(List<ResourcePersistentId> thePersistentIds) {
		List<String> sqlList = myDeleteExpungeSqlBuilder.pidsToDeleteExpungeSql(thePersistentIds);

		ourLog.debug("Executing {} delete expunge sql commands", sqlList.size());
		long totalDeleted = 0;
		for (String sql : sqlList) {
			ourLog.trace("Executing sql " + sql);
			totalDeleted += myEntityManager.createNativeQuery(sql).executeUpdate();
		}
		ourLog.info("{} records deleted", totalDeleted);
		// TODO KHS instead of logging progress, produce result chunks that get aggregated into a delete expunge report
	}


}
