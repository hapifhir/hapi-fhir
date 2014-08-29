package ca.uhn.fhir.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class NameChanges {

	@SuppressWarnings("unchecked")
	@Test
	public void testNameChanges() throws IOException, ClassNotFoundException {
		FhirContext ctx = new FhirContext();
		ImmutableSet<ClassInfo> names = ClassPath.from(getClass().getClassLoader()).getTopLevelClasses(Patient.class.getPackage().getName());
		List<String> changes = new ArrayList<String>();
		
		for (ClassInfo classInfo : names) {
			
			RuntimeResourceDefinition def = ctx.getResourceDefinition((Class<? extends IResource>) Class.forName(classInfo.getName()));
			for (RuntimeSearchParam nextParam : def.getSearchParams()) {
				if (nextParam.getParamType() == SearchParamTypeEnum.COMPOSITE) {
					continue;
				}
				
				String name = nextParam.getName();
				if (name.contains("[x]")) {
					continue;
				}
				
				if (name.startsWith("_")) {
					continue; // _id and _language
				}

				String path = nextParam.getPath();
				
				if (path.contains(" | ")) {
					changes.add(def.getName() + ": " + name + " has multiple paths so there is no obvious name (" + nextParam.getDescription() + ")");
					continue;
				}
				
				path = path.substring(path.indexOf('.') + 1);
				
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < path.length(); i++) {
					char nextChar = path.charAt(i);
					if (Character.isUpperCase(nextChar)) {
						b.append('-');
					}else if (nextChar == '.') {
						b.append('-');
						continue;
					}
					b.append(Character.toLowerCase(nextChar));
				}
				
				if (name.equals(b.toString())==false) {
					changes.add(def.getName() + "," + name + "," + b + "," + nextParam.getDescription());
				}
			}
			
			
		}
		
		System.out.println("Resource,old name,new name,description");
		
		for (String next : changes) {
			System.out.println(next);
		}
		
	}
	
}
	
