package ca.uhn.fhir.jpa.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.rp.dstu.BaseJavaConfigDstu1;

@Configuration
@EnableTransactionManagement()
public class TestDstu1Config extends BaseJavaConfigDstu1 {

	@Override
	@Bean(name="myDaoConfigDstu1")
	public DaoConfig daoConfigDstu1() {
		return new DaoConfig();
	}
	
	@Bean(name="myPersistenceDataSourceDstu1")
	public DataSource dataSourceDstu1() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		retVal.setUrl("jdbc:derby:memory:myUnitTestDB;create=true");
		retVal.setUsername("");
		retVal.setPassword("");
		return retVal;
	}

	@Override
	@Bean(name="myTransactionManagerDstu1")
	public JpaTransactionManager platformTransactionManagerDstu1() {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactoryDstu1());
		retVal.afterPropertiesSet();
		return retVal;
	}

	@Bean
	public EntityManagerFactory entityManagerFactoryDstu1() {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setDataSource(dataSourceDstu1());
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.entity");
		retVal.setJpaVendorAdapter(jpaVendorAdapter());
		retVal.afterPropertiesSet();
		return retVal.getNativeEntityManagerFactory();
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter retVal = new HibernateJpaVendorAdapter();
		retVal.setGenerateDdl(true);
		retVal.setDatabasePlatform(DerbyTenSevenDialect.class.getName());
		return retVal;
	}

	@Override
	protected EntityManager entityManagerDstu1() {
		return entityManagerFactoryDstu1().createEntityManager();
	}


}
