package ca.uhn.fhir.jpa.term;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TerminologyLoaderSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvc.class);

	public void loadSnomedCt(byte[] theZipBytes) {
		
		Map<String, TermConcept> id2concept = new HashMap<String, TermConcept>(); 
		
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(theZipBytes));
		try {
			for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null; ) {
				ZippedFileInputStream entryStream = new ZippedFileInputStream(zis);
				byte[] bytes = IOUtils.toByteArray(entryStream);
				ourLog.info("Read file {} - {} bytes", nextEntry.getName(), bytes.length);
				
				String string = new String(bytes, "UTF-8");
				CSVParser parsed = CSVParser.parse(string, CSVFormat.newFormat('\t').withFirstRecordAsHeader());
				ourLog.info("Header map: {}", parsed.getHeaderMap());
			}
		} catch (IOException e) {
			throw new InternalErrorException(e);
		} finally {
			IOUtils.closeQuietly(zis);
		}
	}
	
	
	
	private static class ZippedFileInputStream extends InputStream {

	    private ZipInputStream is;

	    public ZippedFileInputStream(ZipInputStream is){
	        this.is = is;
	    }

	    @Override
	    public int read() throws IOException {
	        return is.read();
	    }

	    @Override
	    public void close() throws IOException {
	        is.closeEntry();
	    }
	}


}
