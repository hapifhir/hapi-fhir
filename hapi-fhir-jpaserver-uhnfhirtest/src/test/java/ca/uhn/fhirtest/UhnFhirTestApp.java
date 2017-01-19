package ca.uhn.fhirtest;

import java.util.UUID;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class UhnFhirTestApp {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UhnFhirTestApp.class);

	public static void main(String[] args) throws Exception {

		int myPort = 8888;
		String base = "http://localhost:" + myPort + "/baseDstu2";

		//		new File("target/testdb").mkdirs();
		System.setProperty("fhir.db.location", "./target/testdb");
		System.setProperty("fhir.db.location.dstu2", "./target/testdb_dstu2");
		System.setProperty("fhir.lucene.location.dstu2", "./target/testlucene_dstu2");
		System.setProperty("fhir.db.location.dstu3", "./target/fhirtest_dstu3");
		System.setProperty("fhir.lucene.location.dstu3", "./target/testlucene_dstu3");
		System.setProperty("fhir.db.location.tdl2", "./target/testdb_tdl2");
		System.setProperty("fhir.lucene.location.tdl2", "./target/testlucene_tdl2");
		System.setProperty("fhir.db.location.tdl3", "./target/testdb_tdl3");
		System.setProperty("fhir.lucene.location.tdl3", "./target/testlucene_tdl3");
		System.setProperty("fhir.baseurl.dstu1", base.replace("Dstu2", "Dstu1"));
		System.setProperty("fhir.baseurl.dstu2", base);
		System.setProperty("fhir.baseurl.dstu3", base.replace("Dstu2", "Dstu3"));
		System.setProperty("fhir.baseurl.tdl2", base.replace("baseDstu2", "testDataLibraryDstu2"));
		System.setProperty("fhir.baseurl.tdl3", base.replace("baseDstu2", "testDataLibraryStu3"));
		System.setProperty("fhir.tdlpass", "aa,bb");
		
		Server server = new Server(myPort);

		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor("src/main/webapp/WEB-INF/web.xml");
		root.setResourceBase("target/hapi-fhir-jpaserver");

		root.setParentLoaderPriority(true);

		server.setHandler(root);

		try {
			server.start();
		} catch (Exception e) {
			ourLog.error("Failure during startup", e);
		}

		// base = "http://fhir.healthintersections.com.au/open";
		// base = "http://spark.furore.com/fhir";

		if (false) {
			FhirContext ctx = FhirContext.forDstu3();
			IGenericClient client = ctx.newRestfulGenericClient(base);
			//			client.setLogRequestAndResponse(true);

			Organization o1 = new Organization();
			o1.getNameElement().setValue("Some Org");
			MethodOutcome create = client.create().resource(o1).execute();
			IIdType orgId = (IIdType) create.getId();

			Patient p1 = new Patient();
			p1.getMeta().addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
			p1.addIdentifier().setSystem("foo:bar").setValue("12345");
			p1.addName().setFamily("Smith").addGiven("John");
			p1.getManagingOrganization().setReferenceElement(orgId.toUnqualifiedVersionless());

			Subscription subs = new Subscription();
			subs.setStatus(SubscriptionStatus.ACTIVE);
			subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
			subs.setCriteria("Observation?");
			client.create().resource(subs).execute();

			//			for (int i = 0; i < 1000; i++) {
			//				
			//				Patient p = (Patient) resources.get(0);
			//				p.addName().addFamily("Transaction"+i);
			//				
			//				ourLog.info("Transaction count {}", i);
			//				client.transaction(resources);
			//			}

			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();
			client.create().resource(p1).execute();

			client.create().resource(p1).execute();

		}

	}
}
