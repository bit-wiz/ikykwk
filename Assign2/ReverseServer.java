import ReverseModule.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

public class ReverseServer {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);

            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            ReverseImpl reverseImpl = new ReverseImpl();
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(reverseImpl);
            Reverse reverseRef = ReverseHelper.narrow(ref);

            NamingContextExt Naming = NamingContextExtHelper.narrow(
                orb.resolve_initial_references("NameService")
            );

            Naming.rebind(Naming.to_name("Reverse"), reverseRef);

            System.out.println("Reverse Server is ready and waiting...");
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
