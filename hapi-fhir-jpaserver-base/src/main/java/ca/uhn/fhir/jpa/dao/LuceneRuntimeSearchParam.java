package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

// fixme mb find a better module and package for this. ISearchParamExtractor is in jpa, and I wonder ...
public class LuceneRuntimeSearchParam {
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
			// fixme extract and test.
			// fixme MB find all CodeableConcept  targeted by an sp.
			// fixme MB pull Strings as well to support :text
			case TOKEN:
				allText = mySearchParamExtractor.extractValues(mySearchParam.getPath(), theResource).stream()
					.flatMap(v -> {
						String type = v.fhirType();
						switch (type) {
							case "CodeableConcept":
								return extractCodeableConceptTexts(v);
							// FIXME what other types contribute to :text?  https://hl7.org/fhir/search.html#token
							// CodeableConcept.text, Coding.display, or Identifier.type.text.
						}
						return Stream.empty();
					})
					.collect(Collectors.joining(" "));
				break;
			default:
				// we only support token for now.
				break;
		}
		if (StringUtils.isNotBlank(allText)) {
			// fixme introduce wrapper type to support more than just :text
			retVal.addIndexData(mySearchParam.getName(), allText);
		}
	}

	Stream<String> extractCodeableConceptTexts(IBase theElement) {
		return Stream.concat(
			myFhirPath.evaluate(theElement, "text", IPrimitiveType.class).stream()
				.map(p -> p.getValueAsString()),
			myFhirPath.evaluate(theElement, "coding.display", IPrimitiveType.class).stream()
				.map(p -> p.getValueAsString()));
	}
}
