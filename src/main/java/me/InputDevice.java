package me;
import java.io.InputStream;
import java.util.Scanner;
public class InputDevice {
    InputStream IS;
    Scanner scanner;
    public InputDevice(InputStream x){
        IS=x;
        scanner=new Scanner(IS);
    }
    public String read(){
        String answer="";
        try{
        answer=scanner.nextLine();
        }
        catch(Exception e){
            answer="";
            // e.printStackTrace();
        }
        // try (Scanner scanner = new Scanner(IS)) {
        //     answer=scanner.nextLine();
        // }
        // catch(Exception e){
        //     System.err.println("Caught an error: creating input device");
        // }
        return answer;
    }
}
