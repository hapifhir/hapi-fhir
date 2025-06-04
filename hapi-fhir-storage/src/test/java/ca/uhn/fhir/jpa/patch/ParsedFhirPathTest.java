package ca.uhn.fhir.jpa.patch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ParsedFhirPathTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParsedFhirPathTest.class);

	@Test
	public void parseSimple() {
		// setup
		String path = "Patient.name.family";

		// test
		ParsedFhirPath parsedPath = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsedPath);
		assertEquals(path, parsedPath.getRawPath());

		validateList(parsedPath, List.of("Patient", "name", "family"), node -> {});
	}

	@Test
	public void parseWithIndex() {
		// setup
		String path = "Patient.name.given[1]";

		// test
		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsedFhirPath);
		assertEquals(path, parsedFhirPath.getRawPath());
		assertTrue(parsedFhirPath.endsWithAnIndex());

		validateList(parsedFhirPath, List.of("Patient", "name", "given", "[1]"), node -> {
			if (node.getValue().equals("[1]]")) {
				assertTrue(node.hasListIndex());
				assertEquals(1, node.getListIndex());
			}
		});
	}

	@Test
	public void parseWithIndexOnFilter() {
		// setup
		String path = "Appointment.participant.actor.where(reference.startsWith('Patient'))[0]";

		// test
		ParsedFhirPath parsedPath = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsedPath);
		assertEquals(path, parsedPath.getRawPath());
		assertTrue(parsedPath.endsWithAnIndex());
		assertEquals(0, parsedPath.getTail().getListIndex());

		AtomicReference<Consumer<ParsedFhirPath.FhirPathNode>> atomicRef = new AtomicReference<>();
		Consumer<ParsedFhirPath.FhirPathNode> supplier = node -> {
			List<String> subList;
			if (node.getValue().equals("where")) {
				subList = List.of("reference", "startsWith");
			} else if (node.getValue().equals("first")) {
				subList = List.of();
			} else if (node.getValue().equals("startsWith")) {
				subList = List.of("'Patient'");
			} else {
				subList = List.of();
			}

			if (!subList.isEmpty()) {
				assertTrue(node instanceof ParsedFhirPath.FhirPathFunction);
				ParsedFhirPath.FhirPathFunction fn = (ParsedFhirPath.FhirPathFunction) node;
				assertNotNull(fn.getContainedExp());

				validateList(fn.getContainedExp(), subList, atomicRef.get());
			}
		};
		atomicRef.set(supplier);

		validateList(parsedPath, List.of("Appointment", "participant", "actor", "where", "[0]"), atomicRef.get());
	}

	@Test
	public void parseSimpleNestedExpression() {
		// setup
		String path = "Appointment.participant.actor.reference.where(startsWith('Patient')).first()";

		// test
		ParsedFhirPath parsedPath = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsedPath);
		assertEquals(path, parsedPath.getRawPath());
		assertTrue(parsedPath.endsWithAFilter());

		AtomicReference<Consumer<ParsedFhirPath.FhirPathNode>> atomicRef = new AtomicReference<>();
		Consumer<ParsedFhirPath.FhirPathNode> supplier = node -> {
			List<String> subList;
			if (node.getValue().equals("where")) {
				subList = List.of("startsWith");
			} else if (node.getValue().equals("first")) {
				subList = List.of();
			} else if (node.getValue().equals("startsWith")) {
				subList = List.of("'Patient'");
			} else {
				subList = List.of();
			}

			if (!subList.isEmpty()) {
				assertTrue(node instanceof ParsedFhirPath.FhirPathFunction);
				ParsedFhirPath.FhirPathFunction fn = (ParsedFhirPath.FhirPathFunction) node;
				assertNotNull(fn.getContainedExp());

				validateList(fn.getContainedExp(), subList, atomicRef.get());
			}
		};
		atomicRef.set(supplier);

		validateList(parsedPath,
			List.of("Appointment", "participant", "actor", "reference", "where", "first"),
			supplier);
	}

	@Test
	public void parse_filterWithValue_works() {
		// setup
		String path = "Patient.name.given.skip(2)";

		// test
		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsedFhirPath);
		assertEquals(path, parsedFhirPath.getRawPath());
		assertTrue(parsedFhirPath.endsWithFilterOrIndex());

		validateList(parsedFhirPath, List.of("Patient", "name", "given", "skip"), n -> {
			if (n.getValue().equals("skip")) {
				assertTrue(n instanceof ParsedFhirPath.FhirPathFunction);
				ParsedFhirPath.FhirPathFunction f = (ParsedFhirPath.FhirPathFunction) n;
				assertEquals("2", f.getContainedExp().getRawPath());
				assertEquals("2", f.getContainedExp().getHead().getValue());
			}
		});
	}

	@Test
	public void parseComplexNestedExpression() {
		// setup
		String path = "Appointment.participant.actor.where(reference.startsWith('Patient')).first()";

		// test
		ParsedFhirPath parsedPath = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsedPath);
		assertEquals(path, parsedPath.getRawPath());
		assertTrue(parsedPath.endsWithAFilter());

		AtomicReference<Consumer<ParsedFhirPath.FhirPathNode>> atomicRef = new AtomicReference<>();
		Consumer<ParsedFhirPath.FhirPathNode> supplier = node -> {
			List<String> subList;
			if (node.getValue().equals("where")) {
				subList = List.of("reference", "startsWith");
			} else if (node.getValue().equals("first")) {
				subList = List.of();
			} else if (node.getValue().equals("startsWith")) {
				subList = List.of("'Patient'");
			} else {
				subList = List.of();
			}

			if (!subList.isEmpty()) {
				assertTrue(node instanceof ParsedFhirPath.FhirPathFunction);
				ParsedFhirPath.FhirPathFunction fn = (ParsedFhirPath.FhirPathFunction) node;
				assertNotNull(fn.getContainedExp());

				validateList(fn.getContainedExp(), subList, atomicRef.get());
			}
		};
		atomicRef.set(supplier);

		validateList(parsedPath,
			List.of("Appointment", "participant", "actor", "where", "first"),
			supplier);
	}

	@Test
	public void testingHeadAndTail() {
		// setup
		String path = "Appointment.participant.actor.where(reference.startsWith('Patient/'))";

		// test
		ParsedFhirPath parsed = ParsedFhirPath.parse(path);

		// validate
		assertNotNull(parsed.getHead());
		assertNotNull(parsed.getTail());
	}

	static Stream<Arguments> getLastElementNameParams() {
		return Stream.of(
			Arguments.of("Appointment.participant.actor.where(reference.startsWith('Patient'))", "reference"),
			Arguments.of("Patient.name.given.first()", "given"),
			Arguments.of("Patient.name.given[1]", "given")
		);
	}

	@ParameterizedTest
	@MethodSource("getLastElementNameParams")
	public void getLastElementName_basicTest(String thePath, String theLastElement) {
		// setup
		ParsedFhirPath parsed = ParsedFhirPath.parse(thePath);

		ParsedFhirPath.FhirPathNode n = parsed.getFinalPathNode();
		assertEquals(theLastElement, parsed.getLastElementName());

	}

	@Test
	public void getPathUntilPreCondition_basicTest() {
		// setup
		String subpath;
		String path = "Appointment.participant.actor.reference.startsWith('Patient')";

		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);

		// tests
		subpath = parsedFhirPath.getPathUntilPreCondition(ParsedFhirPath.FhirPathNode::isFunction);
		assertEquals("Appointment.participant.actor.reference", subpath);

		subpath = parsedFhirPath.getPathUntilPreCondition(n -> n.getNext() == parsedFhirPath.getTail());
		assertEquals("Appointment.participant.actor", subpath);
	}

	private void validateList(ParsedFhirPath theParsedPath, List<String> theParts, Consumer<ParsedFhirPath.FhirPathNode> thePerNodeAction) {
		ParsedFhirPath.FhirPathNode current = null;
		ParsedFhirPath.FhirPathNode previous = null;

		assertNotNull(theParsedPath.getHead());
		assertNotNull(theParsedPath.getTail());

		current = theParsedPath.getHead();
		for (String part : theParts) {
			assertNotNull(current, "Next value is null when expected " + part);
			assertEquals(previous, current.getPrevious(), "Previous node does not match");
			assertEquals(part, current.getValue());

			// for additional validation on the element
			thePerNodeAction.accept(current);

			// update current to next
			previous = current;
			current = current.getNext();
		}

		// verify that there is no next
		assertNull(current);
	}

	@Test
	public void parse_withListPath_works() {
		// setup
		String path = "Patient.name.given[1]";

		// test
		ParsedFhirPath parsed = ParsedFhirPath.parse(path);

		assertTrue(parsed.endsWithAnIndex());

		assertTrue(parsed.getTail().hasListIndex());
	}
}
