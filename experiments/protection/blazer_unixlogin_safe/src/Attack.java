public class Attack {
    public static void prim(){
        String user = "root";
        String pass = "";
        for (int i=0; i < 50000; i++){
            Timing.login_safe("root", "");
        }
    }
    public static void measure(String input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;
        String pass = "";

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = Timing.login_safe(input, pass);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        Timing.resetMap("root", "");

        prim();
        if (input > 0)
            measure("root");
        else
            measure("admin");

    }
}