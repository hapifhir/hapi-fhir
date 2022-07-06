package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSqlBuilder;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSvcImpl;
import ca.uhn.fhir.jpa.reindex.Batch2DaoSvcImpl;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

public class Batch2SupportConfig {

	@Bean
	public IBatch2DaoSvc batch2DaoSvc() {
		return new Batch2DaoSvcImpl();
	}

	@Bean
	public IDeleteExpungeSvc deleteExpungeSvc(EntityManager theEntityManager, DeleteExpungeSqlBuilder theDeleteExpungeSqlBuilder) {
		return new DeleteExpungeSvcImpl(theEntityManager, theDeleteExpungeSqlBuilder);
	}

	@Bean
	DeleteExpungeSqlBuilder deleteExpungeSqlBuilder(ResourceTableFKProvider theResourceTableFKProvider, DaoConfig theDaoConfig, IJpaIdHelperService theIdHelper, IResourceLinkDao theResourceLinkDao) {
		return new DeleteExpungeSqlBuilder(theResourceTableFKProvider, theDaoConfig, theIdHelper, theResourceLinkDao);
	}
}
