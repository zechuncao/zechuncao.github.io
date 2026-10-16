/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 3 of 4: the deque.
 *
 * Write the six lines marked "Your line goes here", three in addToFront
 * and three in removeBack. The two methods right above them, addToBack
 * and removeFront, are written for you: the ones you write are their
 * mirror image, with firstNode and previous in place of lastNode and next.
 *
 * Nothing else in this file needs changing: main() makes a LinkedDeque
 * object of that class and runs the example on it, and the Driver class at
 * the end of the file only prints and checks. Press Run before you type
 * anything, then fill in one blank at a time.
 *
 * The console reads the deque twice: front to back along the next
 * references, and back to front along the previous references. A deque
 * whose next links are right and whose previous links are not looks
 * correct in one column and wrong in the other.
 */
public class Exercise3
{
    /* ===== The example run ==============================================
       main() makes a LinkedDeque -- an object of the class you are about to
       finish -- and hands it to the Driver. Each line below performs one
       operation on that object and then says how the deque should read
       afterwards, front to back and back to front.
       =================================================================== */
    public static void main(String[] args)
    {
        LinkedDeque<String> d = new LinkedDeque<String>();
        Driver run = new Driver(d);

        //                      front .. back    back .. front
        run.addToBack("B",      "B",             "B");
        run.addToFront("A",     "A B",           "B A");
        run.addToBack("C",      "A B C",         "C B A");
        run.removeBack("C",     "A B",           "B A");
        run.removeFront("A",    "B",             "B");
        run.removeBack("B",     "(empty)",       "(empty)");
        run.addToFront("D",     "D",             "D");

        run.summary();
    }
}


interface DequeInterface<T>
{
    void addToFront(T newEntry);
    void addToBack(T newEntry);
    T removeFront();
    T removeBack();
    T getFront();
    T getBack();
    boolean isEmpty();
    void clear();
}


class EmptyQueueException extends RuntimeException
{
    public EmptyQueueException()
    {
        super("the deque is empty");
    }
}


final class LinkedDeque<T> implements DequeInterface<T>
{
    private DLNode firstNode;   // the front entry
    private DLNode lastNode;    // the back entry

    public LinkedDeque()
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

    public T getBack()
    {
        if (isEmpty())
            throw new EmptyQueueException();
        else
            return lastNode.getData();
    } // end getBack

    /* ---- written for you: the back end ---------------------------------- */

    public void addToBack(T newEntry)
    {
        DLNode newNode = new DLNode(lastNode, newEntry, null);

        if (isEmpty())
            firstNode = newNode;
        else
            lastNode.setNext(newNode);

        lastNode = newNode;
    } // end addToBack

    public T removeFront()
    {
        T front = getFront();   // might throw EmptyQueueException

        firstNode = firstNode.getNext();

        if (firstNode == null)
            lastNode = null;
        else
            firstNode.setPrevious(null);

        return front;
    } // end removeFront

    /* ---- yours: the same two moves at the other end --------------------- */

    public void addToFront(T newEntry)
    {
        DLNode newNode = new DLNode(null, newEntry, firstNode);

        if (isEmpty())
        {
            // Your line goes here: the deque was empty  (1)

        }
        else
        {
            // Your line goes here: the old front has a previous  (1)

        }

        // Your line goes here: the new node is the front now  (1)

    } // end addToFront

    public T removeBack()
    {
        T back = getBack();   // might throw EmptyQueueException

        // Your line goes here: step lastNode back one node  (1)


        if (lastNode == null)
        {
            // Your line goes here: the deque is empty now  (1)

        }
        else
        {
            // Your line goes here: nothing follows the new back  (1)

        }

        return back;
    } // end removeBack

    private class DLNode
    {
        private T data;
        private DLNode next;        // the node after this one
        private DLNode previous;    // the node before this one

        private DLNode(DLNode previousNode, T entry, DLNode nextNode)
        {
            previous = previousNode;
            data = entry;
            next = nextNode;
        }

        private T getData()
        {
            return data;
        }

        private DLNode getNext()
        {
            return next;
        }

        private void setNext(DLNode nextNode)
        {
            next = nextNode;
        }

        private DLNode getPrevious()
        {
            return previous;
        }

        private void setPrevious(DLNode previousNode)
        {
            previous = previousNode;
        }
    } // end DLNode


    /* ===== For this lab only ============================================
       Not part of the deque ADT. Reading the chain in both directions is
       what tells a half-linked deque from a correct one.
       =================================================================== */

    static final int WALK_LIMIT = 20;       // a chain this short cannot be
                                            // longer, so an unfinished blank
                                            // cannot send the walk on forever

    String frontToBack()
    {
        StringBuilder sb = new StringBuilder();
        DLNode current = firstNode;
        int steps = 0;

        while (current != null && steps < WALK_LIMIT)
        {
            if (steps > 0)
                sb.append(" ");

            sb.append(current.getData().toString());
            current = current.getNext();
            steps++;
        }

        if (steps == WALK_LIMIT)
            return "(next loops round)";

        return sb.length() == 0 ? "(empty)" : sb.toString();
    }

    String backToFront()
    {
        StringBuilder sb = new StringBuilder();
        DLNode current = lastNode;
        int steps = 0;

        while (current != null && steps < WALK_LIMIT)
        {
            if (steps > 0)
                sb.append(" ");

            sb.append(current.getData().toString());
            current = current.getPrevious();
            steps++;
        }

        if (steps == WALK_LIMIT)
            return "(previous loops round)";

        return sb.length() == 0 ? "(empty)" : sb.toString();
    }
} // end LinkedDeque


/* ===== The driver: printing and checking. Nothing to change. ============
   Driver is not a deque. It holds the LinkedDeque object main() made,
   calls the four methods you and the file wrote between you, catches what
   an unfinished blank throws, and prints one row per operation.
   ======================================================================= */
final class Driver
{
    private final LinkedDeque<String> d;

    private int rows = 0;          // operations reported
    private int wrong = 0;
    private int backwardOnly = 0;  // the next links agreed, the previous did not

    Driver(LinkedDeque<String> deque)
    {
        d = deque;

        System.out.println("Doubly linked deque: every node refers to the node before"
                           + " it and the node after it.");
        System.out.println();
        System.out.println("operation        ok?   front .. back    back .. front");
        System.out.println("---------        ---   -------------    -------------");

        show("start", "(empty)", "(empty)");
    }

    void addToFront(String entry, String f2b, String b2f)
    {
        add(true, entry, f2b, b2f);
    }

    void addToBack(String entry, String f2b, String b2f)
    {
        add(false, entry, f2b, b2f);
    }

    void removeFront(String wantEntry, String f2b, String b2f)
    {
        remove(true, wantEntry, f2b, b2f);
    }

    void removeBack(String wantEntry, String f2b, String b2f)
    {
        remove(false, wantEntry, f2b, b2f);
    }

    private void add(boolean atFront, String entry, String f2b, String b2f)
    {
        String label = (atFront ? "addToFront " : "addToBack ") + entry;

        try
        {
            if (atFront)
                d.addToFront(entry);
            else
                d.addToBack(entry);
        }
        catch (RuntimeException e)
        {
            broke(label, "it threw " + e.getClass().getSimpleName()
                  + " on a reference that was never assigned");
            return;
        }

        show(label, f2b, b2f);
    }

    private void remove(boolean atFront, String wantEntry, String f2b, String b2f)
    {
        String label = (atFront ? "removeFront -> " : "removeBack -> ") + wantEntry;

        try
        {
            String got = atFront ? d.removeFront() : d.removeBack();

            if (!wantEntry.equals(got))
            {
                broke(label, "it returned " + got + ", not " + wantEntry);
                return;
            }
        }
        catch (EmptyQueueException e)
        {
            broke(label, "the deque says it is empty, so the add did not store anything");
            return;
        }
        catch (RuntimeException e)
        {
            broke(label, "it threw " + e.getClass().getSimpleName()
                  + " on a reference that was never assigned");
            return;
        }

        show(label, f2b, b2f);
    }

    private void show(String label, String f2b, String b2f)
    {
        String forward = d.frontToBack();
        String backward = d.backToFront();
        boolean ok = forward.equals(f2b) && backward.equals(b2f);
        rows++;

        if (!ok)
            wrong++;

        if (forward.equals(f2b) && !backward.equals(b2f))
            backwardOnly++;

        System.out.printf("%-16s %-5s %-16s %s%n", label, ok ? "ok" : "WRONG",
                          forward, backward);

        if (!ok)
            System.out.printf("%-16s %-5s %-16s %s%n", "", "", f2b, b2f);
    }

    /* An operation that could not even be compared with what it should be. */
    private void broke(String label, String why)
    {
        rows++;
        wrong++;
        System.out.printf("%-16s %-5s %s%n", label, "WRONG", why);
    }

    void summary()
    {
        System.out.println();

        if (wrong == 0)
        {
            System.out.println("RESULT: PASS -- addToFront and removeBack are both right.");
            return;
        }

        System.out.println("RESULT: FAIL on " + wrong + " of " + rows + " operations.");

        if (backwardOnly > 0)
            System.out.println("  - front to back was right and back to front was not, so the"
                               + " next references are being kept up to date and the previous"
                               + " references are not. Back to front is the walk removeBack"
                               + " depends on.");

        System.out.println("  - the second line under a WRONG operation is what it should"
                           + " have printed.");
    }
} // end Driver
