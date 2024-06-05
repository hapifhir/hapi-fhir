package ca.uhn.fhir.jpa.search.elastic;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ElasticsearchHibernatePropertiesBuilderTest {

	ElasticsearchHibernatePropertiesBuilder myPropertiesBuilder = spy(ElasticsearchHibernatePropertiesBuilder.class);

	@Test
	public void testHostsCannotContainProtocol() {
		String host = "localhost:9200";
		String protocolHost = "https://" + host;
		String failureMessage = "Elasticsearch URLs cannot include a protocol, that is a separate property. Remove http:// or https:// from this URL.";

		myPropertiesBuilder
			.setProtocol("https")
			.setUsername("whatever")
			.setPassword("whatever");

		//SUT
		try {
			myPropertiesBuilder.setHosts(protocolHost)
				.apply(new Properties());
			fail("");		} catch (ConfigurationException e ) {
			assertEquals(Msg.code(2139) + failureMessage, e.getMessage());
		}

		Properties properties = new Properties();
		myPropertiesBuilder
			.setHosts(host)
			.apply(properties);

		assertEquals(host, properties.getProperty(BackendSettings.backendKey(ElasticsearchBackendSettings.HOSTS)));

	}
}
