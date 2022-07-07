import org.apache.ftpserver.util.StringUtils;

public class Attack {
    public static void prim(){
        StringUtils.pad_safe("rootroot", '1', true, 8);
        for (int i=0; i < 50000; i++){
            StringUtils.pad_safe("root", '1', true, 8);
        }
    }
    public static void measure(String input){
        long startTime;
        long endTime; 
        String answer1;
        long cost1;

        startTime = System.nanoTime();
        endTime = System.nanoTime();
        startTime = System.nanoTime();
        answer1 = StringUtils.pad_safe(input, '1', true, 8);
        endTime = System.nanoTime();
        cost1 = endTime - startTime;
        System.out.println("cost: " + cost1);
        System.out.println("answer: " + answer1);
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);
        
        StringUtils.safeMode = true;

        prim();

        if (input > 0)
            measure("root");
        else
            measure("rootroot");

    }
}