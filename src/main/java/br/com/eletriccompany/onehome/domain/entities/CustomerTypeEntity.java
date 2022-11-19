package br.com.eletriccompany.onehome.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_customer_type")
public class CustomerTypeEntity {
    @Id
    private Integer id;
    @Column(unique = true)
    private String name;
}