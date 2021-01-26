package ca.uhn.fhir.cql.common.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.LibrarySourceLoader;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.NamespaceManager;
import org.cqframework.cql.elm.execution.Library;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.stream.XMLInputFactory;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslatorHelperTest implements CqlProviderTestBase {
	private static String sampleCql = "library ASF_FHIR version '1.0.0'\n" +
		"using FHIR version '4.0.0'\n";

	@Mock
	LibraryManager libraryManager;
	@Mock
	NamespaceManager namespaceManager;
	@Mock
	LibrarySourceLoader librarySourceLoader;
	@Mock
	ModelManager modelManager;

	@Test
	public void testGetTranslatorWhereNoNamespaces() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(namespaceManager.hasNamespaces()).thenReturn(false);
		CqlTranslator translator = TranslatorHelper.getTranslator(sampleCql, libraryManager, modelManager);
		assertNotNull("translator should not be NULL!", translator);
	}

	@Test
	public void testGetTranslatorWhereHasNamespaces() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(libraryManager.getLibrarySourceLoader()).thenReturn(librarySourceLoader);
		when(namespaceManager.hasNamespaces()).thenReturn(true);
		CqlTranslator translator = TranslatorHelper.getTranslator(sampleCql, libraryManager, modelManager);
		assertNotNull("translator should not be NULL!", translator);
	}

	@Test
	public void testGetNullPointerExceptionWhenCqlIsBlankString() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(libraryManager.getLibrarySourceLoader()).thenReturn(librarySourceLoader);
		when(namespaceManager.hasNamespaces()).thenReturn(true);
		CqlTranslator translator = null;
		try {
			translator = TranslatorHelper.getTranslator("   ", libraryManager, modelManager);
			fail();
		} catch(NullPointerException e) {
			assertNull("translator should be NULL!", translator);
		}
	}

	@Test
	public void testTranslatorReadLibrary() {
		Library library = null;
		try {
			library = TranslatorHelper.readLibrary(new ByteArrayInputStream(stringFromResource("dstu3/hedis-ig/library-asf-elm.xml").getBytes()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull("library should not be NULL!", library);
	}

	@Override
	public FhirContext getFhirContext() {
		return null;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return null;
	}
}
