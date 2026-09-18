package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.util.CsvUtil;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import ca.uhn.fhir.rest.api.server.bulk.IResourceConverter;
import org.apache.commons.csv.CSVPrinter;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.util.Map;

public class BulkExportCSVConverter implements IResourceConverter {

	/**
	 * Interface to allow the creator to define how a resource
	 * should be written to the csvprinter
	 */
	public interface IResourceToCSV {
		/**
		 * Write the provided resource into the provided CSVPrinter
		 * to match with the headers provided.
		 *
		 * Each resource is different so it's on the caller to define how
		 * resources are printed.
		 */
		void process(CSVPrinter thePrinter, IBaseResource theResource) throws IOException;
	}

	/**
	 * A map of resourcetype -> list of headers (in csv)
	 */
	private final Map<String, String[]> myHeadersMap;

	/**
	 * The processor.
	 */
	private final IResourceToCSV myCsvPrinter;

	public BulkExportCSVConverter(Map<String, String[]> theResourceTypeToHeaders,
								  IResourceToCSV thePrinter) {
		myHeadersMap = theResourceTypeToHeaders;
		myCsvPrinter = thePrinter;
	}

	@Override
	public ConvertedFiles consume(BulkExportResourceList theResources, BulkExportJobParameters theJobParameters) {
		ConvertedFiles files = new ConvertedFiles();

		ConvertedFile file = new ConvertedFile();
		if (!theResources.getResources().isEmpty()) {
			// each invocation will have the same resources
			file.setResourceType(theResources.getResources().get(0).fhirType());
		}
		file.setMimeType("text/csv");

		/*
		 * each resource will have to be parsed on its own into
		 * its own csv
		 */
		String resourceType = file.getResourceType();
		byte[] bytes = CsvUtil.writeCsvToByteArray(
			myHeadersMap.get(resourceType),
			printer -> {
				for (IBaseResource resource : theResources.getResources()) {
					myCsvPrinter.process(printer, resource);
				}
			}
		);
		file.setBytes(bytes);

		// we can control how many of these we want per input resources.
		// but there's only a small number of resources so
		// we'll only return the 1 file per resource input
		files.addFile(file);

		return files;
	}
}
