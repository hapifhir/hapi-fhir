package ca.uhn.fhirtest;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class UhnFhirTestApp {

	public static void main(String[] args) throws Exception {

		int myPort = 8888;
		String base = "http://localhost:" + myPort + "/base";
		
		//		new File("target/testdb").mkdirs();
		System.setProperty("fhir.db.location", "./target/testdb");
		System.setProperty("fhir.db.location.dstu2", "./target/testdb_dstu2");
		System.setProperty("fhir.baseurl.dstu1", base + "Dstu1");
		System.setProperty("fhir.baseurl.dstu2", base + "Dstu1");
		
		Server server = new Server(myPort);

		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor("target/hapi-fhir-jpaserver/WEB-INF/web.xml");
		root.setResourceBase("target/hapi-fhir-jpaserver");

		root.setParentLoaderPriority(true);

		server.setHandler(root);

		server.start();

		// base = "http://fhir.healthintersections.com.au/open";
		// base = "http://spark.furore.com/fhir";

		if (true) {
			FhirContext ctx = new FhirContext();
			IGenericClient client = ctx.newRestfulGenericClient(base);
//			client.setLogRequestAndResponse(true);

			Organization o1 = new Organization();
			o1.getName().setValue("Some Org");
			MethodOutcome create = client.create(o1);
			IdDt orgId = (IdDt) create.getId();

			Patient p1 = new Patient();
			p1.addIdentifier("foo:bar", "12345");
			p1.addName().addFamily("Smith").addGiven("John");
			p1.getManagingOrganization().setReference(orgId);

			TagList list = new TagList();
			list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);
			client.create(p1);

			List<IResource> resources = ctx.newJsonParser().parseBundle(IOUtils.toString(UhnFhirTestApp.class.getResourceAsStream("/bundle.json"))).toListOfResources();
			client.transaction().withResources(resources).execute();

//			for (int i = 0; i < 1000; i++) {
//				
//				Patient p = (Patient) resources.get(0);
//				p.addName().addFamily("Transaction"+i);
//				
//				ourLog.info("Transaction count {}", i);
//				client.transaction(resources);
//			}

			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);
			client.create(p1);

			client.create(p1);

		}

	}

}
