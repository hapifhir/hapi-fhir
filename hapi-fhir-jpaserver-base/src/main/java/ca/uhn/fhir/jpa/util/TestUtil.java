package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.Subselect;
import org.hibernate.validator.constraints.Length;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.InstantType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Ascii.toUpperCase;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TestUtil {
	public static final int MAX_COL_LENGTH = 2000;
	private static final int MAX_LENGTH = 30;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestUtil.class);
	private static Set<String> ourReservedWords;

	/**
	 * non instantiable
	 */
	private TestUtil() {
		super();
	}

	/**
	 * This is really only useful for unit tests, do not call otherwise
	 */
	@SuppressWarnings("UnstableApiUsage")
	public static void scanEntities(String packageName) throws IOException, ClassNotFoundException {

		try (InputStream is = TestUtil.class.getResourceAsStream("/mysql-reserved-words.txt")) {
			String contents = IOUtils.toString(is, Constants.CHARSET_UTF8);
			String[] words = contents.split("\\n");
			ourReservedWords = Arrays.stream(words)
				.filter(t -> isNotBlank(t))
				.map(t -> toUpperCase(t))
				.collect(Collectors.toSet());
		}

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

		Subselect subselect = theClazz.getAnnotation(Subselect.class);
		boolean isView = (subselect != null);

		scan(theClazz, theNames, theIsSuperClass, isView);

		for (Field nextField : theClazz.getDeclaredFields()) {
			if (Modifier.isStatic(nextField.getModifiers())) {
				continue;
			}

			ourLog.info(" * Scanning field: {}", nextField.getName());
			scan(nextField, theNames, theIsSuperClass, isView);

			Lob lobClass = nextField.getAnnotation(Lob.class);
			if (lobClass != null) {
				if (nextField.getType().equals(byte[].class) == false) {
					//Validate.isTrue(false);
				}
			}

			boolean isTransient = nextField.getAnnotation(Transient.class) != null;
			if (!isTransient) {
				boolean hasColumn = nextField.getAnnotation(Column.class) != null;
				boolean hasJoinColumn = nextField.getAnnotation(JoinColumn.class) != null;
				boolean hasEmbeddedId = nextField.getAnnotation(EmbeddedId.class) != null;
				OneToMany oneToMany = nextField.getAnnotation(OneToMany.class);
				OneToOne oneToOne = nextField.getAnnotation(OneToOne.class);
				boolean isOtherSideOfOneToManyMapping = oneToMany != null && isNotBlank(oneToMany.mappedBy());
				boolean isOtherSideOfOneToOneMapping = oneToOne != null && isNotBlank(oneToOne.mappedBy());
				Validate.isTrue(
					hasColumn ||
						hasJoinColumn ||
						isOtherSideOfOneToManyMapping ||
						isOtherSideOfOneToOneMapping ||
						hasEmbeddedId, "Non-transient has no @Column or @JoinColumn or @EmbeddedId: " + nextField);
			}


		}

		if (theClazz.getSuperclass().equals(Object.class)) {
			return;
		}

		scanClass(theNames, theClazz.getSuperclass(), true);
	}

	private static void scan(AnnotatedElement theAnnotatedElement, Set<String> theNames, boolean theIsSuperClass, boolean theIsView) {
		Table table = theAnnotatedElement.getAnnotation(Table.class);
		if (table != null) {

			// Banned name because we already used it once
			ArrayList<String> bannedNames = Lists.newArrayList("CDR_USER_2FA", "TRM_VALUESET_CODE");
			Validate.isTrue(!bannedNames.contains(table.name().toUpperCase()));

			Validate.isTrue(table.name().toUpperCase().equals(table.name()));

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

		JoinColumn joinColumn = theAnnotatedElement.getAnnotation(JoinColumn.class);
		if (joinColumn != null) {
			String columnName = joinColumn.name();
			validateColumnName(columnName, theAnnotatedElement);

			assertNotADuplicateName(columnName, null);
			ForeignKey fk = joinColumn.foreignKey();
			if (theIsSuperClass) {
				Validate.isTrue(isBlank(fk.name()), "Foreign key on " + theAnnotatedElement.toString() + " has a name() and should not as it is a superclass");
			} else {
				Validate.notNull(fk);
				Validate.isTrue(isNotBlank(fk.name()), "Foreign key on " + theAnnotatedElement.toString() + " has no name()");
				Validate.isTrue(fk.name().startsWith("FK_"));
				assertNotADuplicateName(fk.name(), theNames);
			}
		}

		Column column = theAnnotatedElement.getAnnotation(Column.class);
		if (column != null) {
			String columnName = column.name();
			validateColumnName(columnName, theAnnotatedElement);

			assertNotADuplicateName(columnName, null);
			Validate.isTrue(column.unique() == false, "Should not use unique attribute on column (use named @UniqueConstraint instead) on " + theAnnotatedElement.toString());

			boolean hasLob = theAnnotatedElement.getAnnotation(Lob.class) != null;
			Field field = (Field) theAnnotatedElement;

			/*
			 * For string columns, we want to make sure that an explicit max
			 * length is always specified, and that this max is always sensible.
			 * Unfortunately there is no way to differentiate between "explicitly
			 * set to 255" and "just using the default of 255" so we have banned
			 * the exact length of 255.
			 */
			if (field.getType().equals(String.class)) {
				if (!hasLob) {
					if (!theIsView && column.length() == 255) {
						throw new IllegalStateException("Field does not have an explicit maximum length specified: " + field);
					}
					if (column.length() > MAX_COL_LENGTH) {
						throw new IllegalStateException("Field is too long: " + field);
					}
				}

				Size size = theAnnotatedElement.getAnnotation(Size.class);
				if (size != null) {
					if (size.max() > MAX_COL_LENGTH) {
						throw new IllegalStateException("Field is too long: " + field);
					}
				}

				Length length = theAnnotatedElement.getAnnotation(Length.class);
				if (length != null) {
					if (length.max() > MAX_COL_LENGTH) {
						throw new IllegalStateException("Field is too long: " + field);
					}
				}
			}

		}

		GeneratedValue gen = theAnnotatedElement.getAnnotation(GeneratedValue.class);
		SequenceGenerator sg = theAnnotatedElement.getAnnotation(SequenceGenerator.class);
		Validate.isTrue((gen != null) == (sg != null));
		if (gen != null) {
			assertNotADuplicateName(gen.generator(), theNames);
			assertNotADuplicateName(sg.name(), null);
			assertNotADuplicateName(sg.sequenceName(), null);
			assertEquals(gen.generator(), sg.name());
			assertEquals(gen.generator(), sg.sequenceName());
		}

	}

	private static void validateColumnName(String theColumnName, AnnotatedElement theElement) {
		if (!theColumnName.equals(theColumnName.toUpperCase())) {
			throw new IllegalArgumentException("Column name must be all upper case: " + theColumnName + " found on " + theElement);
		}
		if (ourReservedWords.contains(theColumnName)) {
			throw new IllegalArgumentException("Column name is a reserved word: " + theColumnName + " found on " + theElement);
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

	public static void sleepAtLeast(long theMillis) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				ourLog.info("Sleeping for {}ms", timeToSleep);
				Thread.sleep(timeToSleep);
			} catch (InterruptedException theE) {
				ourLog.error("Interrupted", theE);
			}
		}
	}


	public static void clearAllStaticFieldsForUnitTest() {
		ca.uhn.fhir.util.TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static InstantType getTimestamp(IBaseResource resource) {
		return new InstantType(new Date(resource.getMeta().getLastUpdated().getTime()));
	}

	public static void sleepOneClick() {
		sleepAtLeast(1);
	}


}
