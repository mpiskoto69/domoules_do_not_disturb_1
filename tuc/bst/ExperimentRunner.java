import java.util.Random;

public class ExperimentRunner {

    static Random random = new Random();

    private static final int[] NS = { 30, 50, 100, 200, 500, 800, 1000, 5000, 10000, 100000 };

    public static void main(String[] args) {

        printInorderForN30();

        System.out.println("\n==============================================================");
        System.out.println("Operation A - Insert");
        System.out.println("==============================================================");
        printHeader();
        for (int N : NS) {
            printInsertRow(N);
        }

        System.out.println("\n==============================================================");
        System.out.println("Operation B - Delete");
        System.out.println("==============================================================");
        printHeader();
        for (int N : NS) {
            printDeleteRow(N);
        }

        System.out.println("\n==============================================================");
        System.out.println("Operation C - Search");
        System.out.println("==============================================================");
        printHeader();
        for (int N : NS) {
            printSearchRow(N);
        }

        System.out.println("\n==============================================================");
        System.out.println("Operation D - Range Search");
        System.out.println("==============================================================");
        printHeader();
        for (int N : NS) {
            printRangeRow(N);
        }
    }

   private static void printInorderForN30() {
    int N = 30;

    DynamicMemoryBST tree1 = new DynamicMemoryBST();
    ArrayBST tree2 = new ArrayBST(200000);
    SortedArrayBinarySearch tree3 = new SortedArrayBinarySearch(200000);

    int[] initialKeys = random.ints(1, 2 * N + 1)
            .distinct()
            .limit(N)
            .toArray();

    for (int key : initialKeys) {
        tree1.insert(key);
        tree2.insert(key);
        tree3.insert(key);
    }

    System.out.println("N = 30");

    tree1.printName();
    System.out.print("Inorder traversal: ");
    tree1.inorder();

    tree2.printName();
    System.out.print("Inorder traversal: ");
    tree2.inorder();
}

    private static void printHeader() {
        System.out.println(
                "N | DynOps | DynTime | DynLevels | ArrOps | ArrTime | ArrLevels | BinOps | BinTime | BinLevels");
        System.out.println(
                "----------------------------------------------------------------------------------------------------");
    }

    private static int getK(int N) {
        if (N < 201) {
            return 20;
        }
        if (N < 1001) {
            return 50;
        }
        return 100;
    }

    private static String avg(long total, int K) {
        double value = (double) total / K;
        return String.format(java.util.Locale.US, "%.2f", value); // format with 2 decimal places
    }

    private static ExperimentStructures buildStructures(int N) {
        DynamicMemoryBST tree1 = new DynamicMemoryBST();
        ArrayBST tree2 = new ArrayBST(200000);
        SortedArrayBinarySearch tree3 = new SortedArrayBinarySearch(200000);

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

    private static void printInsertRow(int N) {
        int K = getK(N);
        ExperimentStructures s = buildStructures(N);

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

        printRow(N, time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    private static void printDeleteRow(int N) {
        int K = getK(N);
        ExperimentStructures s = buildStructures(N);

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

        printRow(N, time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    private static void printSearchRow(int N) {
        int K = getK(N);
        ExperimentStructures s = buildStructures(N);

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

        printRow(N, time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    private static void printRangeRow(int N) {
        int K = getK(N);
        ExperimentStructures s = buildStructures(N);

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

        printRow(N, time1, ops1, lev1, time2, ops2, lev2, time3, ops3, lev3, K);
    }

    private static void printRow(
            int N,
            long time1, long ops1, long lev1,
            long time2, long ops2, long lev2,
            long time3, long ops3, long lev3,
            int K) {

        String row = N + " | " +
                avg(ops1, K) + " | " +
                avg(time1, K) + " | " +
                avg(lev1, K) + " | " +
                avg(ops2, K) + " | " +
                avg(time2, K) + " | " +
                avg(lev2, K) + " | " +
                avg(ops3, K) + " | " +
                avg(time3, K) + " | " +
                avg(lev3, K);

        System.out.println(row);
    }
}