package br.com.gramado.parkingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column
    private String color;

    @Column
    private String model;

    @Column
    private String plate;

}
