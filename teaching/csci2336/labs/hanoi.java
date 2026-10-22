/* CSCI 2336 -- Chapter 4: the Towers of Hanoi.
 *
 * solveTowers below is the method from the slides, line for line. main runs
 * it on three disks, so the moves it prints are the moves the slide traces,
 * and then counts the moves for one disk through ten.
 *
 * Press Run. Then change DISKS and run it again: four disks take 15 moves,
 * five take 31, ten take 1023. No line of solveTowers changes.
 */
public class TowersOfHanoi
{
   // The number of disks the printed run starts with. Change this one.
   static final int DISKS = 3;

   // A run prints 2^DISKS - 1 lines, so a large DISKS prints more than
   // anyone reads. Above this the run prints the counts only.
   static final int PRINT_LIMIT = 12;

   public static void main(String[] args)
   {
      System.out.println("Every disk from startPole to endPole, with");
      System.out.println("tempPole to put things down on. One disk at a");
      System.out.println("time, and never onto a smaller one.");
      System.out.println();
      System.out.println("A is startPole, B is tempPole, C is endPole.");
      System.out.println();

      if (DISKS < 1 || DISKS > PRINT_LIMIT)
      {
         System.out.println("DISKS is " + DISKS + ", which prints too many");
         System.out.println("lines to read. Set it from 1 to " + PRINT_LIMIT
                            + " to see the moves.");
      }
      else
      {
         System.out.println(DISKS + " disks, A to C:");
         System.out.println();

         solveTowers(DISKS, 'A', 'B', 'C');

         System.out.println();
         System.out.println("Count the lines above: " + countMoves(DISKS)
                            + " moves.");
      }

      System.out.println();
      System.out.println("disks   moves");
      System.out.println("-----   -----");

      for (int disks = 1; disks <= 10; disks++)
         System.out.printf("%5d   %5d\n", disks, countMoves(disks));
   }

   // The method on the slide. It moves one disk itself and hands the other
   // disks to a copy of itself, twice.
   public static void solveTowers(int disks, char start, char temp, char end)
   {
      if (disks == 1)
         System.out.println("Move a disk from " + start + " to " + end);
      else
      {
         solveTowers(disks - 1, start, end, temp);
         System.out.println("Move a disk from " + start + " to " + end);
         solveTowers(disks - 1, temp, start, end);
      }
   }

   // solveTowers with the printing taken out and the moves added up: the one
   // move it makes itself, plus the two smaller towers.
   public static int countMoves(int disks)
   {
      if (disks == 1)
         return 1;

      return countMoves(disks - 1) + 1 + countMoves(disks - 1);
   }
}
