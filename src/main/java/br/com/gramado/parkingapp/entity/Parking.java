package br.com.gramado.parkingapp.entity;

import br.com.gramado.parkingapp.util.TypeParking;
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
public class Parking  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicle vehicule;

    @Enumerated(value = EnumType.STRING)
    private TypeParking typeParking;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTimeStart;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTimeEnd;

    @Column
    private String plate;

}
