import java.util.*;

public class Bully {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];
        System.out.println("Enter unique process IDs:");
        for (int i = 0; i < n; i++) processes[i] = sc.nextInt();

        System.out.print("Enter crashed process ID: ");
        int crashed = sc.nextInt();
        System.out.print("Enter initiator process ID: ");
        int initiator = sc.nextInt();

        Arrays.sort(processes);

        List<Integer> active = new ArrayList<>();

        for (int p : processes) if (p != crashed) active.add(p);

        if (!active.contains(initiator)) {
            System.out.println("Invalid initiator since it has crashed.");
            return;
        }

        int curr = initiator;
        while (true) {
            int idx = active.indexOf(curr);

            List<Integer> higher = new ArrayList<>();

            for (int i = idx + 1; i < active.size(); i++) {
                System.out.println(curr + " -> Election to " + active.get(i));
                higher.add(active.get(i));
            }

            if (higher.isEmpty()) {
                System.out.println(curr + " is the coordinator.");
                break;
            }

            for (int h : higher) {
                System.out.println(h + " -> OK to " + curr);
            }

            curr = higher.get(0);
        }

        System.out.println("Coordinator announcement:");
        for (int p : active) {
            if (p != curr) System.out.println(curr + " -> Coordinator to " + p);
        }
    }
}
