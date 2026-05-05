

import java.util.Random;

public class ExperimentRunner {

    private static final Random random = new Random();

    // Input sizes required by the assignment
    private static final int[] NS = {30, 50, 100, 200, 500, 800, 1000, 5000, 10000, 100000};

    public static void main(String[] args) {

        // First, print inorder traversals for N = 30 before any measurements
        printInorderForN30();

        // Run the full experiment once for each N and store all results
        ExperimentResults[] allResults = new ExperimentResults[NS.length];

        for (int i = 0; i < NS.length; i++) {
            allResults[i] = runExperimentForN(NS[i]);
        }

        // Print table for operation A
        System.out.println("\n==============================================================");
        System.out.println("Operation A - Insert");
        System.out.println("==============================================================");
        printHeader();
        for (int i = 0; i < NS.length; i++) {
            printRow(NS[i], allResults[i].insertMetrics);
        }

        // Print table for operation B
        System.out.println("\n==============================================================");
        System.out.println("Operation B - Delete");
        System.out.println("==============================================================");
        printHeader();
        for (int i = 0; i < NS.length; i++) {
            printRow(NS[i], allResults[i].deleteMetrics);
        }

        // Print table for operation C
        System.out.println("\n==============================================================");
        System.out.println("Operation C - Search");
        System.out.println("==============================================================");
        printHeader();
        for (int i = 0; i < NS.length; i++) {
            printRow(NS[i], allResults[i].searchMetrics);
        }

        // Print table for operation D
        System.out.println("\n==============================================================");
        System.out.println("Operation D - Range Search");
        System.out.println("==============================================================");
        printHeader();
        for (int i = 0; i < NS.length; i++) {
            printRow(NS[i], allResults[i].rangeMetrics);
        }
    }

    /**
     * Returns K according to the assignment specification.
     */
    private static int getK(int N) {
        if (N <= 200) {
            return 20;
        }
        if (N <= 1000) {
            return 50;
        }
        return 100;
    }

    /**
     * Returns a parameterized capacity for the array-based structures.
     */
    private static int getCapacity(int N) {
        int K = getK(N);
        return N + K + 10;
    }

    /**
     * Builds fresh structures for a given N and fills them with N distinct random keys
     * from the range [1, 2N].
     */
    private static ExperimentStructures buildStructures(int N) {
        int capacity = getCapacity(N);

        DynamicMemoryBST tree1 = new DynamicMemoryBST();
        ArrayBST tree2 = new ArrayBST(capacity);
        SortedArrayBinarySearch tree3 = new SortedArrayBinarySearch(capacity);

        int[] initialKeys = random.ints(1, 2 * N + 1)
                .distinct()
                .limit(N)
                .toArray();

        for (int key : initialKeys) {
            tree1.insert(key);
            tree2.insert(key);
            tree3.insert(key);
        }

        return new ExperimentStructures(tree1, tree2, tree3);
    }

    /**
     * Prints inorder traversals for N = 30 for the two BST implementations.
     */
    private static void printInorderForN30() {
        int N = 30;
        ExperimentStructures s = buildStructures(N);

        System.out.println("N = 30");

        s.tree1.printName();
        System.out.print("Inorder traversal: ");
        s.tree1.inorder();

        s.tree2.printName();
        System.out.print("Inorder traversal: ");
        s.tree2.inorder();
    }

    /**
     * Prints the table header.
     */
    private static void printHeader() {
        System.out.println(
                "N | DynOps | DynTime(ns) | DynLevels | ArrOps | ArrTime(ns) | ArrLevels | BinOps | BinTime(ns) | BinLevels");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Returns the average formatted with 2 decimal digits.
     */
    private static String avg(long total, int K) {
        double value = (double) total / K;
        return String.format(java.util.Locale.US, "%.2f", value);
    }

    /**
     * Runs the full experiment for one value of N on the same structures.
     */
    private static ExperimentResults runExperimentForN(int N) {
        int K = getK(N);
        ExperimentStructures s = buildStructures(N);

        Metrics insertMetrics = measureInsert(s, N, K);
        Metrics deleteMetrics = measureDelete(s, N, K);
        Metrics searchMetrics = measureSearch(s, N, K);
        Metrics rangeMetrics = measureRange(s, N, K);

        return new ExperimentResults(insertMetrics, deleteMetrics, searchMetrics, rangeMetrics);
    }

    /**
     * Measures operation A: insertion of K random keys from [1, 2N].
     */
    private static Metrics measureInsert(ExperimentStructures s, int N, int K) {
        long time1 = 0, time2 = 0, time3 = 0;
        long ops1 = 0, ops2 = 0, ops3 = 0;
        long lev1 = 0, lev2 = 0, lev3 = 0;

        int[] keys = random.ints(1, 2 * N + 1).limit(K).toArray();

        for (int key : keys) {
            s.tree1.resetMetrics();
            long start = System.nanoTime();
            s.tree1.insert(key);
            time1 += System.nanoTime() - start;
            ops1 += s.tree1.getOperations();
            lev1 += s.tree1.getLevels();

            s.tree2.resetMetrics();
            start = System.nanoTime();
            s.tree2.insert(key);
            time2 += System.nanoTime() - start;
            ops2 += s.tree2.getOperations();
            lev2 += s.tree2.getLevels();

            s.tree3.resetMetrics();
            start = System.nanoTime();
            s.tree3.insert(key);
            time3 += System.nanoTime() - start;
            ops3 += s.tree3.getOperations();
            lev3 += s.tree3.getLevels();
        }

        return new Metrics(time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    /**
     * Measures operation B: deletion of K random keys from [1, 2N].
     */
    private static Metrics measureDelete(ExperimentStructures s, int N, int K) {
        long time1 = 0, time2 = 0, time3 = 0;
        long ops1 = 0, ops2 = 0, ops3 = 0;
        long lev1 = 0, lev2 = 0, lev3 = 0;

        int[] keys = random.ints(1, 2 * N + 1).limit(K).toArray();

        for (int key : keys) {
            s.tree1.resetMetrics();
            long start = System.nanoTime();
            s.tree1.delete(key);
            time1 += System.nanoTime() - start;
            ops1 += s.tree1.getOperations();
            lev1 += s.tree1.getLevels();

            s.tree2.resetMetrics();
            start = System.nanoTime();
            s.tree2.delete(key);
            time2 += System.nanoTime() - start;
            ops2 += s.tree2.getOperations();
            lev2 += s.tree2.getLevels();

            s.tree3.resetMetrics();
            start = System.nanoTime();
            s.tree3.delete(key);
            time3 += System.nanoTime() - start;
            ops3 += s.tree3.getOperations();
            lev3 += s.tree3.getLevels();
        }

        return new Metrics(time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    /**
     * Measures operation C: search of K random keys from [1, 2N].
     */
    private static Metrics measureSearch(ExperimentStructures s, int N, int K) {
        long time1 = 0, time2 = 0, time3 = 0;
        long ops1 = 0, ops2 = 0, ops3 = 0;
        long lev1 = 0, lev2 = 0, lev3 = 0;

        int[] keys = random.ints(1, 2 * N + 1).limit(K).toArray();

        for (int key : keys) {
            s.tree1.resetMetrics();
            long start = System.nanoTime();
            s.tree1.search(key);
            time1 += System.nanoTime() - start;
            ops1 += s.tree1.getOperations();
            lev1 += s.tree1.getLevels();

            s.tree2.resetMetrics();
            start = System.nanoTime();
            s.tree2.search(key);
            time2 += System.nanoTime() - start;
            ops2 += s.tree2.getOperations();
            lev2 += s.tree2.getLevels();

            s.tree3.resetMetrics();
            start = System.nanoTime();
            s.tree3.search(key);
            time3 += System.nanoTime() - start;
            ops3 += s.tree3.getOperations();
            lev3 += s.tree3.getLevels();
        }

        return new Metrics(time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    /**
     * Measures operation D: range search using K random pairs of keys from [1, 2N].
     */
    private static Metrics measureRange(ExperimentStructures s, int N, int K) {
        long time1 = 0, time2 = 0, time3 = 0;
        long ops1 = 0, ops2 = 0, ops3 = 0;
        long lev1 = 0, lev2 = 0, lev3 = 0;

        for (int i = 0; i < K; i++) {
            int a = random.nextInt(2 * N) + 1;
            int b = random.nextInt(2 * N) + 1;
            int low = Math.min(a, b);
            int high = Math.max(a, b);

            s.tree1.resetMetrics();
            long start = System.nanoTime();
            s.tree1.rangeSearch(low, high);
            time1 += System.nanoTime() - start;
            ops1 += s.tree1.getOperations();
            lev1 += s.tree1.getLevels();

            s.tree2.resetMetrics();
            start = System.nanoTime();
            s.tree2.rangeSearch(low, high);
            time2 += System.nanoTime() - start;
            ops2 += s.tree2.getOperations();
            lev2 += s.tree2.getLevels();

            s.tree3.resetMetrics();
            start = System.nanoTime();
            s.tree3.rangeSearch(low, high);
            time3 += System.nanoTime() - start;
            ops3 += s.tree3.getOperations();
            lev3 += s.tree3.getLevels();
        }

        return new Metrics(time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    /**
     * Prints one table row for one value of N.
     */
    private static void printRow(int N, Metrics m) {
        String row = N + " | " +
                avg(m.ops1, m.K) + " | " +
                avg(m.time1, m.K) + " | " +
                avg(m.lev1, m.K) + " | " +
                avg(m.ops2, m.K) + " | " +
                avg(m.time2, m.K) + " | " +
                avg(m.lev2, m.K) + " | " +
                avg(m.ops3, m.K) + " | " +
                avg(m.time3, m.K) + " | " +
                avg(m.lev3, m.K);

        System.out.println(row);
    }
}