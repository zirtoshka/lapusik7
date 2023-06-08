package exceptions;

import utilities.DataBaseHandler;

public class DataBaseAuthorizationException extends Exception{
    public DataBaseAuthorizationException(String msg){
        super(msg);
    }
}
