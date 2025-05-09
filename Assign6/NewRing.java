import java.util.*;

public class NewRing {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input
        System.out.print("Number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];

        System.out.println("Enter unique process IDs:");
        for (int i = 0; i < n; i++) processes[i] = sc.nextInt();

        System.out.print("Enter crashed process IDs count: ");
        int crashCount = sc.nextInt();
        Set<Integer> crashed = new HashSet<>();

        System.out.println("Enter crashed process IDs:");
        for (int i = 0; i < crashCount; i++) crashed.add(sc.nextInt());

        System.out.print("Enter initiator process ID: ");
        int initiator = sc.nextInt();

        // Build alive list with order
        List<Integer> ring = new ArrayList<>();
        for (int p : processes) ring.add(p);

        if (crashed.contains(initiator)) {
            System.out.println("Initiator is crashed.");
            return;
        }

        // Find initiator index in ring
        int start = ring.indexOf(initiator);
        List<Integer> path = new ArrayList<>();
        Set<Integer> candidates = new HashSet<>();

        System.out.println("\n--- Election Start ---");
        int idx = start;

        do {
            int pid = ring.get(idx);
            if (crashed.contains(pid)) {
                System.out.println("Process " + pid + ": Crashed - Skipped");
            } else {
                System.out.println("Process " + pid + ": Alive");
                path.add(pid);
                candidates.add(pid);
            }
            System.out.println("Current path: " + path);
            idx = (idx + 1) % n;
        } while (idx != start);

        path.add(initiator); // close the ring

        int coordinator = Collections.max(candidates);
        System.out.println("\n--- Election Complete ---");
        System.out.println("Final Path (ring): " + path);
        System.out.println("Elected Coordinator: " + coordinator);
    }
}
