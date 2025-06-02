package com.minewaku.trilog.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.dto.request.RegisterRequest;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.Role;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.User_;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.mapper.RoleMapper;
import com.minewaku.trilog.mapper.UserMapper;
import com.minewaku.trilog.repository.MediaRepository;
import com.minewaku.trilog.repository.RoleRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IUserService;
import com.minewaku.trilog.specification.SpecificationBuilder;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.metamodel.SingularAttribute;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
    
    @Autowired
    private MediaRepository mediaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper mapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private SpecificationBuilder<User> specBuilder;
    
    @Autowired
    private ErrorUtil errorUtil;
    
    private Set<SingularAttribute<User, ?> > allowedFieldsForFetch = new HashSet<>();
    private Set<SingularAttribute<User, ?> > allowedFieldsForSearch = new HashSet<>();

    @PostConstruct
    public void init() {
        allowedFieldsForFetch.add(User_.name);
        allowedFieldsForFetch.add(User_.email);
        allowedFieldsForFetch.add(User_.birthdate);
        @SuppressWarnings("unchecked")
        SingularAttribute<User, ?> createdDate = (SingularAttribute<User, ?>) (SingularAttribute<?, ?>) User_.createdDate;
        allowedFieldsForFetch.add(createdDate);
    }
    
    @Override
    public Page<UserDTO> findAll(Pageable pageable, Map<String, String> params) {
        try {
        	Specification<User> spec = specBuilder.buildSpecification(params, allowedFieldsForFetch);
            Page<User> users = userRepository.findAll(spec, pageable);
            Page<UserDTO> userDTOs = users.map(user -> userMapper.entityToDto(user));
            return userDTOs;

        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage("invalid.parameters"), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Page<UserDTO> search(Map<String, String> params, Pageable pageable) {
        try {
//        	Specification<User> spec = specBuilder.buildSpecification(params);
            Page<User> users = userRepository.findAll(pageable);
            return users.map(user -> userMapper.entityToDto(user));

        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage("invalid.params.search"), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO findById(int id) {
    	
        try {
            User user = userRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            return userMapper.entityToDto(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
	public List<RoleDTO> getRolesByUserId(int userId) {
		try {
            return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.user")))
            		.getRoles()
            		.stream()
            		.map(role -> mapper.entityToDto(role))
            		.toList();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}



    @Override
    public UserDTO create(RegisterRequest request) {
        try {
        	userRepository.findByEmail(request.getEmail()).ifPresent(s -> {
    			throw errorUtil.ERROR_DETAILS.get(errorUtil.EMAIL_ALREADY_EXISTS);
    		});

    		Role userRole = roleRepository.findByName("USER").orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.ROLE_NOT_FOUND));

    		// Create the roles list
    		Set<Role> defaultRoles = new HashSet<>();
    		defaultRoles.add(userRole);

    		// Create and save the user with explicit field setting
    		User user = User.builder().email(request.getEmail()).roles(defaultRoles).name(request.getName()).hashed_password(passwordEncoder.encode(request.getPassword())).birthdate(request.getBirthdate()).phone(request.getPhone()).address(request.getAddress()).isActive(true).isEnabled(true).isLocked(false)
    				.isDeleted(false).build();
    		user = userRepository.save(user);
            return userMapper.entityToDto(user);
        } catch (Exception e) {
        	throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO update(int id, UserDTO user) {
        try {
            userRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            User savedUser = userRepository.save(userMapper.dtoToEntity(user)); 
            return userMapper.entityToDto(savedUser);
        } catch (Exception e) {
        	throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public UserDTO patch(int id, UserDTO user) {
        try {
            User existingUser = userRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 	
            userMapper.update(existingUser, user);
            User savedUser = userRepository.save(existingUser);
            return userMapper.entityToDto(savedUser);
        } catch (Exception e) {
        	throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        try {
            userRepository.softDeleteUsers(ids); 
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @Cacheable(value = "media", keyGenerator = "customKeyGenerator")
//    @Cacheable(value = "media")
    @Override
    public MediaDTO getImage(int userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            return mediaMapper.entityToDto(user.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @Cacheable(value = "media", keyGenerator = "customKeyGenerator")
//    @Cacheable(value = "media")
    @Override
    public MediaDTO getCover(int userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            return mediaMapper.entityToDto(user.getCover());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @CachePut(value = "media", keyGenerator = "customKeyGenerator")
//    @CachePut(value = "media")
    @Override
    public MediaDTO updateImage(int userId, int imageId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
            Media image = mediaRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.media")));
            user.setImage(image);
            User savedUser = userRepository.save(user);
            return savedUser.getImage() != null ? mediaMapper.entityToDto(savedUser.getImage()) : null;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @CachePut(value = "media", keyGenerator = "customKeyGenerator")
//    @CachePut(value = "media")
    @Override
    public MediaDTO updateCover(int userId, int coverId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
            Media image = mediaRepository.findById(coverId).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.media")));
            user.setCover(image);
            User savedUser = userRepository.save(user);
            return savedUser.getCover() != null ? mediaMapper.entityToDto(savedUser.getCover()) : null;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}