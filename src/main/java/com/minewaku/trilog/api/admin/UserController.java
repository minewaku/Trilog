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
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.User.UpdatedUserDTO;
import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.dto.common.request.RegisterRequest;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.facade.UploadFileFacade;
import com.minewaku.trilog.search.service.ESIPostService;
import com.minewaku.trilog.search.service.ESIUserService;
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
	 * @summary GET /api/v1/users/{id}/posts - @see {@link #getPostsByUserId}
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
	 * @summary DELETE /api/v1/users/{ids} - @see {@link #delete}
	 * 
	 */

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
    @Autowired
    private ESIUserService esUserService;
    
    @Autowired
    private ESIPostService esPostService;

	@Autowired
	private UploadFileFacade uploadFileFacade;

	@GetMapping("")
	public ResponseEntity<Page<UserDTO>> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(params, pageable));
	}

	@GetMapping("/search")
	public ResponseEntity<CursorPage<UserDTO>> search(@RequestParam String q, @RequestBody Cursor cursor) {
		return ResponseEntity.status(HttpStatus.OK).body(esUserService.suggestUsers(q, cursor));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findbyId(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
	}
	
	@GetMapping("/{userId}/posts")
	public ResponseEntity<CursorPage<PostDTO>> getPostsByUserId(@PathVariable Integer userId, @RequestBody Cursor cursor) {
		return ResponseEntity.status(HttpStatus.OK).body(esPostService.findByUserId(userId, cursor));
	}

	@GetMapping("/{id}/image")
	public ResponseEntity<MediaDTO> getImage(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getImage(id));
	}

	@GetMapping("/{id}/cover")
	public ResponseEntity<MediaDTO> getCover(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getCover(id));
	}
    
	@PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MediaDTO> uploadImage(@PathVariable Integer id, @RequestPart("files") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK).body(uploadFileFacade.uploadUserImage(id, file));
	}

	@PostMapping(path = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MediaDTO> uploadCover(@PathVariable Integer id, @RequestPart("files") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK).body(uploadFileFacade.uploadUserCover(id, file));
	}
	

	
    @GetMapping("{id}/roles")
    public ResponseEntity<List<String>> getRolesByUserId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.getRolesByUserId(id)); 
    }
    
    @PostMapping("{userId}/roles/{roleIds}")
    public ResponseEntity<Void> addRolesToUser(@PathVariable Integer userId, @PathVariable String roleIds) {
    	List<Integer> roleIdList = DataPreprocessingUtil.parseCommaSeparatedIds(roleIds);
    	roleService.addRolesToUser(userId, roleIdList);
    	return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @DeleteMapping("{userId}/roles/{roleIds}")
	public ResponseEntity<Void> removeRolesFromUser(@PathVariable Integer userId, @PathVariable String roleIds) {
		List<Integer> roleIdList = DataPreprocessingUtil.parseCommaSeparatedIds(roleIds);
		roleService.removeRolesFromUser(userId, roleIdList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
    

	@PostMapping("")
	public ResponseEntity<UserDTO> create(@RequestBody RegisterRequest user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UpdatedUserDTO user) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
	}
	
	@DeleteMapping("/{ids}")
	public ResponseEntity<Void> delete(@PathVariable String ids) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
		userService.delete(idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
