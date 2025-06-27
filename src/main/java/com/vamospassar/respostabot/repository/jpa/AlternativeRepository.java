package com.vamospassar.respostabot.repository.jpa;


import com.vamospassar.respostabot.model.jpa.Alternative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlternativeRepository extends JpaRepository<Alternative, UUID> {

}
