package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.VersionIndependentConcept;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.jpa.util.LoggingRule;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRequestOperationCallback;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mockito;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseJpaTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaTest.class);

	protected static final String CM_URL = "http://example.com/my_concept_map";
	protected static final String CS_URL = "http://example.com/my_code_system";
	protected static final String CS_URL_2 = "http://example.com/my_code_system2";
	protected static final String CS_URL_3 = "http://example.com/my_code_system3";
	protected static final String CS_URL_4 = "http://example.com/my_code_system4";
	protected static final String VS_URL = "http://example.com/my_value_set";
	protected static final String VS_URL_2 = "http://example.com/my_value_set2";

	protected ServletRequestDetails mySrd;
	protected ArrayList<IServerInterceptor> myServerInterceptorList;
	protected IRequestOperationCallback myRequestOperationCallback = mock(IRequestOperationCallback.class);

	@Rule
	public LoggingRule myLoggingRule = new LoggingRule();

	@After
	public final void afterPerformCleanup() {
		BaseHapiFhirResourceDao.setDisableIncrementOnUpdateForUnitTest(false);
	}

	@Before
	public void beforeCreateSrd() {
		mySrd = mock(ServletRequestDetails.class, Mockito.RETURNS_DEEP_STUBS);
		when(mySrd.getRequestOperationCallback()).thenReturn(myRequestOperationCallback);
		myServerInterceptorList = new ArrayList<>();
		when(mySrd.getServer().getInterceptors()).thenReturn(myServerInterceptorList);
		when(mySrd.getUserData()).thenReturn(new HashMap<>());
		when(mySrd.getHeaders(eq(JpaConstants.HEADER_META_SNAPSHOT_MODE))).thenReturn(new ArrayList<>());
	}

	protected abstract FhirContext getContext();

	/**
	 * Sleep until at least 1 ms has elapsed
	 */
	public void sleepUntilTimeChanges() throws InterruptedException {
		StopWatch sw = new StopWatch();
		while (sw.getMillis() == 0) {
			Thread.sleep(10);
		}
	}

	protected org.hl7.fhir.dstu3.model.Bundle toBundle(IBundleProvider theSearch) {
		org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
		for (IBaseResource next : theSearch.getResources(0, theSearch.size())) {
			bundle.addEntry().setResource((Resource) next);
		}
		return bundle;
	}

	protected org.hl7.fhir.r4.model.Bundle toBundleR4(IBundleProvider theSearch) {
		org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
		for (IBaseResource next : theSearch.getResources(0, theSearch.size())) {
			bundle.addEntry().setResource((org.hl7.fhir.r4.model.Resource) next);
		}
		return bundle;
	}

	@SuppressWarnings({"rawtypes"})
	protected List toList(IBundleProvider theSearch) {
		return theSearch.getResources(0, theSearch.size());
	}

	protected List<String> toUnqualifiedIdValues(IBaseBundle theFound) {
		List<String> retVal = new ArrayList<String>();

		List<IBaseResource> res = BundleUtil.toListOfResources(getContext(), theFound);
		int size = res.size();
		ourLog.info("Found {} results", size);
		for (IBaseResource next : res) {
			retVal.add(next.getIdElement().toUnqualified().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedIdValues(IBundleProvider theFound) {
		List<String> retVal = new ArrayList<String>();
		int size = theFound.size();
		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualified().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBaseBundle theFound) {
		List<String> retVal = new ArrayList<String>();

		List<IBaseResource> res = BundleUtil.toListOfResources(getContext(), theFound);
		int size = res.size();
		ourLog.info("Found {} results", size);
		for (IBaseResource next : res) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound) {
		List<String> retVal = new ArrayList<String>();
		Integer size = theFound.size();
		ourLog.info("Found {} results", size);

		if (size == null) {
			size = 99999;
		}

		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(Bundle theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (Entry next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getId().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(IBundleProvider theFound) {
		List<IIdType> retVal = new ArrayList<>();
		Integer size = theFound.size();
		StopWatch sw = new StopWatch();
		while (size == null) {
			int timeout = 20000;
			if (sw.getMillis() > timeout) {
				fail("Waited over "+timeout+"ms for search");
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException theE) {
				//ignore
			}
			size = theFound.size();
		}

		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(List<IBaseResource> theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (IBaseResource next : theFound) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(org.hl7.fhir.dstu3.model.Bundle theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (BundleEntryComponent next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(org.hl7.fhir.r4.model.Bundle theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected String[] toValues(IIdType... theValues) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (IIdType next : theValues) {
			retVal.add(next.getValue());
		}
		return retVal.toArray(new String[retVal.size()]);
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@AfterClass
	public static void afterClassShutdownDerby() {
		// DriverManager.getConnection("jdbc:derby:;shutdown=true");
		// try {
		// DriverManager.getConnection("jdbc:derby:memory:myUnitTestDB;drop=true");
		// } catch (SQLNonTransientConnectionException e) {
		// // expected.. for some reason....
		// }
	}

	public static String loadClasspath(String resource) throws IOException {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream(resource);
		if (bundleRes == null) {
			throw new NullPointerException("Can not load " + resource);
		}
		String bundleStr = IOUtils.toString(bundleRes);
		return bundleStr;
	}

	public static void purgeDatabase(DaoConfig theDaoConfig, IFhirSystemDao<?,?> theSystemDao, ISearchParamPresenceSvc theSearchParamPresenceSvc, ISearchCoordinatorSvc theSearchCoordinatorSvc, ISearchParamRegistry theSearchParamRegistry) {
		theSearchCoordinatorSvc.cancelAllActiveSearches();

		boolean expungeEnabled = theDaoConfig.isExpungeEnabled();
		theDaoConfig.setExpungeEnabled(true);
		theSystemDao.expunge(new ExpungeOptions().setExpungeEverything(true));
		theDaoConfig.setExpungeEnabled(expungeEnabled);

		theSearchParamPresenceSvc.flushCachesForUnitTest();
		theSearchParamRegistry.forceRefresh();
	}

	public static Set<String> toCodes(Set<TermConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<>();
		for (TermConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	public static Set<String> toCodes(List<VersionIndependentConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<String>();
		for (VersionIndependentConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	public static void waitForSize(int theTarget, List<?> theList) {
		StopWatch sw = new StopWatch();
		while (theList.size() != theTarget && sw.getMillis() <= 15000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= 15000) {
			fail("Size " + theList.size() + " is != target " + theTarget + " - Got: " + theList.toString());
		}
	}

	public static void waitForSize(int theTarget, Callable<Number> theCallable) throws Exception {
		waitForSize(theTarget, 10000, theCallable);
	}

	public static void waitForSize(int theTarget, int theTimeout, Callable<Number> theCallable) throws Exception {
		StopWatch sw = new StopWatch();
		while (theCallable.call().intValue() != theTarget && sw.getMillis() < theTimeout) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= theTimeout) {
			fail("Size " + theCallable.call() + " is != target " + theTarget);
		}
	}

}
