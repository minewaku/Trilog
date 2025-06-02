package com.minewaku.trilog.api.admin;

import java.util.List;
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

import com.minewaku.trilog.dto.PermissionDTO;
import com.minewaku.trilog.service.impl.PermissionService;
import com.minewaku.trilog.util.DataPreprocessingUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Permission", description = "Permission API")
@RestController
@RequestMapping("/api/v1/permissisons")
public class PermissionController {
	
	/**
	 * ALL AVAILABLE APIS
	 * 
	 * @summary GET /api/v1/permissisons - @see {@link #findAll}
	 * @summary GET /api/v1/permissisons/search - @see {@link #search}
	 * @summary GET /api/v1/permissisons/{id} - @see {@link #findById}
	 * 
	 * @summary POST /api/v1/permissisons - @see {@link #create}
	 * @summary PUT /api/v1/permissisons/{id} - @see {@link #update}
	 * @summary DELETE /api/v1/permissisons/{ids} - @see {@link #delete}
	 * 
	 */
	
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<Page<PermissionDTO>> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        Page<PermissionDTO> permissions = permissionService.findAll(params, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                            .body(permissions); 
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PermissionDTO>> search(@RequestParam Map<String, String> params, Pageable pageable) {
            Page<PermissionDTO> permissions = permissionService.search(params, pageable);
            return ResponseEntity.status(HttpStatus.OK)
                                .body(permissions);
    }   

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> findById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(permissionService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO permission) {
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(permissionService.create(permission));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> update(@PathVariable int id, @RequestBody PermissionDTO permission) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(permissionService.update(id, permission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String ids) {
    	List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
    	permissionService.delete(idList);
        return ResponseEntity.status(HttpStatus.OK)
                            .build();
    }
}
