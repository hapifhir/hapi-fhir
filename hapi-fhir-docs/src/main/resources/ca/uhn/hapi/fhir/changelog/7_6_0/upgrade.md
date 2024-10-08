# Measures and Care Gaps

DQM `$care-gaps` and `$evaluate-measure`  will now convert parameters `periodStart` and `periodEnd`
according to a timezone supplied by the client, not the server timezone as it was previously.  Clients can leverage this
functionality by passing in a new `Timezone` header (ex: `America/Denver`).  If nothing is supplied, it will default to
UTC.

# SDC $populate operation

The `subject` parameter of the `Questionnaire/$populate` operation has been changed to expect a `Reference` as specified
in the SDC IG. 
