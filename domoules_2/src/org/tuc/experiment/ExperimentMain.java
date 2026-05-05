package org.tuc.experiment;

import org.tuc.avl.AVLTree;
import org.tuc.bst.BSTree;
import org.tuc.btree.BTree;
import org.tuc.linearhashing.LinearHashing;
import org.tuc.interfaces.SearchInsert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class ExperimentMain {

    private static final int[] N_VALUES = {20, 50, 100, 200, 1000, 2500, 5000, 10000, 20000, 40000, 60000, 80000, 100000, 200000, 1000000, 3000000};
    private static String dataDir;

    static {
        // Try to find the data directory in common locations
        String[] potentialPaths = {
            System.getProperty("data.dir"),
            "src/org/tuc/randomnumbers/",
            "domoules_2/src/org/tuc/randomnumbers/",
            "../src/org/tuc/randomnumbers/",
            "domoules_do_not_disturb_1/domoules_2/src/org/tuc/randomnumbers/"
        };

        for (String path : potentialPaths) {
            if (path != null && Files.isDirectory(Paths.get(path))) {
                dataDir = path.endsWith("/") ? path : path + "/";
                break;
            }
        }

        if (dataDir == null) {
            dataDir = "src/org/tuc/randomnumbers/"; // Fallback to original
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Experiment...");
        System.out.println("Using data directory: " + Paths.get(dataDir).toAbsolutePath());
        
        if (!Files.isDirectory(Paths.get(dataDir))) {
            System.err.println("CRITICAL: Data directory not found! Please ensure you are running from the project root or specify -Ddata.dir=/path/to/randomnumbers/");
            return;
        }

        // Arrays to store results for the tables
        double[][] insertTimes = new double[N_VALUES.length][6];
        double[][] searchTimes = new double[N_VALUES.length][6];
        double[][] searchLevels = new double[N_VALUES.length][6];
        double[][] rangeLevels = new double[N_VALUES.length][4]; // Only for first 4 structures

        for (int i = 0; i < N_VALUES.length; i++) {
            int N = N_VALUES[i];
            int K = getK(N);
            System.out.println("Processing N = " + N + " (K = " + K + ")");

            int[] initialKeys;
            try {
                initialKeys = readInts(dataDir + "numbers-" + N + ".bin");
            } catch (IOException e) {
                System.err.println("Error reading file for N=" + N + ": " + e.getMessage());
                continue;
            }

            // Create structures
            SearchInsert[] structures = new SearchInsert[6];
            structures[0] = new BSTree();
            structures[1] = new AVLTree();
            structures[2] = new BTree(6); // T=6 => Max keys 11 (Order 10 approx)
            structures[3] = new BTree(301); // T=301 => Max keys 601 (Order 600 approx)
            structures[4] = new LinearHashing(100, 500);
            structures[5] = new LinearHashing(1000, 500);

            // Initial Insertion
            for (int key : initialKeys) {
                for (SearchInsert si : structures) {
                    si.insert(key);
                }
            }

            Random rand = new Random();

            // 1. Insertion Measurement
            int[] keysToInsert = rand.ints(1, 3 * N + 1).distinct().limit(K).toArray();
            for (int sIdx = 0; sIdx < 6; sIdx++) {
                long totalTime = 0;
                for (int key : keysToInsert) {
                    long start = System.nanoTime();
                    structures[sIdx].insert(key);
                    long end = System.nanoTime();
                    totalTime += (end - start);
                }
                insertTimes[i][sIdx] = (double) totalTime / K;
            }

            // 2. Search Measurement
            int[] keysToSearch = rand.ints(1, 3 * N + 1).distinct().limit(K).toArray();
            for (int sIdx = 0; sIdx < 6; sIdx++) {
                long totalTime = 0;
                long totalLevels = 0;
                for (int key : keysToSearch) {
                    long start = System.nanoTime();
                    structures[sIdx].searchKey(key);
                    long end = System.nanoTime();
                    totalTime += (end - start);
                    totalLevels += getLevels(structures[sIdx]);
                }
                searchTimes[i][sIdx] = (double) totalTime / K;
                searchLevels[i][sIdx] = (double) totalLevels / K;
            }

            // 3. Range Query Measurement (only for trees)
            int[] rangeStarts = rand.ints(1, 3 * N + 1).distinct().limit(K).toArray();
            for (int sIdx = 0; sIdx < 4; sIdx++) {
                long totalLevels = 0;
                for (int startKey : rangeStarts) {
                    structures[sIdx].rangeQuery(startKey, startKey + 200);
                    totalLevels += getLevels(structures[sIdx]);
                }
                rangeLevels[i][sIdx] = (double) totalLevels / K;
            }
        }

        printTable("Insert Times (ns)", insertTimes, true);
        printTable("Search Times (ns)", searchTimes, true);
        printTable("Search Levels", searchLevels, true);
        printTable("Range Query Levels", rangeLevels, false);
    }

    private static int getK(int N) {
        if (N <= 200) return 10;
        if (N <= 1000) return 50;
        return 100;
    }

    private static long getLevels(SearchInsert si) {
        if (si instanceof AVLTree) return ((AVLTree) si).getLastAccessedLevels();
        if (si instanceof BSTree) return ((BSTree) si).getLastAccessedLevels();
        if (si instanceof BTree) return ((BTree) si).getLastAccessedLevels();
        if (si instanceof LinearHashing) return ((LinearHashing) si).getLastAccessedLevels();
        return 0;
    }

    private static int[] readInts(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        byte[] bytes = Files.readAllBytes(path);
        int[] ints = new int[bytes.length / 4];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < ints.length; i++) {
            ints[i] = bb.getInt();
        }
        return ints;
    }

    private static void printTable(String title, double[][] data, boolean allStructures) {
        System.out.println("\n--- " + title + " ---");
        String header;
        if (allStructures) {
            header = String.format("%-10s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s", "N", "BST", "AVL", "BTree10", "BTree600", "Linear100", "Linear1000");
        } else {
            header = String.format("%-10s | %-12s | %-12s | %-12s | %-12s", "N", "BST", "AVL", "BTree10", "BTree600");
        }
        System.out.println(header);
        System.out.println("-".repeat(header.length()));

        for (int i = 0; i < N_VALUES.length; i++) {
            StringBuilder row = new StringBuilder(String.format("%-10d", N_VALUES[i]));
            for (int j = 0; j < data[i].length; j++) {
                row.append(String.format(" | %-12.2f", data[i][j]));
            }
            System.out.println(row.toString());
        }
    }
}
