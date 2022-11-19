package br.com.eletriccompany.onehome.domain.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_clock_register")
public class ClockRegisterEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    private ClockEntity clock;
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false, precision = 10)
    private float kwh;
    @Column(nullable = false, precision = 10)
    private float kwhCost;
}
