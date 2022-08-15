package ca.uhn.fhir.cql.common.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.LibrarySourceLoader;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.NamespaceManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.hl7.elm.r1.VersionedIdentifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslatorHelperTest implements CqlProviderTestBase {
	private static String sampleCql = "library ASF_FHIR version '1.0.0'\n" +
		"using FHIR version '4.0.0'\n";

	@Mock
	LibraryManager libraryManager;
	@Mock
	ModelManager modelManager;
	@Mock
	NamespaceManager namespaceManager;
	@Mock
	LibrarySourceLoader librarySourceLoader;

	//LibraryManager libraryManager;
	//ModelManager modelManager;
	//NamespaceManager namespaceManager;
	//LibrarySourceLoader librarySourceLoader;

	//@BeforeEach
	//@BeforeAll
	@BeforeEach
	public void createMocks() {
		MockitoAnnotations.openMocks(this);
		//libraryManager = Mockito.mock(LibraryManager.class);
		//modelManager = Mockito.mock(ModelManager.class);
		//namespaceManager = Mockito.mock(NamespaceManager.class);
		//librarySourceLoader = Mockito.mock(LibrarySourceLoader.class);
	}

	//@AfterAll
	//@After
	@AfterEach
	public void resetMocks() {
		reset(libraryManager);
		reset(modelManager);
		reset(namespaceManager);
		reset(librarySourceLoader);
	}

	@Test
	public void testGetTranslatorWhereNoNamespaces() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(namespaceManager.hasNamespaces()).thenReturn(false);
		CqlTranslator translator = TranslatorHelper.getTranslator(sampleCql, libraryManager, modelManager);
		assertNotNull(translator, "translator should not be NULL!");
	}

	//@Test
	// NOTE: This runs OK by itself, but always fails when running as part of this Class!
	public void testGetTranslatorWhereHasNamespaces() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(libraryManager.getLibrarySourceLoader()).thenReturn(librarySourceLoader);
		when(namespaceManager.hasNamespaces()).thenReturn(true);
		CqlTranslator translator = TranslatorHelper.getTranslator(sampleCql, libraryManager, modelManager);
		assertNotNull(translator, "translator should not be NULL!");
	}

	//@Test
	// NOTE: This runs OK by itself, but always fails when running as part of this Class!
	public void testGetNullPointerExceptionFromGetTranslatorWhenCqlIsBlankString() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(libraryManager.getLibrarySourceLoader()).thenReturn(librarySourceLoader);
		when(namespaceManager.hasNamespaces()).thenReturn(true);
		CqlTranslator translator = null;
		try {
			translator = TranslatorHelper.getTranslator("   ", libraryManager, modelManager);
			fail();
		} catch (NullPointerException e) {
			assertNull(translator, "translator should be NULL!");
		}
	}

	//@Test
	// NOTE: This one Fails when using the mockito-core library. It wants the mockito-inline library instead,
	// but when we replace the -core with teh -inline one, it causes failures in the hapi-fhir-android project!
	//    The used MockMaker SubclassByteBuddyMockMaker does not support the creation of static mocks
	//    Mockito's inline mock maker supports static mocks based on the Instrumentation API.
	//    You can simply enable this mock mode, by placing the 'mockito-inline' artifact where you are currently using 'mockito-core'.
	//    Note that Mockito's inline mock maker is not supported on Android.
	public void testGetIllegalArgumentExceptionFromGetTranslatorWhenCqlIsInvalid() {
		ArrayList<CqlTranslator.Options> options = new ArrayList<>();
		options.add(CqlTranslator.Options.EnableAnnotations);
		options.add(CqlTranslator.Options.EnableLocators);
		options.add(CqlTranslator.Options.DisableListDemotion);
		options.add(CqlTranslator.Options.DisableListPromotion);
		options.add(CqlTranslator.Options.DisableMethodInvocation);
		CqlTranslator translator = null;
		try {
			MockedStatic<CqlTranslator> cqlTranslator = mockStatic(CqlTranslator.class);
			when(CqlTranslator.fromStream(any(InputStream.class), any(ModelManager.class), any(LibraryManager.class), any())).thenThrow(IOException.class);
			translator = TranslatorHelper.getTranslator(new ByteArrayInputStream("INVALID-FILENAME".getBytes(StandardCharsets.UTF_8)), libraryManager, modelManager);
			fail();
		} catch (IllegalArgumentException | IOException e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertNull(translator, "translator should be NULL!");
		}
	}

	@Test
	public void testFromTranslateLibrary() {
		when(libraryManager.getNamespaceManager()).thenReturn(namespaceManager);
		when(libraryManager.getLibrarySourceLoader()).thenReturn(librarySourceLoader);
		when(namespaceManager.hasNamespaces()).thenReturn(true);
		ArrayList<CqlTranslator.Options> options = new ArrayList<>();
		options.add(CqlTranslator.Options.EnableAnnotations);
		options.add(CqlTranslator.Options.EnableLocators);
		options.add(CqlTranslator.Options.DisableListDemotion);
		options.add(CqlTranslator.Options.DisableListPromotion);
		options.add(CqlTranslator.Options.DisableMethodInvocation);
		Library library = null;
		try {
			library = TranslatorHelper.translateLibrary("INVALID-FILENAME", libraryManager, modelManager);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(library, "library should not be NULL!");
	}

	@Test
	public void testGetIllegalArgumentExceptionFromReadLibraryWhenCqlIsBlankString() {
		Library library = null;
		try {
			library = TranslatorHelper.readLibrary(new ByteArrayInputStream("INVALID-XML-DOCUMENT".getBytes()));
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(library, "library should be NULL!");
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
		assertNotNull(library, "library should not be NULL!");
	}

	@Test
	public void testErrorsToStringWithEmptyList() {
		ArrayList<CqlTranslatorException> errors = new ArrayList<>();
		String result = TranslatorHelper.errorsToString(errors);
		assertTrue(StringUtils.isEmpty(result));
	}

	@Test
	public void testErrorsToStringWithNonEmptyList() {
		ArrayList<CqlTranslatorException> errors = new ArrayList<>();
		CqlTranslatorException e = new CqlTranslatorException("Exception");
		TrackBack trackBack = new TrackBack(new VersionedIdentifier(), 0, 0, 0, 0);
		e.setLocator(trackBack);
		errors.add(e);
		String result = TranslatorHelper.errorsToString(errors);
		assertTrue(!StringUtils.isEmpty(result));
		assertEquals("null [0:0, 0:0] Exception", result);
	}

	@Override
	public FhirContext getTestFhirContext() {
		return null;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return null;
	}
}
