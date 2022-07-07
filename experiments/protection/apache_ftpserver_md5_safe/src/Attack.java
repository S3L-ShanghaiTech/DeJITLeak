import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;

public class Attack {
    private static PasswordEncryptor pe = new Md5PasswordEncryptor(true);
    private static String user = "root";
    private static String pass = pe.encrypt(user);
    public static void prim(){
        for (int i=0; i < 50000; i++){
            pe.matches(user, pass);
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
        answer1 = pe.matches(user, input);
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
            measure(pass);

    }
}