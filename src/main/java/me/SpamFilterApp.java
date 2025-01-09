package me;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
public class SpamFilterApp {
    String programloc="/home/razvan/repos/penguinmail/spamFilterprogram";
    String input;
    String fileInput="appinput.txt";
    String fileOutput="a.out";
    int answer=0;
    public SpamFilterApp(String inp){
        input=inp;
    }
    public void execute(){
        String commands[]={programloc,fileInput};
        Path ifpath=Paths.get("appinput.txt");
        try {
            Files.write(ifpath,Arrays.asList(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProcessBuilder processb=new ProcessBuilder(commands);
        processb.redirectErrorStream(true);
        try{
        Process process=processb.start();
        int exitcode=process.waitFor();
        if (exitcode==0){
            Path outputfilePath=Paths.get(fileOutput);
            try {
                List <String> lines=Files.readAllLines(outputfilePath);
                if (lines.getFirst().equals("1")){
                    answer=1;
                }
                else{
                    answer=0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
