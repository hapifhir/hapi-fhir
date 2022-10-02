//package ca.uhn.fhir.jpa.model.dialect;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
//
//import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
//import de.jpdigital.maven.plugins.hibernate5ddl.DdlGenerator;
//import de.jpdigital.maven.plugins.hibernate5ddl.DdlGeneratorHibernate54;
//import de.jpdigital.maven.plugins.hibernate5ddl.Dialect;
//import de.jpdigital.maven.plugins.hibernate5ddl.GenerateDdlMojo;
//import org.apache.maven.plugin.MojoFailureException;
//
//import java.io.File;
//import java.util.HashSet;
//import java.util.Set;
//
//public class Tmp {
//
//	public static void main(String[] args) throws MojoFailureException {
//
//		DdlGeneratorHibernate54 s = new DdlGeneratorHibernate54();
//		Dialect dialect = Dialect.H2;
//
//		Set<Class<?>> classes = new HashSet<>();
////		classes.add(ResourceIndexedSearchParamString.class);
//
//		GenerateDdlMojo mojo = new GenerateDdlMojo();
//		mojo.setOutputDirectory(new File("tmp"));
//
//		Set<Package> packages = new HashSet<>();
//		packages.add(ResourceIndexedSearchParamString.class.getPackage());
//
//		s.generateDdl(dialect, packages, classes, mojo);
//
//	}
//
//}
