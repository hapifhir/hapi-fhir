When upgrading to this release, there are a few important notes to be aware of: 

* This release removes the Legacy Search Builder. If you upgrade to this release, the new Search Builder will automatically be used.

* This release will break existing implementations which use the subscription delete feature. The place where the extension needs to be installed on the Subscription resource has now changed. While it used to be on the top-level Subscription resource, the extension should now be added to the Subscription's `channel` element.

Here is how the subscription should have looked before:

```json
{
    "resourceType": "Subscription",
    "id": "1",
    "status": "active",
    "reason": "Monitor resource persistence events",
    "criteria": "Patient",
    "channel": {

        "type": "rest-hook",
        "payload": "application/json"
    },
   "extension": [
      {
         "url": "http://hapifhir.io/fhir/StructureDefinition/subscription-send-delete-messages",
         "valueBoolean": "true"
      }
   ]
}
```

And here is how it should now look:

```json

{
    "resourceType": "Subscription",
    "id": "1",
    "status": "active",
    "reason": "Monitor resource persistence events",
    "criteria": "Patient",
    "channel": {
        "extension": [
            {
                "url": "http://hapifhir.io/fhir/StructureDefinition/subscription-send-delete-messages",
                "valueBoolean": "true"
            }
        ],
        "type": "rest-hook",
        "payload": "application/json"
    }
}
```
