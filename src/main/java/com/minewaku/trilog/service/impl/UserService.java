package com.minewaku.trilog.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.exception.UserNotFoundException;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.mapper.RoleMapper;
import com.minewaku.trilog.mapper.UserMapper;
import com.minewaku.trilog.repository.MediaRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IUserService;
import com.minewaku.trilog.specification.UserSpecification;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Override
    public Page<UserDTO> findAll(Map<String, String> params, Pageable pageable) {
        try {
            Specification<User> spec = Specification.where(null);
            Set<String> allowedParams = new HashSet<>(Arrays.asList("name", "email", "phone"));

            if (!params.keySet().stream().allMatch(allowedParams::contains)) {
                throw new IllegalArgumentException();
            }

            if(StringUtils.hasLength(params.get("name"))) {
                spec = spec.and(UserSpecification.hasName(params.get("name")));
            }

            if(StringUtils.hasLength(params.get("email"))) {
                spec = spec.and(UserSpecification.hasEmail(params.get("email")));
            }

            if(StringUtils.hasLength(params.get("phone"))) {
                spec = spec.and(UserSpecification.hasPhone(params.get("phone")));
            }

            Page<User> users = userRepository.findAll(spec, pageable);
            return users.map(user -> userMapper.entityToDto(user));

        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.MESSAGES.getString("invalid.params.search"), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Page<UserDTO> search(Map<String, String> params, Pageable pageable) {
        try {
            Specification<User> spec = Specification.where(null);
            Set<String> allowedParams = new HashSet<>(Arrays.asList("name", "email", "phone"));

            if (!params.keySet().stream().allMatch(allowedParams::contains)) {
                throw new IllegalArgumentException();
            }

            if(StringUtils.hasLength(params.get("name"))) {
                spec = spec.and(UserSpecification.containsName(params.get("name")));
            }

            if(StringUtils.hasLength(params.get("email"))) {
                spec = spec.and(UserSpecification.containsEmail(params.get("email")));
            }

            if(StringUtils.hasLength(params.get("phone"))) {
                spec = spec.and(UserSpecification.containsPhone(params.get("phone")));
            }

            Page<User> users = userRepository.findAll(spec, pageable);
            return users.map(user -> userMapper.entityToDto(user));

        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.MESSAGES.getString("invalid.params.search"), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO findById(int id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.user"))); 
            return userMapper.entityToDto(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO create(UserDTO user) {
        try {
            User savedUser = userRepository.save(userMapper.dtoToEntity(user)); 
            return userMapper.entityToDto(savedUser);
        } catch (Exception e) {
            throw new RuntimeException(MessageUtil.MESSAGES.getString("error.create.user"), e);
        }
    }

    @Override
    public UserDTO update(int id, UserDTO user) {
        try {
            userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.user"))); 
            User savedUser = userRepository.save(userMapper.dtoToEntity(user)); 
            return userMapper.entityToDto(savedUser);
        } catch (Exception e) {
            throw new RuntimeException(MessageUtil.MESSAGES.getString("error.create.user"), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.user"))); 
            userRepository.deleteById(id); 
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public RoleDTO getRole(int id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.role"))); 
            return user.getRole() != null ? roleMapper.entityToDto(user.getRole()) : null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Cacheable(value = "media", keyGenerator = "customKeyGenerator")
    @Override
    public MediaDTO getImage(int userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.media"))); 
            return mediaMapper.entityToDto(user.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Cacheable(value = "media", keyGenerator = "customKeyGenerator")
    @Override
    public MediaDTO getCover(int userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.media"))); 
            return mediaMapper.entityToDto(user.getCover());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @CachePut(value = "media", keyGenerator = "customKeyGenerator")
    @Override
    public MediaDTO updateImage(int userId, int imageId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.media")));
            Media image = mediaRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException(MessageUtil.MESSAGES.getString("error.get.media")));
            user.setImage(image);
            User savedUser = userRepository.save(user);
            return savedUser.getImage() != null ? mediaMapper.entityToDto(savedUser.getImage()) : null;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @CachePut(value = "media", keyGenerator = "customKeyGenerator")
    @Override
    public MediaDTO updateCover(int userId, int coverId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(MessageUtil.MESSAGES.getString("error.get.media")));
            Media image = mediaRepository.findById(coverId).orElseThrow(() -> new EntityNotFoundException(MessageUtil.MESSAGES.getString("error.get.media")));
            user.setCover(image);
            User savedUser = userRepository.save(user);
            return savedUser.getCover() != null ? mediaMapper.entityToDto(savedUser.getCover()) : null;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
