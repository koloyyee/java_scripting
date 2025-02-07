
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.alibaba.fastjson2:fastjson2:2.0.54

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

/**
 * HttpClient, HttpResponse, and HttpRequest
 * 
 * https://openjdk.org/groups/net/httpclient/recipes.html
 */
public class GetCheese {

    HttpClient client;
    HttpRequest.Builder reqBuilder;

    public static void main(String... args) {
        var cheese = new GetCheese();
        var cheeses = cheese.getAll().join();
        cheeses.forEach(System.out::println);
        var morbier = cheese.getByName("morbier").join();
        System.out.println(morbier);
        var names = cheese.getAllNames().join();
        System.out.println(names);
    }

    public GetCheese() {
        this.client = HttpClient.newHttpClient();
        this.reqBuilder = HttpRequest.newBuilder();

    }

    CompletableFuture<List<String>> getAllNames() {
        String url = "https://cheese-api.onrender.com/cheeses/";
        var request = reqBuilder
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    List<Cheese> cheeses = JSON.parseObject(json, new TypeReference<List<Cheese>>() {
                    });
                    return cheeses.stream().map(Cheese::name).toList();
                });
    }

    CompletableFuture<List<Cheese>> getAll() {

        String url = "https://cheese-api.onrender.com/cheeses/";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> JSON.parseObject(json, new TypeReference<List<Cheese>>() {
        }));
    }

    CompletableFuture<Cheese> getByName(String name) {
        String url = "https://cheese-api.onrender.com/cheese/" + name;
        var request = reqBuilder
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    System.out.println("Raw Json: " + json);
                    var cheese = JSON.parseObject(json, Cheese.class);
                    return cheese;
                });
    }
}

record Cheese(String name, String milk, String image, String description, String raw_Description) {

}
