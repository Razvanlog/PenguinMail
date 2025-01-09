package me;
import java.io.IOException;
import java.io.OutputStream;
public class OutputDevice {
    OutputStream OS;
    public OutputDevice(OutputStream x){
        OS=x;
    }
    public void print_terminal(String a){
        // System.out.printf("%s",a);
        try{
        OS.write(a.getBytes());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
