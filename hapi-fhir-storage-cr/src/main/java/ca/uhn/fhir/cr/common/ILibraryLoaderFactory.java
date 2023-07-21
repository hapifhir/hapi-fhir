package ca.uhn.fhir.cr.common;

import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;

import java.util.List;

public interface ILibraryLoaderFactory {
	LibraryLoader create(List<LibrarySourceProvider> theLibraryContentProviders);
}
