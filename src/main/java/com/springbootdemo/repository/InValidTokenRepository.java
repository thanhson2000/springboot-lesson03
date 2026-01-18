package com.springbootdemo.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springbootdemo.entity.InValidToken;

public interface InValidTokenRepository extends JpaRepository<InValidToken, String> {

  @Modifying
  @Query("DELETE FROM InValidToken i WHERE i.expiryTime < :currentTime")
  int clearExpiredToken(@Param("currentTime") Date currentTime);

  @Query("SELECT COUNT(i) FROM InValidToken i WHERE i.expiryTime < :currentTime")
  int countInValidToken(@Param("currentTime") Date currentTime);
}
