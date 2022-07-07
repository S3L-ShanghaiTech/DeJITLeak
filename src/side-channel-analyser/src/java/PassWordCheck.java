public class PassWordCheck
{

  private static String pw = "000000000000000000000000000";
  //private static String pw = "0000";
  private static int t = 0;

  public static Boolean check(String guess){
    for ( int i = 0; i < pw.length(); i++ ){
      if( i >= guess.length() || guess.charAt(i) != pw.charAt(i) ){
        return false;
      }
      
      try {
        Thread.sleep(t);                
      } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    
    }
    return true;
  }

  public static void main(String[] args)
  {
    t = Integer.parseInt(args[0]);
    int result = check(args[1]) ? 1 : 0;    

  }

}
