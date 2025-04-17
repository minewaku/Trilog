package com.minewaku.trilog.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.minewaku.trilog.entity.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
	Optional<ConfirmationToken> findByToken(String token);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM ConfirmationToken ct WHERE ct.expiredDate < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}
