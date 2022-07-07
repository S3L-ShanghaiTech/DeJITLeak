public class Attack {
    public static void prim(){
        String ref = "password";
        for (int i=0; i < 50000; i++){
            User.passwordsEqual_safe(ref, ref);
        }
    }
    public static void measure(String input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;
        String ref = "password";

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = User.passwordsEqual_safe(input, ref);
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
            measure("PASSWORD");

    }
}