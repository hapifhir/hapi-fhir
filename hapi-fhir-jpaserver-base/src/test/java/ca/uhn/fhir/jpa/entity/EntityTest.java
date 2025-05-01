package ca.uhn.fhir.jpa.entity;

import com.google.common.reflect.ClassPath;
import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class EntityTest {

	private static final String PACKAGE_BASE = "ca.uhn.fhir.jpa.entity";

	private static final Map<String, List<String>> CLASS_TO_FIELD_NAME_EXCEPTIONS = new HashMap<>();

	static {
		CLASS_TO_FIELD_NAME_EXCEPTIONS.put("TermValueSetConceptViewOracle",
			List.of("myConceptOrder"));

		CLASS_TO_FIELD_NAME_EXCEPTIONS.put("ResourceSearchView",
			List.of("myHasTags"));

		CLASS_TO_FIELD_NAME_EXCEPTIONS.put("HapiFhirEnversRevision",
			List.of("myRev"));

		CLASS_TO_FIELD_NAME_EXCEPTIONS.put("Batch2JobInstanceEntity",
			List.of("myProgress", "myErrorCount"));

		CLASS_TO_FIELD_NAME_EXCEPTIONS.put("TermValueSetConceptView",
			List.of("myConceptOrder"));

		CLASS_TO_FIELD_NAME_EXCEPTIONS.put("TermConcept",
			List.of("myCodeSystemVersionPid"));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void nullableFieldsAreNotPrimitivesTest() throws IOException {
		Set<Class> classes = ClassPath.from(ClassLoader.getSystemClassLoader())
			.getAllClasses()
			.stream()
			.filter(clazz -> clazz.getPackageName().startsWith(PACKAGE_BASE))
			.map(ClassPath.ClassInfo::load)
			.collect(Collectors.toSet());

		for (Class clazz : classes) {
			String className = clazz.getName().replace(PACKAGE_BASE + ".", "");
			for (Field field : clazz.getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column == null) {
					continue;
				}

				if (isPrimitiveFieldNullable(className, field, column)) {
					fail(String.format("Column %s on Entity %s is nullable, but has been defined as primitive.",
						field.getName(), className)
						+ " If this is a new field on an existing Table, this can result in deserialization issues when reading old data."
						+ " Either change to a nullable type (Object) or ensure existing data is migrated and add as an exception.");
				}
			}
		}
	}

	private boolean isPrimitiveFieldNullable(String className, Field field, Column column) {
		return column.nullable()
			&& field.getType().isPrimitive()
			&& !(CLASS_TO_FIELD_NAME_EXCEPTIONS.containsKey(className) && CLASS_TO_FIELD_NAME_EXCEPTIONS.get(className).contains(field.getName()));
	}
}
