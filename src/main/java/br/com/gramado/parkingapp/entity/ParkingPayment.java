package br.com.gramado.parkingapp.entity;

import br.com.gramado.parkingapp.util.TypePayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "parkingPayment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPayment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    @Column
    private boolean isPaid;

    @Enumerated(value = EnumType.STRING)
    private TypePayment PaymentType;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTimePayment;

    @Column
    private double price;
}
