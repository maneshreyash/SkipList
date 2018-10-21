package ssm170730;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * Implementation of Skip List Data Structure, an indexed list of items which provides O (Log n) runtime for
 * add, remove, get etc. operations.
 * Skip list is an implementation of Dictionary Abstract Data type which compete with balanced search trees like AVL,
 * Red-Black trees.
 * The skip list prefers to skip certain indexes for its basic operations which provides us with faster
 * results than any other type of list.
 * <p>
 * This project has been developed by Shreyash Mane ssm170730, Sunny Bangale shb170230,
 * Ketki Mahajan krm150330 and Ameya Kasar aak170230
 */

public class SkipList<T extends Comparable<? super T>> {
    static final int PossibleLevels = 33;

    Entry head;
    Entry tail;
    Entry[] last;
    int size;
    int maxLevel;
    Random random;

    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        int[] span;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            span = new int[lev];
        }

        public E getElement() {
            return element;
        }
    }

    // Constructor
    public SkipList() {

        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        size = 0;
        maxLevel = 1;
        last = new Entry[PossibleLevels];
        random = new Random();

        for (int i = 0; i < PossibleLevels; i++) {
            //Initially making all the indices of last to point to head
            last[i] = head;
            head.next[i] = tail;
        }
        tail.prev = head;
    }

    /**
     * Helps us to trace a path to reach an element in the skip list by updating the @last array
     * where the last[0] stores the location of the Entry just previous to the @{@link Entry} element in the
     * list, if the element is not present, last[0] will store the position of @{@link Entry} element least smaller
     * than x
     *
     * @param x Element to be found in the list
     */
    private void find(T x) {
        Entry<T> p = head;
        for (int i = maxLevel - 1; i >= 0; i--) {
            while (p.next[i].element != null && (x.compareTo((T) (p.next[i].element)) > 0)) {
                p = p.next[i];
            }
            last[i] = p;
        }
    }

    /**
     * Returns true if the @x is present in the list
     *
     * @param x element whose presence is to be searched for
     * @return false if absent
     */
    public boolean contains(T x) {
        if (size == 0) {
            return false;
        }
        find(x);
        if (last[0].next[0].element != null) {//Check if it is added to the end
            return x.compareTo((T) (last[0].next[0].element)) == 0;
        } else {
            return false;
        }
    }

    /**
     * Add x to list. If x already exists, reject it. Returns true if new node is added to list
     *
     * @param x Element to be added in the list
     * @return true if add is successful
     */

    public boolean add(T x) {


        if (size == 0) {
            int level = maxLevel;
            Entry<T> ent = new Entry<T>(x, level);
            for (int i = 0; i < level; i++) {
                head.next[i] = ent;
                ent.next[i] = tail;
            }
            ent.prev = head;
            tail.prev = ent;
            size++;
            updateSpan(level, ent);
            return true;
        }

        if (contains(x)) {
            return false;
        }
        if (addHelper(x)) {
            /*Entry spanner = head;
            if (spanner == head) {
                updateSpan(maxLevel, spanner);
                spanner = spanner.next[0];
            }
            while (x.compareTo((T) spanner.element) > 0) {
                updateSpan(spanner.next.length, spanner);
                spanner = spanner.next[0];
            }*/
            updateSpanForPreviousElements();
        }
        return true;
    }

    /**
     * A utility method that helps us in adding an element when there is atleast 1 element in the list
     *
     * @param x Eleement to be added in the list
     * @return true if add is successful
     */
    private boolean addHelper(T x) {

        int level = chooseLevel();
        Entry<T> ent = new Entry<T>(x, level);

        for (int i = 0; i < level; i++) {
            if (last[i].next[i] != null) {//Checks if it is the last element
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
            } else {
                ent.next[i] = tail;
                last[i].next[i] = ent;
            }
        }
        ent.next[0].prev = ent;
        ent.prev = last[0];
        size++;
        updateSpan(level, ent);
        return true;
    }

    /**
     * Updates the span array for the given @{@link Entry} by using the provided length of the next[] for the entry
     *
     * @param level the length of next[] for the new entry
     * @param ent   for which the span is to be calculated
     */
    private void updateSpan(int level, Entry ent) {
        ent.span[0] = 0;
        int count = 0;
        for (int i = 1; i < level; i++) {
            count = 0;
            Entry cursor = ent;
            //checking tail
            if (ent.next[i].element == null) {
                while (cursor.next[0].element != null) {
                    count++;
                    cursor = cursor.next[0];
                }
            } else {
                while (cursor.next[0].element != null && ((T) ent.next[i].element).compareTo((T) cursor.next[0].element) != 0) {
                    count++;
                    cursor = cursor.next[0];
                }
            }
            ent.span[i] = count;
        }
    }


    private void updateSpanForPreviousElements() {
        last[0].span[0] = 0;
        int i = 1;
        while (i <= maxLevel - 1) {
            if (last[i] == last[i - 1]) {
                last[i].span[i] = last[i - 1].span[i - 1];
                i++;
            } else {
                int count = 0;
                Entry cursor = last[i - 1];
                while (cursor != last[i]) {
                    cursor = cursor.prev;
                    count++;
                }
                last[i].span[i] = last[i].span[i - 1] + count;
            }
            i++;
        }
    }

    /**
     * Provides with a the level for each Entry and updates the @maxLevel param each time this method is called
     *
     * @return the level that will be assigned to the calling entity
     */
    private int chooseLevel() {
        int level = 1 + Integer.numberOfTrailingZeros(random.nextInt());
        int newLevel = Math.min(level, maxLevel + 1);

        if (newLevel > maxLevel) {
            maxLevel = newLevel;
        }
        return newLevel;
    }

    /**
     * Finds smallest element that is greater or equal to x
     *
     * @param x for which ceiling value is needed
     * @return Ceiling value for x
     */
    public T ceiling(T x) {
        find(x);
        if (last[0].next[0].element == null) { // Checks if it is the last element in the list
            return null;
        } else {
            return (T) last[0].next[0].element;
        }
    }

    /**
     * Returns first element of list
     *
     * @return first element in the list, null if list is empty
     */
    public T first() {
        if (size > 0) {
            return (T) ((head.next[0]).element);
        }
        return null;
    }

    /**
     * Finds largest element that is less than or equal to x
     *
     * @param x for which floor value is needed
     * @return Floor value for x
     */
    public T floor(T x) {
        if (size > 0) {
            if (contains(x)) {
                return (T) last[0].next[0].element;
            } else {
                return (T) last[0].element;
            }
        }
        return null;
    }

    /**
     * Returns element at index n of list.  First element is at index 0.
     *
     * @param n the index at which the element is to be retrieved from
     * @return the element present at n index
     *
     */
    public T get(int n) {
        return getLog(n);
    }

    /**
     * Provides an O(n) algorithm for get(n) where we iterate over all the items from head till n
     *
     * @param n the index n at which the element is seeked at
     * @return the element at index n
     */
    public T getLinear(int n) {
        if (n < 0 || n > size - 1) {
            return null;
        }
        Entry<T> p = head;
        for (int i = 0; i <= n; i++) {
            p = p.next[0];
        }
        return p.element;

    }

    /**
     * Enables us to get the element at index n in O(log n) expected time
     *
     * @param n the index n at which the element is seeked at
     * @return the element at index n
     */
    public T getLog(int n) {
        if (n > size - 1 || n < 0) {
            return null;
        }

        Entry<T> cursor = head;
        int i = maxLevel - 1;
        int t = 0;
        while (i >= 0) {
            if (cursor.span[i] >= n - t) {
                if (n == 0 && cursor == head) {
                    return (T) cursor.next[0].getElement();
                }
                i--;
            } else if (cursor.span[i] < n - t) {
                if (cursor == head) {
                    if (cursor.span[i] == 0) {
                        //t++;
                        cursor = cursor.next[i];
                    } else if (cursor.span[i] > 0) {
                        t = t + cursor.span[i];
                        cursor = cursor.next[i];
                    }
                } else {
                    if (cursor.span[i] == 0) {
                        t++;
                        cursor = cursor.next[i];
                    } else if (cursor.span[i] > 0) {
                        t = t + cursor.span[i] + 1;
                        cursor = cursor.next[i];
                    }

                }
            }
            //else
        }
        if (n == t) {
            return cursor.getElement();
        }
        return null;
    }

    /**
     * Checks if the skiplist is empty
     *
     * @return true if size is 0
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return new SkipListIterator();
    }

    /**
     * Returns last element of list
     *
     * @return last element if size > 0 else returns null
     */
    public T last() {
        T temp = null;
        if (size > 0) {
            //return (T) (tail.prev.element);
            Entry curr = head;
            while (curr != tail) {
                temp = (T) curr.element;
                curr = curr.next[0];
            }
        }
        return temp;
    }

    /**
     * Reorganizes the elements of the list into a perfect skip list
     */
    /**
     * Reorganizes the elements of the list into a perfect skip list
     */
    public void rebuild() {

        if (size == 0) {
            return;
        }
        int maxL = (int) Math.ceil(Math.log(size + 1) / Math.log(2));
        assignHeight(0, size + 1, maxL);

        Entry<T> cursor;
        Entry<T> nextCursor;
        int count = 0;
        for (int i = 1; i < maxL; i++) {
            cursor = head;
            nextCursor = cursor.next[0];

            count = 0;
            if (cursor.next[0] == tail) {
                cursor.next[i] = tail;
            } else {
                while (true) {
                    if (nextCursor.next.length > i) {
                        cursor.next[i] = nextCursor;
                        cursor.span[i] = count;

                        cursor = nextCursor;
                        nextCursor = cursor.next[0];
                        count = 0;

                    } else {
                        nextCursor = nextCursor.next[0];
                        count++;
                    }

                    if (nextCursor == tail) {
                        cursor.next[i] = tail;
                        cursor.span[i] = count;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Assigns level for the next[] for all the {@link Entry} using Divide & Conquer strategy
     *
     * @param left  leftmost index of the dataset
     * @param right rightmost index of the dataset
     * @param maxL  ideal maxLevel to be assigned
     */

    private void assignHeight(long left, long right, int maxL) {
        int maxHeight = 0;
        if (left == right) {
            return;
        }
        int rightHeight = 0;
        int leftHeight = 0;
        long mid = 0;
        if (left == 0 && right == size) {
            maxHeight = maxL;
            mid = (int) Math.pow(2, maxHeight - 1);
        }

        if (left == 0 && right == size + 1) {
            maxHeight = maxL;
            mid = (int) Math.pow(2, maxHeight - 1);
        } else {
            Entry<T> entryRight = getLinearEntry(right);
            if (entryRight.next[0] == tail) {
                rightHeight = maxL;
            } else if (entryRight != tail) {
                rightHeight = entryRight.next.length;
            } else {
                rightHeight = (int) (Math.log(size + 2) / Math.log(2));
            }
            leftHeight = getLinearEntry(left).next.length;

            maxHeight = Math.min(rightHeight, leftHeight) - 1;
            mid = left + (right - left) / 2;
        }

        //finding the middle element index
        if (mid == left) {
            return;
        }
        //geting the element at mid index
        Entry<T> temp = getLinearEntry(mid);

        if (temp == tail || temp == null) {
        } else {
            //storing the next pointer
            Entry<T> nextTemp = null;
            if (temp.next[0] == tail || temp.next[0] == null) {
                nextTemp = temp.next[0];
                temp.next = new Entry[1];
                temp.span = new int[1];
            } else {
                nextTemp = temp.next[0];

                if (maxHeight > 0) {
                    temp.next = new Entry[maxHeight];
                    temp.span = new int[maxHeight];
                } else {
                    //maxHeight +
                    temp.next = new Entry[1];
                    temp.span = new int[1];
                }
            }
            temp.next[0] = nextTemp;
            nextTemp.prev = temp;

        }
        assignHeight(left, mid, maxHeight);
        assignHeight(mid, right, maxHeight);

    }

    /**
     * Provides you with the entry at index n
     *
     * @param n value of index
     * @return entry at index n, else null
     */

    private Entry<T> getLinearEntry(long n) {
        if (n < 0 || n > size + 1) {
            return null;
        }
        Entry<T> p = head;

        for (int i = 0; i < n; i++) {
            p = p.next[0];
        }
        return p;
    }


    // Remove x from list.  Removed element is returned. Return null if x not in list
    /**
     *  Remove x from list.  Removed element is returned. Return null if x not in list
     * @param x the element to be removed
     * @return the element removed, else null
     */
    public T remove(T x) {
        if (size < 0) {
            return null;
        }
        if (!contains(x)) {
            return null;
        }
        Entry<T> ent = last[0].next[0];
        ent.next[0].prev = last[0];
        for (int i = 0; i < ent.next.length; i++) {
            last[i].next[i] = ent.next[i];
        }
        //updateSpanAfterRemove(ent);
        updateSpanForPreviousElements();
        size--;
        return ent.element;
    }

    /**
     * An utility method that helps us to update the span of all the elements occuring before the passed element.
     * Used after calling the remove method, to keep the spans[] for all the elements consistent
     *
     * @param ent before which all span[] of elements should be updated
     */
    private void updateSpanAfterRemove(Entry<T> ent) {
        Entry spanner = head;
        updateSpan(maxLevel, spanner);
        spanner = spanner.next[0];
        if (ent.next[0] == tail) {
            while (spanner != tail) {
                updateSpan(spanner.next.length, spanner);
                spanner = spanner.next[0];
            }
        } else {
            while (((T) ent.next[0].element).compareTo((T) spanner.element) > 0) {
                updateSpan(spanner.next.length, spanner);
                spanner = spanner.next[0];
            }
        }
    }

    /**
     * Return the number of elements in the list
     *
     * @return @size of the list
     */
    public int size() {
        return size;
    }

    ////////////////////////// Methods for printing//////////////////////////////////////
    public void printList(SkipList<T> skipList) {
        if (size > 0) {
            Entry cursor = head.next[0];
            while (cursor != tail) {
                System.out.println(cursor.next.length + " : " + cursor.element + " Span Array : " + Arrays.toString(cursor.span));
                cursor = cursor.next[0];
            }
            Iterator<T> it = skipList.iterator();
            while (it.hasNext()) {
                System.out.print(" " + it.next());
            }
        } else {
            System.out.println("List is empty, nothing to print");
        }
    }

    public void printListArray() {
        if (size > 0) {
            Entry cursor = head.next[0];
            System.out.println("Max Level = " + (maxLevel - 1));
            System.out.println("Size = " + size);
            System.out.println();
            System.out.println("Element   No of Levels");
            System.out.println(head.element + " " + maxLevel);
            while (cursor != tail) {
                System.out.print(" " + cursor.element);
                System.out.println(" \t " + cursor.next.length);
                cursor = cursor.next[0];
            }
            System.out.println();

            System.out.println("Printing Last Array = ");
            for (int i = maxLevel - 1; i >= 0; i--) {
                System.out.println(last[i].element);
            }
        }
    }

    /**
     * Custom Iterator implementation that allows us to iterate through the elements of the skip list
     */
    private class SkipListIterator implements Iterator<T> {
        Entry<T> cursor;
        boolean ready = false;

        SkipListIterator() {
            cursor = head;
        }

        @Override
        public boolean hasNext() {
            return cursor.next[0] != null && cursor.next[0].element != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                ready = true;
                cursor = cursor.next[0];
                return cursor.element;
            }
            return null;
        }

        // Removes the current element (retrieved by the most recent next())
        //@next() should be called before calling remove
        @Override
        public void remove() {
            if (ready && cursor != head && cursor != tail) {
                SkipList.this.remove(cursor.element);
                ready = false;
                cursor = cursor.prev;
            } else { //"No Elements to remove or element cannot be removed"
                return;
            }
        }
    }
}

