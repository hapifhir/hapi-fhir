package ca.uhn.fhir.spring.boot.autoconfigure;

/*-
 * #%L
 * hapi-fhir-spring-boot-autoconfigure
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


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import ca.uhn.fhir.jpa.config.JpaDstu2Config;
import ca.uhn.fhir.jpa.config.dstu3.JpaDstu3Config;
import ca.uhn.fhir.jpa.config.r4.JpaR4Config;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.provider.BaseJpaProvider;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for HAPI FHIR.
 *
 * @author Mathieu Ouellet
 */
@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties(FhirProperties.class)
public class FhirAutoConfiguration {


	private final FhirProperties properties;

	public FhirAutoConfiguration(FhirProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public FhirContext fhirContext() {
		FhirContext fhirContext = new FhirContext(properties.getVersion());
		return fhirContext;
	}


	@Configuration
	@ConditionalOnClass(AbstractJaxRsProvider.class)
	@EnableConfigurationProperties(FhirProperties.class)
	@ConfigurationProperties("hapi.fhir.rest")
	@SuppressWarnings("serial")
	static class FhirRestfulServerConfiguration extends RestfulServer {

		private final FhirProperties properties;

		private final FhirContext fhirContext;

		private final List<IResourceProvider> resourceProviders;

		private final IPagingProvider pagingProvider;

		private final List<FhirRestfulServerCustomizer> customizers;

		public FhirRestfulServerConfiguration(
			FhirProperties properties,
			FhirContext fhirContext,
			ObjectProvider<List<IResourceProvider>> resourceProviders,
			ObjectProvider<IPagingProvider> pagingProvider,
			ObjectProvider<List<IServerInterceptor>> interceptors,
			ObjectProvider<List<FhirRestfulServerCustomizer>> customizers) {
			this.properties = properties;
			this.fhirContext = fhirContext;
			this.resourceProviders = resourceProviders.getIfAvailable();
			this.pagingProvider = pagingProvider.getIfAvailable();
			this.customizers = customizers.getIfAvailable();
		}

		private void customize() {
			if (this.customizers != null) {
				AnnotationAwareOrderComparator.sort(this.customizers);
				for (FhirRestfulServerCustomizer customizer : this.customizers) {
					customizer.customize(this);
				}
			}
		}

		@Bean
		public ServletRegistrationBean fhirServerRegistrationBean() {
			ServletRegistrationBean registration = new ServletRegistrationBean(this, this.properties.getServer().getPath());
			registration.setLoadOnStartup(1);
			return registration;
		}

		@Override
		protected void initialize() throws ServletException {
			super.initialize();

			setFhirContext(this.fhirContext);
			setResourceProviders(this.resourceProviders);
			setPagingProvider(this.pagingProvider);

			setServerAddressStrategy(new HardcodedServerAddressStrategy(this.properties.getServer().getPath()));

			customize();
		}
	}

	@Configuration
	@ConditionalOnClass(BaseJpaProvider.class)
	@ConditionalOnBean(DataSource.class)
	@EnableConfigurationProperties(FhirProperties.class)
	static class FhirJpaServerConfiguration {
		@Autowired
		private ScheduledExecutorService myScheduledExecutorService;

		@Configuration
		@EntityScan(basePackages = {"ca.uhn.fhir.jpa.entity", "ca.uhn.fhir.jpa.model.entity"})
		@Import({
			SubscriptionChannelConfig.class,
			SubscriptionProcessorConfig.class,
			SubscriptionSubmitterConfig.class
		})
		static class FhirJpaDaoConfiguration {

			@Autowired
			private EntityManagerFactory emf;

			@Bean
			@Primary
			public PlatformTransactionManager hapiTransactionManager() {
				return new JpaTransactionManager(emf);
			}

			@Bean
			@ConditionalOnMissingBean
			@ConfigurationProperties("hapi.fhir.jpa")
			public DaoConfig fhirDaoConfig() {
				DaoConfig fhirDaoConfig = new DaoConfig();
				return fhirDaoConfig;
			}

			@Bean
			@ConditionalOnMissingBean
			@ConfigurationProperties("hapi.fhir.jpa")
			public PartitionSettings partitionSettings() {
				return new PartitionSettings();
			}


			@Bean
			@ConditionalOnMissingBean
			@ConfigurationProperties("hapi.fhir.jpa")
			public ModelConfig fhirModelConfig() {
				return fhirDaoConfig().getModelConfig();
			}
		}

		@Configuration
		@ConditionalOnBean({DaoConfig.class, RestfulServer.class})
		@SuppressWarnings("rawtypes")
		static class RestfulServerCustomizer implements FhirRestfulServerCustomizer {

			private final BaseJpaSystemProvider systemProviders;

			public RestfulServerCustomizer(ObjectProvider<BaseJpaSystemProvider> systemProviders) {
				this.systemProviders = systemProviders.getIfAvailable();
			}

			@Override
			public void customize(RestfulServer server) {
				server.setPlainProviders(systemProviders);
			}
		}

		@Configuration
		@Import({JpaDstu3Config.class, HapiJpaConfig.class})
		@ConditionalOnMissingBean(type = "ca.uhn.fhir.jpa.config.JpaConfig")
		@ConditionalOnProperty(name = "hapi.fhir.version", havingValue = "DSTU3")
		static class Dstu3 {
		}

		@Configuration
		@Import({JpaDstu2Config.class, HapiJpaConfig.class})
		@ConditionalOnMissingBean(type = "ca.uhn.fhir.jpa.config.JpaConfig")
		@ConditionalOnProperty(name = "hapi.fhir.version", havingValue = "DSTU2")
		static class Dstu2 {
		}

		@Configuration
		@Import({JpaR4Config.class, HapiJpaConfig.class})
		@ConditionalOnMissingBean(type = "ca.uhn.fhir.jpa.config.JpaConfig")
		@ConditionalOnProperty(name = "hapi.fhir.version", havingValue = "R4")
		static class R4 {
		}
	}

	@Configuration
	@Conditional(FhirValidationConfiguration.SchemaAvailableCondition.class)
	@ConditionalOnProperty(name = "hapi.fhir.validation.enabled", matchIfMissing = true)
	static class FhirValidationConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public RequestValidatingInterceptor requestValidatingInterceptor() {
			return new RequestValidatingInterceptor();
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(name = "hapi.fhir.validation.request-only", havingValue = "false")
		public ResponseValidatingInterceptor responseValidatingInterceptor() {
			return new ResponseValidatingInterceptor();
		}

		static class SchemaAvailableCondition extends ResourceCondition {

			SchemaAvailableCondition() {
				super("ValidationSchema",
					"hapi.fhir.validation",
					"schema-location",
					"classpath:/org/hl7/fhir/instance/model/schema",
					"classpath:/org/hl7/fhir/dstu2016may/model/schema",
					"classpath:/org/hl7/fhir/dstu3/model/schema");
			}
		}
	}

	@Configuration
	@ConditionalOnProperty("hapi.fhir.server.url")
	@EnableConfigurationProperties(FhirProperties.class)
	static class FhirRestfulClientConfiguration {

		private final FhirProperties properties;

		private final List<IClientInterceptor> clientInterceptors;

		public FhirRestfulClientConfiguration(FhirProperties properties, ObjectProvider<List<IClientInterceptor>> clientInterceptors) {
			this.properties = properties;
			this.clientInterceptors = clientInterceptors.getIfAvailable();
		}

		@Bean
		@ConditionalOnBean(IRestfulClientFactory.class)
		public IGenericClient fhirClient(final IRestfulClientFactory clientFactory) {
			IGenericClient fhirClient = clientFactory.newGenericClient(this.properties.getServer().getUrl());
			if (!CollectionUtils.isEmpty(this.clientInterceptors)) {
				for (IClientInterceptor interceptor : this.clientInterceptors) {
					fhirClient.registerInterceptor(interceptor);
				}
			}
			return fhirClient;
		}

		@Configuration
		@ConditionalOnClass(HttpClient.class)
		@ConditionalOnMissingClass("okhttp3.OkHttpClient")
		static class Apache {

			private final FhirContext context;

			public Apache(FhirContext context) {
				this.context = context;
			}

			@Bean
			@ConditionalOnMissingBean
			@ConfigurationProperties("hapi.fhir.rest.client.apache")
			public IRestfulClientFactory fhirRestfulClientFactory() {
				ApacheRestfulClientFactory restfulClientFactory = new ApacheRestfulClientFactory(this.context);
				return restfulClientFactory;
			}
		}

		@Configuration
		@ConditionalOnClass(OkHttpClient.class)
		static class OkHttp {

			private final FhirContext context;

			public OkHttp(FhirContext context) {
				this.context = context;
			}

			@Bean
			@ConditionalOnMissingBean
			@ConfigurationProperties("hapi.fhir.rest.client.okhttp")
			public IRestfulClientFactory fhirRestfulClientFactory() {
				OkHttpRestfulClientFactory restfulClientFactory = new OkHttpRestfulClientFactory(this.context);
				return restfulClientFactory;
			}
		}
	}

}
