package br.com.eletriccompany.onehome.domain.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private CustomerTypeEntity customerType;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String zipcode;
    @OneToOne
    private UserEntity user;
    @OneToOne
    private ClockEntity clock;
    @Column(nullable = false)
    private boolean active;
}
