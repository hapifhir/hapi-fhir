/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.jpa;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.base.Ascii;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.reflect.ClassPath;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Subselect;
import org.hibernate.validator.constraints.Length;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is only used at build-time. It scans the various Hibernate entity classes
 * and enforces various rules (appropriate table names, no duplicate names, etc.)
 */
public class JpaModelScannerAndVerifier {

	public static final int MAX_COL_LENGTH = 4000;
	private static final int MAX_LENGTH = 30;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaModelScannerAndVerifier.class);

	/**
	 * We will keep track of all the index names and the columns they
	 * refer to.
	 * ---
	 * H2 automatically adds Indexes to ForeignKey constraints.
	 * This *does not happen* in other databases.
	 * ---
	 * But because we should be indexing foreign keys, we have to explicitly
	 * add an index to a foreign key.
	 * But if we give it a new name, SchemaMigrationTest will complain about an extra
	 * index that doesn't exist in H2.
	 * ---
	 * tl;dr
	 * Due to the quirks of supported DBs, we must have index names that duplicate their
	 * foreign key constraint names.
	 * So we'll be keeping a list of them here.
	 */
	private static final Multimap<String, String> ourIndexNameToColumn = HashMultimap.create();

	private static Set<String> ourReservedWords;
	public JpaModelScannerAndVerifier() {
		super();
	}


	/**
	 * This is really only useful for unit tests, do not call otherwise
	 */
	@SuppressWarnings("UnstableApiUsage")
	public void scanEntities(String... thePackageNames) throws IOException, ClassNotFoundException {

		try (InputStream is = ClasspathUtil.loadResourceAsStream("/mysql-reserved-words.txt")) {
			String contents = IOUtils.toString(is, Constants.CHARSET_UTF8);
			String[] words = contents.split("\\n");
			ourReservedWords = Arrays.stream(words)
				.filter(StringUtils::isNotBlank)
				.map(Ascii::toUpperCase)
				.collect(Collectors.toSet());
		}

		for (String packageName : thePackageNames) {
			ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(JpaModelScannerAndVerifier.class.getClassLoader()).getTopLevelClassesRecursive(packageName);
			Set<String> names = new HashSet<>();

			if (classes.size() <= 1) {
				throw new InternalErrorException(Msg.code(1623) + "Found no classes");
			}

			for (ClassPath.ClassInfo classInfo : classes) {
				Class<?> clazz = Class.forName(classInfo.getName());
				Entity entity = clazz.getAnnotation(Entity.class);
				Embeddable embeddable = clazz.getAnnotation(Embeddable.class);
				if (entity == null && embeddable == null) {
					continue;
				}

				scanClass(names, clazz);

			}
		}
	}

	private void scanClass(Set<String> theNames, Class<?> theClazz) {
		Map<String, Integer> columnNameToLength = new HashMap<>();

		scanClassOrSuperclass(theNames, theClazz, false, columnNameToLength);

		Table table = theClazz.getAnnotation(Table.class);
		if (table != null) {

			// This is the length for MySQL per https://dev.mysql.com/doc/refman/8.0/en/innodb-limits.html
			// No idea why 3072. what a weird limit but I'm sure they have their reason.
			int maxIndexLength = 3072;

			for (UniqueConstraint nextIndex : table.uniqueConstraints()) {
				int indexLength = calculateIndexLength(nextIndex.columnNames(), columnNameToLength, nextIndex.name());
				if (indexLength > maxIndexLength) {
					throw new IllegalStateException(Msg.code(1624) + "Index '" + nextIndex.name() + "' is too long. Length is " + indexLength + " and must not exceed " + maxIndexLength + " which is the maximum MySQL length");
				}
			}

		}

	}

	private void scanClassOrSuperclass(Set<String> theNames, Class<?> theClazz, boolean theIsSuperClass, Map<String, Integer> columnNameToLength) {
		ourLog.info("Scanning: {}", theClazz.getSimpleName());

		Subselect subselect = theClazz.getAnnotation(Subselect.class);
		boolean isView = (subselect != null);

		scan(theClazz, theNames, theIsSuperClass, isView);

		boolean foundId = false;
		for (Field nextField : theClazz.getDeclaredFields()) {
			if (Modifier.isStatic(nextField.getModifiers())) {
				continue;
			}

			ourLog.info(" * Scanning field: {}", nextField.getName());
			scan(nextField, theNames, theIsSuperClass, isView);

			Id id = nextField.getAnnotation(Id.class);
			boolean isId = id != null;
			if (isId) {
				Validate.isTrue(!foundId, "Multiple fields annotated with @Id");
				foundId = true;

				if (Long.class.equals(nextField.getType())) {

					GeneratedValue generatedValue = nextField.getAnnotation(GeneratedValue.class);
					if (generatedValue != null) {
						Validate.notBlank(generatedValue.generator(), "Field has no @GeneratedValue.generator(): %s", nextField);
						assertNotADuplicateName(generatedValue.generator(), theNames);
						assertEqualsForIdGenerator(nextField, generatedValue.strategy(), GenerationType.AUTO);

						GenericGenerator genericGenerator = nextField.getAnnotation(GenericGenerator.class);
						SequenceGenerator sequenceGenerator = nextField.getAnnotation(SequenceGenerator.class);
						Validate.isTrue(sequenceGenerator != null ^ genericGenerator != null);

						if (genericGenerator != null) {
							assertEqualsForIdGenerator(nextField, "ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator", genericGenerator.type().getName());
							assertEqualsForIdGenerator(nextField, "native", genericGenerator.strategy());
							assertEqualsForIdGenerator(nextField, generatedValue.generator(), genericGenerator.name());
						} else {
							Validate.notNull(sequenceGenerator);
							assertEqualsForIdGenerator(nextField, generatedValue.generator(), sequenceGenerator.name());
							assertEqualsForIdGenerator(nextField, generatedValue.generator(), sequenceGenerator.sequenceName());
						}
					}
				}

			}

			boolean isTransient = nextField.getAnnotation(Transient.class) != null;
			if (!isTransient) {
				boolean hasColumn = nextField.getAnnotation(Column.class) != null;
				boolean hasJoinColumn = nextField.getAnnotation(JoinColumn.class) != null;
				boolean hasEmbeddedId = nextField.getAnnotation(EmbeddedId.class) != null;
				boolean hasEmbedded = nextField.getAnnotation(Embedded.class) != null;
				OneToMany oneToMany = nextField.getAnnotation(OneToMany.class);
				OneToOne oneToOne = nextField.getAnnotation(OneToOne.class);
				boolean isOtherSideOfOneToManyMapping = oneToMany != null && isNotBlank(oneToMany.mappedBy());
				boolean isOtherSideOfOneToOneMapping = oneToOne != null && isNotBlank(oneToOne.mappedBy());
				boolean isField = nextField.getAnnotation(org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField.class) != null;
				isField |= nextField.getAnnotation(org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField.class) != null;
				isField |= nextField.getAnnotation(org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField.class) != null;
				Validate.isTrue(
					hasEmbedded ||
						hasColumn ||
						hasJoinColumn ||
						isOtherSideOfOneToManyMapping ||
						isOtherSideOfOneToOneMapping ||
						hasEmbeddedId ||
						isField, "Non-transient has no @Column or @JoinColumn or @EmbeddedId: " + nextField);

				int columnLength = 16;
				String columnName = null;
				boolean nullable = false;
				if (hasColumn) {
					Column column = nextField.getAnnotation(Column.class);
					columnName = column.name();
					columnLength = column.length();
					nullable = column.nullable();
				}
				if (hasJoinColumn) {
					JoinColumn joinColumn = nextField.getAnnotation(JoinColumn.class);
					columnName = joinColumn.name();
					nullable = joinColumn.nullable();
				}
				if (isId) {
					nullable = false;
				}

				if (nullable && !isView) {
					Validate.isTrue(!nextField.getType().isPrimitive(), "Field [%s] has a nullable primitive type: %s", nextField.getName(), nextField.getType());
				}

				if (columnName != null) {
					if (nextField.getType().isAssignableFrom(String.class)) {
						// MySQL treats each char as the max possible byte count in UTF-8 for its calculations
						columnLength = columnLength * 4;
					}

					columnNameToLength.put(columnName, columnLength);
				}

			}


		}

		for (Class<?> innerClass : theClazz.getDeclaredClasses()) {
			Embeddable embeddable = innerClass.getAnnotation(Embeddable.class);
			if (embeddable != null) {
				scanClassOrSuperclass(theNames, innerClass, false, columnNameToLength);
			}

		}

		if (theClazz.getSuperclass().equals(Object.class)) {
			return;
		}

		scanClassOrSuperclass(theNames, theClazz.getSuperclass(), true, columnNameToLength);
	}

	private void scan(AnnotatedElement theAnnotatedElement, Set<String> theNames, boolean theIsSuperClass, boolean theIsView) {
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
				Validate.isTrue(nextConstraint.name().startsWith("IDX_") || nextConstraint.name().startsWith("FK_"),
					nextConstraint.name() + " must start with IDX_ or FK_ (last one when indexing a FK column)");

				// add the index names to the collection of allowable duplicate fk names
				String[] cols = nextConstraint.columnList().split(",");
				for (String col : cols) {
					ourIndexNameToColumn.put(nextConstraint.name(), col);
				}
			}
		}

		JoinColumn joinColumn = theAnnotatedElement.getAnnotation(JoinColumn.class);
		if (joinColumn != null) {
			String columnName = joinColumn.name();
			validateColumnName(columnName, theAnnotatedElement);

			assertNotADuplicateName(columnName, null);
			ForeignKey fk = joinColumn.foreignKey();
			if (theIsSuperClass) {
				Validate.isTrue(isBlank(fk.name()), "Foreign key on " + theAnnotatedElement + " has a name() and should not as it is a superclass");
			} else {
				Validate.notNull(fk);
				Validate.isTrue(isNotBlank(fk.name()), "Foreign key on " + theAnnotatedElement + " has no name()");

				// Validate FK naming.
				// temporarily allow two hibernate legacy sp fk names until we fix them
				List<String> legacySPHibernateFKNames = Arrays.asList(
					"FKC97MPK37OKWU8QVTCEG2NH9VN", "FKGXSREUTYMMFJUWDSWV3Y887DO");
				Validate.isTrue(fk.name().startsWith("FK_") || legacySPHibernateFKNames.contains(fk.name()),
					"Foreign key " + fk.name() + " on " + theAnnotatedElement + " must start with FK_");

				if (ourIndexNameToColumn.containsKey(fk.name())) {
					// this foreign key has the same name as an existing index
					// let's make sure it's on the same column
					Collection<String> columns = ourIndexNameToColumn.get(fk.name());
					assertThat(columns.contains(columnName)).as(String.format("Foreign key %s duplicates index name, but column %s is not part of the index!", fk.name(), columnName)).isTrue();
				} else {
					// verify it's not a duplicate
					assertNotADuplicateName(fk.name(), theNames);
				}
			}
		}

		Column column = theAnnotatedElement.getAnnotation(Column.class);
		if (column != null) {
			String columnName = column.name();
			validateColumnName(columnName, theAnnotatedElement);

			assertNotADuplicateName(columnName, null);
			Validate.isTrue(column.unique() == false, "Should not use unique attribute on column (use named @UniqueConstraint instead) on " + theAnnotatedElement);

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
						throw new IllegalStateException(Msg.code(1626) + "Field does not have an explicit maximum length specified: " + field);
					}
				}

				Size size = theAnnotatedElement.getAnnotation(Size.class);
				if (size != null) {
					if (size.max() > MAX_COL_LENGTH) {
						throw new IllegalStateException(Msg.code(1628) + "Field is too long: " + field);
					}
				}

				Length length = theAnnotatedElement.getAnnotation(Length.class);
				if (length != null) {
					if (length.max() > MAX_COL_LENGTH) {
						throw new IllegalStateException(Msg.code(1629) + "Field is too long: " + field);
					}
				}
			}

		}

	}

	private void validateColumnName(String theColumnName, AnnotatedElement theElement) {
		if (!theColumnName.equals(theColumnName.toUpperCase())) {
			throw new IllegalArgumentException(Msg.code(1630) + "Column name must be all upper case: " + theColumnName + " found on " + theElement);
		}
		if (ourReservedWords.contains(theColumnName)) {
			throw new IllegalArgumentException(Msg.code(1631) + "Column name is a reserved word: " + theColumnName + " found on " + theElement);
		}
		if (theColumnName.startsWith("_")) {
			throw new IllegalArgumentException(Msg.code(2272) + "Column name "+ theColumnName +" starts with an '_' (underscore). This is not permitted for oracle field names. Found on " + theElement);
		}
	}

	private static int calculateIndexLength(String[] theColumnNames, Map<String, Integer> theColumnNameToLength, String theIndexName) {
		int retVal = 0;
		for (String nextName : theColumnNames) {
			Integer nextLength = theColumnNameToLength.get(nextName);
			if (nextLength == null) {
				throw new IllegalStateException(Msg.code(1625) + "Index '" + theIndexName + "' references unknown column: " + nextName);
			}
			retVal += nextLength;
		}
		return retVal;
	}

	private static void assertEqualsForIdGenerator(Field theSource, Object theExpectedGenerator, Object theActualGenerator) {
		Validate.isTrue(theExpectedGenerator.equals(theActualGenerator), "Value " + theActualGenerator + " doesn't match expected " + theExpectedGenerator + " for ID generator on " + theSource);
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

}
