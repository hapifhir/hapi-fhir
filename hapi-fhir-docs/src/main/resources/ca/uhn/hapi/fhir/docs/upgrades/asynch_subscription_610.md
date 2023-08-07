
### Asynchronous Submission Processing

Previously, resource modification events were submitted to the message broker for processing in a synchronous manner.  
The synchronous mechanism has been replaced by an asynchronous one.

Synchronous event submission provided low latency delivery to the subscription matching module which allowed for a
responsive publishing of subscriptions.   The drawback of such mechanism is that it would not permit retrying event submission
upon initial submission failure, effectively dropping the event.  Asynchronous event submission is introduced to prevent
the loss of events at the expense of publishing responsiveness. 

