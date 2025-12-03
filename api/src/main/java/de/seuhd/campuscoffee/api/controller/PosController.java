package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.openapi.CrudOperation;
import de.seuhd.campuscoffee.api.dtos.PosDto;
import de.seuhd.campuscoffee.api.mapper.DtoMapper;
import de.seuhd.campuscoffee.api.mapper.PosDtoMapper;
import de.seuhd.campuscoffee.domain.model.enums.CampusType;
import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import de.seuhd.campuscoffee.domain.ports.api.PosService;
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
import static de.seuhd.campuscoffee.api.openapi.Resource.OSM_NODE;
import static de.seuhd.campuscoffee.api.openapi.Resource.POS;

/**
 * Controller for handling POS-related API requests.
 */
@Tag(name="Points of Sale (POS)", description="Operations for managing coffee points of sale.")
@Controller
@RequestMapping("/api/pos")
@Slf4j
@RequiredArgsConstructor
public class PosController extends CrudController<Pos, PosDto, Long> {
    private final PosService posService;
    private final PosDtoMapper posDtoMapper;

    @Override
    protected @NonNull CrudService<Pos, Long> service() {
        return posService;
    }

    @Override
    protected @NonNull DtoMapper<Pos, PosDto> mapper() {
        return posDtoMapper;
    }

    @Operation
    @CrudOperation(operation=GET_ALL, resource=POS)
    @GetMapping("")
    public @NonNull ResponseEntity<List<PosDto>> getAll() {
        return super.getAll();
    }

    @Operation
    @CrudOperation(operation=GET_BY_ID, resource=POS)
    @GetMapping("/{id}")
    public @NonNull ResponseEntity<PosDto> getById(
            @Parameter(description="Unique identifier of the POS to retrieve.", required=true)
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Operation
    @CrudOperation(operation=CREATE, resource=POS)
    @PostMapping("")
    public @NonNull ResponseEntity<PosDto> create(
            @Parameter(description="Data of the POS to create.", required=true)
            @RequestBody @Valid PosDto posDto) {
        return super.create(posDto);
    }

    @Operation
    @CrudOperation(operation=UPDATE, resource=POS)
    @PutMapping("/{id}")
    public @NonNull ResponseEntity<PosDto> update(
            @Parameter(description="Unique identifier of the POS to update.", required=true)
            @PathVariable Long id,
            @Parameter(description="Data of the POS to update.", required=true)
            @RequestBody @Valid PosDto posDto) {
        return super.update(id, posDto);
    }

    @Operation
    @CrudOperation(operation=DELETE, resource=POS)
    @DeleteMapping("/{id}")
    public @NonNull ResponseEntity<Void> delete(
            @Parameter(description="Unique identifier of the POS to delete.", required=true)
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Operation
    @CrudOperation(operation=FILTER, resource=POS)
    @GetMapping("/filter")
    public ResponseEntity<PosDto> filter(
            @Parameter(description="Name of the POS to retrieve.", required=true)
            @RequestParam("name") String name) {
        return ResponseEntity.ok(
                posDtoMapper.fromDomain(posService.getByName(name))
        );
    }

    @Operation
    @CrudOperation(operation=IMPORT, resource=POS, externalResource=OSM_NODE)
    @PostMapping("/import/osm/{nodeId}")
    public ResponseEntity<PosDto> importFromOsm(
            @Parameter(description="Unique identifier of the OpenStreetMap node to import.", required=true)
            @PathVariable Long nodeId,
            @Parameter(description="Campus type to assign to the imported POS.", required=true)
            @RequestParam("campus_type") CampusType campusType) {
        PosDto createdPos = posDtoMapper.fromDomain(
                posService.importFromOsmNode(nodeId, campusType)
        );
        return ResponseEntity
                .created(getLocation(createdPos.getId()))
                .body(createdPos);
    }
}
