import java.util.*;

public class LeaderElection {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== Leader Election Simulation =====");
        System.out.println("1. Bully Algorithm");
        System.out.println("2. Ring Algorithm");
        System.out.print("Choose the algorithm (1 or 2): ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                runBullyAlgorithm();
                break;
            case 2:
                runRingAlgorithm();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    static void runBullyAlgorithm() {
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];
        System.out.println("Enter process IDs:");
        for (int i = 0; i < n; i++) {
            processes[i] = sc.nextInt();
        }

        System.out.print("Enter number of crashed processes: ");
        int crashCount = sc.nextInt();
        Set<Integer> crashed = new HashSet<>();
        if (crashCount > 0) {
            System.out.println("Enter IDs of crashed processes:");
            for (int i = 0; i < crashCount; i++) {
                crashed.add(sc.nextInt());
            }
        }

        Arrays.sort(processes);
        List<Integer> active = new ArrayList<>();
        for (int p : processes) if (!crashed.contains(p)) active.add(p);

        System.out.println("\n--- Bully Election Started ---\n");

        Map<Integer, List<String>> electionLog = new LinkedHashMap<>();
        Map<Integer, List<String>> okLog = new LinkedHashMap<>();
        Map<Integer, String> conclusionLog = new LinkedHashMap<>();

        for (int initiator : processes) {
            List<String> eLogs = new ArrayList<>();
            List<String> okLogs = new ArrayList<>();

            if (crashed.contains(initiator)) {
                eLogs.add("Process " + initiator + " is crashed. No election.");
                electionLog.put(initiator, eLogs);
                okLog.put(initiator, okLogs);
                conclusionLog.put(initiator, "Crashed.");
                continue;
            }

            eLogs.add("Process " + initiator + " starts election.");
            boolean gotReply = false;

            for (int target : processes) {
                if (target > initiator) {
                    eLogs.add(" -> Sends ELECTION to Process " + target);
                    if (!crashed.contains(target)) {
                        okLogs.add(" <- OK from Process " + target);
                        gotReply = true;
                    } else {
                        okLogs.add(" !! No reply from Process " + target + " (crashed)");
                    }
                }
            }

            if (!gotReply) {
                conclusionLog.put(initiator, "Becomes COORDINATOR.");
            } else {
                conclusionLog.put(initiator, "Waiting for coordinator announcement.");
            }

            electionLog.put(initiator, eLogs);
            okLog.put(initiator, okLogs);
        }

        int coordinator = active.get(active.size() - 1);
        System.out.println("===> Final COORDINATOR is Process " + coordinator + "\n");

        // Display logs
        for (int pid : processes) {
            System.out.println("=== Logs for Process " + pid + " ===");
            System.out.println("Election Messages:");
            for (String msg : electionLog.get(pid)) System.out.println(msg);
            System.out.println("OK Messages:");
            for (String msg : okLog.get(pid)) System.out.println(msg);
            System.out.println("Conclusion: " + conclusionLog.get(pid));
            System.out.println();
        }
    }

    static void runRingAlgorithm() {
    System.out.print("Enter number of processes in the ring: ");
        int n = sc.nextInt();
        int[] ring = new int[n];

        System.out.println("Enter process IDs in ring order:");
        for (int i = 0; i < n; i++) {
            ring[i] = sc.nextInt();
        }

        System.out.print("Enter initiator process ID: ");
        int initiator = sc.nextInt();

        System.out.print("Enter number of crashed processes: ");
        int crashCount = sc.nextInt();
        Set<Integer> crashed = new HashSet<>();
        if (crashCount > 0) {
            System.out.println("Enter IDs of crashed processes:");
            for (int i = 0; i < crashCount; i++) {
                crashed.add(sc.nextInt());
            }
        }

        System.out.println("\n=== ELECTION PHASE ===");

        int index = -1;
        for (int i = 0; i < n; i++) {
            if (ring[i] == initiator) {
                index = i;
                break;
            }
        }

        if (index == -1 || crashed.contains(initiator)) {
            System.out.println("Invalid or crashed initiator.");
            return;
        }

        List<Integer> activeList = new ArrayList<>();
        int current = index;
        int start = ring[current];

        System.out.println("[" + start + "] Initiates Election.");
        if (!crashed.contains(start)) activeList.add(start);

        do {
            int next = (current + 1) % n;
            int sender = ring[current];
            int receiver = ring[next];

            if (crashed.contains(receiver)) {
                System.out.println("[" + sender + "] -> Sends election to [" + receiver + "]");
                System.out.println("[" + receiver + "] is CRASHED. Skipping...");
            } else {
                System.out.println("[" + sender + "] -> Sends election to [" + receiver + "]");
                if (!activeList.contains(receiver)) {
                    System.out.println("[" + receiver + "] -> Adds itself to active list");
                    activeList.add(receiver);
                }
            }

            current = next;
        } while (ring[current] != initiator);

        System.out.println("[" + initiator + "] <- Election message returned to initiator");
        System.out.println("Final Active List: " + activeList);

        int coordinator = Collections.max(activeList);
        System.out.println("[" + initiator + "] -> Elects COORDINATOR: [" + coordinator + "]");

        // Coordinator announcement phase
        System.out.println("\n=== COORDINATOR ANNOUNCEMENT PHASE ===");

        current = index;
        do {
            int next = (current + 1) % n;
            int sender = ring[current];
            int receiver = ring[next];

            if (crashed.contains(receiver)) {
                System.out.println("[" + sender + "] -> Forwards COORDINATOR(" + coordinator + ") to [" + receiver + "]");
                System.out.println("[" + receiver + "] is CRASHED. Skipping...");
            } else {
                System.out.println("[" + sender + "] -> Forwards COORDINATOR(" + coordinator + ") to [" + receiver + "]");
            }

            current = next;
        } while (ring[current] != initiator);

        System.out.println("[" + initiator + "] <- Coordinator announcement completed.");

        // Visual Ring Structure
        System.out.println("\nRing: ");
        for (int i = 0; i < ring.length; i++) {
            if (crashed.contains(ring[i])) {
                System.out.print("[" + ring[i] + "](CRASHED) -> ");
            } else {
                System.out.print("[" + ring[i] + "] -> ");
            }
        }
        System.out.println("back to [" + ring[0] + "]");
    }
}
