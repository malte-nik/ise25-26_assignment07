package de.seuhd.campuscoffee.domain.implementation;

import de.seuhd.campuscoffee.domain.model.objects.User;
import de.seuhd.campuscoffee.domain.ports.data.CrudDataService;
import de.seuhd.campuscoffee.domain.ports.data.UserDataService;
import de.seuhd.campuscoffee.domain.ports.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

/**
 * Implementation of the User service that handles business logic related to user entities.
 */
@Slf4j
@Service
public class UserServiceImpl extends CrudServiceImpl<User, Long> implements UserService {
    private final UserDataService userDataService;

    public UserServiceImpl(@NonNull UserDataService userDataService) {
        super(User.class);
        this.userDataService = userDataService;
    }

    @Override
    protected CrudDataService<User, Long> dataService() {
        return userDataService;
    }

    @Override
    public @NonNull User getByLoginName(@NonNull String loginName) {
        log.debug("Retrieving user with login name: {}", loginName);
        return userDataService.getByLoginName(loginName);
    }
}
