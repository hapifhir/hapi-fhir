package ca.uhn.fhir.jpa.bulk.imp.job;

import ca.uhn.fhir.jpa.bulk.imp.model.ResourceListChunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class BulkImportFileWriter implements ItemWriter<ResourceListChunk> {
	@Override
	public void write(List<? extends ResourceListChunk> theItems) throws Exception {
		for (ResourceListChunk next : theItems) {

		}
	}
}
