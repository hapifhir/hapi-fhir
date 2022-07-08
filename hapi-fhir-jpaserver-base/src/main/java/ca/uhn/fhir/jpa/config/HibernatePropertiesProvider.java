package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.util.ReflectionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.dialect.Dialect;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

public class HibernatePropertiesProvider {

	@Autowired
	private LocalContainerEntityManagerFactoryBean myEntityManagerFactory;
	private Dialect myDialect;
	private String myHibernateSearchBackend;

	@VisibleForTesting
	public void setDialectForUnitTest(Dialect theDialect) {
		myDialect = theDialect;
	}

	public Dialect getDialect() {
		Dialect dialect = myDialect;
		if (dialect == null) {
			String dialectClass = (String) myEntityManagerFactory.getJpaPropertyMap().get("hibernate.dialect");
			dialect = ReflectionUtil.newInstanceOrReturnNull(dialectClass, Dialect.class);
			Validate.notNull(dialect, "Unable to create instance of class: %s", dialectClass);
			myDialect = dialect;
		}
		return dialect;
	}

	public String getHibernateSearchBackend(){
		String hibernateSearchBackend = myHibernateSearchBackend;
		if (StringUtils.isBlank(hibernateSearchBackend)) {
			hibernateSearchBackend = (String) myEntityManagerFactory.getJpaPropertyMap().get(BackendSettings.backendKey(BackendSettings.TYPE));
			Validate.notNull(hibernateSearchBackend, BackendSettings.backendKey(BackendSettings.TYPE) + " property is unset!");
			myHibernateSearchBackend = hibernateSearchBackend;
		}
		return myHibernateSearchBackend;
	}


	public DataSource getDataSource() {
		return myEntityManagerFactory.getDataSource();
	}
}
