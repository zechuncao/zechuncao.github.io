/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 2 of 4: the circular array.
 *
 * Write the four lines marked "Your two lines go here". Nothing else in
 * this file needs changing. main() holds the example run: one line per
 * operation, with the state the queue should be in afterwards written
 * beside it.
 *
 * Press Run before you type anything. Every operation reports WRONG.
 * Fill in one blank, press Run again, and watch the WRONG lines turn into
 * ok from the top down. The first WRONG line is the one to fix.
 */
public class Exercise2
{
    /* ===== The example run ==============================================
       The queue holds 5 entries in an array of 6 slots, because one slot is
       always left unused. After each operation comes frontIndex, backIndex,
       the number of entries, and every slot in the array; a slot shown as _
       is null.
       =================================================================== */
    public static void main(String[] args)
    {
        ArrayQueue<String> q = new ArrayQueue<String>(5);

        header();
        show(q, "start", 0, 5, 0, "[_, _, _, _, _, _]");

        //         entry   f  b  cnt  slots
        enqueue(q, "A",    0, 0, 1,   "[A, _, _, _, _, _]");
        enqueue(q, "B",    0, 1, 2,   "[A, B, _, _, _, _]");
        enqueue(q, "C",    0, 2, 3,   "[A, B, C, _, _, _]");
        dequeue(q, "A",    1, 2, 2,   "[_, B, C, _, _, _]");
        dequeue(q, "B",    2, 2, 1,   "[_, _, C, _, _, _]");
        enqueue(q, "D",    2, 3, 2,   "[_, _, C, D, _, _]");
        enqueue(q, "E",    2, 4, 3,   "[_, _, C, D, E, _]");
        enqueue(q, "F",    2, 5, 4,   "[_, _, C, D, E, F]");
        enqueue(q, "G",    2, 0, 5,   "[G, _, C, D, E, F]");
        enqueue(q, "H",    0, 5, 6,   "[C, D, E, F, G, H, _, _, _, _, _, _]");

        summary(q);
    }


    /* ===== The driver: printing and checking. Nothing to change. ======== */

    static int rows = 0;        // operations reported
    static int wrong = 0;

    static void header()
    {
        System.out.println("Circular array queue: 6 slots, one unused, so 5 entries fit.");
        System.out.println();
        System.out.println("operation      f  b  cnt  ok?   slots");
        System.out.println("---------      -  -  ---  ---   -----");
    }

    static void enqueue(ArrayQueue<String> q, String entry,
                        int f, int b, int cnt, String slots)
    {
        try
        {
            q.enqueue(entry);
        }
        catch (RuntimeException e)
        {
            broke("enqueue " + entry, "it threw " + e.getClass().getSimpleName()
                  + ", so an index ran off the end of the array");
            return;
        }

        show(q, "enqueue " + entry, f, b, cnt, slots);
    }

    static void dequeue(ArrayQueue<String> q, String wantEntry,
                        int f, int b, int cnt, String slots)
    {
        String label = "dequeue -> " + wantEntry;

        try
        {
            String got = q.dequeue();

            if (!wantEntry.equals(got))
            {
                broke(label, "it returned " + got + ", not " + wantEntry);
                return;
            }
        }
        catch (EmptyQueueException e)
        {
            broke(label, "the queue says it is empty, so enqueue stored nothing");
            return;
        }

        show(q, label, f, b, cnt, slots);
    }

    static void show(ArrayQueue<String> q, String label,
                     int f, int b, int cnt, String slots)
    {
        boolean ok = q.frontIdx() == f && q.backIdx() == b
                     && q.count() == cnt && q.slots().equals(slots);
        rows++;

        if (!ok)
            wrong++;

        System.out.printf("%-14s %-2d %-2d %-4d %-5s %s%n", label, q.frontIdx(),
                          q.backIdx(), q.count(), ok ? "ok" : "WRONG", q.slots());

        if (!ok)
            System.out.printf("%-14s %-2d %-2d %-4d %-5s %s%n", "", f, b, cnt, "", slots);
    }

    /* An operation that could not even be compared with what it should be. */
    static void broke(String label, String why)
    {
        rows++;
        wrong++;
        System.out.printf("%-14s %-2s %-2s %-4s %-5s %s%n", label, "-", "-", "-",
                          "WRONG", why);
    }

    static void summary(ArrayQueue<String> q)
    {
        System.out.println();

        if (wrong == 0)
        {
            System.out.println("RESULT: PASS -- enqueue and dequeue are both right.");
            return;
        }

        System.out.println("RESULT: FAIL on " + wrong + " of " + rows
                           + " operations.");
        System.out.println("front to back, right now:  " + q.frontToBack());

        if (q.count() == 0)
            System.out.println("  - the queue is empty, so enqueue never stored an entry.");

        System.out.println("  - the second line under a WRONG operation is what it should"
                           + " have printed.");
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

        // Your two lines go here  (2)


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
                queue[index] = oldQueue[frontIndex];
                frontIndex = (frontIndex + 1) % oldSize;
            } // end for

            frontIndex = 0;
            backIndex = oldSize - 2;
        } // end if
    } // end ensureCapacity

    private boolean isFull()
    {
        return frontIndex == ((backIndex + 2) % queue.length);
    } // end isFull


    /* ===== For this lab only ============================================
       Not part of the queue ADT. The console needs to see the two indices
       and every slot, which is what this exercise is about.
       =================================================================== */

    static final int WALK_LIMIT = 40;

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
       unfinished blank cannot send this into an endless walk. */
    String frontToBack()
    {
        StringBuilder sb = new StringBuilder();
        int live = count();
        int i = frontIndex;
        int steps = 0;

        while (steps < live && steps < WALK_LIMIT)
        {
            if (steps > 0)
                sb.append(" ");

            sb.append(queue[i] == null ? "?" : queue[i].toString());
            i = (i + 1) % queue.length;
            steps++;
        }

        return sb.length() == 0 ? "(empty)" : sb.toString();
    }
} // end ArrayQueue
