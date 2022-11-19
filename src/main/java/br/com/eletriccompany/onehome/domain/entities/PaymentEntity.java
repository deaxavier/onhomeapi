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
@Entity(name = "tbl_payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(nullable = false)
    private String reference;
    private Timestamp date;
    private Timestamp paymentAt;
    private Timestamp dueDate;
    private float cost;
    @ManyToOne
    private PaymentStatusEntity status;
    @ManyToOne
    private PaymentMethodEntity method;
    @ManyToOne
    private CustomerEntity customer;
}
