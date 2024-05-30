//Warning: Don't change this line.  If you change the package name, your code will not compile, and you will get zero points.
package comp2011.a1;

/*
 * @author Yixin Cao (September 11, 2023)
 *
 * Simulating a doubly linked list with an array.
 *
 */
public class DListOnArray_22103808d_WangRuijie { // Please change!
    private int[] arr;
    private static final int SIZE = 126; // it needs to be a multiplier of 3.

    private int head;
    private int tail;
    private int unusedHead;

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public DListOnArray_22103808d_WangRuijie() {

        arr = new int[SIZE];
        arr[2] = 0;
        arr[SIZE - 2] = 0;
        for (int i = 5; i < SIZE - 1; i += 3) {
            arr[i] = i - 3;
            arr[i - 1] = i;
        }

        head = 0;
        tail = 0;
        unusedHead = 2;
    }

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public boolean isEmpty() {
        return head == 0;
    }

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public boolean isFull() {return unusedHead == 0;}

    public void err() {
        System.out.println("Oops...");
    }

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public void insertFirst(int x) {
        if (isFull()) {
            err();
            return;
        }
        int temp = unusedHead;
        unusedHead = arr[temp + 2];
        arr[temp] = 0;
        arr[temp + 1] = x;
        arr[temp + 2] = head;
        if (isEmpty()) {
            tail = temp;
        } else {
            arr[head] = temp;
        }
        head = temp;
        if (isFull()) {
            unusedHead = 0;
        }
    }

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public void insertLast(int x) {
        if (isFull()) {
            err();
            return;
        }
        int temp = unusedHead;
        unusedHead = arr[temp + 2];
        arr[temp] = tail;
        arr[temp + 1] = x;
        arr[temp + 2] = 0;
        if (isEmpty()) {
            head = temp;
        } else {
            arr[tail + 2] = temp;
        }
        tail = temp;
        if (isFull()) {
            unusedHead = 0;
        }
    }

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public int deleteFirst() {
        if (isEmpty()) {
            err();
            return -1;
        }
        int temp = head;
        head = arr[head + 2];
        arr[temp] = 0;
        arr[temp + 2] = unusedHead;
        unusedHead = temp;
        if (isEmpty()) {
            tail = 0;
        } else {
            arr[head] = 0;
        }
        return arr[temp + 1];
    }

    /**
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP
     */
    public int deleteLast() {
        if (isEmpty()) {
            err();
            return -1;
        }
        if (head == tail) {
            head = 0;
        } else {
            arr[arr[tail] + 2] = 0;
        }
        int temp = tail;
        tail = arr[temp];
        arr[temp + 2] = unusedHead;
        unusedHead = temp;
        /*if (head == tail) {
            head = 0;
        } else {
            arr[tail + 2] = 0;
        }*/
        return arr[temp + 1];
    }

    /*
     * Optional, this runs in O(n) time.
     */
    public void reverse() {
        if (isEmpty() || head == tail) {
            return;
        }
       /* do {
            int cur = ex;
            ex = arr[ex];
            arr[cur] = arr[cur + 2];
            arr[cur + 2] = ex;
        } while (arr[ex] != 0);
        */
        int temp = head;
        head = tail;
        tail = temp;
        int curr = head;
        while (curr != 0) {
            temp = arr[curr];
            arr[curr] = arr[curr + 2];
            arr[curr + 2] = temp;
            curr = arr[curr + 2];
        }
    }


    /*
     * Optional, but you cannot test without it.
    // this method should print out the numbers in the list in order
    // for example, after the demonstration, it should be "75, 85, 38, 49"
    */
    public String toString() {
        if (isEmpty()) {
            return "";
        }
        StringBuilder output = new StringBuilder();
        int cur = head;
        while(cur != 0) {
            output.append(arr[cur + 1]);
            if (arr[cur + 2] != 0) {
                output.append(", ");
            }
            cur = arr[cur + 2];
        }
        return output.toString();
    }

    /*
     * The following is prepared for your reference.
     * You may freely revise it to test your code.
     */
    public static void main(String[] args) {
        DListOnArray_22103808d_WangRuijie list = new DListOnArray_22103808d_WangRuijie();
        // You may use the following line to print out data (the array),
        // so you can monitor what happens with your operations.
        //System.out.println(list.toString(list.data));

        System.out.println(list);
        list.insertFirst(75);
        list.insertFirst(99);
        list.insertLast(85);
        list.insertLast(38);
        System.out.println(list);
        list.deleteFirst();
        System.out.println(list);
        list.insertLast(49);
        System.out.println(list);
        list.reverse();
        System.out.println(list);
        list.insertFirst(99);
        System.out.println(list);
        list.deleteLast();
        System.out.println(list);
        list.reverse();
        System.out.println(list);
        list.deleteLast();
        System.out.println(list);
        list.deleteLast();
        System.out.println(list);
        list.deleteLast();
        System.out.println(list);
        list.deleteLast();
        System.out.println(list);
        list.deleteLast();
        System.out.println(list);
    }
}

