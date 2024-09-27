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
import com.minewaku.trilog.service.impl.RateLimitService;
import com.minewaku.trilog.util.LogUtil;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Media", description = "Media API")
@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private RateLimitService rateLimitService;

    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> findById(HttpServletRequest request, @PathVariable int id) {
        Bucket bucket = rateLimitService.redisBucket(request, "RATE_LIMIT_IN_MINUTES", 1000, 1);
        LogUtil.LOGGER.info("Remain Tokens hehee: " + bucket.getAvailableTokens());
        
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if(probe.isConsumed()){
            return ResponseEntity.status(HttpStatus.OK)
                                .body(mediaService.findById(id));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                            .build();
    }

    @PostMapping("/")
    public ResponseEntity<MediaDTO> create(@RequestBody MediaDTO file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(mediaService.create(file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaDTO> update(@PathVariable int id, @RequestBody MediaDTO file) {
        return ResponseEntity.status(HttpStatus.OK)
                            .body(mediaService.update(id, file));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        mediaService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                            .build();
    }
    
}
