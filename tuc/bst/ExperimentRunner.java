import java.util.Random;

public class ExperimentRunner {

    static Random random = new Random();

    public static void main(String[] args) {

        int[] Ns = {30,50,100,200,500,800,1000,5000,10000,100000};

        for(int N : Ns){

            System.out.println("\n==============================");
            System.out.println("N = " + N);
            System.out.println("==============================");

            runExperiment(N);
        }
    }

    private static void runExperiment(int N){

        DynamicMemoryBST tree1 = new DynamicMemoryBST();
        ArrayBST tree2 = new ArrayBST(200000);
        SortedArrayBinarySearch tree3 = new SortedArrayBinarySearch(200000);

        int[] initialKeys =
                random.ints(1,2*N+1)
                        .distinct()
                        .limit(N)
                        .toArray();

        for(int key : initialKeys){

            tree1.insert(key);
            tree2.insert(key);
            tree3.insert(key);
        }

        if(N == 30){

            System.out.print("Dynamic BST inorder: ");
            tree1.inorder();

            System.out.print("Array BST inorder: ");
            tree2.inorder();
        }

        int K = getK(N);

        System.out.println("\nInsert measurements:");
        measureInsert(tree1, tree2, tree3, N, K);

        System.out.println("\nDelete measurements:");
        measureDelete(tree1, tree2, tree3, N, K);

        System.out.println("\nSearch measurements:");
        measureSearch(tree1, tree2, tree3, N, K);

        System.out.println("\nRangeSearch measurements:");
        measureRange(tree1, tree2, tree3, N, K);
    }

    private static int getK(int N){

        if(N < 201) return 20;
        if(N < 1001) return 50;
        return 100;
    }

    private static double avg(long total,int K){
        return Math.round(((double)total / K) * 100.0) / 100.0;
    }

    private static void printResults(
            String name,
            long time,
            long ops,
            long levels,
            int K){

        System.out.println(name + " mean time: " + avg(time,K));
        System.out.println(name + " mean ops: " + avg(ops,K));
        System.out.println(name + " mean levels: " + avg(levels,K));
        System.out.println();
    }

    private static void measureInsert(
            DynamicMemoryBST t1,
            ArrayBST t2,
            SortedArrayBinarySearch t3,
            int N,
            int K){

        long time1=0,time2=0,time3=0;
        long ops1=0,ops2=0,ops3=0;
        long lev1=0,lev2=0,lev3=0;

        int[] keys = random.ints(1,2*N+1).limit(K).toArray();

        for(int key : keys){

            t1.resetMetrics();
            long start = System.nanoTime();
            t1.insert(key);
            time1 += System.nanoTime() - start;
            ops1 += t1.getOperations();
            lev1 += t1.getLevels();

            t2.resetMetrics();
            start = System.nanoTime();
            t2.insert(key);
            time2 += System.nanoTime() - start;
            ops2 += t2.getOperations();
            lev2 += t2.getLevels();

            t3.resetMetrics();
            start = System.nanoTime();
            t3.insert(key);
            time3 += System.nanoTime() - start;
            ops3 += t3.getOperations();
            lev3 += t3.getLevels();
        }

        printResults("Dynamic BST",time1,ops1,lev1,K);
        printResults("Array BST",time2,ops2,lev2,K);
        printResults("BinarySearch",time3,ops3,lev3,K);
    }

    private static void measureDelete(
            DynamicMemoryBST t1,
            ArrayBST t2,
            SortedArrayBinarySearch t3,
            int N,
            int K){

        long time1=0,time2=0,time3=0;
        long ops1=0,ops2=0,ops3=0;
        long lev1=0,lev2=0,lev3=0;

        int[] keys = random.ints(1,2*N+1).limit(K).toArray();

        for(int key : keys){

            t1.resetMetrics();
            long start = System.nanoTime();
            t1.delete(key);
            time1 += System.nanoTime() - start;
            ops1 += t1.getOperations();
            lev1 += t1.getLevels();

            t2.resetMetrics();
            start = System.nanoTime();
            t2.delete(key);
            time2 += System.nanoTime() - start;
            ops2 += t2.getOperations();
            lev2 += t2.getLevels();

            t3.resetMetrics();
            start = System.nanoTime();
            t3.delete(key);
            time3 += System.nanoTime() - start;
            ops3 += t3.getOperations();
            lev3 += t3.getLevels();
        }

        printResults("Dynamic BST",time1,ops1,lev1,K);
        printResults("Array BST",time2,ops2,lev2,K);
        printResults("BinarySearch",time3,ops3,lev3,K);
    }

    private static void measureSearch(
            DynamicMemoryBST t1,
            ArrayBST t2,
            SortedArrayBinarySearch t3,
            int N,
            int K){

        long time1=0,time2=0,time3=0;
        long ops1=0,ops2=0,ops3=0;
        long lev1=0,lev2=0,lev3=0;

        int[] keys = random.ints(1,2*N+1).limit(K).toArray();

        for(int key : keys){

            t1.resetMetrics();
            long start = System.nanoTime();
            t1.search(key);
            time1 += System.nanoTime() - start;
            ops1 += t1.getOperations();
            lev1 += t1.getLevels();

            t2.resetMetrics();
            start = System.nanoTime();
            t2.search(key);
            time2 += System.nanoTime() - start;
            ops2 += t2.getOperations();
            lev2 += t2.getLevels();

            t3.resetMetrics();
            start = System.nanoTime();
            t3.search(key);
            time3 += System.nanoTime() - start;
            ops3 += t3.getOperations();
            lev3 += t3.getLevels();
        }

        printResults("Dynamic BST",time1,ops1,lev1,K);
        printResults("Array BST",time2,ops2,lev2,K);
        printResults("BinarySearch",time3,ops3,lev3,K);
    }

    private static void measureRange(
            DynamicMemoryBST t1,
            ArrayBST t2,
            SortedArrayBinarySearch t3,
            int N,
            int K){

        long time1=0,time2=0,time3=0;
        long ops1=0,ops2=0,ops3=0;
        long lev1=0,lev2=0,lev3=0;

        for(int i=0;i<K;i++){

            int a = random.nextInt(2*N)+1;
            int b = random.nextInt(2*N)+1;

            int low = Math.min(a,b);
            int high = Math.max(a,b);

            t1.resetMetrics();
            long start = System.nanoTime();
            t1.rangeSearch(low,high);
            time1 += System.nanoTime() - start;
            ops1 += t1.getOperations();
            lev1 += t1.getLevels();

            t2.resetMetrics();
            start = System.nanoTime();
            t2.rangeSearch(low,high);
            time2 += System.nanoTime() - start;
            ops2 += t2.getOperations();
            lev2 += t2.getLevels();

            t3.resetMetrics();
            start = System.nanoTime();
            t3.rangeSearch(low,high);
            time3 += System.nanoTime() - start;
            ops3 += t3.getOperations();
            lev3 += t3.getLevels();
        }

        printResults("Dynamic BST",time1,ops1,lev1,K);
        printResults("Array BST",time2,ops2,lev2,K);
        printResults("BinarySearch",time3,ops3,lev3,K);
    }
}