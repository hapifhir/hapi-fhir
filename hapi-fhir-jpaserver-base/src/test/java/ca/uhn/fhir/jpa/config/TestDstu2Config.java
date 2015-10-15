package ca.uhn.fhir.jpa.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.rp.dstu2.BaseJavaConfigDstu2;

@Configuration
@EnableTransactionManagement()
public class TestDstu2Config extends BaseJavaConfigDstu2 implements TransactionManagementConfigurer {

	@Bean(name="myDaoConfigDstu2")
	public DaoConfig daoConfigDstu2() {
		return new DaoConfig();
	}
	
	@Bean(name="myPersistenceDataSourceDstu2")
	public DataSource dataSourceDstu2() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		retVal.setUrl("jdbc:derby:memory:myUnitTestDB;create=true");
		retVal.setUsername("");
		retVal.setPassword("");
		return retVal;
	}

	@Bean(name="myTransactionManagerDstu2")
	public JpaTransactionManager platformTransactionManagerDstu2() {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactoryDstu2().getNativeEntityManagerFactory());
		retVal.afterPropertiesSet();
		return retVal;
	}

	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryDstu2() {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setPersistenceUnitName("PU_HapiFhirJpaDstu2");
		retVal.setDataSource(dataSourceDstu2());
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setJpaProperties(jpaProperties());
		retVal.afterPropertiesSet();
		return retVal;
	}
	
   private Properties jpaProperties() {
      Properties extraProperties = new Properties();
      extraProperties.put("hibernate.format_sql", "true");
      extraProperties.put("hibernate.show_sql", "false");
      extraProperties.put("hibernate.hbm2ddl.auto", "update");
      return extraProperties;
  }

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return platformTransactionManagerDstu2();
	}
   
}
