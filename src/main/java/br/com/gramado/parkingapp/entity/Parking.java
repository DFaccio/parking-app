package br.com.gramado.parkingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTimeStart;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTimeEnd;

    @Column
    private String plate;

    @ManyToOne
    @JoinColumn(name = "price_table_id")
    private PriceTable priceTable;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column
    private boolean isFinished;
}
