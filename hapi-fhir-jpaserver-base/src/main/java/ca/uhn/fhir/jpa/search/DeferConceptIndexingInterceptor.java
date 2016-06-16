package ca.uhn.fhir.jpa.search;

import org.hibernate.search.indexes.interceptor.DontInterceptEntityInterceptor;

public class DeferConceptIndexingInterceptor extends DontInterceptEntityInterceptor
//implements EntityIndexingInterceptor<TermConcept> 
{
	// nothing for now
}
