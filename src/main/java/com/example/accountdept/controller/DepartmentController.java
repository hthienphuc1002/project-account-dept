package com.example.accountdept.controller;

import com.example.accountdept.dto.*;
import com.example.accountdept.entity.DepartmentType;
import com.example.accountdept.service.DepartmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService service;

    @GetMapping
    public Page<DepartmentDTO> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) DepartmentType type,
            @RequestParam(required = false) Instant createdFrom,
            @RequestParam(required = false) Instant createdTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        Sort s = Sort.by(Sort.Direction.DESC, "createdDate");
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                s = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
            } else {
                s = Sort.by(sort);
            }
        }
        Pageable pageable = PageRequest.of(page, size, s);
        return service.list(search, type, createdFrom, createdTo, pageable);
    }

    @GetMapping("/{id}")
    public DepartmentDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> create(@Valid @RequestBody DepartmentCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> update(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
