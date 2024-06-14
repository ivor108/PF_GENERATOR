package ru.cloudcom.pf.generator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cloudcom.pf.generator.entity.ChtzEntity;

public interface ChtzRepository  extends JpaRepository<ChtzEntity, String> {
}
