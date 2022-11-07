package ca.uhn.fhir.cr.common.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.cqframework.cql.cql2elm.CqlCompilerException;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.tracking.TrackBack;

public class Translators {

	private Translators() {
	}

	public static String errorsToString(Iterable<CqlCompilerException> exceptions) {
		ArrayList<String> errors = new ArrayList<>();
		for (CqlCompilerException error : exceptions) {
			TrackBack tb = error.getLocator();
			String lines = tb == null ? "[n/a]"
				: String.format("%s[%d:%d, %d:%d]",
				(tb.getLibrary() != null ? tb.getLibrary().getId()
					+ (tb.getLibrary().getVersion() != null ? ("-" + tb.getLibrary().getVersion()) : "")
					: ""),
				tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
			errors.add(lines + error.getMessage());
		}

		return errors.toString();
	}

	public static CqlTranslator getTranslator(InputStream cqlStream, LibraryManager libraryManager,
															ModelManager modelManager, CqlTranslatorOptions cqlTranslatorOptions) {
		CqlTranslator translator;
		try {
			translator = CqlTranslator.fromStream(cqlStream, modelManager,
				libraryManager,
				cqlTranslatorOptions.getOptions()
					.toArray(new CqlTranslatorOptions.Options[cqlTranslatorOptions.getOptions().size()]));
		} catch (IOException e) {
			throw new IllegalArgumentException(
				String.format("Errors occurred translating library: %s", e.getMessage()));
		}

		return translator;
	}
}
