Changes made:

- Implemented the required SearchInsert interface and adapter methods in four data structures under src/org/tuc:
  - avl/AVLTree.java: added imports; class now implements org.tuc.interfaces.SearchInsert; added searchKey(boolean) adapter and rangeQuery(low,high) implemented via in-order traversal.
  - bst/BSTree.java: same as AVL (searchKey + rangeQuery via in-order traversal).
  - btree/BTree.java: class implements interface; searchKey uses existing Contain(); rangeQuery collects keys in [low,high] by traversing nodes.
  - linearhashing/LinearHashing.java: class implements interface; searchKey delegates to existing search(); rangeQuery returns empty list (hashing doesn't support range queries).

Verification:
- Compiled all Java sources with javac; compilation succeeded.

Notes / next steps:
- The BTree "order" parameter left as-is; adapt constructor parameters if you want the exact course definition mapping (order K -> node holds up to K keys).
- To compile locally: javac -d bin $(find src -name '*.java')

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>