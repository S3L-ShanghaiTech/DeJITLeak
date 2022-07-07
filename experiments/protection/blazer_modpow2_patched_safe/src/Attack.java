import java.math.BigInteger;

public class Attack {
    public static void prim(){
        BigInteger pub1 = new BigInteger("1");
        BigInteger pub2 = new BigInteger("65537");
        BigInteger input1 = new BigInteger("1111111111111111",2);
        BigInteger input2 = new BigInteger("11111111111111111111111111111111",2);

        for (int i=0; i < 25000; i++){
            ModPow2.modPow2_safe(pub1,input1,pub2,16);
            ModPow2.modPow2_safe(pub1,input2,pub2,32);
            // ModPow2.modPow2_safe(pub1,new BigInteger("1111111111111111111111111111111111111111111111111111111111111111",2),pub2,64);
            // ModPow2.modPow2_safe(pub1,new BigInteger("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",2),pub2,128);
            // ModPow2.modPow2_safe(pub1,new BigInteger("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",2),pub2,256);
        }
    }
    public static void measure(BigInteger input){
        long startTime;
        long endTime; 
        BigInteger answer1;
        long cost1;
        BigInteger pub1 = new BigInteger("1");
        BigInteger pub2 = new BigInteger("65537");

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = ModPow2.modPow2_safe(pub1, input, pub2, 4);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        prim();

        if (input > 0)
            measure(new BigInteger("15"));
        else
            measure(new BigInteger("14"));

    }
}