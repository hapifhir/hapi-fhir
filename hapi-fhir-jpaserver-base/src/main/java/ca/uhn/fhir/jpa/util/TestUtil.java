package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TestUtil {
	private static final int MAX_LENGTH = 30;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestUtil.class);

	/**
	 * non instantiable
	 */
	private TestUtil() {
		super();
	}

	/**
	 * This is really only useful for unit tests, do not call otherwise
	 */
	public static void scanEntities(String packageName) throws IOException, ClassNotFoundException {
		ImmutableSet<ClassInfo> classes = ClassPath.from(TestUtil.class.getClassLoader()).getTopLevelClasses(packageName);
		Set<String> names = new HashSet<String>();

		if (classes.size() <= 1) {
			throw new InternalErrorException("Found no classes");
		}

		for (ClassInfo classInfo : classes) {
			Class<?> clazz = Class.forName(classInfo.getName());
			Entity entity = clazz.getAnnotation(Entity.class);
			if (entity == null) {
				continue;
			}

			scanClass(names, clazz, false);

		}
	}

	private static void scanClass(Set<String> theNames, Class<?> theClazz, boolean theIsSuperClass) {
		ourLog.info("Scanning: {}", theClazz.getSimpleName());

		scan(theClazz, theNames, theIsSuperClass);

		for (Field nextField : theClazz.getDeclaredFields()) {
			ourLog.info(" * Scanning field: {}", nextField.getName());
			scan(nextField, theNames, theIsSuperClass);
		}

		if (theClazz.getSuperclass().equals(Object.class)) {
			return;
		}

		scanClass(theNames, theClazz.getSuperclass(), true);
	}

	private static void scan(AnnotatedElement ae, Set<String> theNames, boolean theIsSuperClass) {
		Table table = ae.getAnnotation(Table.class);
		if (table != null) {
			assertNotADuplicateName(table.name(), theNames);
			for (UniqueConstraint nextConstraint : table.uniqueConstraints()) {
				assertNotADuplicateName(nextConstraint.name(), theNames);
				Validate.isTrue(nextConstraint.name().startsWith("IDX_"), nextConstraint.name() + " must start with IDX_");
			}
			for (Index nextConstraint : table.indexes()) {
				assertNotADuplicateName(nextConstraint.name(), theNames);
				Validate.isTrue(nextConstraint.name().startsWith("IDX_"), nextConstraint.name() + " must start with IDX_");
			}
		}

		JoinColumn joinColumn = ae.getAnnotation(JoinColumn.class);
		if (joinColumn != null) {
			assertNotADuplicateName(joinColumn.name(), null);
			ForeignKey fk = joinColumn.foreignKey();
			if (theIsSuperClass) {
				Validate.isTrue(isBlank(fk.name()), "Foreign key on " + ae.toString() + " has a name() and should not as it is a superclass");
			} else {
				Validate.notNull(fk);
				Validate.isTrue(isNotBlank(fk.name()), "Foreign key on " + ae.toString() + " has no name()");
				Validate.isTrue(fk.name().startsWith("FK_"));
				assertNotADuplicateName(fk.name(), theNames);
			}
		}

		Column column = ae.getAnnotation(Column.class);
		if (column != null) {
			assertNotADuplicateName(column.name(), null);
			Validate.isTrue(column.unique() == false, "Should not use unique attribute on column (use named @UniqueConstraint instead) on " + ae.toString());
		}

		GeneratedValue gen = ae.getAnnotation(GeneratedValue.class);
		SequenceGenerator sg = ae.getAnnotation(SequenceGenerator.class);
		Validate.isTrue((gen != null) == (sg != null));
		if (gen != null) {
			assertNotADuplicateName(gen.generator(), theNames);
			assertNotADuplicateName(sg.name(), null);
			assertNotADuplicateName(sg.sequenceName(), null);
			assertEquals(gen.generator(), sg.name());
			assertEquals(gen.generator(), sg.sequenceName());
		}

	}

	private static void assertEquals(String theGenerator, String theName) {
		Validate.isTrue(theGenerator.equals(theName));
	}

	private static void assertNotADuplicateName(String theName, Set<String> theNames) {
		if (isBlank(theName)) {
			return;
		}
		Validate.isTrue(theName.length() <= MAX_LENGTH, "Identifier \"" + theName + "\" is " + theName.length() + " chars long");
		if (theNames != null) {
			Validate.isTrue(theNames.add(theName), "Duplicate name: " + theName);
		}
	}

	public static void sleepAtLeast(int theMillis) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				ourLog.info("Sleeping for {}ms", timeToSleep);
				Thread.sleep(timeToSleep);
			} catch (InterruptedException theE) {
				theE.printStackTrace();
			}
		}
	}


}
