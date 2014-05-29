package ca.uhn.fhirtest;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class TestRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;
	
	private ClassPathXmlApplicationContext myAppCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestRestfulServer.class);
	
	@Override
	protected void initialize() {
		super.initialize();
		
		try {
			ourLog.info("Creating database");
			DriverManager.getConnection("jdbc:derby:directory:" + System.getProperty("fhir.db.location") + ";create=true");
		} catch (Exception e) {
			ourLog.error("Failed to create database: {}",e);
		}
		
		myAppCtx = new ClassPathXmlApplicationContext("fhir-spring-uhnfhirtest-config.xml", "hapi-jpaserver-springbeans.xml");
		
		Collection<IResourceProvider> beans = myAppCtx.getBeansOfType(IResourceProvider.class).values();
		for (IResourceProvider nextResourceProvider : beans) {
			ourLog.info(" * Have resource provider for: {}", nextResourceProvider.getResourceType().getSimpleName());
		}
		setResourceProviders(beans);
		
		IFhirSystemDao systemDao = myAppCtx.getBean(IFhirSystemDao.class);
		JpaSystemProvider sp = new JpaSystemProvider(systemDao);
		setPlainProviders(sp);
	}

	@Override
	public void destroy() {
		super.destroy();
		
		myAppCtx.close();
		
		try {
			ourLog.info("Shutting down derby");
			DriverManager.getConnection("jdbc:derby:directory:" + System.getProperty("fhir.db.location") + ";shutdown=true");
		} catch (Exception e) {
			ourLog.info("Failed to create database: {}",e.getMessage());
		}
	}

}
