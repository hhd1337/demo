package com.umc.hackathon_demo.domain.s3;

import com.umc.hackathon_demo.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
    Optional<Uuid> findByUuid(String uuid);
}

