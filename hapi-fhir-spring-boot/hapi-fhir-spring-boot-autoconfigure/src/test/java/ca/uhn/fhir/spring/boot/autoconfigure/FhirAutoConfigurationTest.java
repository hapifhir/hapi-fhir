package ca.uhn.fhir.spring.boot.autoconfigure;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.spring.boot.autoconfigure.FhirAutoConfiguration.FhirJpaServerConfiguration.Dstu3;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.env.MockEnvironment;

import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link FhirAutoConfiguration}.
 *
 * @author Mathieu Ouellet
 */
public class FhirAutoConfigurationTest {

	private AnnotationConfigApplicationContext context;

	@AfterEach
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void withFhirContext() throws Exception {
		load();
		assertThat(this.context.getBeansOfType(FhirContext.class)).hasSize(1);
	}

	@Test
	public void withRestfulServer() {
		load("hapi.fhir.server.path:/hapi-fhir/*");
		assertThat(this.context.getBeansOfType(ServletRegistrationBean.class)).hasSize(1);
		assertThat(this.context.getBeansOfType(RestfulServer.class)).hasSize(1);
		assertThat(this.context.getBean(ServletRegistrationBean.class).getUrlMappings()).contains("/hapi-fhir/*");
	}

	@Test
	public void withNoValidation() {
		load("hapi.fhir.validation.enabled:false");
		try {
			this.context.getBean(RequestValidatingInterceptor.class);
			fail();
		} catch (NoSuchBeanDefinitionException e) {
			// good
		}
	}

	@Test
	@Disabled
	public void withValidation() {
		load();
		assertThat(this.context.getBeansOfType(IServerInterceptor.class)).hasSize(1);
	}

	@Test
	@Disabled
	public void withValidations() {
		load("hapi.fhir.validation.request-only:false");
		assertThat(this.context.getBeansOfType(IServerInterceptor.class)).hasSize(2);
	}

	@Test
	@Disabled
	public void withCustomValidationSchemaLocation() {
		load("hapi.fhir.validation.schema-location:custom-schema-location");
		assertThat(this.context.getBeansOfType(IServerInterceptor.class)).hasSize(1);
	}

	@Test
	public void withApacheHttpClient() {
		load(new HidePackagesClassLoader("okhttp3"), "hapi.fhir.server.url:http://localhost:8080");
		assertThat(this.context.getBeansOfType(ApacheRestfulClientFactory.class)).hasSize(1);
		assertThat(this.context.getBeansOfType(OkHttpRestfulClientFactory.class)).hasSize(0);
	}

	@Test
	public void withOkHttpClient() {
		load("hapi.fhir.server.url:http://localhost:8080");
		assertThat(this.context.getBeansOfType(OkHttpRestfulClientFactory.class)).hasSize(1);
		assertThat(this.context.getBeansOfType(ApacheRestfulClientFactory.class)).hasSize(0);
	}

	private void load(String... environment) {
		load(new Class<?>[]{FhirAutoConfiguration.class}, null, environment);
	}

	private void load(ClassLoader classLoader, String... environment) {
		load(new Class<?>[]{FhirAutoConfiguration.class}, classLoader, environment);
	}

	private void load(Class<?>[] configs, String... environment) {
		load(configs, null, environment);
	}

	private void load(Class<?>[] configs, ClassLoader classLoader, String... environment) {
		MockEnvironment env = new MockEnvironment();

		for (String next : environment) {
			String nextKey = next.substring(0, next.indexOf(':'));
			String nextValue = next.substring(next.indexOf(':') + 1);
			env.setProperty(nextKey, nextValue);
		}


		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.setEnvironment(env);
		if (classLoader != null) {
			applicationContext.setClassLoader(classLoader);
		}
		if (configs != null) {
			applicationContext.register(configs);
		}
		applicationContext.refresh();
		this.context = applicationContext;
	}

	private static final class HidePackagesClassLoader extends URLClassLoader {

		private final String[] hiddenPackages;

		private HidePackagesClassLoader(String... hiddenPackages) {
			super(new URL[0], FhirAutoConfigurationTest.class.getClassLoader());
			this.hiddenPackages = hiddenPackages;
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
			for (String hiddenPackage : this.hiddenPackages) {
				if (name.startsWith(hiddenPackage)) {
					throw new ClassNotFoundException();
				}
			}
			return super.loadClass(name, resolve);
		}

	}

}
