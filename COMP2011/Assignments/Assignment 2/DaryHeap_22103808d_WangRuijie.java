//Warning: Don't change this line.  If you change the package name, your code will not compile, and you will get zero points.
package comp2011.a2;


import java.util.Arrays;


/**
 *
 *
 * My student ID is 22103808d, I'm implementing a 3-ary min heap.


* VERY IMPORTANT.
* 
* I've discussed this question with the following students:
*     1. ALL BY MYSELF
* 
* I've sought help from the following Internet resources and books:
*     1. ALL BY MYSELF
*
*/ 
public class DaryHeap_22103808d_WangRuijie<T extends Comparable<T>> {

    private final T[] minHeap;
    private int currentLength;
    private final int CAPACITY;

    public DaryHeap_22103808d_WangRuijie (int capacity) {
        this.CAPACITY = capacity;
        this.minHeap = (T[]) new Comparable[CAPACITY];
        this.currentLength = 0;
    }

    public void insert(T x) {
        if (currentLength == CAPACITY) {
            throw new IllegalStateException("The min heap is now full");
        }
        minHeap[currentLength] = x;
        up(currentLength);
        ++currentLength;
    }

    // Running time: O(log_3 n).
    public T removeRoot() {
        if (currentLength == 0) {
            return null;
        }
        T root = minHeap[0];
        minHeap[0] = minHeap[currentLength - 1];
        --currentLength;
        down(0);
        return root;
    }

    // Running time: O(log_3 n).
    private void up(int c) {
        int parentIndex = (c - 1) / 3;
        if (parentIndex >= 0 && minHeap[parentIndex].compareTo(minHeap[c]) > 0) {
            T temp = minHeap[parentIndex];
            minHeap[parentIndex] = minHeap[c];
            minHeap[c] = temp;
            up(parentIndex);
        }
    }

    // Running time: O(log_3 n).
    private void down(int ind) {
        int minChildIndex = 3 * ind + 1;
        int maxChildIndex = 3 * ind + 3;
        int minimum = ind;
        for (int i = minChildIndex; i <= (maxChildIndex < currentLength - 1 ? maxChildIndex : currentLength -1); ++i) {
            if (minHeap[minimum].compareTo(minHeap[i]) > 0) {
                minimum = i;
            }
        }
        if (ind != minimum) {
            T temp = minHeap[ind];
            minHeap[ind] = minHeap[minimum];
            minHeap[minimum] = temp;
            down(minimum);
        }
    }

    /**
     * Merge the given <code>heap</code> with <code>this</code>.
     * The result will be stored in <code>this</code>.
     *
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     1. ALL BY MYSELF
     *
     * I've sought help from the following Internet resources and books:
     *     1. ALL BY MYSELF
     *
     * Running time: O(n + m).
     */
    public void merge(DaryHeap_22103808d_WangRuijie<T> heap) {

        T[] newHeap = (T[]) new Comparable[this.CAPACITY];

        int index = 0;

        for (int i = 0; i < this.currentLength; i++) {
            newHeap[index] = this.minHeap[i];
            ++index;
        }

        for (int j = 0; j < heap.currentLength; j++) {
            if (index < this.CAPACITY) {
                newHeap[index] = heap.minHeap[j];
                ++index;
            }
        }
        this.currentLength = index;

        int n = this.currentLength - 1;
        int p = (n - 1) / 3;
        for (int k = p; k >= 0; --k) {
            heapify(newHeap, k, n);
        }

        for (int k = 0; k < this.currentLength; k++) {
            this.minHeap[k] = newHeap[k];
        }

    }

    public void heapify(T[] arr, int i, int n) {
        int child1 = 3 * i + 1;
        int child2 = 3 * i + 2;
        int child3 = 3 * i + 3;
        int min = i;

        if (child1 <= n && arr[min].compareTo(arr[child1]) > 0) {
            min = child1;
        }
        if (child2 <= n && arr[min].compareTo(arr[child2]) > 0) {
            min = child2;
        }
        if (child3 <= n && arr[min].compareTo(arr[child3]) > 0) {
            min = child3;
        }

        if (min != i) {
            T temp = arr[i];
            arr[i] = arr[min];
            arr[min] = temp;
            heapify(arr, min, n);
        }

    }
    
    /*
     * Make sure you test your code thoroughly.
     * The more test cases, the better.
     */
    public static void main(String[] args) {

        DaryHeap_22103808d_WangRuijie<Integer> heap1 = new DaryHeap_22103808d_WangRuijie<>(20);
        DaryHeap_22103808d_WangRuijie<Integer> heap2 = new DaryHeap_22103808d_WangRuijie<>(20);

        heap1.insert(5);
        heap1.insert(10);
        heap1.insert(15);
        heap1.insert(2);
        heap1.insert(3);
        heap1.insert(80);
        heap1.insert(4);
        heap1.insert(1);
        heap1.insert(12);
        heap1.insert(7);
        heap1.insert(50);
        heap1.insert(22);


        System.out.println("Heap 1: " + Arrays.toString(heap1.minHeap));

        heap2.insert(998);
        heap2.insert(49);
        heap2.insert(9);
        heap2.insert(11);
        heap2.insert(20);
        heap2.insert(98);
        heap2.insert(23);
        heap2.insert(27);
        heap2.insert(8);

        heap1.merge(heap2);
        System.out.println("Heap 1: " + Arrays.toString(heap1.minHeap));

        for (int i = 1; i <= 20; i++) {
            System.out.println(heap1.removeRoot());
        }
    }
}
