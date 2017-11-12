package ca.uhn.fhir.jpa.cqf.ruler.config;

import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.dstu3.model.IdType;
import org.opencds.cqf.cql.execution.LibraryLoader;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ca.uhn.fhir.jpa.cqf.ruler.helpers.LibraryHelper.errorsToString;
import static ca.uhn.fhir.jpa.cqf.ruler.helpers.LibraryHelper.readLibrary;
import static ca.uhn.fhir.jpa.cqf.ruler.helpers.LibraryHelper.translateLibrary;

/**
 * Created by Christopher on 1/11/2017.
 */
public class STU3LibraryLoader implements LibraryLoader {

    private LibraryManager libraryManager;
    private ModelManager modelManager;
    private LibraryResourceProvider provider;
    private Map<String, Library> libraries = new HashMap<>();

    public Map<String, Library> getLibraries() {
        return this.libraries;
    }

    public STU3LibraryLoader(LibraryResourceProvider provider, LibraryManager libraryManager, ModelManager modelManager) {
        this.libraryManager = libraryManager;
        this.modelManager = modelManager;
        this.provider = provider;
    }

    private Library resolveLibrary(VersionedIdentifier libraryIdentifier) {
        if (libraryIdentifier == null) {
            throw new IllegalArgumentException("Library identifier is null.");
        }

        if (libraryIdentifier.getId() == null) {
            throw new IllegalArgumentException("Library identifier id is null.");
        }

        Library library = libraries.get(libraryIdentifier.getId());
        if (library != null && libraryIdentifier.getVersion() != null
                && !libraryIdentifier.getVersion().equals(library.getIdentifier().getVersion())) {
            throw new IllegalArgumentException(String.format("Could not load library %s, version %s because version %s is already loaded.",
                    libraryIdentifier.getId(), libraryIdentifier.getVersion(), library.getIdentifier().getVersion()));
        }
        else {
            library = loadLibrary(libraryIdentifier);
            libraries.put(libraryIdentifier.getId(), library);
        }

        return library;
    }

    private Library loadLibrary(VersionedIdentifier libraryIdentifier) {
        IdType id = new IdType(libraryIdentifier.getId());
        org.hl7.fhir.dstu3.model.Library library = provider.getDao().read(id);

        InputStream is = null;
        for (org.hl7.fhir.dstu3.model.Attachment content : library.getContent()) {
            is = new ByteArrayInputStream(content.getData());
            if (content.getContentType().equals("application/elm+xml")) {
                return readLibrary(is);
            }
            else if (content.getContentType().equals("text/cql")) {
                return translateLibrary(is, libraryManager, modelManager);
            }
        }

        org.hl7.elm.r1.VersionedIdentifier identifier = new org.hl7.elm.r1.VersionedIdentifier()
                .withId(libraryIdentifier.getId())
                .withSystem(libraryIdentifier.getSystem())
                .withVersion(libraryIdentifier.getVersion());

        ArrayList<CqlTranslatorException> errors = new ArrayList<>();
        org.hl7.elm.r1.Library translatedLibrary = libraryManager.resolveLibrary(identifier, errors).getLibrary();

        if (errors.size() > 0) {
            throw new IllegalArgumentException(errorsToString(errors));
        }

        try {
            return readLibrary(new ByteArrayInputStream(CqlTranslator.fromStream(is == null ? new ByteArrayInputStream(new byte[]{}) : is, modelManager, libraryManager).convertToXml(translatedLibrary).getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException | IOException e) {
            throw new IllegalArgumentException(String.format("Errors occurred translating library %s%s.",
                    identifier.getId(), identifier.getVersion() != null ? ("-" + identifier.getVersion()) : ""));
        }
    }

    @Override
    public Library load(VersionedIdentifier versionedIdentifier) {
        return resolveLibrary(versionedIdentifier);
    }
}
