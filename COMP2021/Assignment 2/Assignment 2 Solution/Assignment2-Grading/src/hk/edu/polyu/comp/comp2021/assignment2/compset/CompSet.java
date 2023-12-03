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
        this();

        if (elements == null)
            throw new IllegalArgumentException();

        for (T ele : elements) {
            add(ele);
        }
    }

    /**
     * Get the total number of elements stored in 'this'.
     */
    public int getCount() {
        int result = 0;
        for (List<T> bucket : storage) {
            result += bucket == null ? 0 : bucket.size();
        }
        return result;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    /**
     * Whether 'element' is contained in 'this'?
     */
    public boolean contains(T element) {
        if (element == null || isEmpty())
            return false;

        int index = getIndex(element);
        return storage.get(index) != null && storage.get(index).contains(element);
    }

    /**
     * Get all elements of 'this' as a list.
     */
    public List<T> getElements() {
        List<T> result = new ArrayList<>(getCount());
        for (List<T> elements : storage) {
            if (elements != null) {
                result.addAll(elements);
            }
        }
        return result;
    }

    /**
     * Add 'element' to 'this', if it is not contained in 'this' yet.
     * Throw IllegalArgumentException if 'element' is null.
     */
    public void add(T element) {
        if (element == null)
            throw new IllegalArgumentException();

        if (contains(element))
            return;

        if(storage.get(getIndex(element)) == null)
            storage.set(getIndex(element), new ArrayList<>());

        storage.get(getIndex(element)).add(element);
    }

    /**
     * Two CompSets are equivalent is they contain the same elements.
     * The order of the elements inside each CompSet is irrelevant.
     */
    public boolean equals(Object other){
        if(other == this) return true;

        if(!(other instanceof CompSet<?>))
            return false;
        CompSet<?> otherSet = (CompSet<?>) other;

        if(otherSet.getCount() != getCount())
            return false;

        List<?> uniqueElements = getElements();
        for(Object o: otherSet.getElements()){
            if(!uniqueElements.contains(o))
                return false;
        }
        return true;
    }

    /**
     * Remove 'element' from 'this', if it is contained in 'this'.
     * Throw IllegalArgumentException if 'element' is null.
     */
    public void remove (T element) {
        if(element == null)
            throw new IllegalArgumentException();

        if(!contains(element))
            return;

        storage.get(getIndex(element)).remove(element);
    }

    //========================================================================== private methods

    private int getIndex(T element) {
        return element.hashCode() % NUBMER_OF_BUCKETS;
    }

}


