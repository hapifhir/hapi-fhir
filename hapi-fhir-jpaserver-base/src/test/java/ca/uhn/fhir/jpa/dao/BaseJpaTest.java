package ca.uhn.fhir.jpa.dao;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;

import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class BaseJpaTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaTest.class);
	protected ServletRequestDetails mySrd;

	@SuppressWarnings({ "rawtypes" })
	protected List toList(IBundleProvider theSearch) {
		return theSearch.getResources(0, theSearch.size());
	}

	@Before
	public void beforeCreateSrd() {
		mySrd = mock(ServletRequestDetails.class);
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

	protected List<IIdType> toUnqualifiedVersionlessIds(org.hl7.fhir.dstu3.model.Bundle theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (BundleEntryComponent next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(IBundleProvider theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		int size = theFound.size();
		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add((IIdType) next.getIdElement().toUnqualifiedVersionless());
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
	
	protected List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound) {
		List<String> retVal = new ArrayList<String>();
		int size = theFound.size();
		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(List<IBaseResource> theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (IBaseResource next : theFound) {
			retVal.add((IIdType) next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	@AfterClass
	public static void afterClassShutdownDerby() throws SQLException {
//		DriverManager.getConnection("jdbc:derby:;shutdown=true");
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

}
