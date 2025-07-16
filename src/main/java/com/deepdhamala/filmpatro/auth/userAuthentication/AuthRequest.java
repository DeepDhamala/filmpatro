package com.deepdhamala.filmpatro.auth.userAuthentication;

/**
 * Marker interface for authentication request DTOs.
 * <p>
 * Implement this interface in classes representing different types of authentication requests,
 * such as username/password, OAuth, or other custom authentication mechanisms.
 * </p>
 *
 * Example usage:
 * <pre>
 * public class UsernamePasswordAuthRequest implements AuthRequest {
 *     private String username;
 *     private String password;
 *     // getters and setters
 * }
 * </pre>
 */
public interface AuthRequest {
}
