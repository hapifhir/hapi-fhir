package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This is the default converter for expanded resources.
 * If no format defined (or if format is NDJson) we will use this converter.
 */
public class NDJsonConverter implements IResourceConverter {

	private static final Logger ourLog = getLogger(NDJsonConverter.class);

	private final JpaStorageSettings myStorageSettings;

	private final FhirContext myFhirContext;

	public NDJsonConverter(FhirContext theContext, JpaStorageSettings theSettings) {
		myFhirContext = theContext;
		myStorageSettings = theSettings;
	}

	@Override
	public ConvertedFiles consume(BulkExportResourceList theResources, BulkExportJobParameters theJobParameters) {
		IParser parser = getParser();

		ConvertedFiles convertedResources = new ConvertedFiles();

		ListMultimap<String, String> resourceTypeToStringifiedResources = ArrayListMultimap.create();
		Map<String, Integer> resourceTypeToTotalSize = new HashMap<>();
		for (IBaseResource resource : theResources.getResources()) {
			String type = myFhirContext.getResourceType(resource);
			int existingSize = resourceTypeToTotalSize.getOrDefault(type, 0);

			String jsonResource = parser.encodeResourceToString(resource);
			int newSize = existingSize + jsonResource.length();

			// If adding another stringified resource to the list for the given type
			// would exceed the configured maximum allowed, then let's send the current
			// list and flush it. Note that if a single resource exceeds the configurable
			// maximum then we have no choice but to send it
			long bulkExportFileMaximumSize = myStorageSettings.getBulkExportFileMaximumSize();
			if (newSize > bulkExportFileMaximumSize) {
				if (existingSize == 0) {
					// If no files are already in the collection, then this one file
					// is bigger than the maximum allowable. We'll allow it in that
					// case
					ourLog.warn(
						"Single resource size {} exceeds allowable maximum of {}, so will ignore maximum",
						newSize,
						bulkExportFileMaximumSize);
				} else {
					// Otherwise, flush the contents now before adding the next file
					List<String> stringifiedResources = resourceTypeToStringifiedResources.get(type);
					ConvertedFile convertedFile = writeStringifiedResources(type, stringifiedResources);

					resourceTypeToStringifiedResources.removeAll(type);
					newSize = jsonResource.length();
				}
			}

			resourceTypeToStringifiedResources.put(type, jsonResource);
			resourceTypeToTotalSize.put(type, newSize);
		}

		for (String nextResourceType : resourceTypeToStringifiedResources.keySet()) {
			List<String> stringifiedResources = resourceTypeToStringifiedResources.get(nextResourceType);
			ConvertedFile file = writeStringifiedResources(nextResourceType, stringifiedResources);

		}

		return convertedResources;
	}

	private ConvertedFile writeStringifiedResources(String theResourceType, List<String> theStringifiedResources) {
		ConvertedFile file = new ConvertedFile();
		file.setResourceType(theResourceType);
		file.setMimeType(Constants.CT_APP_NDJSON);

		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			try (OutputStreamWriter writer = getStreamWriter(os)) {
				for (String next : theStringifiedResources) {
					writer.append(next);
					writer.append("\n");
				}
			}
			file.setBytes(os.toByteArray());
		} catch (IOException ex) {
			// TODO - better error handling
			// ex can come from either outputstream or streamwriter
			throw new RuntimeException(ex);
		}

		ourLog.info(
			"Expanding of {} resources of type {} completed",
			theStringifiedResources.size(),
			theResourceType);

		return file;
	}

	private IParser getParser() {
		return myFhirContext.newJsonParser().setPrettyPrint(false);
	}

	/**
	 * Returns an output stream writer
	 * (exposed for testing)
	 */
	protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
		return new OutputStreamWriter(theOutputStream, Constants.CHARSET_UTF8);
	}
}
