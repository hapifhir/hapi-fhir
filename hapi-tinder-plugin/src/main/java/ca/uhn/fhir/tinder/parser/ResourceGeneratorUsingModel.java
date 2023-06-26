package ca.uhn.fhir.tinder.parser;

import ca.uhn.fhir.i18n.Msg;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.apache.commons.lang.WordUtils;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.SearchParameter;
import org.apache.maven.plugin.MojoFailureException;

public class ResourceGeneratorUsingModel extends BaseStructureParser {

	private String myFilenameSuffix;
	private String myTemplate;
	private File myTemplateFile;
	private Collection<String> myResourceNames;

	public ResourceGeneratorUsingModel(String theVersion, String theBaseDir) throws MojoFailureException {
		super(theVersion, theBaseDir);
	}

	@Override
	protected String getFilenameSuffix() {
		return myFilenameSuffix;
	}

	@Override
	protected String getTemplate() {
		return myTemplate;
	}

	@Override
	protected File getTemplateFile() {
		return myTemplateFile;
	}

	@Override
	public void setFilenameSuffix(String theFilenameSuffix) {
		myFilenameSuffix = theFilenameSuffix;
	}

	@Override
	public void setTemplate(String theTemplate) {
		myTemplate = theTemplate;
	}

	@Override
	public void setTemplateFile(File theTemplateFile) {
		myTemplateFile = theTemplateFile;
	}

	public void setBaseResourceNames(List<String> theBaseResourceNames) {
		myResourceNames = theBaseResourceNames;
	}

	public void parse() {
		for (String nextResourceName : myResourceNames) {
			RuntimeResourceDefinition def = getCtx().getResourceDefinition(nextResourceName);
			
			Resource resource = new Resource();
			resource.setName(def.getName());
			resource.setElementName(def.getName());
			addResource(resource);
			
			for (RuntimeSearchParam nextSearchParam : def.getSearchParams()) {
				SearchParameter param = new SearchParameter(getVersion(), def.getName());

				List<RuntimeSearchParam> compositeOfParams = nextSearchParam
					.getComponents()
					.stream()
					.map(t -> def.getSearchParams().stream().filter(y -> y.getUri().equals(t.getReference())).findFirst().orElseThrow(() -> new IllegalStateException()))
					.collect(Collectors.toList());
				if (nextSearchParam.getParamType() == RestSearchParameterTypeEnum.COMPOSITE && compositeOfParams.size() != 2) {
					throw new IllegalStateException(Msg.code(163) + "Search param " + nextSearchParam.getName() + " on base " + nextSearchParam.getBase() + " has components: " + nextSearchParam.getComponents());
				}

				param.setName(nextSearchParam.getName());
				param.setDescription(nextSearchParam.getDescription());
				param.setCompositeOf(toCompositeOfStrings(compositeOfParams));
				param.setCompositeTypes(toCompositeOfTypes(compositeOfParams));
				param.setPath(nextSearchParam.getPath());
				param.setType(nextSearchParam.getParamType().getCode());
				
				resource.addSearchParameter(param);
			}
		}
	}

	private List<String> toCompositeOfStrings(List<RuntimeSearchParam> theCompositeOf) {
		if (theCompositeOf == null) {
			return null;
		}
		ArrayList<String> retVal = new ArrayList<>();
		for (RuntimeSearchParam next : theCompositeOf) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	private List<String> toCompositeOfTypes(List<RuntimeSearchParam> theCompositeOf) {
		if (theCompositeOf == null) {
			return null;
		}
		ArrayList<String> retVal = new ArrayList<String>();
		for (RuntimeSearchParam next : theCompositeOf) {
			String type = next.getParamType().getCode();
			type = WordUtils.capitalize(type);
			retVal.add(type);
		}
		return retVal;
	}

}
