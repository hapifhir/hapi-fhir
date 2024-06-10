package ca.uhn.fhir.jpa.partition;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PartitionLookupSvcImplTest {

	@Autowired
	private PartitionLookupSvcImpl myPartitionLookupSvc;
	@MockBean
	PartitionSettings myPartitionSettings;
	@MockBean
	IInterceptorService myInterceptorBroadcaster;
	@MockBean
	IPartitionDao myPartitionDao;
	@MockBean
	private FhirContext myFhirCtx;
	@MockBean
	private PlatformTransactionManager myTxManager;
	@MockBean
	private MemoryCacheService myMemoryCacheService;

	@Configuration
	static class SpringContext {
		@Bean
		public PartitionLookupSvcImpl partitionLookupSvcImplTest() {
			return new PartitionLookupSvcImpl();
		}
	}
	@Test
	void generateRandomUnusedPartitionId() {
		when(myPartitionDao.findById(any())).thenReturn(Optional.empty());
		for (int i = 0; i<10000; i++) {
			int randomUnusedPartitionId = myPartitionLookupSvc.generateRandomUnusedPartitionId();
			assertTrue(randomUnusedPartitionId >= 1);
		}
	}
}
