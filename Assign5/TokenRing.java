import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TokenRing {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Total number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];

        System.out.println("Enter process IDs:");
        for (int i = 0; i < n; i++) {
            processes[i] = sc.nextInt();
        }

        //sort processes
        Arrays.sort(processes);

        int token = 0;
        for (int i = 0; i < n; i++) {
            int pid = processes[token];
            System.out.println("\nToken is with Process " + pid);
            System.out.print("Pass token and write message? (y/n): ");
            String choice = sc.next().toLowerCase();

            switch (choice) {
                case "y":

                    System.out.print("Enter message for Process " + pid + ": ");
                    System.out.print(" Process " + pid + " is in CRITICAL SECTION\n");
                    String msg = sc.next();
                    try (
                        
                        FileWriter fw = new FileWriter("process_log.txt", true)
                        
                        ) {
                        fw.write("Process " + pid + ": " + msg + "\n");
                        System.out.println("Message saved.");
                    } catch (IOException e) {
                        System.out.println("Error writing to file.");
                    }
                    System.out.println("Process " + pid + " passed token to Process " + processes[(token + 1) % n]);
                    break;

                case "n":
                    System.out.println("Process " + pid + " skipped.");
                    break;

                default:
                    System.out.println("Invalid input. Skipping process.");
            }

            token = (token + 1) % n;
        }

        System.out.println("\nToken ring completed.");
        sc.close();
    }
}
