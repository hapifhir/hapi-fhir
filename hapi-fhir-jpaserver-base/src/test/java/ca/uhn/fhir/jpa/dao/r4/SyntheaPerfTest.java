package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.config.TestR4WithLuceneDisabledConfig;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.search.reindex.BlockPolicy;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Meta;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4WithLuceneDisabledConfig.class})
@DirtiesContext
public class SyntheaPerfTest extends BaseJpaTest {

	static {
		System.setProperty("unlimited_db_connection", "true");
	}

	private static final Logger ourLog = LoggerFactory.getLogger(SyntheaPerfTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	public static final String PATH_TO_SYNTHEA_OUTPUT = "../../synthea/output/fhir/";
	public static final int CONCURRENCY = 4;
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	@Disabled
	@Test
	public void testLoadSynthea() throws Exception {
		assertEquals(100, TestR4Config.getMaxThreads());

		myDaoConfig.setResourceEncoding(ResourceEncodingEnum.JSON);
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.INLINE);
		myDaoConfig.setMatchUrlCacheEnabled(true);
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setDeleteEnabled(false);

		List<Path> files = Files
			.list(FileSystems.getDefault().getPath(PATH_TO_SYNTHEA_OUTPUT))
			.filter(t->t.toString().endsWith(".json"))
			.collect(Collectors.toList());

		List<Path> meta = files.stream().filter(t -> t.toString().contains("hospital") || t.toString().contains("practitioner")).collect(Collectors.toList());
		new Uploader(meta);

		List<Path> nonMeta = files.stream().filter(t -> !t.toString().contains("hospital") && !t.toString().contains("practitioner")).collect(Collectors.toList());

		new Uploader(Collections.singletonList(nonMeta.remove(0)));
//		new Uploader(Collections.singletonList(nonMeta.remove(0)));
//		new Uploader(Collections.singletonList(nonMeta.remove(0)));
//		new Uploader(Collections.singletonList(nonMeta.remove(0)));

		new Uploader(nonMeta);
	}

	@Override
	protected FhirContext getContext() {
		return myCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}


	private class Uploader {

		private final ThreadPoolTaskExecutor myExecutor;
		private final int myTotal;
		private final StopWatch mySw;
		private final AtomicInteger myFilesCounter = new AtomicInteger(0);
		private final AtomicInteger myResourcesCounter = new AtomicInteger(0);

		public Uploader(List<Path> thePaths) throws ExecutionException, InterruptedException {
			Validate.isTrue(thePaths.size() > 0);
			myTotal = thePaths.size();

			myExecutor = new ThreadPoolTaskExecutor();
			myExecutor.setCorePoolSize(0);
			myExecutor.setMaxPoolSize(CONCURRENCY);
			myExecutor.setQueueCapacity(100);
			myExecutor.setAllowCoreThreadTimeOut(true);
			myExecutor.setThreadNamePrefix("Uploader-");
			myExecutor.setRejectedExecutionHandler(new BlockPolicy());
			myExecutor.initialize();

			mySw = new StopWatch();
			List<Future<?>> futures = new ArrayList<>();
			for (Path next : thePaths) {
				futures.add(myExecutor.submit(new MyTask(next)));
			}

			for (Future<?> next : futures) {
				next.get();
			}

			ourLog.info("Finished uploading {} files with {} resources in {} - {} files/sec - {} res/sec",
				myFilesCounter.get(),
				myResourcesCounter.get(),
				mySw,
				mySw.formatThroughput(myFilesCounter.get(), TimeUnit.SECONDS),
				mySw.formatThroughput(myResourcesCounter.get(), TimeUnit.SECONDS));
		}

		private class MyTask implements Runnable {

			private final Path myPath;

			public MyTask(Path thePath) {
				myPath = thePath;
			}

			@Override
			public void run() {
				Bundle bundle;
				try (FileReader reader = new FileReader(myPath.toFile())) {
					bundle = ourCtx.newJsonParser().parseResource(Bundle.class, reader);
				} catch (IOException e) {
					throw new InternalErrorException(e);
				}

				mySystemDao.transaction(new SystemRequestDetails(myInterceptorRegistry), bundle);

				int fileCount = myFilesCounter.incrementAndGet();
				myResourcesCounter.addAndGet(bundle.getEntry().size());

				if (fileCount % 10 == 0) {
					ourLog.info("Have uploaded {} files with {} resources in {} - {} files/sec - {} res/sec",
						myFilesCounter.get(),
						myResourcesCounter.get(),
						mySw,
						mySw.formatThroughput(myFilesCounter.get(), TimeUnit.SECONDS),
						mySw.formatThroughput(myResourcesCounter.get(), TimeUnit.SECONDS));
				}
			}
		}


	}



}
