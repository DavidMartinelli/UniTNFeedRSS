package it.unitn.hci.utils;

public class TODOException extends UnsupportedOperationException
{

    private static final long serialVersionUID = 7422539470790224796L;


    public TODOException(String message)
    {
        super("TODO: " + message);
    }

}
