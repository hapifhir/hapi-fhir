package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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
			
			ourLog.info("Scanning: {}", clazz.getSimpleName());
			
			scan(clazz, names);
			
			for (Field nextField : clazz.getDeclaredFields()) {
				ourLog.info(" * Scanning field: {}", nextField.getName());
				scan(nextField, names);
			}
			
		}
	}

	private static void scan(AnnotatedElement ae, Set<String> theNames) {
		Table table = ae.getAnnotation(Table.class);
		if (table != null) {
			assertThat(table.name(), theNames);
			for (UniqueConstraint nextConstraint : table.uniqueConstraints()) {
				assertThat(nextConstraint.name(), theNames);
				Validate.isTrue(nextConstraint.name().startsWith("IDX_"), nextConstraint.name() + " must start with IDX_");
			}
			for (Index nextConstraint : table.indexes()) {
				assertThat(nextConstraint.name(), theNames);
				Validate.isTrue(nextConstraint.name().startsWith("IDX_"), nextConstraint.name() + " must start with IDX_");
			}
		}
		
		JoinColumn joinColumn = ae.getAnnotation(JoinColumn.class);
		if (joinColumn != null) {
			assertThat(joinColumn.name(), null);
			ForeignKey fk = joinColumn.foreignKey();
			Validate.notNull(fk);
			Validate.isTrue(isNotBlank(fk.name()));
			Validate.isTrue(fk.name().startsWith("FK_"));
			assertThat(fk.name(), theNames);
		}

		Column column = ae.getAnnotation(Column.class);
		if (column != null) {
			assertThat(column.name(), null);
		}

		GeneratedValue gen = ae.getAnnotation(GeneratedValue.class);
		SequenceGenerator sg = ae.getAnnotation(SequenceGenerator.class);
		Validate.isTrue((gen != null) == (sg != null));
		if (gen != null) {
			assertThat(gen.generator(), theNames);
			assertThat(sg.name(), null);
			assertThat(sg.sequenceName(), null);
			assertEquals(gen.generator(), sg.name());
			assertEquals(gen.generator(), sg.sequenceName());
		}

	}

	private static void assertEquals(String theGenerator, String theName) {
		Validate.isTrue(theGenerator.equals(theName));
	}

	private static void assertThat(String theName, Set<String> theNames) {
		Validate.isTrue(theName.length() <= MAX_LENGTH, "Identifier \"" + theName + "\" is " + theName.length() + " chars long");
		if (theNames != null) {
			Validate.isTrue(theNames.add(theName), "Duplicate name: " + theName);
		}
	}

}
