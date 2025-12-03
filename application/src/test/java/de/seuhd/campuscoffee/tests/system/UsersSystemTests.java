package de.seuhd.campuscoffee.tests.system;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.objects.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;
import static de.seuhd.campuscoffee.tests.SystemTestUtils.assertEqualsIgnoringIdAndTimestamps;
import static de.seuhd.campuscoffee.tests.SystemTestUtils.assertEqualsIgnoringTimestamps;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * System tests for the operations related to Users.
 */
public class UsersSystemTests extends AbstractSysTest {

    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserFixturesForInsertion().getFirst();
        User createdUser = userDtoMapper.toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

        assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
    }

    @Test
    void createUser_invalidLoginName() {
        User invalidUserToCreate = TestFixtures.getUserFixturesForInsertion().getFirst().toBuilder().loginName("-").build();
        int statusCode = userRequests.createAndReturnStatusCodes(List.of(userDtoMapper.fromDomain(invalidUserToCreate))).getFirst();
        assertThat(statusCode)
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAllCreatedUsers() {
        List<User> createdUserList = TestFixtures.createUserFixtures(userService);

        List<User> retrievedUsers = userRequests.retrieveAll()
                .stream()
                .map(userDtoMapper::toDomain)
                .toList();

        assertEqualsIgnoringTimestamps(retrievedUsers, createdUserList);
    }

    @Test
    void getUserById() {
        List<User> createdUserList = TestFixtures.createUserFixtures(userService);
        User createdUser = createdUserList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(
                userRequests.retrieveById(createdUser.id())
        );

        assertEqualsIgnoringTimestamps(retrievedUser, createdUser);
    }

    @Test
    void filterUserByLoginName() {
        List<User> createdUsers = TestFixtures.createUserFixtures(userService);
        User createdUser = createdUsers.getFirst();
        String loginName = createdUser.loginName();
        User filteredUser = userDtoMapper.toDomain(userRequests.retrieveByFilter("login_name", loginName));

        assertEqualsIgnoringTimestamps(filteredUser, createdUser);
    }

    @Test
    void updateUser() {
        List<User> createdUserList = TestFixtures.createUserFixtures(userService);
        User userToUpdate = createdUserList.getFirst();

        // update fields
        userToUpdate = userToUpdate.toBuilder()
                .loginName(userToUpdate.loginName() + "_updated")
                .emailAddress("updated." + userToUpdate.emailAddress())
                .build();

        User updatedUser = userDtoMapper.toDomain(userRequests.update(List.of(userDtoMapper.fromDomain(userToUpdate))).getFirst());

        assertEqualsIgnoringTimestamps(updatedUser, userToUpdate);

        // verify changes persist
        User retrievedUser = userDtoMapper.toDomain(userRequests.retrieveById(userToUpdate.id()));

        assertEqualsIgnoringTimestamps(retrievedUser, userToUpdate);
    }

    @Test
    void deleteUser() {
        List<User> createdUserList = TestFixtures.createUserFixtures(userService);
        User userToDelete = createdUserList.getFirst();
        Objects.requireNonNull(userToDelete.id());

        List<Integer> statusCodes = userRequests.deleteAndReturnStatusCodes(List.of(userToDelete.id(), userToDelete.id()));

        // the first deletion should return 204 No Content, the second should return 404 Not Found
        assertThat(statusCodes)
                .containsExactly(HttpStatus.NO_CONTENT.value(), HttpStatus.NOT_FOUND.value());

        List<Long> remainingUserIds = userRequests.retrieveAll()
                .stream()
                .map(UserDto::getId)
                .toList();
        assertThat(remainingUserIds)
                .doesNotContain(userToDelete.id());
    }
}