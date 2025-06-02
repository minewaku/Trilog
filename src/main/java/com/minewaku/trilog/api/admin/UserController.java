package com.minewaku.trilog.api.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.dto.request.RegisterRequest;
import com.minewaku.trilog.facade.UploadFileFacade;
import com.minewaku.trilog.service.impl.RoleService;
import com.minewaku.trilog.service.impl.UserService;
import com.minewaku.trilog.util.DataPreprocessingUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	/**
	 * ALL AVAILABLE APIS
	 * 
	 * @summary GET /api/v1/users - @see {@link #findAll}
	 * @summary GET /api/v1/users/search - @see {@link #search}
	 * @summary GET /api/v1/users/{id} - @see {@link #findbyId}
	 * 
	 * @summary GET /api/v1/users/{id}/image - @see {@link #getImage}
	 * @summary GET /api/v1/users/{id}/cover - @see {@link #getCover}
	 * @summary POST /api/v1/users/{id}/image - @see {@link #uploadImage}
	 * @summary POST /api/v1/users/{id}/cover - @see {@link #uploadCover}
	 * 
	 * @summary GET /api/v1/users/{id}/roles - @see {@link #getRolesByUserId}
	 * @summary POST /api/v1/users/{userId}/roles/{roleIds} - @see {@link #addRolesToUser}
	 * @summary DELETE /api/v1/users/{userId}/roles/{rolesIds}- @see {@link #removeRolesFromUser}
	 * 
	 * @summary POST /api/v1/users - @see {@link #create}
	 * @summary PUT /api/v1/users/{id} - @see {@link #update}
	 * @summary PATCH /api/v1/users/{id} - @see {@link #patch}
	 * @summary DELETE /api/v1/users/{ids} - @see {@link #delete}
	 * 
	 */

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private UploadFileFacade uploadFileFacade;

	@GetMapping("")
	public ResponseEntity<Page<UserDTO>> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable, params));
	}

	@GetMapping("/search")
	public ResponseEntity<Page<UserDTO>> search(@RequestParam Map<String, String> params, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.search(params, pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findbyId(@PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
	}
	
	

	@GetMapping("/{id}/image")
	public ResponseEntity<MediaDTO> getImage(@PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getImage(id));
	}

	@GetMapping("/{id}/cover")
	public ResponseEntity<MediaDTO> getCover(@PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getCover(id));
	}
    
	@PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MediaDTO> uploadImage(@PathVariable int id, @RequestPart("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK).body(uploadFileFacade.uploadUserImage(id, file));
	}

	@PostMapping(path = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MediaDTO> uploadCover(@PathVariable int id, @RequestPart("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK).body(uploadFileFacade.uploadUserCover(id, file));
	}
	

	
    @GetMapping("{id}/roles")
    public ResponseEntity<List<RoleDTO>> getRolesByUserId(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.getRolesByUserId(id)); 
    }
    
//    @PostMapping("{id}/roles")
//    public ResponseEntity<RoleDTO> addRoleByUserId(@PathVariable int id, @RequestBody Map<String, Integer> payload) {
//    	Integer roleId = payload.get("roleId");
//    	return ResponseEntity.ok().body(roleService.addRoleByUserId(id, roleId));
//    }
    
    @PostMapping("{userId}/roles/{roleIds}")
    public ResponseEntity<Void> addRolesToUser(@PathVariable int userId, @PathVariable String roleIds) {
    	List<Integer> roleIdList = DataPreprocessingUtil.parseCommaSeparatedIds(roleIds);
    	roleService.addRolesToUser(userId, roleIdList);
    	return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @DeleteMapping("{userId}/roles/{roleIds}")
	public ResponseEntity<Void> removeRolesFromUser(@PathVariable int userId, @PathVariable String roleIds) {
		List<Integer> roleIdList = DataPreprocessingUtil.parseCommaSeparatedIds(roleIds);
		roleService.removeRolesFromUser(userId, roleIdList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
    

	@PostMapping("")
	public ResponseEntity<UserDTO> create(@RequestBody RegisterRequest user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable int id, @RequestBody UserDTO user) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<UserDTO> patch(@PathVariable int id, @RequestBody UserDTO user) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.patch(id, user));
	}	
	
	@DeleteMapping("/{ids}")
	public ResponseEntity<Void> delete(@PathVariable String ids) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
		userService.delete(idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// @PutMapping("/{id}/image")
	// public ResponseEntity<MediaDTO> updateImage(@PathVariable int id,
	// @RequestPart("file") MultipartFile file) {
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(uploadFileFacade.updateUserImage(id, file));
	// }

	// @PutMapping("/{id}/cover")
	// public ResponseEntity<MediaDTO> updateCover(@PathVariable int id,
	// @RequestPart("file") MultipartFile file) {
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(uploadFileFacade.updateUserCover(id, file));
	// }
}
