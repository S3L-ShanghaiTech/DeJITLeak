import com.jdriven.stateless.security.User;
import javax.xml.bind.DatatypeConverter;

public class Attack {
    private static String userTokenInvalid;
    private static String userTokenValid;
    private static User tmp;
    private static TokenHandler th;
    public static void init(){

        byte[] secretKey = { 15, 23, -12, 17, 3 }; // YN just random, but fixed for all experiments
        String userBytesString = "test";
        int invalidCharacterIndex = 1;
        th = new TokenHandler(secretKey, true);

        byte[] validHash = th.hmac.doFinal(DatatypeConverter.parseBase64Binary(userBytesString));
        String hashByteStringValid = DatatypeConverter.printBase64Binary(validHash);
        userTokenValid = userBytesString + TokenHandler.SEPARATOR + hashByteStringValid;

        /* Generate a hash with same size but the wrong content. */
        invalidCharacterIndex = invalidCharacterIndex % validHash.length;
        byte[] invalidHash = new byte[validHash.length];
        for (int i = 0; i < invalidHash.length; i++) {
            if (i == invalidCharacterIndex) {
                invalidHash[i] = (byte) ((validHash[i] == 42) ? 21 : 42);
            } else {
                invalidHash[i] = validHash[i];
            }
        }
        String hashByteStringInvalid = DatatypeConverter.printBase64Binary(invalidHash);
        userTokenInvalid = userBytesString + TokenHandler.SEPARATOR + hashByteStringInvalid;


    }
    public static void prim(){
        for (int i=0; i < 50000; i++){
            tmp = th.parseUserFromToken(userTokenValid);
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
        tmp = th.parseUserFromToken(input);
        endTime = System.nanoTime();
        answer1 = (tmp == TokenHandler.VALID_USER);
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);
        init();
        prim();
        if (input > 0)
            measure(userTokenValid);
        else
            measure(userTokenInvalid);

    }
}