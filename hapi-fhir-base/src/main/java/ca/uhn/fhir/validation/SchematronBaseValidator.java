package ca.uhn.fhir.validation;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Locale;

import javax.xml.transform.stream.StreamSource;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;

import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.schematron.ISchematronResource;
import com.phloc.schematron.SchematronHelper;
import com.phloc.schematron.xslt.SchematronResourceSCH;

public class SchematronBaseValidator implements IValidator {

	@Override
	public void validate(ValidationContext theCtx) {

		IResource resource = theCtx.getResource();
		RuntimeResourceDefinition baseDef = theCtx.getFhirContext().getResourceDefinition(resource).getBaseDefinition();
		Class<? extends IResource> baseResourceClass = baseDef.getImplementingClass();
		Package pack = baseResourceClass.getPackage();
		
		String pathToBase = pack.getName().replace('.', '/') + '/' + baseDef.getName().toLowerCase() + ".sch";
		InputStream baseIs = ResourceValidator.class.getClassLoader().getResourceAsStream(pathToBase);
		if (baseIs == null) {
			throw new ValidationFailureException("No schematron found for resource type: " + baseDef.getImplementingClass().getCanonicalName());
		}

		ISchematronResource sch = SchematronResourceSCH.fromClassPath(pathToBase);
		StreamSource source = new StreamSource(new StringReader(theCtx.getXmlEncodedResource()));

		SchematronOutputType results = SchematronHelper.applySchematron(sch, source);
		if (results == null) {
			return;
		}
		
		IResourceErrorGroup errors = SchematronHelper.convertToResourceErrorGroup(results, baseDef.getName());
		
		if (errors.getAllErrors().containsOnlySuccess()) {
			return;
		}
		
		for (IResourceError next : errors.getAllErrors().getAllResourceErrors()) {
			Issue issue = theCtx.getOperationOutcome().addIssue();
			switch(next.getErrorLevel()) {
			case ERROR:
				issue.setSeverity(IssueSeverityEnum.ERROR);
				break;
			case FATAL_ERROR:
				issue.setSeverity(IssueSeverityEnum.FATAL);
				break;
			case WARN:
				issue.setSeverity(IssueSeverityEnum.WARNING);
				break;
			case INFO:
			case SUCCESS:
				continue;
			}
			
			issue.getDetails().setValue(next.getAsString(Locale.getDefault()));
		}
		
	}

	
	
	
}
