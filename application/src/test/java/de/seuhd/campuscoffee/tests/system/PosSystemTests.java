package de.seuhd.campuscoffee.tests.system;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.posRequests;
import static de.seuhd.campuscoffee.tests.SystemTestUtils.assertEqualsIgnoringIdAndTimestamps;
import static de.seuhd.campuscoffee.tests.SystemTestUtils.assertEqualsIgnoringTimestamps;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * System tests for the operations related to POS (Point of Sale).
 */
public class PosSystemTests extends AbstractSysTest {

    @Test
    void createPos() {
        Pos posToCreate = TestFixtures.getPosFixturesForInsertion().getFirst();
        Pos createdPos = posDtoMapper.toDomain(posRequests.create(List.of(posDtoMapper.fromDomain(posToCreate))).getFirst());

        assertEqualsIgnoringIdAndTimestamps(createdPos, posToCreate);
    }

    @Test
    void getAllCreatedPos() {
        List<Pos> createdPosList = TestFixtures.createPosFixtures(posService);

        List<Pos> retrievedPos = posRequests.retrieveAll()
                .stream()
                .map(posDtoMapper::toDomain)
                .toList();

        assertEqualsIgnoringTimestamps(retrievedPos, createdPosList);
    }

    @Test
    void getPosById() {
        List<Pos> createdPosList = TestFixtures.createPosFixtures(posService);
        Pos createdPos = createdPosList.getFirst();

        Pos retrievedPos = posDtoMapper.toDomain(
                posRequests.retrieveById(createdPos.id())
        );

        assertEqualsIgnoringTimestamps(retrievedPos, createdPos);
    }

    @Test
    void filterPosByName() {
        List<Pos> createdPosList = TestFixtures.createPosFixtures(posService);
        Pos createdPos = createdPosList.getFirst();
        String posName = createdPos.name();
        Pos filteredPos = posDtoMapper.toDomain(posRequests.retrieveByFilter("name", posName));

        assertEqualsIgnoringTimestamps(filteredPos, createdPos);
    }

    @Test
    void updatePos() {
        List<Pos> createdPosList = TestFixtures.createPosFixtures(posService);
        Pos posToUpdate = createdPosList.getFirst();

        // update fields using toBuilder() pattern (records are immutable)
        posToUpdate = posToUpdate.toBuilder()
                .name(posToUpdate.name() + " (Updated)")
                .description("Updated description")
                .build();

        Pos updatedPos = posDtoMapper.toDomain(posRequests.update(List.of(posDtoMapper.fromDomain(posToUpdate))).getFirst());

        assertEqualsIgnoringTimestamps(updatedPos, posToUpdate);

        // verify changes persist
        Pos retrievedPos = posDtoMapper.toDomain(posRequests.retrieveById(posToUpdate.id()));

        assertEqualsIgnoringTimestamps(retrievedPos, posToUpdate);
    }

    @Test
    void deletePos() {
        List<Pos> createdPosList = TestFixtures.createPosFixtures(posService);
        Pos posToDelete = createdPosList.getFirst();
        Objects.requireNonNull(posToDelete.id());

        List<Integer> statusCodes = posRequests.deleteAndReturnStatusCodes(List.of(posToDelete.id(), posToDelete.id()));

        // the first deletion should return 204 No Content, the second should return 404 Not Found
        assertThat(statusCodes)
                .containsExactly(HttpStatus.NO_CONTENT.value(), HttpStatus.NOT_FOUND.value());

        List<Long> remainingPosIds = posRequests.retrieveAll()
                .stream()
                .map(PosDto::getId)
                .toList();
        assertThat(remainingPosIds)
                .doesNotContain(posToDelete.id());
    }
}
