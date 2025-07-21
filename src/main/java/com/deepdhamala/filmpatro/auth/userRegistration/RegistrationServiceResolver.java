package com.deepdhamala.filmpatro.auth.userRegistration;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RegistrationServiceResolver {

    private final Map<Class<?>, UserRegistrationService<?>> registrationServicesMap;

    public RegistrationServiceResolver(List<UserRegistrationService<?>> services) {
        this.registrationServicesMap = services.stream()
                .collect(Collectors.toMap(UserRegistrationService::getSupportedDtoType, s -> s));
    }

    @SuppressWarnings("unchecked")
    public <T> UserRegistrationService<T> resolve(Class<T> dtoType) {
        return (UserRegistrationService<T>) registrationServicesMap.get(dtoType);
    }

}
