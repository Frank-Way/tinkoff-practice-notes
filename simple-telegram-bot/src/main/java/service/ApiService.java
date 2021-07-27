package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import model.ServiceUser;
import model.Note;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;


public class ApiService {
    private static final String baseURL = "http://localhost:8080/users";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static ServiceUser getUserById(Long id) throws IOException, InterruptedException {
        String url = baseURL + "/" + id.toString();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        var response = client.send(request, new JsonBodyHandler<>(ServiceUser.class));
        return response.body().get();
    }

    public static ServiceUser getUserByLogin(String login) throws IOException, InterruptedException {
        String url = baseURL + "?login=" + login;
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        var response = client.send(request, new JsonBodyHandler<>(ServiceUser.class));
        return response.body().get();
    }

    public static void createUser(ServiceUser user) throws IOException, InterruptedException {
        String url = baseURL + "/";
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(user)))
                .build();
        var response = client.send(request, new JsonBodyHandler<>(ServiceUser.class));
    }

    public static List<Note> getNotesByUserId(Long user_id, Long pageNumber, Long pageSize) throws IOException, InterruptedException {
        String url = baseURL + "/" + user_id.toString() + "/notes?page=" + pageNumber.toString() +
                               "&size=" + pageSize.toString() + "&sort=updatedAt,desc";
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body().toString());
        String json = jsonNode.findValue("content").toString();
        Note[] na = objectMapper.readValue(json, Note[].class);
        List<Note> ln = Arrays.asList(objectMapper.readValue(json, Note[].class));
        return ln;
    }

    public static Note getNoteById(Long user_id, Long id) throws IOException, InterruptedException {
        String url = baseURL + "/" + user_id.toString() + "/notes/" + id.toString();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        var response = client.send(request, new JsonBodyHandler<>(Note.class));
        return response.body().get();
    }

    public static void createNote(Long user_id, Note note) throws IOException, InterruptedException {
        String url = baseURL + "/" + user_id.toString() + "/notes";
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(note)))
                .build();
        var response = client.send(request, new JsonBodyHandler<>(Note.class));
    }

    public static Note updateNote(Long user_id, Note note) throws IOException, InterruptedException {
        String url = baseURL + "/" + user_id.toString() + "/notes/" + note.getId().toString();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(note)))
                .build();
        var response = client.send(request, new JsonBodyHandler<>(Note.class));
        return response.body().get();
    }

    public static void deleteNote(Long user_id, Long id) throws IOException, InterruptedException {
        String url = baseURL + "/" + user_id.toString() + "/notes/" + id.toString();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .DELETE()
                .build();
        var response = client.send(request, new JsonBodyHandler<>(Note.class));
    }
}