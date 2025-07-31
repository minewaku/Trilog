package com.minewaku.trilog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.minewaku.trilog.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.image.id = :value WHERE u.id = :id")
    void saveImage(@Param("value") int value, @Param("id") int id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.cover.id = :value WHERE u.id = :id")
    void saveCover(@Param("value") int value, @Param("id") int id);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.id IN :ids")
    int softDeleteUsers(@Param("ids") List<Integer> ids);

    @Query("""
    	    SELECT u FROM User u
    	    LEFT JOIN FETCH u.userRoles ur
    	    LEFT JOIN FETCH ur.role
    	    WHERE u.email = :email
    	""")
    	Optional<User> findByEmailWithRoles(@Param("email") String email);
    
    @EntityGraph(attributePaths = {"userRoles", "userRoles.user"})
    @Query("""
    	    SELECT u FROM User u
    	    LEFT JOIN FETCH u.userRoles ur
    	    LEFT JOIN FETCH ur.role
    	    WHERE u.id = :id
    	""")
    	Optional<User> findByIdWithRoles(@Param("id") int id);


}