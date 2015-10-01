package ca.uhn.fhir.jpa.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;

import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class BaseJpaTest {

	public static String loadClasspath(String resource) throws IOException {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream(resource);
		if (bundleRes == null) {
			throw new NullPointerException("Can not load " + resource);
		}
		String bundleStr = IOUtils.toString(bundleRes);
		return bundleStr;
	}

	@AfterClass
	public static void afterClassShutdownDerby() throws SQLException {
//		try {
//		DriverManager.getConnection("jdbc:derby:memory:myUnitTestDB;drop=true");
//		} catch (SQLNonTransientConnectionException e) {
//			// expected.. for some reason....
//		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected List toList(IBundleProvider theSearch) {
		return theSearch.getResources(0, theSearch.size());
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(IBundleProvider theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		List<IBaseResource> resources = theFound.getResources(0, theFound.size());
		for (IBaseResource next : resources) {
			retVal.add((IIdType) next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(Bundle theFound) {
		List<IIdType> retVal = new ArrayList<IIdType>();
		for (Entry next : theFound.getEntry()) {
//			if (next.getResource()!= null) {
				retVal.add(next.getResource().getId().toUnqualifiedVersionless());
//			}
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

	
}
