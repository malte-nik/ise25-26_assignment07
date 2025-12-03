package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.openapi.CrudOperation;
import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.api.mapper.DtoMapper;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.model.objects.User;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import de.seuhd.campuscoffee.domain.ports.api.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.seuhd.campuscoffee.api.openapi.Operation.*;
import static de.seuhd.campuscoffee.api.openapi.Resource.USER;

@Tag(name="Users", description="Operations related to user management.")
@Controller
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController extends CrudController<User, UserDto, Long> {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Override
    protected @NonNull CrudService<User, Long> service() {
        return userService;
    }

    @Override
    protected @NonNull DtoMapper<User, UserDto> mapper() {
        return userDtoMapper;
    }

    @Operation
    @CrudOperation(operation=GET_ALL, resource=USER)
    @GetMapping("")
    public @NonNull ResponseEntity<List<UserDto>> getAll() {
        return super.getAll();
    }

    @Operation
    @CrudOperation(operation=GET_BY_ID, resource=USER)
    @GetMapping("/{id}")
    public @NonNull ResponseEntity<UserDto> getById(
            @Parameter(description="Unique identifier of the user to retrieve.", required=true)
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Operation
    @CrudOperation(operation=CREATE, resource=USER)
    @PostMapping("")
    public @NonNull ResponseEntity<UserDto> create(
            @Parameter(description="Data of the user to create.", required=true)
            @RequestBody @Valid UserDto userDto) {
        return super.create(userDto);
    }

    @Operation
    @CrudOperation(operation=UPDATE, resource=USER)
    @PutMapping("/{id}")
    public @NonNull ResponseEntity<UserDto> update(
            @Parameter(description="Unique identifier of the user to update.", required=true)
            @PathVariable Long id,
            @Parameter(description="Data of the user to update.", required=true)
            @RequestBody @Valid UserDto userDto) {
        return super.update(id, userDto);
    }

    @Operation
    @CrudOperation(operation=DELETE, resource=USER)
    @DeleteMapping("/{id}")
    public @NonNull ResponseEntity<Void> delete(
            @Parameter(description="Unique identifier of the user to delete.", required=true)
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Operation
    @CrudOperation(operation=FILTER, resource=USER)
    @GetMapping("/filter")
    public ResponseEntity<UserDto> filter(
            @Parameter(description="Login name of the user to retrieve.", required=true)
            @RequestParam("login_name") String loginName) {
        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getByLoginName(loginName))
        );
    }
}
