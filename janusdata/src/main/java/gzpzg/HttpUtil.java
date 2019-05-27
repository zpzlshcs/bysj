package gzpzg;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private OkHttpClient client;
	public HttpUtil() {
		client = new OkHttpClient();
	}
	
	public String get(String url) throws IOException {
	    Request request = new Request.Builder().url(url).build();
	    Response response = client.newCall(request).execute();
	    if (response.isSuccessful()) {
	        return response.body().string();
	    } else {
	        throw new IOException("Unexpected code " + response);
	    }
	}
	public String post(String url, String json) throws IOException {
	    RequestBody body = RequestBody.create(JSON, json);
	    Request request = new Request.Builder()
	      .url(url)
	      .post(body)
	      .build();
	    Response response = client.newCall(request).execute();
	    if (response.isSuccessful()) {
	        return response.body().string();
	    } else {
	        throw new IOException("Unexpected code " + response);
	    }
	}
	
	/**
	 * get with headers
	 * @param url
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public String get(String url,Map<String, String> headers) throws IOException {
		Request.Builder builder = new Request.Builder().url(url);
		if(headers!=null) {
			for(String name:headers.keySet()) {
				builder.header(name, headers.get(name));
			}
		}
	    Request request = builder.build();
	    Response response = client.newCall(request).execute();
	    if (response.isSuccessful()) {
	        return response.body().string();
	    } else {
	        throw new IOException("Unexpected code " + response);
	    }
	}

	/**
	 * post with headers and params
	 * @param url
	 * @param json
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public String post(String url, String json,Map<String, String> headers) throws IOException {
	    RequestBody body = RequestBody.create(JSON, json);
		Request.Builder builder = new Request.Builder().url(url).post(body);
		if(headers!=null) {
			for(String name:headers.keySet()) {
				builder.header(name, headers.get(name));
			}
		}
	    Request request = builder.build();
	    Response response = client.newCall(request).execute();
	    if (response.isSuccessful()) {
	        return response.body().string();
	    } else {
	        throw new IOException("Unexpected code " + response);
	    }
	}
}
