package com.minewaku.trilog.api.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.facade.UploadFileFacade;
import com.minewaku.trilog.service.impl.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UploadFileFacade uploadFileFacade;

    @GetMapping("")
    public ResponseEntity<Page<UserDTO>> findAll(HttpServletRequest request, @RequestParam Map<String, String> params, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.findAll(params, pageable)); 
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> search(@RequestParam Map<String, String> params, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.search(params, pageable)); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findbyId(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.findById(id)); 
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<MediaDTO> getImage(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.getImage(id)); 
    }

    @GetMapping("/{id}/cover")
    public ResponseEntity<MediaDTO> getCover(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.getCover(id)); 
    }
    

    @GetMapping("{id}/roles")
    public ResponseEntity<RoleDTO> findRoleByUserId(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.getRole(id)); 
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(userService.create(user)); 
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<MediaDTO> uploadImage(@PathVariable int id, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(uploadFileFacade.uploadUserImage(id, file)); 
    }

    @PostMapping("/{id}/cover")
    public ResponseEntity<MediaDTO> uploadCover(@PathVariable int id, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(uploadFileFacade.uploadUserCover(id, file)); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable int id, @RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(userService.update(id, user)); 
    }

    // @PutMapping("/{id}/image")
    // public ResponseEntity<MediaDTO> updateImage(@PathVariable int id, @RequestPart("file") MultipartFile file) {
    //     return ResponseEntity.status(HttpStatus.OK)
    //                         .body(uploadFileFacade.updateUserImage(id, file)); 
    // }

    // @PutMapping("/{id}/cover")
    // public ResponseEntity<MediaDTO> updateCover(@PathVariable int id, @RequestPart("file") MultipartFile file) {
    //     return ResponseEntity.status(HttpStatus.OK)
    //                         .body(uploadFileFacade.updateUserCover(id, file)); 
    // }
}
