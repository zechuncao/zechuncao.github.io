/* CSCI 2336 -- Queue Implementations, Round 2: the linked chain.
 *
 * Fill in the four blanks below. Press Run after each one and read the
 * console: every operation prints lastNode and then the chain from front
 * to back, and says ok or WRONG.
 *
 * Watch the lastNode column when the queue empties. That column is the
 * whole point of this round -- enqueue quietly repairs a lastNode that
 * dequeue left behind, so nothing else would ever show you the bug.
 */
public class LinkedQueuePractice
{
    /* What lastNode and the chain should be after each operation. */
    static final String[] EXPECTED =
    {
        "null | (empty)",      // start
        "A | A",               // enqueue A
        "B | A -> B",          // enqueue B
        "B | B",               // dequeue -> A
        "null | (empty)",      // dequeue -> B, the last entry leaves
        "C | C"                // enqueue C
    };

    static int step = 0;
    static int wrong = 0;
    static boolean threw = false;       // an operation that crashed is never "ok"
    static boolean staleLast = false;   // emptied, but lastNode still pointed at a node
    static boolean nullLast = false;    // nodes in the chain, but lastNode was null
    static boolean lostBack = false;    // lastNode not reachable from firstNode

    public static void main(String[] args)
    {
        LinkedQueue<String> q = new LinkedQueue<>();

        System.out.println("Linked queue: firstNode is the front, lastNode is the back.");
        System.out.println();
        System.out.println("operation      lastNode  ok?   front .. back");
        System.out.println("---------      --------  ---   -------------");

        report(q, "start");

        enqueue(q, "A");
        enqueue(q, "B");
        dequeue(q);
        dequeue(q);        // this one empties the queue
        enqueue(q, "C");

        summary(q);
    }


    /* ---- the driver -------------------------------------------------- */

    static void report(LinkedQueue<String> q, String label)
    {
        String actual = q.lastLabel() + " | " + q.frontToBack();
        String want = (step < EXPECTED.length) ? EXPECTED[step] : "";
        boolean ok = want.equals(actual) && !threw;
        threw = false;

        if (q.hasStaleLast())
            staleLast = true;
        else if (!q.isEmpty() && q.lastLabel().equals("null"))
            nullLast = true;
        else if (!q.isEmpty() && !q.backIsReachable())
            lostBack = true;

        if (!ok)
            wrong++;

        System.out.printf("%-14s %-9s %-5s %s%n",
                          label, q.lastLabel(), ok ? "ok" : "WRONG", q.frontToBack());

        if (!ok)
            System.out.printf("%-14s %s%n", "", "should be:  " + want.replace(" | ", "   "));

        step++;
    }

    static void enqueue(LinkedQueue<String> q, String entry)
    {
        try
        {
            q.enqueue(entry);
            report(q, "enqueue " + entry);
        }
        catch (NullPointerException e)
        {
            threw = true;
            report(q, "enqueue " + entry);
            System.out.printf("%-14s %s%n", "",
                              "crashed: lastNode is null, so there is nothing to link to");
        }
    }

    static void dequeue(LinkedQueue<String> q)
    {
        try
        {
            String front = q.dequeue();
            report(q, "dequeue -> " + front);
        }
        catch (EmptyQueueException e)
        {
            threw = true;
            report(q, "dequeue -> ??");
            System.out.printf("%-14s %s%n", "",
                              "the chain is empty, so enqueue is not linking anything in");
        }
    }

    static void summary(LinkedQueue<String> q)
    {
        System.out.println();

        if (wrong == 0 && !staleLast && !nullLast && !lostBack)
        {
            System.out.println("RESULT: PASS -- enqueue and dequeue are both right.");
            return;
        }

        System.out.println("RESULT: FAIL on " + wrong + " of " + EXPECTED.length + " operations.");

        if (q.isEmpty() && q.lastLabel().equals("null") && wrong > 1)
            System.out.println("  - the chain never grew, so enqueue is not linking a node in.");
        else if (nullLast)
            System.out.println("  - the chain had nodes while lastNode was still null, so enqueue"
                               + " is not setting lastNode to the new node.");
        else if (lostBack)
            System.out.println("  - lastNode could not be reached by following next from"
                               + " firstNode, so enqueue is not linking the old back to the new"
                               + " node.");
        else if (staleLast)
            System.out.println("  - the queue emptied but lastNode still pointed at a node, so"
                               + " dequeue is not clearing lastNode when the last entry leaves.");
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


final class LinkedQueue<T> implements QueueInterface<T>
{
    private Node firstNode;   // front of the queue
    private Node lastNode;    // back of the queue

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

    /* The exam writes both if/else below without braces. The braces are
       here only so the file compiles while the blanks are still empty. */
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
        T front = getFront();              // might throw EmptyQueueException

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


    /* ===== For this lab only =============================================
       None of this is part of the queue ADT. It exists so the console can
       show lastNode, which the public API hides: enqueue takes the
       isEmpty() branch and overwrites a stale lastNode on its own, so a
       dequeue that forgets to clear it still behaves correctly from the
       outside. It is a dangling reference, not a wrong answer.

       Every walk stops after WALK_LIMIT nodes, so a node accidentally
       linked to itself prints (cycle?) instead of hanging the tab.
       ===================================================================== */

    private static final int WALK_LIMIT = 50;

    String lastLabel()
    {
        if (lastNode == null)
            return "null";

        return String.valueOf(lastNode.getData()) + (hasStaleLast() ? " (!)" : "");
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

            sb.append(String.valueOf(current.getData()));
            current = current.getNext();
            steps++;
        }

        if (current != null)
            sb.append(" (cycle?)");

        return sb.length() == 0 ? "(empty)" : sb.toString();
    }

    /* True when following next from firstNode actually arrives at lastNode.
       False when enqueue forgot to link the old back to the new node. */
    boolean backIsReachable()
    {
        if (firstNode == null)
            return lastNode == null;

        Node current = firstNode;
        int steps = 0;

        while (current != null && steps < WALK_LIMIT)
        {
            if (current == lastNode)
                return current.getNext() == null;

            current = current.getNext();
            steps++;
        }

        return false;
    }

    /* True when the queue is empty but lastNode still refers to a node --
       the dangling reference that dequeue's lastNode = null prevents. */
    boolean hasStaleLast()
    {
        return firstNode == null && lastNode != null;
    }
} // end LinkedQueue
