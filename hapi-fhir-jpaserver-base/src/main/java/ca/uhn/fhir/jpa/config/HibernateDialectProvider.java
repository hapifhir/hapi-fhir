package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class HibernateDialectProvider {

	@Autowired
	private LocalContainerEntityManagerFactoryBean myEntityManagerFactory;
	private Dialect myDialect;

	public Dialect getDialect() {
		Dialect dialect = myDialect;
		if (dialect == null) {
			String dialectClass = (String) myEntityManagerFactory.getJpaPropertyMap().get("hibernate.dialect");
			try {
				dialect = (Dialect) Class.forName(dialectClass).getConstructor().newInstance();
			} catch (Exception e) {
				throw new ConfigurationException("Can not construct dialect class " + dialectClass, e);
			}
			myDialect = dialect;
		}
		return dialect;
	}

}
