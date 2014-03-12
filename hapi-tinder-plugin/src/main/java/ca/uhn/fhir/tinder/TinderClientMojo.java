package ca.uhn.fhir.tinder;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.ParseException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;

@Mojo(name = "generate-client", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderClientMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderClientMojo.class);

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
	}


	public static void main(String[] args) throws ParseException, IOException, MojoFailureException {
		
//		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
//		HttpClientBuilder builder = HttpClientBuilder.create();
//		builder.setConnectionManager(connectionManager);
//		CloseableHttpClient client = builder.build();
//
//		HttpGet get = new HttpGet("http://fhir.healthintersections.com.au/open/metadata");
//		CloseableHttpResponse response = client.execute(get);
//		
//		String metadataString = EntityUtils.toString(response.getEntity());
//		
//		ourLog.info("Metadata String: {}", metadataString);

		String metadataString = IOUtils.toString(new FileInputStream("src/test/resources/healthintersections-metadata.xml"));
		Conformance conformance = new FhirContext(Conformance.class).newXmlParser().parseResource(Conformance.class, metadataString);
		
		if (conformance.getRest().size() != 1) {
			throw new MojoFailureException("Found "+conformance.getRest().size()+" rest definitions in Conformance resource. Need exactly 1.");
		}

		Rest rest = conformance.getRest().get(0);
		if (rest.getMode().getValueAsEnum() != RestfulConformanceModeEnum.SERVER) {
			throw new MojoFailureException("Conformance mode is not server, found: " + rest.getMode().getValue());
		}
		
		
		
	}

}
