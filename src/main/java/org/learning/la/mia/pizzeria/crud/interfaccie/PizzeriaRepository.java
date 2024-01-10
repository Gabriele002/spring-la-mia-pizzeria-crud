package org.learning.la.mia.pizzeria.crud.interfaccie;

import org.learning.la.mia.pizzeria.crud.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzeriaRepository extends JpaRepository<Pizza, Integer> {
}
