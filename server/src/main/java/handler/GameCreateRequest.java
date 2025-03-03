package handler;

import service.UserService;

public record GameCreateRequest(String authToken, String gameName) {
}
