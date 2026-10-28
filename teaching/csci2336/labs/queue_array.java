/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 2 of 4: the circular array.
 *
 * Write the seven lines marked "Your line(s) go here", in the ArrayQueue
 * class below: two in enqueue, two in dequeue, and three in
 * ensureCapacity, which is the method that grows the array. Nothing else
 * in this file needs changing.
 *
 * main() makes an ArrayQueue and performs ten operations on it, printing
 * the two indices, the number of entries and every slot after each one. The
 * lab page shows the console this run prints when the queue works; compare
 * it with what you get, line by line.
 *
 * Press Run before you type anything: the file compiles and runs with the
 * blanks empty, and every slot stays empty because enqueue stores nothing.
 */
public class Exercise2
{
    public static void main(String[] args)
    {
        ArrayQueue<String> q = new ArrayQueue<String>(5);

        System.out.println("Circular array queue: 6 slots, one unused, so 5 entries fit.");
        System.out.println();
        System.out.println("operation      f  b  cnt  slots");
        System.out.println("---------      -  -  ---  -----");
        System.out.println("start          " + q.state());

        try
        {
            q.enqueue("A");
            System.out.println("enqueue A      " + q.state());

            q.enqueue("B");
            System.out.println("enqueue B      " + q.state());

            q.enqueue("C");
            System.out.println("enqueue C      " + q.state());

            String first = q.dequeue();
            System.out.println("dequeue -> " + first + "   " + q.state());

            String second = q.dequeue();
            System.out.println("dequeue -> " + second + "   " + q.state());

            q.enqueue("D");
            System.out.println("enqueue D      " + q.state());

            q.enqueue("E");
            System.out.println("enqueue E      " + q.state());

            q.enqueue("F");
            System.out.println("enqueue F      " + q.state());

            q.enqueue("G");                 // this one wraps: backIndex 5 -> 0
            System.out.println("enqueue G      " + q.state());

            q.enqueue("H");                 // this one finds the array full
            System.out.println("enqueue H      " + q.state());
        }
        catch (RuntimeException e)
        {
            System.out.println();
            System.out.println("The run stopped after the row above: "
                               + e.getClass().getSimpleName()
                               + ". A blank that is still empty is the usual"
                               + " reason.");
        }
    }
}


interface QueueInterface<T>
{
    void enqueue(T newEntry);
    T dequeue();
    T getFront();
    boolean isEmpty();
    void clear();
}


class EmptyQueueException extends RuntimeException
{
    public EmptyQueueException()
    {
        super("the queue is empty");
    }
}


final class ArrayQueue<T> implements QueueInterface<T>
{
    private T[] queue;          // circular array of queue entries
    private int frontIndex;     // index of the front entry
    private int backIndex;      // index of the back entry

    public ArrayQueue(int initialCapacity)
    {
        @SuppressWarnings("unchecked")
        T[] tempQueue = (T[]) new Object[initialCapacity + 1];   // one unused slot

        queue = tempQueue;
        frontIndex = 0;
        backIndex = initialCapacity;                             // queue.length - 1
    } // end constructor

    public boolean isEmpty()
    {
        return frontIndex == ((backIndex + 1) % queue.length);
    } // end isEmpty

    public void clear()
    {
        for (int i = 0; i < queue.length; i++)
            queue[i] = null;

        frontIndex = 0;
        backIndex = queue.length - 1;
    } // end clear

    public T getFront()
    {
        if (isEmpty())
            throw new EmptyQueueException();
        else
            return queue[frontIndex];
    } // end getFront

    public void enqueue(T newEntry)
    {
        ensureCapacity();   // doubles the array if it is full

        // Your two lines go here: move backIndex, then store  (2)


    } // end enqueue

    public T dequeue()
    {
        T front = getFront();   // might throw EmptyQueueException

        // Your two lines go here: blank the slot, move frontIndex  (2)



        return front;
    } // end dequeue

    private void ensureCapacity()
    {
        if (isFull())
        {
            T[] oldQueue = queue;
            int oldSize = oldQueue.length;

            @SuppressWarnings("unchecked")
            T[] tempQueue = (T[]) new Object[2 * oldSize];
            queue = tempQueue;

            for (int index = 0; index < oldSize - 1; index++)
            {
                // Your line goes here: one entry, in queue order  (1)

            } // end for

            // Your two lines go here: where the front and back are  (2)


        } // end if
    } // end ensureCapacity

    private boolean isFull()
    {
        return frontIndex == ((backIndex + 2) % queue.length);
    } // end isFull


    /* ===== For this lab only ============================================
       Not part of the queue ADT. main() has to see the two indices and
       every slot, which is what this exercise is about.
       =================================================================== */

    /* The four columns main() prints after each operation. */
    String state()
    {
        return String.format("%-2d %-2d %-4d %s", frontIndex, backIndex, count(),
                             slots());
    }

    private int count()
    {
        return (backIndex - frontIndex + 1 + queue.length) % queue.length;
    }

    /* Every slot, including the unused one, with null shown as _ . */
    private String slots()
    {
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < queue.length; i++)
        {
            if (i > 0)
                sb.append(", ");

            sb.append(queue[i] == null ? "_" : queue[i].toString());
        }

        return sb.append("]").toString();
    }
} // end ArrayQueue
