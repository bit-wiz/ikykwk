import ReverseModule.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.io.*;

public class ReverseClient {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            NamingContextExt ncRef = NamingContextExtHelper.narrow(
                orb.resolve_initial_references("NameService")
            );

            Reverse reverseObj = ReverseHelper.narrow(ncRef.resolve_str("Reverse"));

            System.out.print("Enter String: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();

            String result = reverseObj.reverse_string(input);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
