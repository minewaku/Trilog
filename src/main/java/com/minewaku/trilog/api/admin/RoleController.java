package com.minewaku.trilog.api.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.service.impl.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Role", description = "Role API")
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleDTO>> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(roleService.findAll(params, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RoleDTO>> searchByTerm(@RequestParam Map<String, String> params, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(roleService.search(params, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(roleService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO role) {
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(roleService.create(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable int id, @RequestBody RoleDTO role) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(roleService.update(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        roleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                            .build();
    }
    
}
