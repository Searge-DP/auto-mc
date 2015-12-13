package com.wildbamaboy.beam.automc.util;

/**
 * Used to indicate the result of a method call. Is NOT thread-safe.
 */
public class CallResult 
{
	/** Base result object reused on each call to create(). */
	private static CallResult result;
	
	private boolean successful;
	private String message;
	
	private CallResult() { }
	
	private static CallResult create(boolean successful, String message)
	{
		result.successful = successful;
		result.message = message;
		return result;
	}

	public static CallResult False(String message)
	{
		return create(false, message);
	}
	
	public static CallResult True()
	{
		return create(true, "");
	}
	
	public boolean getSuccessful()
	{
		return successful;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	@Override
	public String toString() 
	{
		return String.valueOf(successful) + ":" + message;
	}

	static
	{
		result = new CallResult();
	}
}
