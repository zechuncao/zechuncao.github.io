/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 1 of 4: the linked queue.
 *
 * Write the five lines marked "Your line goes here". Nothing else in this
 * file needs changing. main() holds the example run: one line per
 * operation, with what the queue should look like afterwards written
 * beside it.
 *
 * Press Run before you type anything. Every operation reports WRONG.
 * Fill in one blank, press Run again, and watch the WRONG lines turn into
 * ok from the top down. The first WRONG line is the one to fix.
 */
public class Exercise1
{
    /* ===== The example run ==============================================
       Each line performs one operation and then says what the queue should
       be afterwards: what lastNode refers to, and the entries read from the
       front to the back.
       =================================================================== */
    public static void main(String[] args)
    {
        LinkedQueue<String> q = new LinkedQueue<String>();

        header();
        show(q, "start", "null", "(empty)");

        //            entry   lastNode   front .. back
        tryEnqueue(q, "A",    "A",       "A");
        tryEnqueue(q, "B",    "B",       "A -> B");
        tryDequeue(q, "A",    "B",       "B");
        tryDequeue(q, "B",    "null",    "(empty)");
        tryEnqueue(q, "C",    "C",       "C");

        summary();
    }


    /* ===== The driver: printing and checking. Nothing to change. ========
       tryEnqueue and tryDequeue are not the queue's methods. They call
       them, catch what an unfinished blank throws, and print one row.
       =================================================================== */

    static int rows = 0;        // operations reported
    static int wrong = 0;
    static int keptEntry = 0;   // a dequeue that handed back the front entry
                                // without removing it

    static void header()
    {
        System.out.println("Linked queue: firstNode is the front, lastNode the back.");
        System.out.println();
        System.out.println("operation      lastNode  ok?   front .. back");
        System.out.println("---------      --------  ---   -------------");
    }

    static void tryEnqueue(LinkedQueue<String> q, String entry,
                           String wantLast, String wantChain)
    {
        try
        {
            q.enqueue(entry);
        }
        catch (RuntimeException e)
        {
            broke("enqueue " + entry, "it threw " + e.getClass().getSimpleName()
                  + ", so lastNode was never assigned");
            return;
        }

        show(q, "enqueue " + entry, wantLast, wantChain);
    }

    static void tryDequeue(LinkedQueue<String> q, String wantEntry,
                           String wantLast, String wantChain)
    {
        String label = "dequeue -> " + wantEntry;

        try
        {
            String before = q.frontToBack();
            String got = q.dequeue();

            if (!wantEntry.equals(got))
            {
                broke(label, "it returned " + got + ", not " + wantEntry);
                return;
            }

            if (before.equals(q.frontToBack()))
                keptEntry++;
        }
        catch (RuntimeException e)
        {
            broke(label, "the queue says it is empty, so enqueue stored nothing");
            return;
        }

        show(q, label, wantLast, wantChain);
    }

    static void show(LinkedQueue<String> q, String label,
                     String wantLast, String wantChain)
    {
        String last = q.lastRef();
        String chain = q.frontToBack();
        boolean ok = last.equals(wantLast) && chain.equals(wantChain);
        rows++;

        if (!ok)
            wrong++;

        System.out.printf("%-14s %-9s %-5s %s%n", label, last,
                          ok ? "ok" : "WRONG", chain);

        if (!ok)
            System.out.printf("%-14s %-9s %-5s %s%n", "", wantLast, "", wantChain);
    }

    /* An operation that could not even be compared with what it should be. */
    static void broke(String label, String why)
    {
        rows++;
        wrong++;
        System.out.printf("%-14s %-9s %-5s %s%n", label, "-", "WRONG", why);
    }

    static void summary()
    {
        System.out.println();

        if (wrong == 0)
        {
            System.out.println("RESULT: PASS -- enqueue and dequeue are both right.");
            return;
        }

        System.out.println("RESULT: FAIL on " + wrong + " of " + rows + " operations.");

        if (keptEntry > 0)
            System.out.println("  - a dequeue returned the front entry without removing it,"
                               + " so firstNode is not moving.");

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


final class LinkedQueue<T> implements QueueInterface<T>
{
    private Node firstNode;     // the front entry
    private Node lastNode;      // the back entry

    public LinkedQueue()
    {
        firstNode = null;
        lastNode = null;
    } // end constructor

    public boolean isEmpty()
    {
        return firstNode == null;
    } // end isEmpty

    public void clear()
    {
        firstNode = null;
        lastNode = null;
    } // end clear

    public T getFront()
    {
        if (isEmpty())
            throw new EmptyQueueException();
        else
            return firstNode.getData();
    } // end getFront

    public void enqueue(T newEntry)
    {
        Node newNode = new Node(newEntry, null);

        if (isEmpty())
        {
            // Your line goes here: the queue was empty  (1)

        }
        else
        {
            // Your line goes here: link the old back to the new node  (1)

        }

        // Your line goes here: the new node is the back now  (1)

    } // end enqueue

    public T dequeue()
    {
        T front = getFront();   // might throw EmptyQueueException

        // Your line goes here: unlink the front node  (1)


        if (firstNode == null)
        {
            // Your line goes here: the queue is empty now  (1)

        }

        return front;
    } // end dequeue

    private class Node
    {
        private T data;
        private Node next;

        private Node(T entry, Node nextNode)
        {
            data = entry;
            next = nextNode;
        }

        private T getData()
        {
            return data;
        }

        private Node getNext()
        {
            return next;
        }

        private void setNext(Node nextNode)
        {
            next = nextNode;
        }
    } // end Node


    /* ===== For this lab only ============================================
       Not part of the queue ADT. The console needs to see lastNode, which
       is the one field a wrong answer here can leave stale.
       =================================================================== */

    static final int WALK_LIMIT = 20;       // a chain this short cannot be
                                            // longer, so an unfinished blank
                                            // cannot send the walk on forever

    String lastRef()
    {
        return lastNode == null ? "null" : lastNode.getData().toString();
    }

    String frontToBack()
    {
        StringBuilder sb = new StringBuilder();
        Node current = firstNode;
        int steps = 0;

        while (current != null && steps < WALK_LIMIT)
        {
            if (steps > 0)
                sb.append(" -> ");

            sb.append(current.getData().toString());
            current = current.getNext();
            steps++;
        }

        if (steps == WALK_LIMIT)
            return "(a chain that loops back on itself)";

        return sb.length() == 0 ? "(empty)" : sb.toString();
    }
} // end LinkedQueue
