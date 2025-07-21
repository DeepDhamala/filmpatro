package com.deepdhamala.filmpatro.auth.userRegistration;

/**
 * Service interface for registering new users with different types of registration request dtos.
 * @param <T> The type of registration request dto
 */
public interface UserRegistrationService <T>{

    /**
     * Registers a new user based on the provided registration request dto.
     *
     * @param requestDto The registration request dto containing user details.
     */
    void registerNewUser(T requestDto);

    /**
     * Returns the class type of the supported registration request DTO.
     * This is used to determine which implementation supports which DTO type.
     *
     * @return the Class object of the supported registration request DTO type
     */
    Class<T> getSupportedDtoType();
}
