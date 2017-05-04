package ca.uhn.fhir.jpa.util;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TestUtil {
	private static final int MAX_LENGTH = 30;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestUtil.class);

	/** non instantiable */
	private TestUtil() {
		super();
	}
	
	/**
	 * This is really only useful for unit tests, do not call otherwise
	 */
	public static void scanEntities(String packageName) throws IOException, ClassNotFoundException {
		ImmutableSet<ClassInfo> classes = ClassPath.from(TestUtil.class.getClassLoader()).getTopLevelClasses(packageName);
		
		if (classes.size() <= 1) {
			throw new InternalErrorException("Found no classes");
		}
		
		for (ClassInfo classInfo : classes) {
			Class<?> clazz = Class.forName(classInfo.getName());
			Entity entity = clazz.getAnnotation(Entity.class);
			if (entity == null) {
				continue;
			}
			
			ourLog.info("Scanning: {}", clazz.getSimpleName());
			
			scan(clazz);
			
			for (Field nextField : clazz.getFields()) {
				scan(nextField);
			}
			
		}
	}

	private static void scan(AnnotatedElement ae) {
		Table table = ae.getAnnotation(Table.class);
		if (table != null) {
			assertThat(table.name());
			for (UniqueConstraint nextConstraint : table.uniqueConstraints()) {
				assertThat(nextConstraint.name());
			}
			for (Index nextConstraint : table.indexes()) {
				assertThat(nextConstraint.name());
			}
		}
		
		JoinColumn joinColumn = ae.getAnnotation(JoinColumn.class);
		if (joinColumn != null) {
			assertThat(joinColumn.name());
			ForeignKey fk = joinColumn.foreignKey();
			assertThat(fk.name());
		}

		Column column = ae.getAnnotation(Column.class);
		if (column != null) {
			assertThat(column.name());
		}

		GeneratedValue gen = ae.getAnnotation(GeneratedValue.class);
		if (gen != null) {
			assertThat(gen.generator());
			SequenceGenerator sg = ae.getAnnotation(SequenceGenerator.class);
			assertThat(sg.name());
			assertThat(sg.sequenceName());
			assertEquals(gen.generator(), sg.name());
			assertEquals(gen.generator(), sg.sequenceName());
		}

	}

	private static void assertEquals(String theGenerator, String theName) {
		Validate.isTrue(theGenerator.equals(theName));
	}

	private static void assertThat(String theName) {
		Validate.isTrue(theName.length() <= MAX_LENGTH, "Identifier \"" + theName + "\" is " + theName.length() + " chars long");
	}

}
