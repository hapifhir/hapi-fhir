# Measures and Care Gaps

## TimeZone Header 
DQM `$care-gaps` and `$evaluate-measure`  will convert parameters `periodStart` and `periodEnd`
according to a timezone supplied by the client, not the server timezone as it was previously.  Clients can leverage this
functionality by passing in a new `Timezone` header (ex: `America/Denver`).  If nothing is supplied, it will default to
UTC.

## CareGaps Operation Parameters
### Parameters removed
Certain `$care-gaps` operation parameters have been dropped, because they are not used or likely to be implemented
* `topic`
* `practitioner` is now callable via `subject` parameter
* `organization`
* `program`

### Parameters added:
* `measureIdentifier` now is available to resolve measure resources for evaluation
* `nonDocument` is a new optional parameter that defaults to `false` which returns standard `document` bundle for `$care-gaps`. 
If `true`, this will return summarized subject bundle with only detectedIssue.

# SDC $populate operation

The `subject` parameter of the `Questionnaire/$populate` operation has been changed to expect a `Reference` as specified
in the SDC IG. 
