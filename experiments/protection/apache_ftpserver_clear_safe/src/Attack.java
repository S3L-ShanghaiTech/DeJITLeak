import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;

public class Attack {
    private static PasswordEncryptor pe = new ClearTextPasswordEncryptor(true);
    public static void prim(){
        String pass = "password";
        for (int i=0; i < 50000; i++){
            pe.matches(pass, pass);
        }
    }
    public static void measure(String input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;
        String pass = "password";

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = pe.matches(pass, input);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        prim();
        if (input > 0)
            measure("password");
        else
            measure("admin");

    }
}