import java.util.*;

public class Ring {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of processes: ");
        int n = sc.nextInt();

        boolean[] alive = new boolean[n];
        Arrays.fill(alive, true);

        System.out.print("Number of crashed processes: ");
        int crashCount = sc.nextInt();

        System.out.println("Enter crashed process IDs:");
        for (int i = 0; i < crashCount; i++) {
            int id = sc.nextInt();
            if (id >= 0 && id < n) alive[id] = false;
        }

        System.out.print("Enter initiator process ID: ");
        int start = sc.nextInt();
        if (!alive[start]) {
            System.out.println("Initiator is crashed.");
            return;
        }

        List<Integer> path = new ArrayList<>();
        Set<Integer> candidates = new HashSet<>();
        
        int current = start;

        do {
            if (alive[current]) {
                System.out.println("Process " + current + " is alive.");
                candidates.add(current);
                path.add(current);
            } else {
                System.out.println("Process " + current + " is crashed. Skipped.");
            }

            System.out.println("Election path so far: " + path);
            current = (current + 1) % n;

            /*
            first iteration curret = 1->2->3->5->1
            
            1 = (1+1)%4 = 2
            2 = (2+1)%4 = 3
            3 = (3+1)%4 = 4
            4 = (4+1)%4 = 1

            */

        } while (current != start);

        int coordinator = Collections.max(candidates);
        System.out.println("\nFinal Election Path: " + path);
        System.out.println("Process " + coordinator + " is elected as coordinator.");
        sc.close();
    }
}
