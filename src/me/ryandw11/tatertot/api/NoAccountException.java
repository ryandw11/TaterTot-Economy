package me.ryandw11.tatertot.api;

public class NoAccountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoAccountException() {}

    public NoAccountException(String message)
    {
       super(message);
    }
}
