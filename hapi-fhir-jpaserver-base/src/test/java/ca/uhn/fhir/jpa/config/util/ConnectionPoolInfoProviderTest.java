package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.jpa.config.r4.JpaR4Config;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ConnectionPoolInfoProviderTest {

	public static final long MAX_WAIT_MILLIS = 10_000;
	public static final int MAX_CONNECTIONS_TOTAL = 50;

	private IConnectionPoolInfoProvider tested;


	@Nested
	public class TestBasiDataSourceImplementation {

		@BeforeEach
		void setUp() {
			BasicDataSource myDataSource = new BasicDataSource();
			myDataSource.setMaxWaitMillis(MAX_WAIT_MILLIS);
			myDataSource.setMaxTotal(MAX_CONNECTIONS_TOTAL);
			tested = new BasicDataSourceConnectionPoolInfoProvider(myDataSource);
		}


		@Test
		void testGetMaxWaitMillis() {
			Optional<Long> resOpt = tested.getMaxWaitMillis();
			assertTrue(resOpt.isPresent());
			assertEquals(MAX_WAIT_MILLIS, resOpt.get());
		}

		@Test
		void testGetMaxConnectionSize() {
			Optional<Integer> resOpt = tested.getTotalConnectionSize();
			assertTrue(resOpt.isPresent());
			assertEquals(MAX_CONNECTIONS_TOTAL, resOpt.get());
		}

	}


	@Nested
	public class TestNoOpImplementation {

		@BeforeEach
		void setUp() {
			tested = new NoOpConnectionPoolInfoProvider();
		}


		@Test
		void testGetMaxWaitMillis() {
			Optional<Long> resOpt = tested.getMaxWaitMillis();
			assertFalse(resOpt.isPresent());
		}

		@Test
		void testGetMaxConnectionSize() {
			Optional<Integer> resOpt = tested.getTotalConnectionSize();
			assertFalse(resOpt.isPresent());
		}

		@Test
		void testGetActiveConnections() {
			Optional<Integer> resOpt = tested.getActiveConnections();
			assertFalse(resOpt.isPresent());
		}

	}

	@Nested
	public class TestConfig {

		private JpaR4Config config = new JpaR4Config();

		@Mock DataSource unknownDataSource;

		@Test
		void dataSourceIsBasicDataSource() {
			DataSource ds = new BasicDataSource();
			ReflectionTestUtils.setField(config, "myDataSource", ds);

			IConnectionPoolInfoProvider provider = config.connectionPoolInfoProvider();

			assertTrue(provider.getClass().isAssignableFrom(BasicDataSourceConnectionPoolInfoProvider.class));
			DataSource provideDataSource = (DataSource) ReflectionTestUtils.getField(provider, "myDataSource");
			assertEquals(ds, provideDataSource);
		}

		@Test
		void dataSourceIsProxyDataSourceWrappingBasicDataSource() {
			DataSource ds = new BasicDataSource();
			ProxyDataSource proxyDs = new ProxyDataSource(ds);
			ReflectionTestUtils.setField(config, "myDataSource", proxyDs);

			IConnectionPoolInfoProvider provider = config.connectionPoolInfoProvider();

			assertTrue(provider.getClass().isAssignableFrom(BasicDataSourceConnectionPoolInfoProvider.class));
			DataSource provideDataSource = (DataSource) ReflectionTestUtils.getField(provider, "myDataSource");
			assertEquals(ds, provideDataSource);
		}

		@Test
		void dataSourceIsProxyDataSourceWrappingNotBasicDataSource() {
			ProxyDataSource proxyDs = new ProxyDataSource(unknownDataSource);
			ReflectionTestUtils.setField(config, "myDataSource", proxyDs);

			IConnectionPoolInfoProvider provider = config.connectionPoolInfoProvider();

			assertTrue(provider.getClass().isAssignableFrom(NoOpConnectionPoolInfoProvider.class));
		}

		@Test
		void dataSourceIsNotBasicDataSourceOrProxyDataSource() {
			ReflectionTestUtils.setField(config, "myDataSource", unknownDataSource);

			IConnectionPoolInfoProvider provider = config.connectionPoolInfoProvider();

			assertTrue(provider.getClass().isAssignableFrom(NoOpConnectionPoolInfoProvider.class));
		}

	}

}
