public class Attack {
    public static void prim(){
        for (int i=0; i < 50000; i++){
            MoreSanity.loopAndbranch_safe(233, -1);
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
        answer1 = MoreSanity.loopAndbranch_safe(233, input);
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