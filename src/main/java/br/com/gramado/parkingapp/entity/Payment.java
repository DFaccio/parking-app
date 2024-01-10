package br.com.gramado.parkingapp.entity;

import br.com.gramado.parkingapp.util.enums.TypePayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private boolean isPaid;

    @Enumerated(value = EnumType.STRING)
    private TypePayment paymentType;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTimePayment;

    @Column(precision = 15, scale = 4)
    private BigDecimal price;
}
