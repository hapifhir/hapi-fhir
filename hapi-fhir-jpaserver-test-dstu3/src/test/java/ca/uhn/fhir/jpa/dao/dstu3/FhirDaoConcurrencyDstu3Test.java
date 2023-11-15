package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.collections4.comparators.ReverseComparator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.comparator.ComparableComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirDaoConcurrencyDstu3Test extends BaseJpaDstu3SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirDaoConcurrencyDstu3Test.class);

	@Autowired
	public BasicDataSource myBasicDataSource;
	private int myMaxTotal;

	@AfterEach
	public void afterResetConnectionPool() {
		myBasicDataSource.setMaxTotal(myMaxTotal);
	}

	@BeforeEach
	public void beforeSetUpConnectionPool() {
		myMaxTotal = myBasicDataSource.getMaxTotal();
		myBasicDataSource.setMaxTotal(5);
	}

	@Test
	public void testMultipleConcurrentWritesToSameResource() throws InterruptedException {

		ThreadPoolExecutor exec = new ThreadPoolExecutor(10, 10,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>());

		final AtomicInteger errors = new AtomicInteger();

		List<Future> futures = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			final Patient p = new Patient();
			p.setId("PID");
			p.setActive(true);
			p.setBirthDate(new Date());
			p.addIdentifier().setSystem("foo1");
			p.addIdentifier().setSystem("foo2");
			p.addIdentifier().setSystem("foo3");
			p.addIdentifier().setSystem("foo4");
			p.addName().setFamily("FOO" + i);
			p.addName().addGiven("AAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB1");
			p.addName().addGiven("AAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB2");
			p.addName().addGiven("AAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB3");
			p.addName().addGiven("AAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB4");
			p.addName().addGiven("AAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB5");
			p.addName().addGiven("AAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBB6");

			Organization o = new Organization();
			o.setName("ORG" + i);

			final Bundle t = new Bundle();
			t.setType(BundleType.TRANSACTION);
			t.addEntry()
				.setResource(p)
				.getRequest()
				.setUrl("Patient/PID")
				.setMethod(HTTPVerb.PUT);
			t.addEntry()
				.setResource(o)
				.getRequest()
				.setUrl("Organization")
				.setMethod(HTTPVerb.POST);

			if (i == 0) {
				mySystemDao.transaction(mySrd, t);
			}
			futures.add(exec.submit(new Runnable() {
				@Override
				public void run() {
					try {
						mySystemDao.transaction(mySrd, t);
					} catch (Exception e) {
						ourLog.error("Failed to update", e);
						errors.incrementAndGet();
					}
				}
			}));
		}

		ourLog.info("Shutting down excutor");
		StopWatch sw = new StopWatch();
		for (Future next : futures) {
			while (!next.isDone()) {
				Thread.sleep(20);
			}
		}
		exec.shutdown();
		ourLog.info("Shut down excutor in {}ms", sw.getMillis());
		ourLog.info("Had {} errors", errors.get());

		Patient currentPatient = myPatientDao.read(new IdType("Patient/PID"));
		Long currentVersion = currentPatient.getIdElement().getVersionIdPartAsLong();
		ourLog.info("Current version: {}", currentVersion);

		IBundleProvider historyBundle = myPatientDao.history(new IdType("Patient/PID"), null, null, null, mySrd);
		List<IBaseResource> resources = historyBundle.getResources(0, 1000);
		List<Long> versions = new ArrayList<>();
		for (IBaseResource next : resources) {
			versions.add(next.getIdElement().getVersionIdPartAsLong());
		}

		String message = "Current version is " + currentVersion + " - History is: " + versions;
		ourLog.info(message);

		Collections.sort(versions, new ReverseComparator<>(new ComparableComparator<Long>()));
		Long lastVersion = versions.get(0);
		ourLog.info("Last version: {}", lastVersion);

		//assertEquals(message, currentVersion.intValue(), versions.size());
		assertEquals(currentVersion, lastVersion, message);

	}


}
