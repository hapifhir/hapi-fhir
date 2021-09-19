package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// WIP mb find a better module and package for this. ISearchParamExtractor is in jpa, and I wonder ...
// WIP MB this for sure should find a way to re-use BaseSearchParamExtractor, SearchParamExtractorDstu2, and friends
// There is way too much in there to re-produce independently.
// WIP Need to introduce a factory for the actual index objects to liberate BaseSearchParamExtractor from JPA
public class LuceneRuntimeSearchParam {
	private static final Logger ourLog = LoggerFactory.getLogger(LuceneRuntimeSearchParam.class);

	final RuntimeSearchParam mySearchParam;
	final IFhirPath myFhirPath;
	final ISearchParamExtractor mySearchParamExtractor;

	public LuceneRuntimeSearchParam(RuntimeSearchParam theSearchParam, IFhirPath theFhirPath, ISearchParamExtractor theSearchParamExtractor) {
		this.mySearchParam = theSearchParam;
		this.myFhirPath = theFhirPath;
		this.mySearchParamExtractor = theSearchParamExtractor;
	}

	public void extractLuceneIndexData(IBaseResource theResource, ExtendedLuceneIndexData retVal) {
		// just :text for now.
		String allText = null;
		switch (mySearchParam.getParamType()) {
			// TODO extract and test.
			case TOKEN:
				List<IBase> bases = mySearchParamExtractor.extractValues(mySearchParam.getPath(), theResource);
					allText = bases.stream()
					.flatMap(v -> {
						String type = v.fhirType();
						switch (type) {
							case "CodeableConcept":
								return extractCodeableConceptTexts(v);
							case "Identifier":
								return extractIdentifierTypeTexts(v);
							case "Coding":
								return extractCodingTexts(v);
							default:
								// According to https://hl7.org/fhir/search.html#token,
								// the above are the only types that contribute to :text for type token.
								return Stream.empty();
						}
					})
					.collect(Collectors.joining(" "));
				break;
			// TODO MB pull Strings as well to support :text
			default:
				// we only support token for now.
				break;
		}
		if (StringUtils.isNotBlank(allText)) {
			// TODO introduce wrapper type to support more than just :text
			retVal.addIndexData(mySearchParam.getName(), allText);
		}
	}

	@NotNull
	private Stream<String> extractCodingTexts(IBase theElement) {
		return myFhirPath.evaluate(theElement, "display", IPrimitiveType.class).stream().map(IPrimitiveType::getValueAsString);
	}

	private Stream<String> extractIdentifierTypeTexts(IBase theElement) {
		return myFhirPath.evaluate(theElement, "type.text", IPrimitiveType.class).stream().map(IPrimitiveType::getValueAsString);
	}

	Stream<String> extractCodeableConceptTexts(IBase theElement) {
		return Stream.concat(
			myFhirPath.evaluate(theElement, "text", IPrimitiveType.class).stream()
				.map(IPrimitiveType::getValueAsString),
			myFhirPath.evaluate(theElement, "coding.display", IPrimitiveType.class).stream()
				.map(IPrimitiveType::getValueAsString));
	}
}
