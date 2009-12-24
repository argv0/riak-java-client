package com.basho.riak.client.raw;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basho.riak.client.response.BucketResponse;
import com.basho.riak.client.response.HttpResponse;
import com.basho.riak.client.response.HttpResponseDecorator;
import com.basho.riak.client.util.ClientUtils;
import com.basho.riak.client.util.Constants;

/**
 * Decorates an HttpResponse to interpret listBucket response from Riak's Raw
 * interface which returns a JSON object with the keys "props" and "keys".
 */
public class RawBucketResponse extends HttpResponseDecorator implements BucketResponse {

    private RawBucketInfo bucketInfo = null;

    /**
     * On a 2xx response, parses the JSON response into a {@link RawBucketInfo}.
     * 
     * @param r
     *            The HTTP response from a GET at a Raw bucket
     * @throws JSONException
     *             If the response is a 2xx but contains invalid JSON
     */
    public RawBucketResponse(HttpResponse r) throws JSONException {
        super(r);

        if (r != null && r.isSuccess() && (r.getBody() != null)) {
            JSONObject json = new JSONObject(r.getBody());
            JSONObject props = json.optJSONObject(Constants.RAW_FL_SCHEMA);
            JSONArray jsonKeys = json.optJSONArray(Constants.RAW_FL_KEYS);
            Collection<String> keys = ClientUtils.jsonArrayAsList(jsonKeys);

            bucketInfo = new RawBucketInfo(props, keys);
        }
    }

    public boolean hasBucketInfo() {
        return bucketInfo != null;
    }

    public RawBucketInfo getBucketInfo() {
        return bucketInfo;
    }
}
