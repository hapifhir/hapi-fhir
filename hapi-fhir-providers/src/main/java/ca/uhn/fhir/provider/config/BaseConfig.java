package ca.uhn.fhir.provider.config;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
public class BaseConfig {

	@Autowired
	protected Environment myEnv;

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Resource
	private ApplicationContext myAppCtx;

	// @PostConstruct
	// public void wireResourceDaos() {
	// Map<String, IDao> daoBeans = myAppCtx.getBeansOfType(IDao.class);
	// List bean = myAppCtx.getBean("myResourceProvidersDstu2", List.class);
	// for (IDao next : daoBeans.values()) {
	// next.setResourceDaos(bean);
	// }
	// }

}
