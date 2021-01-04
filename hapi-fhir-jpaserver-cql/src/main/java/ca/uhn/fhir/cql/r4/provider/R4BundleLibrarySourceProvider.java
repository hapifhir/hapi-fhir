package ca.uhn.fhir.cql.r4.provider;

import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Library;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

// Expected to be obsolete by mid-2021
@Deprecated
public class R4BundleLibrarySourceProvider extends VersionComparingLibrarySourceProvider {

    Bundle bundle;
    public R4BundleLibrarySourceProvider(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public InputStream getLibrarySource(VersionedIdentifier versionedIdentifier) {
        Objects.requireNonNull(versionedIdentifier, "versionedIdentifier can not be null.");
        
        Library library = this.getLibrary(versionedIdentifier.getId(), versionedIdentifier.getVersion());
        if (library == null ){
            return null;
        }

        return this.getCqlStream(library);
    }

    public Library getLibrary(String name, String version) {
        if (bundle != null) {
            Library theResource = null;
            for (BundleEntryComponent entry : bundle.getEntry()) {
                if (entry.getResource().fhirType().equals("Library")) {
                    theResource = (Library)entry.getResource();
                    if (theResource.getName().equals(name) && theResource.getVersion().equals(version)) {
                        return theResource;
                    }
                }
            }
        }
        return null;
    }

    private InputStream getCqlStream(Library library) {
        if (library.hasContent()) {
            for (Attachment content : library.getContent()) {
                // TODO: Could use this for any content type, would require a mapping from content type to LanguageServer LanguageId
                if (content.getContentType().equals("text/cql")) {
                    return new ByteArrayInputStream(content.getData());
                }
                // TODO: Decompile ELM if no CQL is available?
            }
        }

        return null;
    }
}
