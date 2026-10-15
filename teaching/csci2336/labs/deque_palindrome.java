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
 * Nothing outside isPalindrome needs changing. main() holds the example
 * run: six words, with the answer and the number of comparisons each one
 * should take written beside it.
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
        header();

        //     word      palindrome?  comparisons
        check("level",   true,        2);
        check("stats",   true,        2);
        check("deed",    true,        2);
        check("sales",   false,       2);
        check("data",    false,       1);
        check("x",       true,        0);

        summary();
    }


    /* ===== The driver: printing and checking. Nothing to change. ======== */

    static int rows = 0;
    static int wrong = 0;
    static int neverUsed = 0;       // a word that made no deque calls at all

    static void header()
    {
        System.out.println("A palindrome reads the same backwards. One comparison per pair"
                           + " of characters.");
        System.out.println();
        System.out.println("word     palindrome?  comparisons  ok?");
        System.out.println("----     -----------  -----------  ---");
    }

    static void check(String word, boolean wantAnswer, int wantComparisons)
    {
        LinkedDeque.comparisons = 0;
        rows++;

        boolean answer;

        try
        {
            answer = isPalindrome(word);
        }
        catch (EmptyQueueException e)
        {
            wrong++;
            System.out.printf("%-8s %-12s %-12s %s   %s%n", word, "--", "--", "WRONG",
                              "it removed from an empty deque, so nothing was added to it");
            return;
        }

        int comparisons = LinkedDeque.comparisons;

        if (comparisons == 0 && word.length() > 1)
            neverUsed++;

        boolean ok = answer == wantAnswer && comparisons == wantComparisons;

        if (!ok)
            wrong++;

        System.out.printf("%-8s %-12s %-12d %s%s%n", word, answer ? "yes" : "no",
                          comparisons, ok ? "ok" : "WRONG",
                          ok ? "" : "   should be " + (wantAnswer ? "yes" : "no")
                               + ", in " + wantComparisons);
    }

    static void summary()
    {
        System.out.println();

        if (wrong == 0)
        {
            System.out.println("RESULT: PASS -- isPalindrome is right on all " + rows
                               + " words.");
            return;
        }

        System.out.println("RESULT: FAIL on " + wrong + " of " + rows + " words.");

        if (neverUsed > 0)
            System.out.println("  - the comparison column is 0, so the second loop is not"
                               + " taking anything off the deque yet.");

        System.out.println("  - a word reported yes with 0 comparisons was never actually"
                           + " checked.");
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
