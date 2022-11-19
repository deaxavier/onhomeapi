package br.com.eletriccompany.onehome.infra.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.eletriccompany.onehome.domain.dto.responses.nativequeries.ClockEventsResumeResponse;
import br.com.eletriccompany.onehome.domain.entities.ClockRegisterEntity;

public interface ClockRegisterRepository extends JpaRepository<ClockRegisterEntity, UUID> {
    List<ClockRegisterEntity> findByDateBetweenAndClockIdOrderByDateDesc(Timestamp from, Timestamp to, UUID clock_id);

    @Query(value = "select date_trunc('day', \"date\") as \"day\", sum(\"kwh\") as \"kwh\", "
            + "sum(kwh * kwh_cost) as \"cost\" from tbl_clock_register tcr "
            + "where \"date\" between (now() - interval  '7 DAY') and now() and clock_id = ?1 "
            + "group by \"day\" order by \"day\" desc limit 7 ", nativeQuery = true)
    List<ClockEventsResumeResponse> resumeSevenDays(UUID clock_id);

    @Query(value = "select date_trunc('month', \"date\") as \"day\", sum(\"kwh\") as \"kwh\", "
            + "sum(kwh * kwh_cost) as \"cost\" from tbl_clock_register tcr "
            + "where \"date\" between (now() - interval  '12 MONTH') and now() and clock_id = ?1 "
            + "group by \"day\" order by \"day\" desc limit 12 ", nativeQuery = true)
    List<ClockEventsResumeResponse> resumeMonth(UUID clock_id);
}
