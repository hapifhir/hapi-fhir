/**
 * The test plan for concurrent MDM processing with match claims. The design under test, with sequence diagrams, is in
 * this module's {@code README.md}.
 *
 * <h2>What must hold</h2>
 * However the units of work for a set of sources interleave, the resulting golden resources and links must be one of
 * the outcomes a single consumer could have produced, processing the sources one at a time in the order they actually
 * committed. Sources whose claims don't overlap must not wait for each other.
 *
 * <h2>Test layers</h2>
 * <table>
 *     <caption>Where each part of the design is tested</caption>
 *     <tr><th>Test</th><th>Covers</th></tr>
 *     <tr><td>{@code MdmMatchClaimKeyTest}, {@code MdmMatchClaimSvcJpaImplTest}, {@code MdmTransactionContextTest}</td>
 *         <td>keys, conflict classification, context checkpoints (unit)</td></tr>
 *     <tr><td>{@code MdmMatchClaimSvcJpaImplIT}</td>
 *         <td>claim, takeover by token, purge, against the database</td></tr>
 *     <tr><td>{@code MdmMatchLinkSvcConcurrencyIT}</td>
 *         <td>the main races, forced with storage hooks: shared EID, matching without EIDs, an EID added concurrently,
 *         non-matching sources running in parallel, retries, joined transactions</td></tr>
 *     <tr><td>{@link ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyMatrixIT}</td>
 *         <td>every thread interleaving against every data pattern (below)</td></tr>
 *     <tr><td>{@link ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyTargetedIT}</td>
 *         <td>T1-T6, cases the matrix can't express: a joined caller racing a consumer, a transaction-service retry,
 *         a claim purged mid-attempt, over a thousand candidates, sources in two partitions sharing an EID, running
 *         out of restarts</td></tr>
 *     <tr><td>{@link ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrentConsumersLoadIT}</td>
 *         <td>the real MDM channel with several consumers, as a smoke test of the whole path</td></tr>
 * </table>
 *
 * <h2>The harness</h2>
 * {@link ca.uhn.fhir.jpa.mdm.svc.concurrency.GatingMdmMatchClaimSvc} wraps the real claim service and parks each
 * worker thread at two gates in every attempt: before it takes its claims (its <b>start</b>, {@code S}) and just before
 * its transaction commits ({@code C}, or {@code A} to make it abort). A
 * {@link ca.uhn.fhir.jpa.mdm.svc.concurrency.ThreadSequence} is a script of these steps.
 * {@link ca.uhn.fhir.jpa.mdm.svc.concurrency.ScriptedInterleavingTestHarness} opens one gate at a time in script order. For
 * example {@code P7}, {@code "S1 S2 C1 S3 C2 C3"}:
 * <pre>
 *  t1  --S1-----------C1------------------
 *  t2  -------S2-----------------C2-------
 *  t3  ----------------------S3-------C3--
 * </pre>
 * Plain latches or {@code LockstepEnumPhaser} can't drive this, because the design itself makes threads wait: a
 * thread released at its start can block inside the database on another thread's claim and never reach its next
 * gate, so a phaser waiting for it would hang. The harness instead waits a bounded time for each step, and records
 * the step as {@code DONE}, {@code BLOCKED} (released but waiting in the database), {@code DEFERRED} (never reached the
 * gate; retried after the script) or {@code SKIPPED} (a scripted restart that never happened). The timeout only
 * classifies a step; it never decides the order of events. Once the script ends every gate opens and the threads run
 * to completion. Conflict retries and restarts that the script doesn't mention happen on their own and are recorded
 * as events.
 *
 * <h2>Why the matrix is exhaustive</h2>
 * Each thread starts before it commits. Renaming threads is the same as handing the sources to the threads in a
 * different order, and the matrix runs every distinct assignment of sources to threads, so only scripts whose starts
 * are in thread order ({@code S1} before {@code S2} before {@code S3}) are needed. That leaves exactly 15 interleavings
 * of three threads ({@code P1}, {@code P4}-{@code P17}) and 3 of two ({@code SERIAL2}, {@code P2}, {@code P3}). On top
 * of these the matrix adds aborts ({@code P18}, {@code P19}, {@code P23A}, {@code P23B}) and repeated starts, which are
 * restarts to claim a newly found candidate ({@code P20}-{@code P22}, {@code P24}).
 * <p>
 * The data patterns in {@link ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyDataPatterns} cover the ways sources
 * can relate:
 * <table>
 *     <caption>Data patterns</caption>
 *     <tr><th>Pattern</th><th>Sources</th></tr>
 *     <tr><td>R1</td><td>one source submitted to every thread</td></tr>
 *     <tr><td>R2, R4/R5</td><td>sources linked only by a shared EID, alone or beside an unrelated source</td></tr>
 *     <tr><td>R3</td><td>a source bridging two others by carrying both of their EIDs</td></tr>
 *     <tr><td>R6, R7, R8/R9, R10/R11</td><td>matching by score without EIDs, with symmetric and one-way candidate
 *         pools</td></tr>
 *     <tr><td>R12-R14, R15-R17</td><td>sources that don't match: these must run with no conflict, restart or
 *         block</td></tr>
 *     <tr><td>R18</td><td>POSSIBLE_MATCH only</td></tr>
 *     <tr><td>R19</td><td>a source blocked from matching</td></tr>
 *     <tr><td>R20, R21</td><td>an EID source and a score match; an EID added by an update before the race</td></tr>
 *     <tr><td>R22</td><td>new sources racing to join a golden resource that already exists</td></tr>
 * </table>
 * Each case is a sequence, a pattern and an assignment. Its expected outcome is written by hand per pattern, keyed by
 * the order in which the sources actually committed, because the design can force a commit order different from the
 * script's. For the non-matching patterns the case also asserts that no thread conflicted, restarted or blocked.
 *
 * <h2>Running</h2>
 * <pre>
 * mvn test-compile failsafe:integration-test -pl hapi-fhir-jpaserver-mdm -Dit.test=MdmConcurrencyMatrixIT
 * mvn test-compile failsafe:integration-test -pl hapi-fhir-jpaserver-mdm -Dit.test=MdmConcurrencyMatrixIT -Dmdm.matrix.filter="P7 x R3 .*"
 * </pre>
 * To check that the tests catch the bug, turn match claims off ({@code setMatchClaimsEnabled(false)} in
 * {@code MdmHelperConfig}): the load test then reports more golden resources than clusters.
 *
 * <h2>Limits</h2>
 * The tests run on H2 only. Other databases signal a blocked claim insert differently (duplicate key, lock timeout or
 * deadlock); {@code MdmMatchClaimSvcJpaImpl.isConcurrencyConflict} maps all of them, but only H2's behaviour is
 * exercised here. The matrix stops at three threads.
 */
// Created by Claude Opus 5.5
package ca.uhn.fhir.jpa.mdm.svc.concurrency;
