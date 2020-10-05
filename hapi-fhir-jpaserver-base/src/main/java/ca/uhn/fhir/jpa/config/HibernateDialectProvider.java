package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.Validate;
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
			dialect = ReflectionUtil.newInstanceOrReturnNull(dialectClass, Dialect.class);
			Validate.notNull(dialect, "Unable to create class: %s", dialectClass);
			myDialect = dialect;
		}
		return dialect;
	}

}
