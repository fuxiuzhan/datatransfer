package com.fxz.channelswitcher;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.mortbay.util.ajax.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EsTest {
    static final String INDEX_NAME = "person_info";
    static final String ES_HOST = "192.168.1.201";
    static final int ES_PORT = 9200;

    public static void main(String[] args) throws IOException {
        //search();
        update();
        getEsClient().close();
    }

    static RestHighLevelClient getEsClient() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ES_HOST, ES_PORT, "http")));
        return client;
    }

    static void update() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("testdb", "8");
        Map<String, String> map = new HashMap<>();
        map.put("address", "address-updated");
        updateRequest.doc(JSON.toString(map), XContentType.JSON);
        UpdateResponse update = getEsClient().update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toString(update));
    }

    static void search() throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("address", "上海")).must(QueryBuilders.termQuery("gender.keyword", "F"));
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("birthday.keyword", "199*"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("address");
        highlightBuilder.preTags("<strong>");
        highlightBuilder.postTags("</strong>");
        builder.highlighter(highlightBuilder);
        builder.query(boolQueryBuilder);
        request.source(builder);
        SearchResponse searchResponse = getEsClient().search(request, RequestOptions.DEFAULT);
        if (searchResponse.getHits().getHits().length > 0) {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                System.out.println(hit.getSourceAsString());
                System.out.println(hit.getHighlightFields().get("address").getFragments()[0].string());
            }
        }
    }
}
