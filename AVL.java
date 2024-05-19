import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.lang.Math.max;

/**
 * Your implementation of an AVL.
 *
 * @author Aron Silberwasser
 * @version 1.0
 * @userid asilberwasser3
 * @GTID 903683147
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        for (T curr : data) {
            if (data == null) {
                throw new IllegalArgumentException("no data can be null");
            } else {
                add(curr);
            }
        }
    }

    /**
     * recursive helper method to make updating height easier
     * @param curr current node in the recursion
     * @return the height of the inputted node
     */
    private int calculateHeight(AVLNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return max(calculateHeight(curr.getRight()), calculateHeight(curr.getLeft())) + 1;
    }

    /**
     * helper method to make updating balance factor easier
     * @param curr input node
     * @return the balance factor of the inputted node
     */
    private int balanceFactor(AVLNode<T> curr) {
        int leftHeight;
        int rightHeight;

        if (curr.getLeft() == null) {
            leftHeight = -1;
        } else {
            leftHeight = curr.getLeft().getHeight();
        }
        if (curr.getRight() == null) {
            rightHeight = -1;
        } else {
            rightHeight = curr.getRight().getHeight();
        }

        return leftHeight - rightHeight;
    }

    /**
     * performs a left rotation
     * @param curr inputted node to be rotated
     * @return left rotated subtree
     */
    private AVLNode<T> leftRotate(AVLNode<T> curr) {
        AVLNode<T> temp1 = curr.getRight();
        AVLNode<T> temp2;
        if (curr.getRight() != null) {
            temp2 = curr.getRight().getLeft();
        } else {
            temp2 = null;
        }

        curr.setRight(temp2);
        temp1.setLeft(curr);

        curr.setBalanceFactor(balanceFactor(curr));
        curr.setHeight(calculateHeight(curr));
        temp1.setBalanceFactor(balanceFactor(temp1));
        temp1.setHeight(calculateHeight(temp1));
        if (temp2 != null) {
            temp2.setBalanceFactor(balanceFactor(temp2));
            temp2.setHeight(calculateHeight(temp2));
        }

        return temp1;
    }

    /**
     * performs a right rotation
     * @param curr inputted node to be rotated
     * @return right rotated subtree
     */
    private AVLNode<T> rightRotate(AVLNode<T> curr) {
        AVLNode<T> temp1 = curr.getLeft();
        AVLNode<T> temp2;
        if (curr.getLeft() != null) {
            temp2 = curr.getLeft().getRight();
        } else {
            temp2 = null;
        }

        curr.setLeft(temp2);
        temp1.setRight(curr);

        curr.setBalanceFactor(balanceFactor(curr));
        curr.setHeight(calculateHeight(curr));
        temp1.setBalanceFactor(balanceFactor(temp1));
        temp1.setHeight(calculateHeight(temp1));
        if (temp2 != null) {
            temp2.setBalanceFactor(balanceFactor(temp2));
            temp2.setHeight(calculateHeight(temp2));
        }

        return temp1;
    }

    /**
     * performs double rotation algorithm
     * @param curr inputted node to be double rotated
     * @return double rotated subtree
     */
    private AVLNode<T> doubleRotation(AVLNode<T> curr) {
        curr.setHeight(calculateHeight(curr));
        curr.setBalanceFactor(balanceFactor(curr));

        if (curr.getBalanceFactor() <= -2) {
            if (curr.getRight().getBalanceFactor() <= -1) {
                curr = leftRotate(curr);
            } else if (curr.getRight().getBalanceFactor() >= 1) {
                curr.setRight(rightRotate(curr.getRight()));
                curr = leftRotate(curr);
            }
        } else if (curr.getBalanceFactor() >= 2) {
            if (curr.getLeft().getBalanceFactor() <= -1) {
                curr.setLeft(leftRotate(curr.getLeft()));
                curr = rightRotate(curr);
            } else if (curr.getLeft().getBalanceFactor() >= 1) {
                curr = rightRotate(curr);
            }
        }

        return curr;
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }

        root = addHelper(data, root);
    }

    /**
     * helper method for recursion to work
     * @param data the data to add
     * @param curr current node
     * @return reinforces node
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> curr) {
        if (curr == null) {
            size++;
            return new AVLNode<>(data);
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(addHelper(data, curr.getLeft()));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(addHelper(data, curr.getRight()));
        }

        return doubleRotation(curr);
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:l
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data, NOT predecessor. As a reminder, rotations can occur
     * after removing the successor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }

        AVLNode<T> dummy = new AVLNode<>(null);
        root = removeHelper(data, root, dummy);
        return dummy.getData();
    }

    /**
     * helper method for recursion to work
     * @param data the data to remove
     * @param curr current node
     * @param dummy dummy node to hold removed data
     * @return reinforces node
     */
    private AVLNode<T> removeHelper(T data, AVLNode<T> curr, AVLNode<T> dummy) {
        if (curr == null) {
            throw new NoSuchElementException("data is not in the tree");
        }

        if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(removeHelper(data, curr.getLeft(), dummy));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(removeHelper(data, curr.getRight(), dummy));
        } else if (data.equals(curr.getData())) {

            dummy.setData(curr.getData());

            if (curr.getRight() == null && curr.getLeft() == null) {
                curr = curr.getRight();
            } else if (curr.getLeft() == null) {
                curr = curr.getRight();
            } else if (curr.getRight() == null) {
                curr = curr.getLeft();
            } else {
                AVLNode<T> succ = curr.getRight();
                while (succ.getLeft() != null) {
                    succ = succ.getRight();
                }

                succ.setLeft(curr.getLeft());
                curr = succ;
            }

            size--;
        }

        return doubleRotation(curr);
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        return getHelper(data, root).getData();
    }

    /**
     * helper method for recursion to work
     * @param data the data to search for
     * @param curr current node
     * @return reinforces node
     */
    private AVLNode<T> getHelper(T data, AVLNode<T> curr) {
        if (curr == null) {
            throw new NoSuchElementException("data is not in tree");
        } else if (data.compareTo(curr.getData()) < 0) {
            return getHelper(data, curr.getLeft());
        } else if (data.compareTo(curr.getData()) > 0) {
            return getHelper(data, curr.getRight());
        }
        return curr;
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }

        return containsHelper(data, root) != null;
    }

    /**
     * helper method for recursion to work
     * @param data the data to search for
     * @param curr current node
     * @return reinforces node
     */
    private AVLNode<T> containsHelper(T data, AVLNode<T> curr) {
        if (curr == null) {
            return null;
        } else if (data.compareTo(curr.getData()) < 0) {
            return getHelper(data, curr.getLeft());
        } else if (data.compareTo(curr.getData()) > 0) {
            return getHelper(data, curr.getRight());
        }
        return curr;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return root.getHeight();
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Find all elements within a certain distance from the given data.
     * "Distance" means the number of edges between two nodes in the tree.
     *
     * To do this, first find the data in the tree. Keep track of the distance
     * of the current node on the path to the data (you can use the return
     * value of a helper method to denote the current distance to the target
     * data - but note that you must find the data first before you can
     * calculate this information). After you have found the data, you should
     * know the distance of each node on the path to the data. With that
     * information, you can determine how much farther away you can traverse
     * from the main path while remaining within distance of the target data.
     *
     * Use a HashSet as the Set you return. Keep in mind that since it is a
     * Set, you do not have to worry about any specific order in the Set.
     * 
     * This must be implemented recursively.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *              50
     *            /    \
     *         25      75
     *        /  \     / \
     *      13   37  70  80
     *    /  \    \      \
     *   12  15    40    85
     *  /
     * 10
     * elementsWithinDistance(37, 3) should return the set {12, 13, 15, 25,
     * 37, 40, 50, 75}
     * elementsWithinDistance(85, 2) should return the set {75, 80, 85}
     * elementsWithinDistance(13, 1) should return the set {12, 13, 15, 25}
     *
     * @param data     the data to begin calculating distance from
     * @param distance the maximum distance allowed
     * @return the set of all data within a certain distance from the given data
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   is the data is not in the tree
     * @throws java.lang.IllegalArgumentException if distance is negative
     */
    public Set<T> elementsWithinDistance(T data, int distance) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("distance cannot be negative");
        }
        
        HashSet<T> set = new HashSet<>();
        AVLNode<T> copyTargetNode = new AVLNode<>(null);
        int start = findLevel(data, root, copyTargetNode) - distance;

        path(data, root, -1 * start, set);
        tracePoints(copyTargetNode, distance, 0, set);

        return set;
    }

    /**
     * finds level of target data in the tree recursively, and copies target node into a dummy node
     * @param data target data
     * @param curr current node in the recursion
     * @param targetNode dummy node so that we can obtain a copy of the target node
     * @return the level the node with the input data is in
     */
    private int findLevel(T data, AVLNode<T> curr, AVLNode<T> targetNode) {
        if (curr == null) {
            throw new NoSuchElementException("data is not in tree");
        } else if (data.compareTo(curr.getData()) < 0) {
            return findLevel(data, curr.getLeft(), targetNode) + 1;
        } else if (data.compareTo(curr.getData()) > 0) {
            return findLevel(data, curr.getRight(), targetNode) + 1;
        }

        targetNode.setRight(curr.getRight());
        targetNode.setLeft(curr.getLeft());
        targetNode.setData(curr.getData());
        targetNode.setHeight(curr.getHeight());
        targetNode.setBalanceFactor(curr.getBalanceFactor());

        return 0;
    }

    /**
     * follows path recursively towards the target node, with each recursion containing
     * another recursion to traverse down the tree [points] levels down.
     * @param data target data is used to traverse down the correct path to the target node
     * @param curr current node in the recursion
     * @param points amount of edges we have traversed down the path towards the target node, given that we start
     *               counting after traversing down [start] number of levels
     * @param set the set we insert elements into
     */
    private void path(T data, AVLNode<T> curr, int points, HashSet<T> set) {
        if (data.compareTo(curr.getData()) < 0) {
            tracePoints(curr, points, 0, set);
            path(data, curr.getLeft(), points + 1, set);
        } else if (data.compareTo(curr.getData()) > 0) {
            tracePoints(curr, points, 0, set);
            path(data, curr.getRight(), points + 1, set);
        }
    }

    /**
     * will traverse down the tree from the given node in all paths, as long as it traverses down
     * only as much as we have traversed down the path towards the target node
     * @param curr current node in recursion
     * @param points amount of edges we have traversed down the path towards the target node, given that we start
     *               counting after traversing down [start] number of levels
     * @param pointsUsed keeps track of how many times we want to do this recursion
     * @param set the set we insert elements into
     */
    private void tracePoints(AVLNode<T> curr, int points, int pointsUsed, HashSet<T> set) {
        if (pointsUsed <= points) {
            tracePoints(curr.getLeft(), points, pointsUsed + 1, set);
            tracePoints(curr.getRight(), points, pointsUsed + 1, set);
            set.add(curr.getData());
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}