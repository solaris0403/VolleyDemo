package com.tony.volleydemo.http.tool;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.tony.volleydemo.http.core.Network;
import com.tony.volleydemo.http.core.RequestQueue;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 2:19:23 PM
 */
public class Volley {
	/** Default on-disk cache directory. */
	private static final String DEFAULT_CACHE_DIR = "volley";

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it. You may set a maximum size of the
	 * disk cache in bytes.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param stack
	 *            An {@link HttpStack} to use for the network, or null for
	 *            default.
	 * @param maxDiskCacheBytes
	 *            the maximum size of the disk cache, in bytes. Use -1 for
	 *            default size.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, HttpStack stack, int maxDiskCacheBytes) {
		File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}
		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				// Prior to Gingerbread, HttpUrlConnection was unreliable.
				// See:http://android-developers.blogspot.com/2011/09/androids-http-clients.html
				stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
			}
		}
		Network network = new BasicNetwork(stack);

		RequestQueue queue;
		if (maxDiskCacheBytes <= -1) {
			// No maximum size specified
			queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
		} else {
			// Disk cache size specified
			queue = new RequestQueue(new DiskBasedCache(cacheDir, maxDiskCacheBytes), network);
		}
		queue.start();
		return queue;
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it. You may set a maximum size of the
	 * disk cache in bytes.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param maxDiskCacheBytes
	 *            the maximum size of the disk cache, in bytes. Use -1 for
	 *            default size.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, int maxDiskCacheBytes) {
		return newRequestQueue(context, null, maxDiskCacheBytes);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param stack
	 *            An {@link HttpStack} to use for the network, or null for
	 *            default.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
		return newRequestQueue(context, stack, -1);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context) {
		return newRequestQueue(context, null);
	}
}
