### Major Database Change

This release makes performance changes to the database definition in a way that is incompatible with releases before 6.4.
Attempting to run version 6.2 or older simultaneously with this release may experience errors when saving new resources.

### Change Tracking and Subscriptions
This release introduces significant a change to the mechanism performing submission of resource modification events
to the message broker.  Previously, an event would be submitted as part of the synchronous transaction
modifying a resource.  Synchronous submission yielded responsive publishing with the caveat that events would be dropped
upon submission failure.

We have replaced the synchronous mechanism with a two stage process.  Events are initially stored in
database upon completion of the transaction and subsequently submitted to the broker by a scheduled task.
This new asynchronous submission mechanism will introduce a slight delay in event publishing.  It is our view that such
delay is largely compensated by the capability to retry submission upon failure which will eliminate event losses.

### Tag, Security Label, and Profile changes

There are some potentially breaking changes: 
* On resource retrieval and before storage, tags, security label and profile collections in resource meta will be 
sorted in lexicographical order. The order of the elements for Coding types (i.e. tags and security labels) is defined 
by the (security, code) pair of each element. This normally should not break any clients because these properties are 
sets according to the FHIR specification, and hence the order of the elements in these collections should not matter. 
Also with this change the following side effects can be observed:
   - If using INLINE tag storage mode, the first update request to a resource which has tags, security 
     labels or profiles could create a superfluous resource version if the update request does not really introduce any 
     change to the resource. This is because the persisted tags, security labels, and profile may not be sorted in 
     lexicographical order, and this would be interpreted as a new resource version since the tags would be sorted 
     before storage after this change. If the update request actually changes the resource, there is no concern here.
     Also, subsequent updates will not create an additional version because of ordering of the meta properties anymore. 
   - These meta collections are sorted in place by the storage layer before persisting the resource, so any piece of 
     code that is calling storage layer directly should not be passing in unmodifiable collections, as it would 
     result in an error. 
