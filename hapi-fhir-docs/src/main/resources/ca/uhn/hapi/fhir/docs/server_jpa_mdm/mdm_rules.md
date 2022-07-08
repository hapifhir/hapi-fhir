# Rules

HAPI MDM rules are defined in a single json document.

Note that in all the following configuration, valid options for `resourceType` include any supported resource, such as `Organization`, `Patient`, `Practitioner`, and `*`. Use `*` if the criteria is identical across both resource types and you would like to apply it to all resources.

Here is an example of a full HAPI MDM rules json document:

```json
{
	"version": "1",
   "mdmTypes" : ["Organization", "Patient", "Practitioner"],
	"candidateSearchParams": [
		{
			"resourceType": "Patient",
			"searchParams": [
				"phone"
			]
		},
		{
			"resourceType": "Patient",
			"searchParams": [
				"birthdate"
			]
		},
		{
			"resourceType": "*",
			"searchParams": [
				"identifier"
			]
		}
	],
	"candidateFilterSearchParams": [],
	"matchFields": [
		{
			"name": "birthday",
			"resourceType": "Patient",
			"resourcePath": "birthDate",
			"matcher": {
				"algorithm": "STRING"
			}
		},
		{
			"name": "phone",
			"resourceType": "Patient",
			"resourcePath": "telecom.value",
			"matcher": {
				"algorithm": "STRING"
			}
		},
		{
			"name": "firstname-meta",
			"resourceType": "Patient",
			"fhirPath": "name.given.first()",
			"matcher": {
				"algorithm": "METAPHONE"
			}
		},
		{
			"name": "lastname-meta",
			"resourceType": "Patient",
			"resourcePath": "name.family",
			"matcher": {
				"algorithm": "METAPHONE"
			}
		},
		{
			"name": "firstname-jaro",
			"resourceType": "Patient",
			"resourcePath": "name.given",
			"similarity": {
				"algorithm": "JARO_WINKLER",
				"matchThreshold": 0.80
			}
		},
		{
			"name": "lastname-jaro",
			"resourceType": "Patient",
			"resourcePath": "name.family",
			"similarity": {
				"algorithm": "JARO_WINKLER",
				"matchThreshold": 0.80
			}
		},
		{
			"name": "org-name",
			"resourceType": "Organization",
			"resourcePath": "name",
			"matcher": {
				"algorithm": "STRING"
			}
		}
	],
	"matchResultMap": {
		"firstname-meta,lastname-meta,birthday": "MATCH",
		"firstname-meta,lastname-meta,phone": "MATCH",
		"firstname-jaro,lastname-jaro,birthday": "POSSIBLE_MATCH",
		"firstname-jaro,lastname-jaro,phone": "POSSIBLE_MATCH",
		"lastname-jaro,phone,birthday": "POSSIBLE_MATCH",
		"firstname-jaro,phone,birthday": "POSSIBLE_MATCH",
        "org-name": "MATCH"
	},
   "eidSystems": {
      "Organization": "https://hapifhir.org/identifier/naming/business-number",
      "Practitioner": "https://hapifhir.org/identifier/naming/license-number"
   }
}
```

Here is a description of how each section of this document is configured.

### candidateSearchParams

These define fields which must have at least one exact match before two resources are considered for matching.  This is like a list of "pre-searches" that find potential candidates for matches, to avoid the expensive operation of running a match score calculation on all resources in the system.  E.g. you may only wish to consider matching two Patients if they either share at least one identifier in common or have the same birthday or the same phone number. The HAPI FHIR server executes each of these searches separately and then takes the union of the results, so you can think of these as `OR` criteria that cast a wide net for potential candidates.  In some MDM systems, these "pre-searches" are called "blocking" searches (since they identify "blocks" of candidates that will be searched for matches).  

If a list of searchParams is specified in a given candidateSearchParams item, then these search parameters are treated as `AND` parameters. In the following candidateSearchParams definition, hapi-fhir will extract given name, family name and identifiers from the incoming Patient and perform two separate searches, first for all Patient resources that have the same given `AND` the same family name as the incoming Patient, and second for all Patient resources that share at least one identifier as the incoming Patient.  Note that if the incoming Patient was missing any of these searchParam values, then that search would be skipped.  E.g. if the incoming Patient had a given name but no family name, then only a search for matching identifiers would be performed.

```json
{
    "candidateSearchParams" : [ 
        {
            "resourceType" : "Patient",
            "searchParams" : ["given", "family"]
        }, {
            "resourceType" : "Patient",
            "searchParam" : "identifier"
        } 
  ]
}
```

### candidateFilterSearchParams
When searching for match candidates, only resources that match this filter are considered.  E.g. you may wish to only search for Patients for which active=true.

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
  ]
}
```

then the above `candidateSearchParams` and `candidateFilterSearchParams` would result in the following two consecutive searches for candidates:
* `Patient?given=Peter,James&family=Chalmers&active=true`
* `Patient?identifier=urn:oid:1.2.36.146.595.217.0.1|12345&active=true`


### matchFields

Once the match candidates have been found, they are then each compared to the incoming Patient resource.  This comparison is made across a list of `matchField`s.  Each matchField returns `true` or `false` indicating whether the candidate and the incoming Patient match on that field.   There are two types of matchFields: `matcher` and `similarity`.  `matcher` matchFields return a `true` or `false` directly, whereas `similarity` matchFields return a score between 0.0 (no match) and 1.0 (exact match) and this score is translated to a `true/false` via a `matchThreshold`.  E.g. if a `JARO_WINKLER` matchField is configured with a `matchThreshold` of 0.8 then that matchField will only return `true` if the `JARO_WINKLER` similarity evaluates to a score >= 0.8.

By default, all matchFields have `exact=false` which means that they will have all diacritical marks removed and all letters will be converted to upper case before matching.  `exact=true` can be added to any matchField to compare the strings as they are originally capitalized and accented.

Here is a matcher matchField that uses the SOUNDEX matcher to determine whether two family names match.

```json
{
	"name": "familyname-soundex", 
   "resourceType": "*",
	"resourcePath": "name.family",
	"matcher": {
			"algorithm": "SOUNDEX"
	}
}
```

Here is a matcher matchField that only matches when two family names are identical.

```json
{
	"name": "familyname-exact",
    "resourceType": "*",
	"resourcePath": "name.family",
	"matcher": {
			"algorithm": "STRING",
			"exact": true
	}
}
```

While it is often suitable to use the `resourcePath` field to indicate the location of the data to be matched, occasionally you will need more direct control over precisely which fields are matched. When performing string matching, the matcher will indiscriminately try to match all elements of the left resource to all elements of the right resource. For example, consider the following two patients and matcher.

```json
{
   "resourceType": "Patient",
   "name": [{
      "given": ["Frank", "John"]
   }]
}
```

```json
{
   "resourceType": "Patient",
   "name": [{
      "given": ["John", "Frank"]
   }]
}
```

```json
{
   "name": "firstname-meta",
   "resourceType": "Patient",
   "resourcePath": "name.given",
   "matcher": {
      "algorithm": "METAPHONE"
   }
}
```

In this example, these two patients would match, as the matcher will compare all elements of `["John", "Frank"]` to all elements of `["Frank", "John"]` and find that there are matches. This is when you would want to use a FHIRPath matcher, as FHIRPath expressions give you more direct control. This following example shows a matcher that would cause these two patient's not to match to each other.

```json
{
   "name": "firstname-meta-fhirpath",
   "resourceType": "Patient",
   "fhirPath": "name.given[0]",
   "matcher": {
      "algorithm": "METAPHONE"
   }
}
```
Since FHIRPath expressions support indexing it is possible to directly indicate that you would only like to compare the first element of each resource. 



Special identifier matching is also available if you need to match on a particular identifier system:
```json
{
	"name": "identifier-ssn",
    "resourceType": "*",
	"resourcePath": "identifier",
	"matcher": {
			"algorithm": "IDENTIFIER",
			"identifierSystem": "http://hl7.org/fhir/sid/us-ssn"
	}
}
```


Here is a similarity matchField that matches when two given names match with a JARO_WINKLER threshold >= 0.8.

```json
{
	"name": "firstname-jaro",
	"resourceType": "*",
	"resourcePath": "name.given",
	"similarity": {
		"algorithm": "JARO_WINKLER",
		"matchThreshold": 0.80
	}
}
```

The following algorithms are currently supported:

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Algorithm</th>
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
            <td>CAVERPHONE2</td>
            <td>matcher</td>
            <td>
              <a href="https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Caverphone2.html">Apache Caverphone2</a>          
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
            <td>NUMERIC</td>
            <td>matcher</td>
            <td>
               Remove all non-numeric characters from the string before comparing.
            </td>
            <td>4169671111 = (416) 967-1111</td>
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
            <td>NICKNAME</td>
            <td>matcher</td>
            <td>
               True if one name is a nickname of the other
            </td>
            <td>Ken = Kenneth, Kenny = Ken.  Allen != Allan.</td>
        </tr>     
				<tr>
            <td>IDENTIFIER</td>
            <td>matcher</td>
            <td>
               Matches when the system and value of the identifier are identical.
            </td>
            <td>If an optional "identifierSystem" is provided, then the identifiers only match when they belong to that system</td>
        </tr>     
        <tr>
            <td>EXTENSION_ANY_ORDER</td>
            <td>matcher</td>
            <td>
               Matches extensions of resources in any order. Matches are made if both resources share at least one extensions that have the same URL and value.
            </td>
            <td></td>
        </tr>
        <tr>
            <td>EMPTY_FIELD</td>
            <td>matcher</td>
            <td>
               Matches an empty field.
            </td>
            <td></td>
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

These entries convert combinations of successful matchFields into an MDM Match Result for overall matching of a given pair of resources.  MATCH results are evaluated take precedence over POSSIBLE_MATCH results.  If the incoming resource matches ALL of the named matchFields listed, then a new match link is created with the assigned matchResult (`MATCH` or `POSSIBLE_MATCH`).

```json
{
	"matchResultMap": {
		"firstname-meta,lastname-meta,birthday": "MATCH",
		"firstname-jaro,lastname-jaro,birthday": "POSSIBLE_MATCH"
	}
}
```

### eidSystems

The external EID systems that the HAPI MDM system can expect to see on incoming resources. These are defined on a per-resource basis. Alternatively, you may use `*` to indicate 
that an EID is valid for all managed resource types. The values must be valid URIs, and the keys must be valid resource types, or `*`.
See [MDM EID](/hapi-fhir/docs/server_jpa_mdm/mdm_eid.html) for details on how EIDs are managed by HAPI MDM.

<p class="helpInfoCalloutBox">
    Note that this field used to be called `eidSystem`. While that field is deprecated, it will continue to work. In the background, it effectively sets the eid for resource type `*`.
</p>

