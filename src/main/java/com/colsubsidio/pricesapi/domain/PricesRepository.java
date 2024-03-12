package com.colsubsidio.pricesapi.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PricesRepository extends JpaRepository<PriceEntity, Long> {
    public List<PriceEntity> findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(Long cadenaId, Long productoId, Date fechaAplicacion, Date fechaAplicacion2);
}
