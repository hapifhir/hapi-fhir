# Rules

HAPI EMPI rules are managed via a single json document.

Note that in all the following configuration, valid options for `resourceType` are `Patient`, `Practitioner`, and `*`. Use `*` if the criteria is identical across both resource types, and you would like to apply it to both practitioners and patients.

Here is an example of a full HAPI EMPI rules json document:

```json
{
	"candidateSearchParams": [
		{
			"resourceType": "Patient",
			"searchParams": ["given", "family"]
		},
		{
			"resourceType": "*",
			"searchParams": ["identifier"]
		},
		{
			"resourceType": "Patient",
			"searchParams": ["general-practitioner"]
		}
	],
	"candidateFilterSearchParams": [
		{
			"resourceType": "*",
			"searchParam": "active",
			"fixedValue": "true"
		}
	],
	"matchFields": [
		{
			"name": "cosine-given-name",
			"resourceType": "*",
			"resourcePath": "name.given",
			"metric": "COSINE",
			"matchThreshold": 0.8,
			"exact": true
		},
		{
			"name": "jaro-last-name",
			"resourceType": "*",
			"resourcePath": "name.family",
			"metric": "JARO_WINKLER",
			"matchThreshold": 0.8
		}
	],
	"matchResultMap": {
		"cosine-given-name" : "POSSIBLE_MATCH",
		"cosine-given-name,jaro-last-name" : "MATCH"
	},
	"eidSystem": "http://company.io/fhir/NamingSystem/custom-eid-system"
}
```

Here is a description of how each section of this document is configured.

### candidateSearchParams
These define fields which must have at least one exact match before two resources are considered for matching.  This is like a list of "pre-searches" that find potential candidates for matches, to avoid the expensive operation of running a match score calculation on all resources in the system.  E.g. you may only wish to consider matching two Patients if they either share at least one identifier in common or have the same birthday.  The HAPI FHIR server executes each of these searches separately and then takes the union of the results, so you can think of these as `OR` criteria that cast a wide net for potential candidates.  In some EMPI systems, these "pre-searches" are called "blocking" searches (since they identify "blocks" of candidates that will be searched for matches).

```json
[ {
    "resourceType" : "Patient",
    "searchParams" : ["given", "family"]
}, {
    "resourceType" : "Patient",
    "searchParam" : "identifier"
} ]
```

### candidateFilterSearchParams
When searching for match candidates, only resources that match this filter are considered.  E.g. you may wish to only search for Patients for which active=true.  Another way to think of these filters is all of them are "AND"ed with each candidateSearchParam above.
```json
[ {
    "resourceType" : "Patient",
    "searchParam" : "active",
    "fixedValue" : "true"
} ]
```

For example, if the incoming patient looked like this:

```json
{
  "resourceType": "Patient",
  "id": "example",
  "identifier": [{
      "system": "urn:oid:1.2.36.146.595.217.0.1",
      "value": "12345"
    }],
  "name": [
    {
      "family": "Chalmers",
      "given": [
        "Peter",
        "James"
      ]
    }
}
```

then the above `candidateSearchParams` and `candidateFilterSearchParams` would result in the following two consecutive searches for candidates:
* `Patient?given=Peter,James&family=Chalmers&active=true`
* `Patient?identifier=urn:oid:1.2.36.146.595.217.0.1|12345&active=true` 
 

### matchFields

Once the match candidates have been found, they are then each compared to the incoming Patient resource.  This comparison is made across a list of `matchField`s.  Each matchField returns `true` or `false` indicating whether the candidate and the incoming Patient match on that field.   There are two types of metrics: `Matcher` and `Similarity`.  Matcher metrics return a `true` or `false` directly, whereas Similarity metrics return a score between 0.0 (no match) and 1.0 (exact match) and this score is translated to a `true/false` via a `matchThreshold`.  E.g. if a `JARO_WINKLER` matchField is configured with a `matchThreshold` of 0.8 then that matchField will return `true` if the `JARO_WINKLER` similarity evaluates to a score >= 8.0.

By default, all matchFields have `exact=false` which means that they will have all diacritical marks removed and converted to upper case before matching.  `exact=true` can be added to any matchField to compare the strings as they are originally capitalized and accented.

Here is a matcher matchField that uses the SOUNDEX matcher to determine whether two family names match.

```json
{
	"name": "family-name-double-metaphone",
	"resourceType": "*",
	"resourcePath": "name.family",
	"metric": "SOUNDEX"
}
```

Here is a matcher matchField that only matches when two family names are identical.

```json
{
	"name": "family-name-exact",
	"resourceType": "*",
	"resourcePath": "name.family",
	"metric": "STRING",
    "exact": true
}
```

Here is a similarity matchField that matches when two given names match with a JARO_WINKLER threshold >0 0.8.

```json
{
    "name" : "given-name-jaro",
    "resourceType" : "Patient",
    "resourcePath" : "name.given",
    "metric" : "JARO_WINKLER",
    "matchThreshold" : 0.8
}
```

The following metrics are currently supported:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Description</th>
            <th>Example</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>CAVERPHONE1</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Caverphone1.html">Apache Caverphone1</a>          
            </td>
            <td>Gail = Gael, Gail != Gale, Thomas != Tom</td>
        </tr>
        <tr>
            <td>CAVERPHONE1</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Caverphone1.html">Apache Caverphone1</a>          
            </td>
            <td>Gail = Gael, Gail = Gale, Thomas != Tom</td>
        </tr>
        <tr>
            <td>COLOGNE</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/ColognePhonetic.html">Apache Cologne Phonetic</a>          
            </td>
            <td></td>
        </tr>        
        <tr>
            <td>DOUBLE_METAPHONE</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/DoubleMetaphone.html">Apache Double Metaphone</a>          
            </td>
            <td>Dury = Durie, Allsop = Allsob, Smith != Schmidt</td>
        </tr>
        <tr>
            <td>MATCH_RATING_APPROACH</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/MatchRatingApproachEncoder.html">Apache Match Rating Approach Encoder</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>METAPHONE</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Metaphone.html">Apache Metaphone</a>          
            </td>
            <td>Dury = Durie, Allsop != Allsob, Smith != Schmidt</td>
        </tr>
        <tr>
            <td>NYSIIS</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Nysiis.html">Apache Nysiis</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>REFINED_SOUNDEX</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/RefinedSoundex.html">Apache Refined Soundex</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>SOUNDEX</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Soundex.html">Apache Soundex</a>          
            </td>
            <td>Jon = John, Thomas != Tom</td>
        </tr>
        <tr>
            <td>STRING</td>
            <td>matcher</td>
            <td>
               Match the values as strings.  This matcher should be used with tokens (e.g. gender).
            </td>
            <td>MCTAVISH = McTavish when exact = false, MCTAVISH != McTavish when exact = true</td>
        </tr>
        <tr>
            <td>SUBSTRING</td>
            <td>matcher</td>
            <td>
               True if one string starts with the other.
            </td>
            <td>Bill = Billy, Egbert = Bert</td>
        </tr>    
        <tr>
            <td>DATE</td>
            <td>matcher</td>
            <td>
               Reduce the precision of the dates to the lowest precision of the two, then compare them as strings.
            </td>
            <td>2019-12,Month = 2019-12-19,Day</td>
        </tr>
        <tr>
            <td>NAME_ANY_ORDER</td>
            <td>matcher</td>
            <td>
               Match names as strings in any order
            </td>
            <td>John Henry = Henry JOHN when exact = false</td>
        </tr>        
        <tr>
            <td>NAME_FIRST_AND_LAST</td>
            <td>matcher</td>
            <td>
               Match names as strings in any order
            </td>
            <td>John Henry = John HENRY when exact=false, John Henry != Henry John</td>
        </tr>     
        <tr>
            <td>JARO_WINKLER</td>
            <td>similarity</td>
            <td>
              <a href="https://github.com/tdebatty/java-string-similarity#jaro-winkler">tdebatty Jaro Winkler</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>COSINE</td>
            <td>similarity</td>
            <td>
              <a href="https://github.com/tdebatty/java-string-similarity#cosine-similarity">tdebatty Cosine Similarity</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>JACCARD</td>
            <td>similarity</td>
            <td>
              <a href="https://github.com/tdebatty/java-string-similarity#jaccard-index">tdebatty Jaccard Index</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>LEVENSCHTEIN</td>
            <td>similarity</td>
            <td>
              <a href="https://github.com/tdebatty/java-string-similarity#normalized-levenshtein">tdebatty Normalized Levenshtein</a>          
            </td>
            <td></td>
        </tr>
        <tr>
            <td>SORENSEN_DICE</td>
            <td>similarity</td>
            <td>
              <a href="https://github.com/tdebatty/java-string-similarity#sorensen-dice-coefficient">tdebatty Sorensen-Dice coefficient</a>          
            </td>
            <td></td>
        </tr>   
     </tbody>
</table>

### matchResultMap

These entries convert combinations of successful matchFields into an EMPI Match Result for overall matching of a given pair of resources.  MATCH results are evaluated take precedence over POSSIBLE_MATCH results.

```json
{
	"matchResultMap": {
		"cosine-given-name" : "POSSIBLE_MATCH",
		"cosine-given-name,jaro-last-name" : "MATCH"
	}
}
```

### eidSystem

The external EID system that the HAPI EMPI system should expect to see on incoming Patient resources. Must be a valid URI.  See [EMPI EID](/hapi-fhir/docs/server_jpa_empi/empi_eid.html) for details on how EIDs are managed by HAPI EMPI.
