package org.corfudb.integration;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.Range;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.corfudb.protocols.wireprotocol.LogData;
import org.corfudb.protocols.wireprotocol.ReadResponse;
import org.corfudb.protocols.wireprotocol.TokenResponse;
import org.corfudb.runtime.BootstrapUtil;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.CorfuRuntime.CorfuRuntimeParameters;
import org.corfudb.runtime.clients.BaseHandler;
import org.corfudb.runtime.clients.IClientRouter;
import org.corfudb.runtime.clients.LayoutClient;
import org.corfudb.runtime.clients.LayoutHandler;
import org.corfudb.runtime.clients.ManagementClient;
import org.corfudb.runtime.clients.ManagementHandler;
import org.corfudb.runtime.clients.NettyClientRouter;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.exceptions.AlreadyBootstrappedException;
import org.corfudb.runtime.exceptions.TransactionAbortedException;
import org.corfudb.runtime.view.Layout;
import org.corfudb.runtime.view.stream.IStreamView;
import org.corfudb.util.CFUtils;
import org.corfudb.util.NodeLocator;
import org.corfudb.util.Sleep;
import org.junit.Before;
import org.junit.Test;

public class ClusterReconfigIT extends AbstractIT {

    private static String corfuSingleNodeHost;

    @Before
    public void loadProperties() {
        corfuSingleNodeHost = (String) PROPERTIES.get("corfuSingleNodeHost");
    }

    private Random getRandomNumberGenerator() {
        final Random randomSeed = new Random();
        final long SEED = randomSeed.nextLong();
        // Keep this print at all times to reproduce any failed test.
        testStatus += "SEED=" + Long.toHexString(SEED);
        return new Random(SEED);
    }

    private Layout get3NodeLayout() {
        return new Layout(
                new ArrayList<>(
                        Arrays.asList("localhost:9000", "localhost:9001", "localhost:9002")),
                new ArrayList<>(
                        Arrays.asList("localhost:9000", "localhost:9001", "localhost:9002")),
                Collections.singletonList(new Layout.LayoutSegment(
                        Layout.ReplicationMode.CHAIN_REPLICATION,
                        0L,
                        -1L,
                        Collections.singletonList(new Layout.LayoutStripe(
                                Arrays.asList("localhost:9000", "localhost:9001", "localhost:9002")
                        )))),
                0L,
                UUID.randomUUID());
    }

    private Layout get1NodeLayout() {
        return new Layout(
                new ArrayList<>(Collections.singletonList("localhost:9000")),
                new ArrayList<>(Collections.singletonList("localhost:9000")),
                Collections.singletonList(new Layout.LayoutSegment(
                        Layout.ReplicationMode.CHAIN_REPLICATION,
                        0L,
                        -1L,
                        Collections.singletonList(new Layout.LayoutStripe(
                                Collections.singletonList("localhost:9000")
                        )))),
                0L,
                UUID.randomUUID());
    }

    /**
     * Refreshes the layout and waits for a limited time for the refreshed layout to satisfy the
     * expected epoch verifier.
     *
     * @param epochVerifier Predicate to test the refreshed epoch value.
     * @param corfuRuntime  Runtime.
     */
    private void waitForEpochChange(Predicate<Long> epochVerifier, CorfuRuntime corfuRuntime) {
        corfuRuntime.invalidateLayout();
        Layout refreshedLayout = corfuRuntime.getLayoutView().getLayout();
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
            if (epochVerifier.test(refreshedLayout.getEpoch())) {
                break;
            }
            corfuRuntime.invalidateLayout();
            refreshedLayout = corfuRuntime.getLayoutView().getLayout();
            Sleep.sleepUninterruptibly(PARAMETERS.TIMEOUT_SHORT);
        }
        assertThat(epochVerifier.test(refreshedLayout.getEpoch())).isTrue();
    }

    /**
     * Creates a message of specified size in bytes.
     *
     * @param msgSize
     * @return
     */
    private static String createStringOfSize(int msgSize) {
        StringBuilder sb = new StringBuilder(msgSize);
        for (int i = 0; i < msgSize; i++) {
            sb.append('a');
        }
        return sb.toString();
    }

    private Process runSinglePersistentServer(String address, int port) throws IOException {
        return new CorfuServerRunner()
                .setHost(address)
                .setPort(port)
                .setLogPath(getCorfuServerLogPath(address, port))
                .setSingle(true)
                .runServer();
    }

    private Process runUnbootstrappedPersistentServer(String address, int port) throws IOException {
        return new CorfuServerRunner()
                .setHost(address)
                .setPort(port)
                .setLogPath(getCorfuServerLogPath(address, port))
                .setSingle(false)
                .runServer();
    }

    private Thread startDaemonWriter(CorfuRuntime runtime, Random r, CorfuTable table,
                                     String data, AtomicBoolean stopFlag) {
        Thread t = new Thread(() -> {
            while (stopFlag.get()) {
                assertThatCode(() -> {
                    try {
                        runtime.getObjectsView().TXBegin();
                        table.put(Integer.toString(r.nextInt()), data);
                        runtime.getObjectsView().TXEnd();
                    } catch (TransactionAbortedException e) {
                        // A transaction aborted exception is expected during
                        // some reconfiguration cases.
                    }
                }).doesNotThrowAnyException();
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    /**
     * A cluster of one node is started - 9000.
     * Then a block of data of 15,000 entries is written to the node.
     * This is to ensure we have at least 1.5 data log files.
     * A daemon thread is instantiated to randomly put data while add node is executed.
     * 2 nodes - 9001 and 9002 are added to the cluster.
     * Finally the epoch and the addition of the 2 nodes in the laout is verified.
     *
     * @throws Exception
     */
    @Test
    public void addNodesTest() throws Exception {
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        final Duration timeout = Duration.ofMinutes(5);
        final Duration pollPeriod = Duration.ofSeconds(5);
        final int workflowNumRetry = 3;

        Process corfuServer_1 = runSinglePersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);

        CorfuRuntime runtime = createDefaultRuntime();

        CorfuTable table = runtime.getObjectsView()
                .build()
                .setType(CorfuTable.class)
                .setStreamName("test")
                .open();

        final String data = createStringOfSize(1_000);

        Random r = getRandomNumberGenerator();
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
            String key = Long.toString(r.nextLong());
            table.put(key, data);
        }

        final AtomicBoolean moreDataToBeWritten = new AtomicBoolean(true);
        Thread t = startDaemonWriter(runtime, r, table, data, moreDataToBeWritten);

        runtime.getManagementView().addNode("localhost:9001", workflowNumRetry,
                timeout, pollPeriod);
        runtime.getManagementView().addNode("localhost:9002", workflowNumRetry,
                timeout, pollPeriod);

        final long epochAfter2Adds = 4L;
        runtime.invalidateLayout();
        assertThat(runtime.getLayoutView().getLayout().getEpoch()).isEqualTo(epochAfter2Adds);

        moreDataToBeWritten.set(false);
        t.join();

        verifyData(runtime);

        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_2);
        shutdownCorfuServer(corfuServer_3);
    }

    /**
     * Queries for all the data in the 3 nodes and checks if the data state matches across the
     * cluster.
     *
     * @param corfuRuntime Connected instance of the runtime.
     * @throws Exception
     */
    private void verifyData(CorfuRuntime corfuRuntime) throws Exception {

        TokenResponse tokenResponse = corfuRuntime.getSequencerView()
                .query(CorfuRuntime.getStreamID("test"));
        long lastAddress = tokenResponse.getTokenValue();

        Map<Long, LogData> map_0 = getAllNonEmptyData(corfuRuntime, "localhost:9000", lastAddress);
        Map<Long, LogData> map_1 = getAllNonEmptyData(corfuRuntime, "localhost:9001", lastAddress);
        Map<Long, LogData> map_2 = getAllNonEmptyData(corfuRuntime, "localhost:9002", lastAddress);

        assertThat(map_1.entrySet()).containsOnlyElementsOf(map_0.entrySet());
        assertThat(map_2.entrySet()).containsOnlyElementsOf(map_0.entrySet());
    }

    /**
     * Fetches all the data from the node.
     *
     * @param corfuRuntime Connected instance of the runtime.
     * @param endpoint     Endpoint ot query for all the data.
     * @param end          End address up to which data needs to be fetched.
     * @return Map of all the addresses contained by the node corresponding to the data stored.
     * @throws Exception
     */
    private Map<Long, LogData> getAllNonEmptyData(CorfuRuntime corfuRuntime,
                                                  String endpoint, long end) throws Exception {
        ReadResponse readResponse = corfuRuntime.getLayoutView().getRuntimeLayout()
                .getLogUnitClient(endpoint)
                .read(Range.closed(0L, end)).get();
        return readResponse.getAddresses().entrySet()
                .stream()
                .filter(longLogDataEntry -> !longLogDataEntry.getValue().isEmpty())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    /**
     * Increments the epoch of the cluster by one. Keeps the remainder of the layout the same.
     *
     * @param corfuRuntime Connected runtime instance.
     * @return Returns the new accepted layout.
     * @throws Exception
     */
    private Layout incrementClusterEpoch(CorfuRuntime corfuRuntime) throws Exception {
        long oldEpoch = corfuRuntime.getLayoutView().getLayout().getEpoch();
        Layout l = new Layout(corfuRuntime.getLayoutView().getLayout());
        l.setEpoch(l.getEpoch() + 1);
        corfuRuntime.getLayoutView().getRuntimeLayout(l).moveServersToEpoch();
        corfuRuntime.getLayoutView().updateLayout(l, 1L);
        corfuRuntime.invalidateLayout();
        assertThat(corfuRuntime.getLayoutView().getLayout().getEpoch()).isEqualTo(oldEpoch + 1);
        return l;
    }

    /**
     * Starts a corfu server and increments the epoch from 0 to 1.
     * The server is then reset and the new layout fetch is expected to return a layout with
     * epoch 0 as all state should have been cleared.
     *
     * @throws Exception
     */
    @Test
    public void resetTest() throws Exception {

        final int PORT_0 = 9000;

        Process corfuServer = runSinglePersistentServer(corfuSingleNodeHost, PORT_0);

        CorfuRuntime corfuRuntime = createDefaultRuntime();
        incrementClusterEpoch(corfuRuntime);
        corfuRuntime.getLayoutView().getRuntimeLayout().getBaseClient("localhost:9000")
                .reset().get();

        corfuRuntime = createDefaultRuntime();
        // The shutdown and reset can take an unknown amount of time and there is a chance that the
        // newer runtime may also connect to the older corfu server (before reset).
        // Hence the while loop.
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
            if (corfuRuntime.getLayoutView().getLayout().getEpoch() == 0L) {
                break;
            }
            Thread.sleep(PARAMETERS.TIMEOUT_SHORT.toMillis());
            corfuRuntime = createDefaultRuntime();
        }
        assertThat(corfuRuntime.getLayoutView().getLayout().getEpoch()).isEqualTo(0L);
        assertThat(shutdownCorfuServer(corfuServer)).isTrue();
    }

    /**
     * Starts a corfu server and increments the epoch from 0 to 1.
     * The server is then restarted and the new layout fetch is expected to return a layout with
     * epoch 2 or greater (recovery can fail and epoch can increase) as the state is not cleared
     * and the restart forces a recovery to increment the epoch.
     *
     * @throws Exception
     */
    @Test
    public void restartTest() throws Exception {

        final int PORT_0 = 9000;

        Process corfuServer = runSinglePersistentServer(corfuSingleNodeHost, PORT_0);

        CorfuRuntime corfuRuntime = createDefaultRuntime();
        Layout l = incrementClusterEpoch(corfuRuntime);
        corfuRuntime.getLayoutView().getRuntimeLayout(l).getBaseClient("localhost:9000")
                .restart().get();

        restartServer(corfuRuntime, DEFAULT_ENDPOINT);

        assertThat(corfuRuntime.getLayoutView().getLayout().getEpoch())
                .isGreaterThanOrEqualTo(l.getEpoch() + 1);
        assertThat(shutdownCorfuServer(corfuServer)).isTrue();
    }

    /**
     * Creates 3 corfu server processes.
     * Connects a runtime and creates a corfu table to write data.
     * One of the node is then shutdown/killed. The cluster then stabilizes to the new
     * epoch by removing the failure from the logunit chain and bootstrapping a backup
     * sequencer (only if required). After the stabilization is complete, another write is
     * attempted. The test passes only if this write goes through.
     *
     * @param killNode Index of the node to be killed.
     * @throws Exception
     */
    private void killNodeAndVerifyDataPath(int killNode)
            throws Exception {

        // Set up cluster of 3 nodes.
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        List<Process> corfuServers = Arrays.asList(corfuServer_1, corfuServer_2, corfuServer_3);
        final Layout layout = get3NodeLayout();
        final int retries = 3;
        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        // Create map and set up daemon writer thread.
        CorfuRuntime runtime = createDefaultRuntime();
        CorfuTable table = runtime.getObjectsView()
                .build()
                .setType(CorfuTable.class)
                .setStreamName("test")
                .open();
        final String data = createStringOfSize(1_000);
        Random r = getRandomNumberGenerator();
        final AtomicBoolean moreDataToBeWritten = new AtomicBoolean(true);
        Thread t = startDaemonWriter(runtime, r, table, data, moreDataToBeWritten);

        // Some preliminary writes into the corfu table.
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_VERY_LOW; i++) {
            runtime.getObjectsView().TXBegin();
            table.put(Integer.toString(r.nextInt()), data);
            runtime.getObjectsView().TXEnd();
        }

        // Killing killNode.
        assertThat(shutdownCorfuServer(corfuServers.get(killNode))).isTrue();

        // We wait for failure to be detected and the cluster to stabilize by waiting for the epoch
        // to increment.
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layout.getEpoch(), runtime);

        // Stop the daemon thread.
        moreDataToBeWritten.set(false);
        t.join();

        // Ensure writes still going through.
        // Fail test if write fails.
        boolean writeAfterKillNode = false;
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
            try {
                runtime.getObjectsView().TXBegin();
                table.put(Integer.toString(r.nextInt()), data);
                runtime.getObjectsView().TXEnd();
                writeAfterKillNode = true;
                break;
            } catch (TransactionAbortedException tae) {
                // A transaction aborted exception is expected during
                // some reconfiguration cases.
                tae.printStackTrace();
            }
            Thread.sleep(PARAMETERS.TIMEOUT_SHORT.toMillis());
        }
        assertThat(writeAfterKillNode).isTrue();

        for (int i = 0; i < corfuServers.size(); i++) {
            if (i == killNode) {
                continue;
            }
            assertThat(shutdownCorfuServer(corfuServers.get(i))).isTrue();
        }
    }

    /**
     * Kill a node containing the head of the log unit chain and the primary sequencer.
     * Ensures writes go through after kill node and cluster re-stabilization.
     *
     * @throws Exception
     */
    @Test
    public void killLogUnitAndPrimarySequencer() throws Exception {
        killNodeAndVerifyDataPath(0);
    }

    /**
     * Kill a node containing one of the log unit chain and the non-primary sequencer.
     * Ensures writes go through after kill node and cluster re-stabilization.
     *
     * @throws Exception
     */
    @Test
    public void killLogUnitAndBackupSequencer() throws Exception {
        killNodeAndVerifyDataPath(1);
    }

    /**
     * Bootstrap cluster of 3 nodes using the BootstrapUtil.
     *
     * @throws Exception
     */
    @Test
    public void test3NodeBootstrapUtility() throws Exception {

        // Set up cluster of 3 nodes.
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        final Layout layout = get3NodeLayout();

        final int retries = 3;
        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        CorfuRuntime corfuRuntime = createDefaultRuntime();
        assertThat(corfuRuntime.getLayoutView().getLayout().equals(layout)).isTrue();

        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_2);
        shutdownCorfuServer(corfuServer_3);
    }

    private void retryBootstrapOperation(Runnable bootstrapOperation) {
        final int retries = 5;
        int retry = retries;
        while (retry-- > 0) {
            try {
                bootstrapOperation.run();
                return;
            } catch (Exception e) {
                Sleep.sleepUninterruptibly(PARAMETERS.TIMEOUT_SHORT);
            }
        }
        fail();
    }

    /**
     * Setup a cluster of 1 node. Bootstrap the nodes.
     * Bootstrap the cluster using the BootstrapUtil. It should assert that the node already
     * bootstrapped is with the same layout and then bootstrap the cluster.
     */
    @Test
    public void test1NodeBootstrapWithBootstrappedNode() throws Exception {

        // Set up cluster of 1 nodes.
        final int PORT_0 = 9000;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        final Layout layout = get1NodeLayout();
        final int retries = 5;

        IClientRouter router = new NettyClientRouter(NodeLocator
                .parseString(corfuSingleNodeHost + ":" + PORT_0),
                CorfuRuntime.CorfuRuntimeParameters.builder().build());
        router.addClient(new LayoutHandler()).addClient(new BaseHandler());
        retryBootstrapOperation(() -> CFUtils.getUninterruptibly(
                new LayoutClient(router, layout.getEpoch()).bootstrapLayout(layout)));
        retryBootstrapOperation(() -> CFUtils.getUninterruptibly(
                new ManagementClient(router, layout.getEpoch()).bootstrapManagement(layout)));

        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        CorfuRuntime corfuRuntime = createDefaultRuntime();
        assertThat(corfuRuntime.getLayoutView().getLayout().equals(layout)).isTrue();

        shutdownCorfuServer(corfuServer_1);
    }

    /**
     * Setup a cluster of 1 node. Bootstrap the node with a wrong layout.
     * Bootstrap the cluster using the BootstrapUtil. It should assert that the node already
     * bootstrapped is with the wrong layout and then fail with the AlreadyBootstrappedException.
     */
    @Test
    public void test1NodeBootstrapWithWrongBootstrappedLayoutServer() throws Exception {

        // Set up cluster of 1 node.
        final int PORT_0 = 9000;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        final Layout layout = get1NodeLayout();
        final int retries = 3;

        IClientRouter router = new NettyClientRouter(NodeLocator
                .parseString(corfuSingleNodeHost + ":" + PORT_0),
                CorfuRuntime.CorfuRuntimeParameters.builder().build());
        router.addClient(new LayoutHandler()).addClient(new BaseHandler());
        Layout wrongLayout = new Layout(layout);
        wrongLayout.getLayoutServers().add("localhost:9005");
        retryBootstrapOperation(() -> CFUtils.getUninterruptibly(
                new LayoutClient(router, layout.getEpoch()).bootstrapLayout(wrongLayout)));

        assertThatThrownBy(() -> BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT))
                .hasCauseInstanceOf(AlreadyBootstrappedException.class);

        shutdownCorfuServer(corfuServer_1);
    }

    /**
     * Setup a cluster of 1 node. Bootstrap the node with a wrong layout.
     * Bootstrap the cluster using the BootstrapUtil. It should assert that the node already
     * bootstrapped is with the wrong layout and then fail with the AlreadyBootstrappedException.
     */
    @Test
    public void test1NodeBootstrapWithWrongBootstrappedManagementServer() throws Exception {

        // Set up cluster of 1 node.
        final int PORT_0 = 9000;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        final Layout layout = get1NodeLayout();
        final int retries = 3;

        IClientRouter router = new NettyClientRouter(NodeLocator
                .parseString(corfuSingleNodeHost + ":" + PORT_0),
                CorfuRuntime.CorfuRuntimeParameters.builder().build());
        router.addClient(new LayoutHandler())
                .addClient(new ManagementHandler())
                .addClient(new BaseHandler());
        Layout wrongLayout = new Layout(layout);
        final ManagementClient managementClient = new ManagementClient(router, layout.getEpoch());
        wrongLayout.getLayoutServers().add("localhost:9005");
        retryBootstrapOperation(() -> CFUtils.getUninterruptibly(
                managementClient.bootstrapManagement(wrongLayout)));

        assertThatThrownBy(() -> BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT))
                .hasCauseInstanceOf(AlreadyBootstrappedException.class);

        shutdownCorfuServer(corfuServer_1);
    }


    /**
     * Starts with 3 nodes on ports 0, 1 and 2.
     * A daemon thread is started for random writes in the background.
     * PART 1. Kill node.
     * First the node on port 0 is killed. The test then waits for the cluster to stabilize and
     * assert that data operations still go through.
     * Part 2. Revive node.
     * Now, the same node is revived and waits for the node to be healed and merged back into the
     * cluster.
     * Finally the data on all the 3 nodes is verified.
     *
     * @throws Exception
     */
    @Test
    public void killAndHealNode() throws Exception {

        // Set up cluster of 3 nodes.
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        final Layout layout = get3NodeLayout();
        final int retries = 3;
        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        // Create map and set up daemon writer thread.
        CorfuRuntime runtime = createDefaultRuntime();
        CorfuTable table = runtime.getObjectsView()
                .build()
                .setType(CorfuTable.class)
                .setStreamName("test")
                .open();
        final String data = createStringOfSize(1_000);
        Random r = getRandomNumberGenerator();

        // Some preliminary writes into the corfu table.
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_VERY_LOW; i++) {
            runtime.getObjectsView().TXBegin();
            table.put(Integer.toString(r.nextInt()), data);
            runtime.getObjectsView().TXEnd();
        }

        // PART 1.
        // Killing killNode.
        assertThat(shutdownCorfuServer(corfuServer_1)).isTrue();

        // We wait for failure to be detected and the cluster to stabilize by waiting for the epoch
        // to increment.
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layout.getEpoch(), runtime);

        // Ensure writes still going through. Daemon writer thread does not ensure this.
        // Fail test if write fails.
        boolean writeAfterKillNode = false;
        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
            try {
                runtime.getObjectsView().TXBegin();
                table.put(Integer.toString(r.nextInt()), data);
                runtime.getObjectsView().TXEnd();
                writeAfterKillNode = true;
                break;
            } catch (TransactionAbortedException tae) {
                // A transaction aborted exception is expected during
                // some reconfiguration cases.
            }
            Sleep.sleepUninterruptibly(PARAMETERS.TIMEOUT_SHORT);
        }
        assertThat(writeAfterKillNode).isTrue();

        // PART 2.
        // Reviving same node.
        corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        final long epochAfterHealingNode = 3L;

        // Waiting node to be healed and added back to the layout.
        waitForEpochChange(refreshedEpoch -> refreshedEpoch >= epochAfterHealingNode, runtime);
        verifyData(runtime);

        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_2);
        shutdownCorfuServer(corfuServer_3);
    }

    /**
     * This test starts a single corfu node. The CorfuRuntime is registered with a
     * systemDownHandler which releases a semaphore. The test waits on this semaphore to
     * complete successfully.
     */
    @Test
    public void testSystemDownHandler() throws Exception {

        final int PORT_0 = 9000;
        Process corfuServer_1 = runSinglePersistentServer(corfuSingleNodeHost, PORT_0);
        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        // Create map and set up daemon writer thread.
        CorfuRuntimeParameters corfuRuntimeParameters = CorfuRuntimeParameters.builder()
                .layoutServer(NodeLocator.parseString("localhost:9000"))
                .cacheDisabled(true)
                .systemDownHandlerTriggerLimit(1)
                // Register the system down handler to throw a RuntimeException.
                .systemDownHandler(() ->
                        assertThatCode(semaphore::release).doesNotThrowAnyException())
                .build();

        CorfuRuntime runtime = CorfuRuntime.fromParameters(corfuRuntimeParameters).connect();

        CorfuTable table = runtime.getObjectsView()
                .build()
                .setType(CorfuTable.class)
                .setStreamName("test")
                .open();
        final String data = createStringOfSize(1_000);
        Random r = getRandomNumberGenerator();

        // A preliminary write into the corfu table.
        runtime.getObjectsView().TXBegin();
        table.put(Integer.toString(r.nextInt()), data);
        runtime.getObjectsView().TXEnd();

        // Killing the server.
        assertThat(shutdownCorfuServer(corfuServer_1)).isTrue();

        Thread t = new Thread(() -> {
            for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
                try {
                    runtime.getObjectsView().TXBegin();
                    table.put(Integer.toString(r.nextInt()), data);
                    runtime.getObjectsView().TXEnd();
                    break;
                } catch (TransactionAbortedException tae) {
                    // A transaction aborted exception is expected during
                    // some reconfiguration cases.
                }
            }
        });
        t.start();

        // Wait for the systemDownHandler to be invoked.
        semaphore.tryAcquire(PARAMETERS.TIMEOUT_LONG.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Ensure that multiple resets on the same epoch reset the log unit server only once.
     * Consider 3 nodes 9000, 9001 and 9002.
     * Kill node 9002 and recover it so that the heal node workflow is triggered, the node is
     * reset and added back to the chain.
     * We then perform a write on addresses 6-10.
     * Now we attempt to reset the node 9002 again on the same epoch and assert that the write
     * performed on address 1 is not erased by the new reset.
     */
    @Test
    public void preventMultipleResets() throws Exception {
        // Set up cluster of 3 nodes.
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        final Layout layout = get3NodeLayout();

        final int retries = 3;
        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        CorfuRuntime runtime = createDefaultRuntime();
        UUID streamId = CorfuRuntime.getStreamID("testStream");
        IStreamView stream = runtime.getStreamsView().get(streamId);
        int counter = 0;

        // Shutdown server 3.
        shutdownCorfuServer(corfuServer_3);
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layout.getEpoch(), runtime);
        final Layout layoutAfterFailure = runtime.getLayoutView().getLayout();

        final int appendNum = 5;
        // Write at address 0-4.
        for (int i = 0; i < appendNum; i++) {
            stream.append(Integer.toString(counter++).getBytes());
        }

        // Restart server 3 and wait for heal node workflow to modify the layout.
        corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layoutAfterFailure.getEpoch(), runtime);

        // Write at address 5-9.
        final long startAddress = 0L;
        final long endAddress = 9L;
        for (int i = 0; i < appendNum; i++) {
            stream.append(Integer.toString(counter++).getBytes());
        }

        // Trigger a reset on the node.
        runtime.getLayoutView().getRuntimeLayout(layoutAfterFailure)
                .getLogUnitClient("localhost:9002")
                .resetLogUnit(layoutAfterFailure.getEpoch()).get();

        // Verify data
        int verificationCounter = 0;
        for (LogData logData : runtime.getLayoutView().getRuntimeLayout()
                .getLogUnitClient("localhost:9002")
                .read(Range.closed(startAddress, endAddress)).get()
                .getAddresses().values()) {
            assertThat(logData.getPayload(runtime))
                    .isEqualTo(Integer.toString(verificationCounter++).getBytes());
        }

        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_2);
        shutdownCorfuServer(corfuServer_3);
    }

    /**
     * Test that a node with a stale layout can recover.
     * Consider a cluster of 3 nodes - 9000, 9001 and 9002.
     * 9002 is first shutdown. This node is now stuck with the layout at epoch 0.
     * Now 9000 is restarted, forcing an epoch increment.
     * Next, 9002 is also restarted. The test then asserts that even though 9002 is lagging
     * by a few epochs, it recovers and is added back to the chain.
     */
    @Test
    public void healNodesWorkflowTest() throws Exception {
        // Set up cluster of 3 nodes.
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        final int nodesCount = 3;
        final Layout layout = get3NodeLayout();

        final int retries = 3;
        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        CorfuRuntime runtime = createDefaultRuntime();
        UUID streamId = CorfuRuntime.getStreamID("testStream");
        IStreamView stream = runtime.getStreamsView().get(streamId);

        shutdownCorfuServer(corfuServer_3);
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layout.getEpoch(), runtime);
        final Layout layoutAfterFailure1 = runtime.getLayoutView().getLayout();

        shutdownCorfuServer(corfuServer_1);
        corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layoutAfterFailure1.getEpoch(), runtime);
        final Layout layoutAfterHeal1 = runtime.getLayoutView().getLayout();
        int counter = 0;

        final int appendNum = 3;
        final long startAddress = 0;
        // Write at address 0-1-2
        for (int i = 0; i < appendNum; i++) {
            stream.append(Integer.toString(counter++).getBytes());
        }

        corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        waitForEpochChange(refreshedEpoch -> refreshedEpoch > layoutAfterHeal1.getEpoch(), runtime);

        // Write at address 3-4-5
        for (int i = 0; i < appendNum; i++) {
            stream.append(Integer.toString(counter++).getBytes());
        }
        final long endAddress = 5;

        for (int i = 0; i < PARAMETERS.NUM_ITERATIONS_MODERATE; i++) {
            Layout finalLayout = runtime.getLayoutView().getLayout();
            if (finalLayout.getUnresponsiveServers().isEmpty()
                    && finalLayout.getSegments().size() == 1
                    && finalLayout.getSegments().get(0).getAllLogServers().size() == nodesCount) {
                break;
            }
            runtime.invalidateLayout();
            Sleep.sleepUninterruptibly(PARAMETERS.TIMEOUT_VERY_SHORT);
        }

        assertThat(runtime.getLayoutView().getLayout().getUnresponsiveServers()).isEmpty();

        // Verify data
        int verificationCounter = 0;
        for (LogData logData : runtime.getLayoutView().getRuntimeLayout()
                .getLogUnitClient("localhost:9002")
                .read(Range.closed(startAddress, endAddress)).get()
                .getAddresses().values()) {
            assertThat(logData.getPayload(runtime))
                    .isEqualTo(Integer.toString(verificationCounter++).getBytes());
        }

        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_2);
        shutdownCorfuServer(corfuServer_3);
    }

    /**
     * Test that a disconnected CorfuRuntime using a systemDownHandler reconnects itself once the
     * cluster is back online. The client disconnection is simulated by taking 2 servers
     * 9000 and 9002 offline by shutting them down. They are then restarted and the date is
     * validated.
     */
    @Test
    public void reconnectDisconnectedClient() throws Exception {
        // Set up cluster of 3 nodes.
        final int PORT_0 = 9000;
        final int PORT_1 = 9001;
        final int PORT_2 = 9002;
        Process corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        Process corfuServer_2 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_1);
        Process corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);
        final Layout layout = get3NodeLayout();

        final int retries = 3;
        BootstrapUtil.bootstrap(layout, retries, PARAMETERS.TIMEOUT_SHORT);

        final int systemDownHandlerLimit = 10;
        CorfuRuntime runtime = CorfuRuntime.fromParameters(CorfuRuntimeParameters.builder()
                .layoutServer(NodeLocator.parseString(DEFAULT_ENDPOINT))
                .systemDownHandlerTriggerLimit(systemDownHandlerLimit)
                // Register the system down handler to throw a RuntimeException.
                .systemDownHandler(() -> {
                    throw new RuntimeException("SystemDownHandler");
                })
                .build()).connect();

        UUID streamId = CorfuRuntime.getStreamID("testStream");
        IStreamView stream = runtime.getStreamsView().get(streamId);

        final int appendNum = 3;
        int counter = 0;
        // Preliminary writes.
        for (int i = 0; i < appendNum; i++) {
            stream.append(Integer.toString(counter++).getBytes());
        }

        // Shutdown the servers 9000 and 9002.
        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_3);

        final Semaphore latch = new Semaphore(1);
        latch.acquire();

        // Invalidate the layout so that there are no cached layouts.
        runtime.invalidateLayout();

        // Spawn a thread which tries to append to the stream. This should initially get stuck and
        // trigger the systemDownHandler.
        // This would start the server and finally wait for the append to complete.
        Thread t = new Thread(() -> {
            final int counterValue = 3;
            while (true) {
                try {
                    stream.append(Integer.toString(counterValue).getBytes());
                    break;
                } catch (RuntimeException sue) {
                    // Release latch and try again..
                    latch.release();
                }
            }
        });
        t.start();

        assertThat(latch.tryAcquire(PARAMETERS.TIMEOUT_LONG.toMillis(), TimeUnit.MILLISECONDS))
                .isTrue();
        // Restart both the servers.
        corfuServer_1 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_0);
        corfuServer_3 = runUnbootstrappedPersistentServer(corfuSingleNodeHost, PORT_2);

        t.join(PARAMETERS.TIMEOUT_LONG.toMillis());

        // Verify data
        final int startAddress = 0;
        final int endAddress = 3;
        for (int i = startAddress; i <= endAddress; i++) {
            assertThat(runtime.getAddressSpaceView().read(i).getPayload(runtime))
                    .isEqualTo(Integer.toString(i).getBytes());
        }

        shutdownCorfuServer(corfuServer_1);
        shutdownCorfuServer(corfuServer_2);
        shutdownCorfuServer(corfuServer_3);
    }
}
