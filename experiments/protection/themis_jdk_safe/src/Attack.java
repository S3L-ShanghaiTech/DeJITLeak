public class Attack {
    private static byte[] digesta = {112, 97, 115, 115, 119, 111, 114, 100};
    private static byte[] digestb = {80, 65, 83, 83, 87, 79, 82, 68};
    public static void prim(){
        for (int i=0; i < 50000; i++){
            MessageDigest.isEqual_safe(digesta, digestb);
        }
    }
    public static void measure(byte[] digest){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = MessageDigest.isEqual_safe(digest, digestb);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        prim();
        if (input > 0)
            measure(digesta);
        else
            measure(digestb);

    }
}