package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.search.reindex.BlockPolicy;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

public class SyntheaPerfTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SyntheaPerfTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	public static final String PATH_TO_SYNTHEA_OUTPUT = "../../synthea/output/fhir/";

	@Test
	public void testLoadSynthea() throws Exception {
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

		List<Path> initialGroup = Lists.newArrayList();
		initialGroup.add(nonMeta.remove(0));
		initialGroup.add(nonMeta.remove(0));
		initialGroup.add(nonMeta.remove(0));
		initialGroup.add(nonMeta.remove(0));
		new Uploader(initialGroup);

		new Uploader(nonMeta);
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
			myExecutor.setMaxPoolSize(5);
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
