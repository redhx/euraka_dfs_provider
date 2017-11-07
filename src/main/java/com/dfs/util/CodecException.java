package com.dfs.util;

/**
 * 算法异常类
 *
 * @author huangxi
 */
public class CodecException extends Exception
{
    private static final long serialVersionUID = 3966129708775022345L;

    public CodecException()
    {
        super();
    }

    public CodecException(String msg)
    {
        super(msg);
    }

    public CodecException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public CodecException(Throwable cause)
    {
        super(cause);
    }
}
