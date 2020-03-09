package com.edu.egg.meetdia.com.repositorios;

import com.edu.egg.meetdia.com.entidades.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepositorio extends JpaRepository<ConfirmationToken, String>{
    @Query("SELECT c FROM ConfirmationToken c WHERE c.confirmationToken= :confirmationToken")
    public ConfirmationToken buscarToken(@Param("confirmationToken") String token);
}
