package com.deepdhamala.filmpatro.auth.userAuthentication;

public interface UserAuthenticationService<R extends AuthRequest, S> {
    /*
     * This interface defines the contract for user authentication services.
     * It provides methods to authenticate a user based on a request and return a response.
     *
     * @param <R> The type of the request object containing authentication details.
     * @param <S> The type of the response object containing authentication results.
     */
    S authenticate(R request);



}
