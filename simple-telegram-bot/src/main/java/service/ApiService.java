package service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import exception.ResourceNotFoundException;
import model.ServiceUser;
import model.Note;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;


public class ApiService {
    private static final String baseURL = "http://localhost:8080/users";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static ServiceUser getUserByLogin(String login) {
        String url = baseURL + "?login=" + login;
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        HttpResponse<Supplier<ServiceUser>> response = null;
        try {
            response = client.send(request, new JsonBodyHandler<>(ServiceUser.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (response.statusCode() == HttpStatus.SC_OK)
            return response.body().get();
        else
            throw new ResourceNotFoundException();
    }

    public static void createUser(ServiceUser user) {
        String url = baseURL + "/";
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder(
                    URI.create(url))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(user)))
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            client.send(request, new JsonBodyHandler<>(ServiceUser.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<Note> getNotesByUserId(Long user_id, Long pageNumber, Long pageSize) {
        String url = baseURL + "/" + user_id.toString() + "/notes?page=" + pageNumber.toString() +
                               "&size=" + pageSize.toString() + "&sort=updatedAt,desc";
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String json = jsonNode.findValue("content").toString();
        List<Note> ln = null;
        try {
            ln = Arrays.asList(objectMapper.readValue(json, Note[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ln;
    }

    public static Note getNoteById(Long user_id, Long id) {
        String url = baseURL + "/" + user_id.toString() + "/notes/" + id.toString();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .GET()
                .build();
        HttpResponse<Supplier<Note>> response = null;
        try {
            response = client.send(request, new JsonBodyHandler<>(Note.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (response.statusCode() == HttpStatus.SC_OK)
            return response.body().get();
        else
            throw new ResourceNotFoundException();
    }

    public static void createNote(Long user_id, Note note) {
        String url = baseURL + "/" + user_id.toString() + "/notes";
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder(
                    URI.create(url))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(note)))
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            client.send(request, new JsonBodyHandler<>(Note.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Note updateNote(Long user_id, Note note){
        String url = baseURL + "/" + user_id.toString() + "/notes/" + note.getId().toString();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder(
                    URI.create(url))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(note)))
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpResponse<Supplier<Note>> response = null;
        try {
            response = client.send(request, new JsonBodyHandler<>(Note.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (response.statusCode() == HttpStatus.SC_OK)
            return response.body().get();
        else
            throw new ResourceNotFoundException();
    }

    public static void deleteNote(Long user_id, Long id) {
        String url = baseURL + "/" + user_id.toString() + "/notes/" + id.toString();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .DELETE()
                .build();
        HttpResponse<Supplier<Note>> response = null;
        try {
            response = client.send(request, new JsonBodyHandler<>(Note.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (response.statusCode() != HttpStatus.SC_OK)
            throw new ResourceNotFoundException();
    }
}