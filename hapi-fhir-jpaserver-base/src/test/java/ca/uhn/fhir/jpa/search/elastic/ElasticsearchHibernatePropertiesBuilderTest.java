package ca.uhn.fhir.jpa.search.elastic;

import ca.uhn.fhir.context.ConfigurationException;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ElasticsearchHibernatePropertiesBuilderTest {

	ElasticsearchHibernatePropertiesBuilder myPropertiesBuilder = spy(ElasticsearchHibernatePropertiesBuilder.class);

	@BeforeEach
	public void prepMocks() {
		//ensures we don't try to reach out to a real ES server on apply.
		doNothing().when(myPropertiesBuilder).injectStartupTemplate(any(), any(), any(), any());
	}

	@Test
	public void testRestUrlCannotContainProtocol() {
		String host = "localhost:9200";
		String protocolHost = "https://" + host;
		String failureMessage = "Elasticsearch URL cannot include a protocol, that is a separate property. Remove http:// or https:// from this URL.";

		myPropertiesBuilder
			.setProtocol("https")
			.setUsername("whatever")
			.setPassword("whatever");

		//SUT
		try {
			myPropertiesBuilder.setRestUrl(protocolHost);
			fail();
		} catch (ConfigurationException e ) {
			assertThat(e.getMessage(), is(equalTo(failureMessage)));
		}

		Properties properties = new Properties();
		myPropertiesBuilder
			.setRestUrl(host)
			.apply(properties);

		assertThat(properties.getProperty(BackendSettings.backendKey(ElasticsearchBackendSettings.HOSTS)), is(equalTo(host)));

	}

}
