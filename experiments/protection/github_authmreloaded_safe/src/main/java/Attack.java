import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.security.crypts.RoyalAuth;

public class Attack {
    private static EncryptionMethod encrMethod = new RoyalAuth(true);
    private static HashedPassword hash0 = encrMethod.computeHash("password", "root");
    private static HashedPassword hash1 = encrMethod.computeHash("rootroot", "root");

    public static void prim(){
        for (int i=0; i < 50000; i++){
            encrMethod.comparePassword("password", hash0, "root");
        }
    }
    public static void measure(HashedPassword input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;
        String pass = "password";

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = encrMethod.comparePassword("password", input, "root");
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);
        prim();
        if (input > 0)
            measure(hash0);
        else
            measure(hash1);

    }
}