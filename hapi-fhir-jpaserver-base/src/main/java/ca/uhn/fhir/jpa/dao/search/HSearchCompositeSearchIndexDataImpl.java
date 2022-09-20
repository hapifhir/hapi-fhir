package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.search.CompositeSearchIndexData;
import ca.uhn.fhir.jpa.model.search.HSearchElementCache;
import ca.uhn.fhir.jpa.model.search.HSearchIndexWriter;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParamComposite;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.util.ObjectUtil;
import org.hibernate.search.engine.backend.document.DocumentElement;

import javax.print.Doc;
import java.util.function.Function;

/**
 * binding of HSearch apis into
 *
 * searchparam and hsearch aren't friends.  Bring them together here.
 */
class HSearchCompositeSearchIndexDataImpl implements CompositeSearchIndexData {

	final ResourceIndexedSearchParamComposite mySearchParamComposite;

	public HSearchCompositeSearchIndexDataImpl(ResourceIndexedSearchParamComposite theSearchParamComposite) {
		mySearchParamComposite = theSearchParamComposite;
	}

	@Override
	public void writeIndexEntry(HSearchIndexWriter theHSearchIndexWriter, HSearchElementCache theRoot) {
		DocumentElement nestedParamRoot = theRoot.getObjectElement(HSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT);

		// we want to re-use the `token`, `quantity` nodes for multiple values.
		DocumentElement compositeRoot = nestedParamRoot.addObject(mySearchParamComposite.getSearchParamName());
		HSearchElementCache compositeRootCache = new HSearchElementCache(compositeRoot);

		// wipmb should we make sure we have at least on value in each component before writing anything?
		// no point writing empty nodes

		for (ResourceIndexedSearchParamComposite.Component subParam : mySearchParamComposite.getComponents()) {
			Function<String, DocumentElement> idxElementLookup = typeKey -> compositeRootCache.getObjectElement(subParam.getSearchParamName(), typeKey);
			// Write the various index nodes.
			// Note: we don't support modifiers with composites, so we don't bother to index :of-type, :text, etc.
			switch (subParam.getSearchParameterType()) {
				case DATE: {
					subParam.getParamIndexValues().stream()
						.flatMap(o->ObjectUtil.safeCast(o, ResourceIndexedSearchParamDate.class).stream())
						.map(ExtendedHSearchIndexExtractor::convertDate)
						.forEach(d-> theHSearchIndexWriter.writeDateFields(idxElementLookup.apply("dt"), d));
				}
				break;
				case TOKEN: {
					subParam.getParamIndexValues().stream()
						.flatMap(o->ObjectUtil.safeCast(o, ResourceIndexedSearchParamToken.class).stream())
						.forEach(rispt-> theHSearchIndexWriter.writeTokenFields(idxElementLookup.apply( "token"), new Tag(rispt.getSystem(), rispt.getValue())));
				}
				break;
				case QUANTITY: {
					subParam.getParamIndexValues().stream()
						.flatMap(o->ObjectUtil.safeCast(o, ResourceIndexedSearchParamQuantity.class).stream())
						.map(ExtendedHSearchIndexExtractor::convertQuantity)
						.forEach(q-> theHSearchIndexWriter.writeQuantityFields(idxElementLookup.apply( HSearchIndexWriter.QTY_IDX_NAME), q));
				}
				break;
				// wipmb head

				default:
					// unsupported
			}
		}
	}

}
