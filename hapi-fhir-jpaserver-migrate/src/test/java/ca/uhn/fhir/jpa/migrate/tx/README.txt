Ken Stevens
I agree with you we should try to find a way to prevent the behaviour we saw in the db when they flood smile servers with large transactions creating the same entity.
My gut-reaction is that an in-memory mutex is not the best way to do this.
Can we coordinate the update vs insert via the database?
e.g. catch an insert failure in a sub-transaction and retry with an update so we only rollback the insert, not the entire transaction

I wonder if hibernate has some kind of a saveOrUpdate method we could use for this.

James Agnew  
I do know that hibernate doesn't support upsert operations currently, which would be another solution to this problem...
We could do it with DB row locking, which would solve it 100% of the time (as opposed to the 90% of the time we'd get with a mutex)
but that would also slow down overall throughput a fair bit in any case where the row doesn't already exist

Ken Stevens  
What about a sub transaction for just the insert and if it fails, try an update instead?

James Agnew  
What if it succeeds, but something later in the transaction fails? you'd have to manually roll it back, and cross your fingers
that nothing else depended on it already.. seems risky

Ken Stevens  
I thought sub transactions rolled back automatically if their parent rolled back?

Ken Stevens  
See NESTED propagation here: https://www.baeldung.com/spring-transactional-propagation-isolation

James Agnew
I guess the question comes down to: when two transactions each have nested transactions that potentially affect the same row,
do they have visibility into that conflict at the end of the nested transaction, or only at the end of the outer transaction

Ken Stevens  
Maybe use READ_UNCOMMITTED isolation on the nested transaction to minimize the chance of that?

James Agnew  
this definitely merits investigation..

Ken Stevens  
I can write a latched up test

James Agnew  
my main lingering concern is speed… i wanna keep in mind that the normal case is definitely not 10 threads all trying to
write the same resource in the middle of a transaction bundle. I’m nervous that if a UPMC bundle is now going to be 30 database transactions
instead of 1 that is going to kill performance

Ken Stevens  
For sure.

Would want to benchmark.  Maybe make it configurable
It’s annoying that upsert isn’t natively supported.  It’s such a common use case.

... time passes ...

Ken Stevens  
Well I made the test

James Agnew  
How does the race condition manifest? like both transactions fail? or you get a long deadlock/timeout?

Ken Stevens  
It is as I feared

The PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED works as expected

However there's a race condition

This works:
Thread 1: insert row
Thread 2: insert row (fails)
Thread 1: commits
Thread 2: updates row (succeeds)

However this fails:
Thread 1: insert row
Thread 2: insert row (fails)
Thread 2: updates row <-- ACK TOO SOON
Thread 1: commits

If we could do something like this, then that would fix it:
Thread 1: insert row
Thread 2: insert row in READ_UNCOMMITTED (fails)
Thread 2: wait for row to be committed (e.g. poll select exists with ISOLATION_READ_COMMITTED)
Thread 1: commits
Thread 2: polling completes
Thread 2: update row

I assume now we're checking for the existence of the row in the same Tx inside a READ_COMMITTED isolation
Maybe instead we should check for the existence inside a READ_UNCOMMITTED and if we find one, poll until we see it with
READ_COMMITTED and then our update is guaranteed to work.

See NestedTxTest.java

Though it's probably too convoluted to read.  The main tests are testNestedRetryLucky and testNestedRetryUnlucky which emulate
the two cases of us being lucky or unlucky with the race conditions.

Performance questions:
Are savepoints and rolling back to them (on a single failed insert) expensive?
Are READ_UNCOMMITTED queries more expensive than READ_COMMITTED queries?

I guess another case to consider is if the other Tx rolls back, then we could be sitting there waiting for a commit that never happens...

The ideal thing would be if there was some way to get the Tx id of the other Tx that has the uncommitted insert and block until that Tx commits or rolls back.
But I don't know if that's even a thing...
