public class Attack {
    public static void prim(){
        int arr[] = {1,2,3,4,5,6,7,8,9,10};
        for (int i=0; i < 50000; i++){
            MoreSanity.array_safe(arr, -1);
        }
    }
    public static void measure(int input){
        long startTime;
        long endTime; 
        boolean answer1;
        long cost1;
        int arr[] = {1,2,3,4,5,6,7,8,9,10};

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = MoreSanity.array_safe(arr, input);
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