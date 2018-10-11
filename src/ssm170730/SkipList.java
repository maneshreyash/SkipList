package ssm170730;/* Starter code for LP2 */

// Change this to netid of any member of team

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

// Skeleton for skip list implementation.

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
            // add more code if needed
            span = new int[lev];
            //System.arraycopy(span, 0, updateSpan(next, x), span.length - 1, span.length);
        }



        public E getElement() {
            return element;
        }
    }

    // Constructor
    public SkipList() {

        head = new Entry<>(null,33);
        tail = new Entry<>(null,33);
        size = 0;
        maxLevel = 1;
        last = new Entry[33];
        random = new Random();

        for (int i = 0; i < 33; i++) {
            last[i] = head;
        }
        tail.prev = head;
    }



    private void find(T x)
    {
        Entry<T> p = head;
        for (int i = maxLevel-1; i >= 0; i--)
        {
            //check NPE for tail
            while (p.next[i].element != null && (x.compareTo((T) (p.next[i].element)) > 0))
            {
                //System.out.println("Inside");
                p = p.next[i];
            }
            //System.out.println("In Find p.next element "+p.next[i].element);
            //System.out.println("In Find p.element " + p.element);
            last[i] = p;
        }
    }


    // Does list contain x?
    public boolean contains(T x) {


        if (size == 0) {
            return false;
        }

        find(x);
        if (last[0].next[0].element != null) {
            return x.compareTo((T) (last[0].next[0].element)) == 0;
        } else {
            return false;
        }
    }
    // Add x to list. If x already exists, reject it. Returns true if new node is added to list



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
            System.out.println(level + ": Added " + ent.element);
            ent.span = updateSpan(level, ent);
            //System.arraycopy(ent.span, 0, updateSpan(level, ent), level - 1, level);
            return true;
        }

        if(contains(x))
        {
            return false;
        }

        if(addHelper(x)){
            //updateHeadSpan();
            Entry spanner = head.next[0];
            while(x.compareTo((T) spanner.element) > 0){
                System.out.println("I'm updating span for: " + spanner.element);
                updateSpan(spanner.next.length, spanner);
                spanner = spanner.next[0];
            }
        }
        return true;
    }


    private boolean addHelper(T x){

        int level = chooseLevel();
        Entry<T> ent = new Entry<T>(x, level);

        for (int i = 0; i < level; i++) {
            //System.out.println(i);
            if (last[i].next[i] != null) {
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
                ent.next[i].prev = ent;
                ent.prev = last[i];
            } else {
                ent.next[i] = tail;
                last[i].next[i] = ent;
                ent.prev = last[i];
                tail.prev = ent;
            }
        }
        size++;
        ent.span = updateSpan(level, ent);
        System.out.println(level + ": Added " + ent.element);
        return true;
    }

    private int[] updateSpan(int level, Entry ent){
        //int[] arr = new int[level];
        ent.span[0] = 0;
        int count = 0;
        for(int i = 1; i < level; i++){
            count = 0;
            Entry cursor = ent;
            if(ent.next[i].element == null){
                while(cursor.next[0].element != null){
                    count++;
                    cursor = cursor.next[0];
                }
            }else {
                while (cursor.next[0].element != null && ((T) ent.next[i].element).compareTo((T) cursor.next[0].element) != 0) {
                    System.out.println("in here ");
                    count++;
                    cursor = cursor.next[0];
                }
            }
            ent.span[i] = count;
        }
        return ent.span;
    }

    private int chooseLevel() {
        int level = 1 + Integer.numberOfTrailingZeros(random.nextInt());
        int newLevel = Math.min(level, maxLevel + 1);

        if (newLevel > maxLevel)
        {
            maxLevel = newLevel;
        }
        return newLevel;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {

        return null;
    }


    // Return first element of list
    public T first() {
        if (size > 0) {
            //System.out.println((head.next[0].next));
            return (T) ((head.next[0]).element);
        }
        return null;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        return null;
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(int n) throws NoSuchElementException{

        //see pdf end

        if(n < 0 || n > size-1)
        {
            throw new NoSuchElementException();
        }
        Entry<T> p = head;
        for (int i = 0; i < n; i++)
        {
            p = p.next[0];
        }
        return p.element;
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
        if (n < 0 || n > size - 1) {
            throw new NoSuchElementException();
        }
        Entry<T> p = head;
        for (int i = 0; i < n; i++) {
            p = p.next[0];
        }
        return p.element;

    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n). Requires maintenance of spans, as discussed in class.
    public T getLog(int n) {

        return null;
    }

    // Is the list empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return new SkipListIterator();
    }

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


    // Return last element of list
    public T last() {
        if (size > 0) {
            return (T) (tail.prev.element);
        }
        return null;
    }

    // Optional operation: Reorganize the elements of the list into a perfect skip list
    // Not a standard operation in skip lists. Eligible for EC.
    public void rebuild() {

    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if(!contains(x))
        {
            return null;
        }
        Entry<T> ent = last[0].next[0];
        for (int i = 0; i < ent.next.length - 1; i++)
        {
            last[i].next[i] = ent.next[i];
        }
        size--;
        return ent.element;
    }

    // Return the number of elements in the list
    public int size() {
        return 0;
    }

    private class SkipListIterator implements Iterator<T> {
        Entry<T> cursor;

        SkipListIterator() {
            cursor = head;
        }

        public boolean hasNext() {
            return cursor.next[0] != null && cursor.next[0].element != null;
        }

        public T next() {
            cursor = cursor.next[0];
            return cursor.element;
        }

        // Removes the current element (retrieved by the most recent next())
       /* public void remove() {

        }*/
    }
}