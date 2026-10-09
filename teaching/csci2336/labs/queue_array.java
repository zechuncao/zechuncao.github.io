/* CSCI 2336 -- Queue Implementations, Round 1: the circular array.
 *
 * Fill in the two blanks below. Press Run after each one and read the
 * console: every operation prints the two indices and every slot in the
 * array, including the one that is always left unused, and says ok or
 * WRONG. A slot shown as _ is null.
 */
public class ArrayQueuePractice
{
    /* What the state should be after each operation: frontIndex, backIndex,
       the number of entries, and every slot. */
    static final String[] EXPECTED =
    {
        "0 5 0 [_, _, _, _, _, _]",                      // start
        "0 0 1 [A, _, _, _, _, _]",                      // enqueue A
        "0 1 2 [A, B, _, _, _, _]",                      // enqueue B
        "0 2 3 [A, B, C, _, _, _]",                      // enqueue C
        "1 2 2 [_, B, C, _, _, _]",                      // dequeue -> A
        "2 2 1 [_, _, C, _, _, _]",                      // dequeue -> B
        "2 3 2 [_, _, C, D, _, _]",                      // enqueue D
        "2 4 3 [_, _, C, D, E, _]",                      // enqueue E
        "2 5 4 [_, _, C, D, E, F]",                      // enqueue F
        "2 0 5 [G, _, C, D, E, F]",                      // enqueue G -- wraps to slot 0
        "0 5 6 [C, D, E, F, G, H, _, _, _, _, _, _]"     // enqueue H -- array doubles first
    };

    static int step = 0;
    static int wrong = 0;
    static boolean threw = false;        // an operation that crashed is never "ok"
    static boolean sawStale = false;     // a slot outside the queue held an entry
    static String staleOp = "";          // the operation that first left one there
    static boolean dequeueKept = false;  // a dequeue that did not shrink the queue

    public static void main(String[] args)
    {
        // 5 usable entries, so 6 slots: one is always left unused.
        ArrayQueue<String> q = new ArrayQueue<>(5);

        System.out.println("Circular array queue: 6 slots, one unused, so 5 entries fit.");
        System.out.println();
        System.out.println("operation      f  b  cnt  ok?   slots");
        System.out.println("---------      -  -  ---  ---   -----");

        report(q, "start");

        enqueue(q, "A");
        enqueue(q, "B");
        enqueue(q, "C");
        dequeue(q);
        dequeue(q);
        enqueue(q, "D");
        enqueue(q, "E");
        enqueue(q, "F");
        enqueue(q, "G");   // has to wrap around to slot 0
        enqueue(q, "H");   // the array is full, so this one doubles it first

        summary(q);
    }


    /* ---- the driver -------------------------------------------------- */

    static void report(ArrayQueue<String> q, String label)
    {
        String actual = String.format("%d %d %d %s",
                                      q.frontIdx(), q.backIdx(), q.count(), q.slots());
        String want = (step < EXPECTED.length) ? EXPECTED[step] : "";
        boolean ok = want.equals(actual) && !threw;
        threw = false;

        if (!sawStale && q.hasStaleSlot())
        {
            sawStale = true;
            staleOp = label;             // enqueue or dequeue tells us whose bug it is
        }

        if (!ok)
            wrong++;

        System.out.printf("%-14s %-2d %-2d %-4d %-5s %s%n",
                          label, q.frontIdx(), q.backIdx(), q.count(),
                          ok ? "ok" : "WRONG", q.slots());

        if (!ok)
            System.out.printf("%-14s %s%n", "", "should be:  " + want);

        step++;
    }

    static void enqueue(ArrayQueue<String> q, String entry)
    {
        q.enqueue(entry);
        report(q, "enqueue " + entry);
    }

    static void dequeue(ArrayQueue<String> q)
    {
        int before = q.count();

        try
        {
            String front = q.dequeue();

            if (q.count() >= before)
                dequeueKept = true;

            report(q, "dequeue -> " + front);
        }
        catch (EmptyQueueException e)
        {
            threw = true;
            report(q, "dequeue -> ??");
            System.out.printf("%-14s %s%n", "",
                              "the queue thinks it is empty, so enqueue is not storing anything");
        }
    }

    static void summary(ArrayQueue<String> q)
    {
        System.out.println();

        if (wrong == 0 && !sawStale && !dequeueKept)
        {
            System.out.println("RESULT: PASS -- enqueue and dequeue are both right.");
            return;
        }

        System.out.println("RESULT: FAIL on " + wrong + " of " + EXPECTED.length + " operations.");
        System.out.println("front to back, right now:  " + q.frontToBack());

        if (q.count() == 0)
            System.out.println("  - the queue is empty, so enqueue never stored an entry.");
        else if (dequeueKept)
            System.out.println("  - a dequeue handed back an entry without removing it, so"
                               + " dequeue is not advancing frontIndex.");
        else if (sawStale && staleOp.startsWith("enqueue"))
            System.out.println("  - " + staleOp + " left its entry outside the queue, so enqueue"
                               + " is storing at the old backIndex: check which of your two lines"
                               + " runs first.");
        else if (sawStale)
            System.out.println("  - a slot outside the queue held an entry, so dequeue is not"
                               + " setting the vacated slot to null.");
        else
            System.out.println("  - look at the first WRONG line above: that is the operation to"
                               + " fix.");
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
    private static final int DEFAULT_CAPACITY = 50;

    public ArrayQueue()
    {
        this(DEFAULT_CAPACITY);
    }

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

    /* Carrano writes this as "while (!isEmpty()) dequeue();". It is a
       bounded loop here so that calling clear() cannot hang while dequeue
       is still blank. */
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
        ensureCapacity();                   // doubles the array if it is full

        // Your two lines go here  (2)


    } // end enqueue

    public T dequeue()
    {
        T front = getFront();               // might throw EmptyQueueException

        // Your two lines go here: clear the slot, advance frontIndex  (2)



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
                queue[index] = oldQueue[frontIndex];
                frontIndex = (frontIndex + 1) % oldSize;
            } // end for

            frontIndex = 0;
            backIndex = oldSize - 2;
        } // end if
    } // end ensureCapacity


    /* ===== For this lab only =============================================
       None of this is part of the queue ADT. It exists so the console can
       show what the two indices are doing. The deck writes the full test
       inline inside ensureCapacity; it is a method here so the driver can
       print it too.
       ===================================================================== */

    boolean isFull()
    {
        return frontIndex == ((backIndex + 2) % queue.length);
    }

    int count()
    {
        return (backIndex - frontIndex + 1 + queue.length) % queue.length;
    }

    int frontIdx()
    {
        return frontIndex;
    }

    int backIdx()
    {
        return backIndex;
    }

    /* Every slot, including the unused one, with null shown as _ . */
    String slots()
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

    /* The live entries, read from frontIndex. Bounded by count(), so an
       unfinished dequeue cannot send this into an endless walk. */
    String frontToBack()
    {
        StringBuilder sb = new StringBuilder();
        int live = count();
        int i = frontIndex;

        for (int k = 0; k < live; k++)
        {
            if (k > 0)
                sb.append(" ");

            sb.append(queue[i] == null ? "?" : queue[i].toString());
            i = (i + 1) % queue.length;
        }

        return sb.length() == 0 ? "(empty)" : sb.toString();
    }

    /* True when a slot that is not part of the queue still holds an entry --
       the loitering that dequeue's queue[frontIndex] = null prevents. */
    boolean hasStaleSlot()
    {
        boolean[] live = new boolean[queue.length];
        int i = frontIndex;

        for (int k = 0; k < count(); k++)
        {
            live[i] = true;
            i = (i + 1) % queue.length;
        }

        for (int s = 0; s < queue.length; s++)
            if (!live[s] && queue[s] != null)
                return true;

        return false;
    }
} // end ArrayQueue
