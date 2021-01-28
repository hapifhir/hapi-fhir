package ca.uhn.fhir.cql.common.evaluation;

import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class LibraryLoaderTest {

	@Mock
	LibraryManager libraryManager;
	@Mock
	ModelManager modelManager;

	@InjectMocks
	LibraryLoader libraryLoader;

	@Test
	public void testGetLibraryManagerReturnsInjectedMock() {
		LibraryManager libraryManager = libraryLoader.getLibraryManager();
		assertNotNull("results of call to getLibraryManager() should NOT be NULL!", libraryManager);
	}

	@Test
	public void testGetModelManagerReturnsInjectedMock() {
		ModelManager modelManager = libraryLoader.getModelManager();
		assertNotNull("results of call to getModelManager() should NOT be NULL!", modelManager);
	}

	@Test
	public void testGetLibrariesReturnsNonNullCollection() {
		Collection<Library> libraries = libraryLoader.getLibraries();
		assertNotNull("results of call to getLibraries() should NOT be NULL!", libraries);
	}
}
