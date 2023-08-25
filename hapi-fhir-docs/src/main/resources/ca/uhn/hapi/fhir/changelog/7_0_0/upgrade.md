This release introduces significant a change to the mechanism performing submission of resource modification events
to the message broker.  Previously, an event would be submitted as part of the synchronous transaction
modifying a resource.  Synchronous submission yielded responsive publishing with the caveat that events would be dropped
upon submission failure.

We have replaced the synchronous mechanism with a two stage process.  Events are initially stored in
database upon completion of the transaction and subsequently submitted to the broker by a scheduled task.
This new asynchronous submission mechanism will introduce a slight delay in event publishing.  It is our view that such
delay is largely compensated by the capability to retry submission upon failure which will eliminate event losses.
