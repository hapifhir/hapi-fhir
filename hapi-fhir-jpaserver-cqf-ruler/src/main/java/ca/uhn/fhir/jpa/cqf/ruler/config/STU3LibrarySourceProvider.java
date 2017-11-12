package ca.uhn.fhir.jpa.cqf.ruler.config;

import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.fhir.dstu3.model.IdType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Christopher on 1/12/2017.
 */
public class STU3LibrarySourceProvider implements LibrarySourceProvider {

    private LibraryResourceProvider provider;

    public STU3LibrarySourceProvider(LibraryResourceProvider provider) {
        this.provider = provider;
    }

    @Override
    public InputStream getLibrarySource(VersionedIdentifier versionedIdentifier) {
        IdType id = new IdType(versionedIdentifier.getId());
        org.hl7.fhir.dstu3.model.Library lib = provider.getDao().read(id);
        for (org.hl7.fhir.dstu3.model.Attachment content : lib.getContent()) {
            if (content.getContentType().equals("text/cql")) {
                return new ByteArrayInputStream(content.getData());
            }
        }

        throw new IllegalArgumentException(String.format("Library %s%s does not contain CQL source content.", versionedIdentifier.getId(),
                versionedIdentifier.getVersion() != null ? ("-" + versionedIdentifier.getVersion()) : ""));
    }
}
