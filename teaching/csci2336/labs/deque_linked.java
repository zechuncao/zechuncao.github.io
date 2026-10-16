/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 3 of 4: the deque.
 *
 * Write the six lines marked "Your line goes here", three in addToFront
 * and three in removeBack. The two methods right above them, addToBack
 * and removeFront, are written for you: the ones you write are their
 * mirror image, with firstNode and previous in place of lastNode and next.
 *
 * Nothing else in this file needs changing. main() makes a LinkedDeque and
 * performs seven operations on it. The lab page shows the console this run
 * prints when the deque works; compare it with what you get, line by line.
 *
 * Each row reads the deque twice: front to back along the next references,
 * and back to front along the previous references. A deque whose next
 * references are right and whose previous references are not is correct in
 * one column and wrong in the other.
 */
public class Exercise3
{
    public static void main(String[] args)
    {
        LinkedDeque<String> d = new LinkedDeque<String>();

        System.out.println("Doubly linked deque: every node refers to the node before"
                           + " it and the node after it.");
        System.out.println();
        System.out.println("operation        front .. back    back .. front");
        System.out.println("---------        -------------    -------------");
        System.out.println("start            " + d.state());

        try
        {
            d.addToBack("B");
            System.out.println("addToBack B      " + d.state());

            d.addToFront("A");
            System.out.println("addToFront A     " + d.state());

            d.addToBack("C");
            System.out.println("addToBack C      " + d.state());

            String back = d.removeBack();
            System.out.println("removeBack -> " + back + "  " + d.state());

            String front = d.removeFront();
            System.out.println("removeFront -> " + front + " " + d.state());

            back = d.removeBack();
            System.out.println("removeBack -> " + back + "  " + d.state());

            d.addToFront("D");              // the deque is empty again here
            System.out.println("addToFront D     " + d.state());
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

    /* The two columns main() prints after each operation. */
    String state()
    {
        return String.format("%-16s %s", frontToBack(), backToFront());
    }

    private String frontToBack()
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

    private String backToFront()
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
