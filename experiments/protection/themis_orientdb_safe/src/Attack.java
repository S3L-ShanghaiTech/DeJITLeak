public class Attack {
    private static String digesta = "password";
    private static String digestb = "PASSWORD";
    private static OSecurityManager manager = new OSecurityManager();
    public static void prim(){
        for (int i=0; i < 50000; i++){
            manager.checkPassword_safe(digesta, digestb);
        }
    }
    public static void measure(String digest){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = manager.checkPassword_safe(digest, digestb);
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