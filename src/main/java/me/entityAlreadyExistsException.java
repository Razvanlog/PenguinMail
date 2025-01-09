package me;

public class entityAlreadyExistsException extends Exception{
    public entityAlreadyExistsException(){
        super();
    }
    public entityAlreadyExistsException(String mess){
        super(mess);
    }
    public entityAlreadyExistsException(String mess,Throwable cause){
        super(mess,cause);
    }
    public entityAlreadyExistsException(Throwable cause){
        super(cause);
    }
}
