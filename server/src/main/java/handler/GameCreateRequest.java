package handler;

public record GameCreateRequest(String authToken, String gameName) {
}
