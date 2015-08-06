package com.tony.volleydemo.http;

/**
 * 封装解析响应交付
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Jul 29, 2015 9:00:56 AM
 */
public class Response<T> {
	/** Parsed response, or null in the case of error. */
	public final T result;

	/** Cache metadata for this response, or null in the case of error. */
	public final Cache.Entry cacheEntry;

	/** Detailed error information if <code>errorCode != OK</code>. */
	public final VolleyError error;

	// True if this response was a soft-expired one and a second one MAY
	// becoming.
	public boolean intermediate = false;

	private Response(T result, Cache.Entry cacheEntry) {
		this.result = result;
		this.cacheEntry = cacheEntry;
		this.error = null;
	}

	private Response(VolleyError error) {
		this.result = null;
		this.cacheEntry = null;
		this.error = error;
	}

	// Returns a successful response containing the parsed result
	public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
		return new Response<T>(result, cacheEntry);
	}

	/**
	 * Returns a failed response containing the given error code and an optional
	 * localized message displayed to the user.
	 */
	public static <T> Response<T> error(VolleyError error) {
		return new Response<T>(error);
	}

	/**
	 * Returns whether this response is considered successful.
	 */
	public boolean isSuccess() {
		return error == null;
	}

	// Callback interface for delivering parsed responses.
	public interface Listener<T> {
		// Called when a response is received
		public void onResponse(T response);
	}

	// Callback interface for delivering error responses
	public interface ErrorListener {
		/**
		 * Callback method that an error has been occurred with the provided
		 * error code and optional user-readable message.
		 */
		public void onErrorResponse(VolleyError error);
	}
}
