package org.tuc.experiment;

import org.tuc.interfaces.SearchInsert;
import org.tuc.avl.AVLTree;
import org.tuc.bst.BSTree;
import org.tuc.btree.BTree;
import org.tuc.linearhashing.LinearHashing;

import java.io.*;
import java.util.*;

public class ExperimentMain {

    private static final int[] N_VALUES = {
            20, 50, 100, 200, 1000,
            2500, 5000, 10000, 20000,
            40000, 60000, 80000, 100000,
            200000, 1000000, 3000000
    };

    private static final String[] NAMES = {
            "BST", "AVL", "BTree10", "BTree600", "Linear100", "Linear1000"
    };

    public static void main(String[] args) {

        System.out.println("==== EXPERIMENT START ====");

        double[][] insertTimes = new double[N_VALUES.length][NAMES.length];
        double[][] searchTimes = new double[N_VALUES.length][NAMES.length];
        double[][] searchLevels = new double[N_VALUES.length][NAMES.length];
        double[][] rangeLevels = new double[N_VALUES.length][NAMES.length];

        for (int row = 0; row < N_VALUES.length; row++) {

            int N = N_VALUES[row];
            int K = getK(N);

            System.out.println("Running N = " + N + " | K = " + K);

            int[] initialKeys = readInts("domoules_2/src/data/numbers-" + N + ".bin");

            SearchInsert[] structures = {
                    new BSTree(),
                    new AVLTree(),
                    new BTree(5), // περίπου order 10
                    new BTree(300), // περίπου order 600
                    new LinearHashing(100, 500),
                    new LinearHashing(1000, 500)
            };

            for (SearchInsert s : structures) {
                for (int key : initialKeys) {
                    s.insert(key);
                }
            }

            insertTimes[row] = measureInsert(structures, K, N);
            searchTimes[row] = measureSearchTimes(structures, K, N);
            searchLevels[row] = measureSearchLevels(structures, K, N);
            rangeLevels[row] = measureRangeLevels(structures, K, N);
        }

        printFullTable("Insertion Times", insertTimes, true);
        printFullTable("Search Times", searchTimes, true);
        printFullTable("Search Access Levels", searchLevels, true);
        printFullTable("Range Search Access Levels", rangeLevels, false);
    }

    private static int[] readInts(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName);
                DataInputStream dis = new DataInputStream(fis)) {

            int size = fis.available() / 4;
            int[] data = new int[size];

            for (int i = 0; i < size; i++) {
                int b1 = dis.read();
                int b2 = dis.read();
                int b3 = dis.read();
                int b4 = dis.read();

                data[i] = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
            }

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    private static double[] measureInsert(SearchInsert[] structures, int K, int N) {
        double[] result = new double[structures.length];
        int[] keys = uniqueRandomInts(K, 1, 3 * N, 1);

        for (int i = 0; i < structures.length; i++) {
            long total = 0;

            for (int key : keys) {
                long start = System.nanoTime();
                structures[i].insert(key);
                long end = System.nanoTime();

                total += end - start;
            }

            result[i] = (double) total / K;
        }

        return result;
    }

    private static double[] measureSearchTimes(SearchInsert[] structures, int K, int N) {
        double[] result = new double[structures.length];
        int[] keys = uniqueRandomInts(K, 1, 3 * N, 2);

        for (int i = 0; i < structures.length; i++) {
            long total = 0;

            for (int key : keys) {
                long start = System.nanoTime();
                structures[i].searchKey(key);
                long end = System.nanoTime();

                total += end - start;
            }

            result[i] = (double) total / K;
        }

        return result;
    }

    private static double[] measureSearchLevels(SearchInsert[] structures, int K, int N) {
        double[] result = new double[structures.length];
        int[] keys = uniqueRandomInts(K, 1, 3 * N, 2);

        for (int i = 0; i < structures.length; i++) {
            long totalLevels = 0;

            for (int key : keys) {
                structures[i].searchKey(key);
                totalLevels += getSearchLevels(structures[i]);
            }

            result[i] = (double) totalLevels / K;
        }

        return result;
    }

    private static double[] measureRangeLevels(SearchInsert[] structures, int K, int N) {
        double[] result = new double[structures.length];
        int[] lows = uniqueRandomInts(K, 1, 3 * N, 3);

        for (int i = 0; i < 4; i++) {
            long totalLevels = 0;

            for (int low : lows) {
                structures[i].rangeQuery(low, low + 200);
                totalLevels += getRangeLevels(structures[i]);
            }

            result[i] = (double) totalLevels / K;
        }

        result[4] = -1;
        result[5] = -1;

        return result;
    }

    private static int getSearchLevels(SearchInsert s) {
        if (s instanceof BSTree)
            return ((BSTree) s).getLastSearchLevels();

        if (s instanceof AVLTree)
            return ((AVLTree) s).getLastSearchLevels();

        if (s instanceof BTree)
            return ((BTree) s).getLastSearchLevels();

        if (s instanceof LinearHashing)
            return ((LinearHashing) s).getLastSearchLevels();

        return 0;
    }

    private static int getRangeLevels(SearchInsert s) {
        if (s instanceof BSTree)
            return ((BSTree) s).getLastRangeLevels();

        if (s instanceof AVLTree)
            return ((AVLTree) s).getLastRangeLevels();

        if (s instanceof BTree)
            return ((BTree) s).getLastRangeLevels();

        return 0;
    }

    private static int[] uniqueRandomInts(int count, int min, int max, long seed) {
        Random r = new Random(seed);

        return r.ints(min, max + 1)
                .distinct()
                .limit(count)
                .toArray();
    }

    private static int getK(int N) {
        if (N < 201)
            return 10;

        if (N < 1001)
            return 50;

        return 100;
    }

    private static void printFullTable(String title, double[][] table, boolean includeLinear) {
        System.out.println("\n\n" + title);
        System.out.println(
                "----------------------------------------------------------------------------------------------------");

        System.out.printf("%-12s", "N");

        for (String name : NAMES) {
            System.out.printf("%-15s", name);
        }

        System.out.println();
        System.out.println(
                "----------------------------------------------------------------------------------------------------");

        for (int i = 0; i < N_VALUES.length; i++) {
            System.out.printf("%-12d", N_VALUES[i]);

            for (int j = 0; j < NAMES.length; j++) {
                if (!includeLinear && j >= 4) {
                    System.out.printf("%-15s", "-");
                } else {
                    System.out.printf("%-15.2f", table[i][j]);
                }
            }

            System.out.println();
        }
    }
}