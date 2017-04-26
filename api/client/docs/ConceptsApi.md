# ConceptsApi

All URIs are relative to *http://knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getConceptDetails**](ConceptsApi.md#getConceptDetails) | **GET** /concepts/{conceptId} | 
[**getConcepts**](ConceptsApi.md#getConcepts) | **GET** /concepts | 


<a name="getConceptDetails"></a>
# **getConceptDetails**
> List&lt;InlineResponse200&gt; getConceptDetails(conceptId)



Retrieves details for a specified concepts in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String conceptId = "conceptId_example"; // String | local object identifier of concept of interest
try {
    List<InlineResponse200> result = apiInstance.getConceptDetails(conceptId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConceptDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conceptId** | **String**| local object identifier of concept of interest |

### Return type

[**List&lt;InlineResponse200&gt;**](InlineResponse200.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConcepts"></a>
# **getConcepts**
> InlineResponse2001 getConcepts(q, sg, pageNumber, pageSize)



Retrieves a (paged) list of concepts in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
List<String> q = Arrays.asList("q_example"); // List<String> | array of keywords or substrings against which to match concept names and synonyms
List<String> sg = Arrays.asList("sg_example"); // List<String> | array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes) 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
try {
    InlineResponse2001 result = apiInstance.getConcepts(q, sg, pageNumber, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConcepts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **q** | [**List&lt;String&gt;**](String.md)| array of keywords or substrings against which to match concept names and synonyms |
 **sg** | [**List&lt;String&gt;**](String.md)| array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes)  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]

### Return type

[**InlineResponse2001**](InlineResponse2001.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json
