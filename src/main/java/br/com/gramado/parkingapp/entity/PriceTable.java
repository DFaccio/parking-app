package br.com.gramado.parkingapp.entity;

import br.com.gramado.parkingapp.util.enums.TypeCharge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "price_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private boolean active;

    @Enumerated(value = EnumType.STRING)
    private TypeCharge typeCharge;

    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal value;

    @Column
    private LocalTime duration;
}
