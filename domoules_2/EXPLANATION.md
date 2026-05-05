# Project Implementation Explanation

This document explains the changes made to the data structures and the implementation of the experiment for comparing search structures.

## 1. Interface Implementation
The `SearchInsert` interface was already mostly implemented in the provided classes, but I ensured that all 4 structures (`AVLTree`, `BSTree`, `BTree`, `LinearHashing`) correctly implement the following methods:
- `void insert(int key)`
- `boolean searchKey(int key)`
- `List<Integer> rangeQuery(int low, int high)`

## 2. Level Tracking
To measure the "levels" accessed (nodes in trees, buckets in linear hashing), I added tracking logic to each class:
- **BSTree / AVLTree**: Each node visited during `searchKey` or `rangeQuery` is counted.
- **BTree**: Each node (page) visited is counted. The `rangeQuery` was optimized to prune branches that do not overlap with the target range.
- **LinearHashing**: Each bucket (primary and overflow) visited during search is counted. `rangeQuery` returns an empty list as specified.

A `lastAccessedLevels` field and a getter were added to each class, which are reset at the beginning of each search or range query.

## 3. BTree Optimization
The initial `rangeQuery` in `BTree.java` was performing an exhaustive search of the entire tree. I refactored it to use the keys in each node to prune the search, significantly improving performance and reducing the number of accessed levels.

## 4. Experiment Logic
The `ExperimentMain` class handles the entire experimental process:
- **Binary File Reading**: Implemented `readInts` to read 4-byte Little Endian integers from the `.bin` files.
- **Data Initialization**: For each $N$, the structures are populated with the initial keys.
- **Measurements**:
    - Average insertion time (nanoseconds) over $K$ operations.
    - Average search time (nanoseconds) and average levels over $K$ operations.
    - Average range query levels over $K$ operations (only for trees).
- **Repetitions ($K$)**: Adjusted based on $N$ ($K=10$ for $N \le 200$, $K=50$ for $N \le 1000$, $K=100$ for $N > 1000$).
- **Output**: Generates readable tables for all measurements.

## 5. Observations from Results
- **AVL vs BST**: The AVL tree maintains logarithmic search depth (approx 21-22 levels for $3 \times 10^6$ nodes), while the BST becomes significantly deeper (up to 100 levels) due to the semi-ordered nature of the input data.
- **BTree**: The BTree with order 600 has a very small height (3 levels for $3 \times 10^6$ nodes), making it ideal for disk-based storage where each level is a disk access.
- **Linear Hashing**: Maintains extremely low access levels (close to 1.0) regardless of $N$, confirming its $O(1)$ average case performance.

## How to run
1. Compile: `javac -d bin src/org/tuc/interfaces/SearchInsert.java src/org/tuc/avl/*.java src/org/tuc/bst/*.java src/org/tuc/btree/*.java src/org/tuc/linearhashing/*.java src/org/tuc/experiment/*.java`
2. Run: `java -cp bin org.tuc.experiment.ExperimentMain`
