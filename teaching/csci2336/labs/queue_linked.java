/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 1 of 4: the linked queue.
 *
 * Write the five lines marked "Your line goes here", in the LinkedQueue
 * class below. Nothing else in this file needs changing.
 *
 * main() makes a LinkedQueue and performs five operations on it, printing
 * the queue after each one. The lab page shows the console this run prints
 * when the queue works; compare it with what you get, line by line.
 *
 * Press Run before you type anything: the file compiles and runs with the
 * blanks empty, and the queue stays empty because enqueue stores nothing.
 */
public class Exercise1
{
    public static void main(String[] args)
    {
        LinkedQueue<String> q = new LinkedQueue<String>();

        System.out.println("Linked queue: firstNode is the front, lastNode the back.");
        System.out.println();
        System.out.println("operation      lastNode  front .. back");
        System.out.println("---------      --------  -------------");
        System.out.println("start          " + q.state());

        try
        {
            q.enqueue("A");
            System.out.println("enqueue A      " + q.state());

            q.enqueue("B");
            System.out.println("enqueue B      " + q.state());

            String first = q.dequeue();
            System.out.println("dequeue -> " + first + "   " + q.state());

            String second = q.dequeue();
            System.out.println("dequeue -> " + second + "   " + q.state());

            q.enqueue("C");
            System.out.println("enqueue C      " + q.state());
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
       Not part of the queue ADT. main() has to see lastNode, the one field
       a wrong answer here can leave stale, and the chain read from the
       front.
       =================================================================== */

    static final int WALK_LIMIT = 20;       // a chain this short cannot be
                                            // longer, so an unfinished blank
                                            // cannot send the walk on forever

    /* The two columns main() prints after each operation. */
    String state()
    {
        return String.format("%-9s %s", lastRef(), frontToBack());
    }

    private String lastRef()
    {
        return lastNode == null ? "null" : lastNode.getData().toString();
    }

    private String frontToBack()
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
