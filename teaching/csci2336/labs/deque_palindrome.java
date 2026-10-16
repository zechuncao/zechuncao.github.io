/* CSCI 2336 -- Chapter 3 Companion Lab, Exercise 4 of 4: a palindrome test.
 *
 * The deque is finished this time -- it is the one you wrote in Exercise 3.
 * What is missing is the method that uses it. Write the five lines marked
 * "Your line(s) go here" in isPalindrome below.
 *
 * A word is a palindrome when it reads the same backwards. Put every
 * character into the deque, then take one character off each end and
 * compare them, until fewer than two are left.
 *
 * Nothing outside isPalindrome needs changing. main() runs six words
 * through it and prints the answer and the number of comparisons each one
 * took. The lab page shows the console this run prints when the method
 * works; compare it with what you get, line by line.
 */
public class Exercise4
{
    /* ===== Yours ======================================================== */

    static boolean isPalindrome(String word)
    {
        DequeInterface<Character> d = new LinkedDeque<Character>();

        for (int i = 0; i < word.length(); i++)
        {
            // Your line goes here: this character goes at the back  (1)

        }

        for (int k = 0; k < word.length() / 2; k++)
        {
            // Your four lines go here: one off each end, compare  (4)




        }

        return true;
    } // end isPalindrome

    /* ===== The example run ==============================================
       word.length() / 2 is how many comparisons a word of that length
       needs, so "level" takes 2 and the middle v is never compared. A
       mismatch stops the loop early, which is why "data" takes 1.
       =================================================================== */
    public static void main(String[] args)
    {
        System.out.println("A palindrome reads the same backwards. One comparison per pair"
                           + " of characters.");
        System.out.println();
        System.out.println("word     palindrome?  comparisons");
        System.out.println("----     -----------  -----------");

        try
        {
            boolean level = isPalindrome("level");
            System.out.printf("level    %-12s %s%n", level, LinkedDeque.comparisons);

            boolean stats = isPalindrome("stats");
            System.out.printf("stats    %-12s %s%n", stats, LinkedDeque.comparisons);

            boolean deed = isPalindrome("deed");
            System.out.printf("deed     %-12s %s%n", deed, LinkedDeque.comparisons);

            boolean sales = isPalindrome("sales");
            System.out.printf("sales    %-12s %s%n", sales, LinkedDeque.comparisons);

            boolean data = isPalindrome("data");
            System.out.printf("data     %-12s %s%n", data, LinkedDeque.comparisons);

            boolean one = isPalindrome("x");
            System.out.printf("x        %-12s %s%n", one, LinkedDeque.comparisons);
        }
        catch (EmptyQueueException e)
        {
            System.out.println();
            System.out.println("The run stopped after the row above: isPalindrome took"
                               + " from a deque that nothing had been put into, so the"
                               + " first blank is the one to write.");
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


/* The finished Exercise 3 deque. Read it if you like; there is nothing to
   change in it. */
final class LinkedDeque<T> implements DequeInterface<T>
{
    private DLNode firstNode;   // the front entry
    private DLNode lastNode;    // the back entry

    public LinkedDeque()
    {
        firstNode = null;
        lastNode = null;
        comparisons = 0;        // for this lab only: one count per word
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

    public void addToBack(T newEntry)
    {
        DLNode newNode = new DLNode(lastNode, newEntry, null);

        if (isEmpty())
            firstNode = newNode;
        else
            lastNode.setNext(newNode);

        lastNode = newNode;
    } // end addToBack

    public void addToFront(T newEntry)
    {
        DLNode newNode = new DLNode(null, newEntry, firstNode);

        if (isEmpty())
            lastNode = newNode;
        else
            firstNode.setPrevious(newNode);

        firstNode = newNode;
    } // end addToFront

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

    public T removeBack()
    {
        T back = getBack();   // might throw EmptyQueueException

        comparisons++;                     // for this lab only: the console
                                           // reports one comparison per pair
        lastNode = lastNode.getPrevious();

        if (lastNode == null)
            firstNode = null;
        else
            lastNode.setNext(null);

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

    /* For this lab only: how many times removeBack has been called, which
       is one per comparison the loop in isPalindrome performs. */
    static int comparisons = 0;
} // end LinkedDeque
