package com.minewaku.trilog.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.service.impl.MediaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Media", description = "Media API")
@RestController
@RequestMapping("/api/v1/media")
public class MediaController {
	
	/**
	 * ALL AVAILABLE APIS
	 * 
	 * @summary GET /api/v1/media/{id} - @see {@link #findById}
	 * 
	 * @summary POST /api/v1/media - @see {@link #create}
	 * @summary PUT /api/v1/media/{id} - @see {@link #update}
	 * @summary DELETE /api/v1/media/{id} - @see {@link #delete}
	 * 
	 */

    @Autowired
    private MediaService mediaService;


    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> findById(HttpServletRequest request, @PathVariable Integer id) {
    	return ResponseEntity.status(HttpStatus.OK).body(mediaService.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<MediaDTO> create(@RequestBody MediaDTO file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(mediaService.create(file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaDTO> update(@PathVariable Integer id, @RequestBody MediaDTO file) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(mediaService.update(id, file));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        mediaService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                            .build();
    }
    
}
