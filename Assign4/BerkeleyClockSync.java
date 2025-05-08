import java.util.*;

public class BerkeleyClockSync {

    static class Client {
        int hour, minute, adjustment;

        Client(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        int timeInMinutes() {
            return hour * 60 + minute;
        }

        int differenceFrom(int serverMinutes) {
            return this.timeInMinutes() - serverMinutes;
        }

        void applyAdjustment(int adjustment) {
            int total = timeInMinutes() + adjustment;
            this.adjustment = adjustment;
            hour = (total / 60) % 24;
            minute = total % 60;
        }

        void printTime() {
            System.out.printf("Client time: %02d:%02d", hour, minute);
            if (adjustment != 0)
                System.out.print(" (adjusted by " + adjustment + " mins)");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get server time
        System.out.print("Enter server time (HH:mm): ");
        String[] serverInput = scanner.nextLine().split(":");
        int serverHour = Integer.parseInt(serverInput[0]);
        int serverMinute = Integer.parseInt(serverInput[1]);
        int serverTotalMinutes = serverHour * 60 + serverMinute;

        // Get client times
        System.out.print("Enter number of clients: ");
        int clientCount = Integer.parseInt(scanner.nextLine());
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < clientCount; i++) {
            System.out.print("Enter Client " + (i + 1) + " time (HH:mm): ");
            String[] timeParts = scanner.nextLine().split(":");
            clients.add(new Client(
                Integer.parseInt(timeParts[0]),
                Integer.parseInt(timeParts[1])
            ));
        }

        // Show initial times
        System.out.println("\nInitial Client Times:");
        for (Client client : clients) client.printTime();

        // Calculate average difference (including server)
        int totalDifference = 0;
        for (int i = 0; i < clientCount; i++) {
            int diff = clients.get(i).differenceFrom(serverTotalMinutes);
            System.out.println("Client " + (i + 1) + " difference: " + diff + " mins");
            totalDifference += diff;
        }
        int averageDiff = totalDifference / (clientCount + 1);
        serverTotalMinutes += averageDiff;
        serverHour = (serverTotalMinutes / 60) % 24;
        serverMinute = serverTotalMinutes % 60;

        System.out.printf("\nAdjusted Server Time: %02d:%02d\n", serverHour, serverMinute);

        // Apply adjustments
        System.out.println("\nSynchronized Client Times:");
        for (Client client : clients) {
            int adjustment = serverTotalMinutes - client.timeInMinutes();
            client.applyAdjustment(adjustment);
            client.printTime();
        }

        scanner.close();
    }
}
