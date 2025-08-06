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
import com.minewaku.trilog.dto.User.UpdatedUserDTO;
import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.dto.common.request.RegisterRequest;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.User_;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.mapper.UserMapper;
import com.minewaku.trilog.repository.MediaRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IUserRoleService;
import com.minewaku.trilog.service.IUserService;
import com.minewaku.trilog.specification.SpecificationBuilder;
import com.minewaku.trilog.util.ErrorUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.transaction.Transactional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private IUserRoleService userRoleService;

	@Autowired
	private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private SpecificationBuilder<User> specBuilder;
    
    @Autowired
    private ErrorUtil errorUtil;
    
    private Set<SingularAttribute<User, ?> > allowedFieldsForFetch = new HashSet<>();

    @PostConstruct
    public void init() {
        allowedFieldsForFetch.add(User_.name);
        allowedFieldsForFetch.add(User_.email);
        @SuppressWarnings("unchecked")
        SingularAttribute<User, ?> createdDate = (SingularAttribute<User, ?>) (SingularAttribute<?, ?>) User_.createdDate;
        allowedFieldsForFetch.add(createdDate);
    }
    
    @Override
    public Page<UserDTO> findAll(Map<String, String> params, Pageable pageable) {
        try {
        	Specification<User> spec = specBuilder.buildSpecification(params, allowedFieldsForFetch);
            Page<User> users = userRepository.findAll(spec, pageable);
            Page<UserDTO> userDTOs = users.map(user -> userMapper.entityToDto(user));
            return userDTOs;

        } catch(IllegalArgumentException e) {
            throw errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_PARAMETERS);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserDTO findById(Integer id) {
        try {
            User user = userRepository.findByIdWithRoles(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            return userMapper.entityToDto(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
	public List<String> getRolesByUserId(Integer userId) {
		try {
            return userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND))
            		.getUserRoles()
            		.stream()
            		.map(userRole -> userRole.getRole().getName())
            		.toList();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


    @Override
    @Transactional
    public UserDTO create(RegisterRequest request) {
        try {
        	// Validating if the email already exists
    		userRepository.findByEmail(request.getEmail()).ifPresent(s -> {
    			throw errorUtil.ERROR_DETAILS.get(errorUtil.EMAIL_ALREADY_EXISTS);
    		});

    		// Creating and saving the user with explicit field settings
    		User user = User.builder().email(request.getEmail())
    				.name(request.getName())
    				.hashed_password(passwordEncoder.encode(request.getPassword()))
    				.birthdate(request.getBirthdate()).phone(request.getPhone())
    				.address(request.getAddress()).isActive(true).isEnabled(false)
    				.isLocked(false)
    				.isDeleted(false)
    				.build();
    		
    		user = userRepository.save(user);
    		
    		// Creating default Role for the user
    		userRoleService.createDefaultUserRole(user);
    		
    		User savedUser = userRepository.findById(user.getId()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
    		
            return userMapper.entityToDto(savedUser);
        } catch (Exception e) {
        	throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO update(Integer id, UpdatedUserDTO user) {
        try {
            User savedUser = userRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            User updatedUser = userRepository.save(userMapper.updateFromDtoToEntity(user, savedUser)); 
            return userMapper.entityToDto(updatedUser);
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

    @Override
    public MediaDTO getImage(Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            return mediaMapper.entityToDto(user.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public MediaDTO getCover(Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
            return mediaMapper.entityToDto(user.getCover());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    
    @Override
    @Transactional
    public MediaDTO updateImage(Integer userId, Integer imageId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
            Media image = mediaRepository.findById(imageId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.MEDIA_NOT_FOUND));
            user.setImage(image);
            User savedUser = userRepository.save(user);
            return savedUser.getImage() != null ? mediaMapper.entityToDto(savedUser.getImage()) : null;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    @Transactional
    public MediaDTO updateCover(Integer userId, Integer coverId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
            Media image = mediaRepository.findById(coverId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.MEDIA_NOT_FOUND));
            user.setCover(image);
            User savedUser = userRepository.save(user);
            return savedUser.getCover() != null ? mediaMapper.entityToDto(savedUser.getCover()) : null;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}