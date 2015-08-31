package ca.uhn.fhir.jpa.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;

import ca.uhn.fhir.rest.server.IBundleProvider;

public class BaseJpaTest {

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
		for (IBaseResource next : theFound.getResources(0, theFound.size())) {
			retVal.add((IIdType) next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	
}
