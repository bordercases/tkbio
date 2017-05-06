/*
 * Translator Knowledge Beacon API
 * This is the Translator Knowledge Beacon web service application programming interface (API).  This OpenAPI (\"Swagger\") document may be used as the input specification into a tool like [Swagger-Codegen](https://github.com/swagger-api/swagger-codegen/blob/master/README.md) to generate client and server code stubs implementing the API, in any one of several supported computer languages and frameworks. In order to customize usage to your web site, you should change the 'host' directive below to your hostname. 
 *
 * OpenAPI spec version: 1.0.11
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.api;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.ApiResponse;
import bio.knowledge.client.Configuration;
import bio.knowledge.client.Pair;
import bio.knowledge.client.ProgressRequestBody;
import bio.knowledge.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import bio.knowledge.client.model.InlineResponse2003;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvidenceApi {
    private ApiClient apiClient;

    public EvidenceApi() {
        this(Configuration.getDefaultApiClient());
    }

    public EvidenceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for getEvidence */
    private com.squareup.okhttp.Call getEvidenceCall(String statementId, String keywords, Integer pageNumber, Integer pageSize, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/evidence/{statementId}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "statementId" + "\\}", apiClient.escapeString(statementId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (keywords != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "keywords", keywords));
        if (pageNumber != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageNumber", pageNumber));
        if (pageSize != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageSize", pageSize));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getEvidenceValidateBeforeCall(String statementId, String keywords, Integer pageNumber, Integer pageSize, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'statementId' is set
        if (statementId == null) {
            throw new ApiException("Missing the required parameter 'statementId' when calling getEvidence(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getEvidenceCall(statementId, keywords, pageNumber, pageSize, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     * @param statementId (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought  (required)
     * @param keywords (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of cited references per page to be returned in a paged set of query results  (optional)
     * @return List&lt;InlineResponse2003&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<InlineResponse2003> getEvidence(String statementId, String keywords, Integer pageNumber, Integer pageSize) throws ApiException {
        ApiResponse<List<InlineResponse2003>> resp = getEvidenceWithHttpInfo(statementId, keywords, pageNumber, pageSize);
        return resp.getData();
    }

    /**
     * 
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     * @param statementId (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought  (required)
     * @param keywords (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of cited references per page to be returned in a paged set of query results  (optional)
     * @return ApiResponse&lt;List&lt;InlineResponse2003&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<InlineResponse2003>> getEvidenceWithHttpInfo(String statementId, String keywords, Integer pageNumber, Integer pageSize) throws ApiException {
        com.squareup.okhttp.Call call = getEvidenceValidateBeforeCall(statementId, keywords, pageNumber, pageSize, null, null);
        Type localVarReturnType = new TypeToken<List<InlineResponse2003>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     * @param statementId (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought  (required)
     * @param keywords (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of cited references per page to be returned in a paged set of query results  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEvidenceAsync(String statementId, String keywords, Integer pageNumber, Integer pageSize, final ApiCallback<List<InlineResponse2003>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getEvidenceValidateBeforeCall(statementId, keywords, pageNumber, pageSize, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<InlineResponse2003>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
