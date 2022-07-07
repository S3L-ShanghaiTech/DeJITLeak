public class Attack {
    public static void prim(){
        for (int i=0; i < 50000; i++){
            Sanity.straightline_safe(-1, 233);
        }
    }
    public static void measure(int input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = Sanity.straightline_safe(input, 233);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);

        prim();

        measure(input);

    }
}