This release introduce significant changes to the mechanism performing submission of resource modification events
to the message broker.  The mechanism found in previous releases would submit an event as part of the transaction
modifying a resource.  Synchronous submission yielded responsive publishing with the caveat that events would be dropped
upon submission failure.

We have replaced the synchronous mechanism with a two stage process.  Events are initially stored in
persistence as part of the operation on a resource and subsequently submitted to the broker by a scheduled task.
This new asynchronous submission mechanism will introduce a slight delay in event publishing.  It is our view that such
delay is largely compensated by the capability to retry submission upon failure which will eliminate event losses.
