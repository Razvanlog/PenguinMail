package me;

public class emptyAccountException extends Exception{
    public emptyAccountException(){
        super();
    }
    public emptyAccountException(String mess){
        super(mess);
    }
    public emptyAccountException(String mess,Throwable cause){
        super(mess,cause);
    }
    public emptyAccountException(Throwable cause){
        super(cause);
    }
}
