package ca.uhn.fhir.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.List;

public interface ISearchDao {

	List<ISuggestion> suggestKeywords(String theContext, String theSearchParam, String theText);
	
	List<Long> search(String theResourceName, SearchParameterMap theParams);

	List<Long> everything(String theResourceName, SearchParameterMap theParams);

	public static interface ISuggestion extends Comparable<ISuggestion> {
		public String getTerm ();
		public float getScore ();

		@Override
		public int compareTo (ISuggestion theO);
	}
}
