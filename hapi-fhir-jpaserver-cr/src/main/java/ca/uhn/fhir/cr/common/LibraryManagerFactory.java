package ca.uhn.fhir.cr.common;

import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;

import java.util.List;

public interface LibraryManagerFactory {
	LibraryManager create(List<LibrarySourceProvider> libraryContentProviders);
}
