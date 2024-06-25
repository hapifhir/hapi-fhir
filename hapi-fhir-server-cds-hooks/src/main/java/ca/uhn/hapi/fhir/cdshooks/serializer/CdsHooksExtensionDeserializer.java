package ca.uhn.hapi.fhir.cdshooks.serializer;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

public class CdsHooksExtensionDeserializer extends StdDeserializer<CdsHooksExtension> {

	private final CdsServiceRegistryImpl myCdsServiceRegistry;
	private final ObjectMapper myObjectMapper;
	public CdsHooksExtensionDeserializer(CdsServiceRegistryImpl theCdsServiceRegistry, ObjectMapper theObjectMapper) {
		super(CdsHooksExtension.class);
		myCdsServiceRegistry = theCdsServiceRegistry;
		myObjectMapper = theObjectMapper;
	}

	@Override
	public CdsHooksExtension deserialize(JsonParser theJsonParser, DeserializationContext theDeserializationContext)
			throws IOException {
//		CdsServiceJson cdsServiceJson = myCdsServiceRegistry.getCdsService();
//		cdsServiceJson.
//		new ObjectMapper().readValue(theJsonParser.getCodec().readTree(theJsonParser).toString(), MyRequestExtension.class)
		JsonNode rootNode = theJsonParser.getCodec().readTree(theJsonParser);
		/**
		 * getParent(hook)
		 */
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		final String serviceId = request.getRequestURI().replace("/cds-services/", "");
		return new CdsHooksExtension();
	}
}
