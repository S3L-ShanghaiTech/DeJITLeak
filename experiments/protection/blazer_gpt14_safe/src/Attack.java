import java.math.BigInteger;

public class Attack {
    private static BigInteger pub1 = new BigInteger("2");
    private static BigInteger pub2 = new BigInteger("65537");
    private static BigInteger input1 = new BigInteger("1111111111111111",2);
    private static BigInteger input2 = new BigInteger("1111111111111110",2);
    public static void prim(){
        for (int i=0; i < 10; i++){
            GPT14.modular_exponentiation_safe(pub1,input1,pub2);
            GPT14.modular_exponentiation_safe(pub1,input2,pub2);
        }
        for (int i=0; i < 25000; i++){
            GPT14.modular_exponentiation_safe(pub1,input1,pub2);
        }
    }
    public static void measure(BigInteger input){
        long startTime;
        long endTime; 
        BigInteger answer1;
        long cost1;

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = GPT14.modular_exponentiation_safe(pub1, input, pub2);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        prim();

        if (input > 0)
            measure(input1);
        else
            measure(input2);

    }
}