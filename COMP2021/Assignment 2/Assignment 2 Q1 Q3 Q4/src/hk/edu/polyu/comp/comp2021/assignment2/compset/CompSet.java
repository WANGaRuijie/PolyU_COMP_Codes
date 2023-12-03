package hk.edu.polyu.comp.comp2021.assignment2.compset;

import java.util.ArrayList;
import java.util.List;

class CompSet<T> {

    /** Each CompSet uses at most 1023 buckets.   */
    private static final int NUBMER_OF_BUCKETS = 1023;

    /** An array of buckets as the storage for each set. */
    private final List<List<T>> storage;

    public CompSet() {
        storage = new ArrayList<>(NUBMER_OF_BUCKETS);
        for(int i = 0; i < NUBMER_OF_BUCKETS; i++){
            storage.add(new ArrayList<T>());
        }
    }

    /**
     * Initialize 'this' with the unique elements from 'elements'.
     * Throw IllegalArgumentException if 'elements' is null.
     */
    public CompSet(List<T> elements) {
        // Add missing code here

        if (elements == null) {
            throw new IllegalArgumentException("The elements are null");
        }

        storage = new ArrayList<>(NUBMER_OF_BUCKETS);
        for(int i = 0; i < NUBMER_OF_BUCKETS; i++){
            storage.add(new ArrayList<T>());
        }

        for (T element : elements) {
            add(element);
        }

    }

    /**
     * Get the total number of elements stored in 'this'.
     */
    public int getCount() {
        // Add missing code here

        int count = 0;
        for (List<T> subList : storage) {
            count += subList.size();
        }
        return count;

    }

    public boolean isEmpty() {
        // Add missing code here

        for (List<T> subList : storage) {
            if (!subList.isEmpty()) {
                return false;
            }
        }
        return true;

    }

    /**
     * Whether 'element' is contained in 'this'?
     */
    public boolean contains(T element) {
        // Add missing code here

        for (List<T> subList : storage) {
            for (T e : subList) {
                if (e.equals(element)) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Get all elements of 'this' as a list.
     */
    public List<T> getElements() {
        // Add missing code here

        List<T> elements = new ArrayList<>();
        for (List<T> subList : storage) {
            elements.addAll(subList);
        }
        return elements;

    }

    /**
     * Add 'element' to 'this', if it is not contained in 'this' yet.
     * Throw IllegalArgumentException if 'element' is null.
     */
    public void add(T element) {
        // Add missing code here

        if (element == null) {
            throw new IllegalArgumentException("The element is null");
        }

        List<T> subList = storage.get(getIndex(element));
        if (!subList.contains(element)) {
            subList.add(element);
        }

    }

    /**
     * Two CompSets are equivalent is they contain the same elements.
     * The order of the elements inside each CompSet is irrelevant.
     */
    public boolean equals(Object other){
        // Add missing code here

        if (!(other instanceof CompSet<?> otherSet)) {
            return false;
        }

        List<T> thisElements = this.getElements();
        List<?> otherElements = otherSet.getElements();

        for (T thisElement : thisElements) {
            if (!otherElements.contains(thisElement)) {
                return false;
            }
        }

        for (Object otherElement : otherElements) {
            if (!thisElements.contains(otherElement)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Remove 'element' from 'this', if it is contained in 'this'.
     * Throw IllegalArgumentException if 'element' is null.
     */
    public void remove (T element) {
        // Add missing code here
        if (element == null) {
            throw new IllegalArgumentException("The element is null");
        }
        List<T> subList = storage.get(getIndex(element));
        subList.remove(element);

    }

    //========================================================================== private methods

    private int getIndex(T element) {
        return element.hashCode() % NUBMER_OF_BUCKETS;
    }

}


