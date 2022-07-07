public class Attack {
    public static byte[] pass0 = "password".getBytes();
    public static byte[] pass1 = "root".getBytes();
    public static void prim(){
        for (int i=0; i < 50000; i++){
            PWCheck.pwcheck3_safe(pass0, pass0);
        }
    }
    public static void measure(byte[] input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = PWCheck.pwcheck3_safe(pass0, input);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        prim();

        if (input > 0)
            measure(pass0);
        else
            measure(pass1);

    }
}