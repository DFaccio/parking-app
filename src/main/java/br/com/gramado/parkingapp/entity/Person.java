package br.com.gramado.parkingapp.entity;

import br.com.gramado.parkingapp.util.enums.DocumentType;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private String name;

    @Column
    private boolean active;

    @Column(nullable = false)
    private String phone;

    @Column
    private String email;

    @Enumerated(value = EnumType.STRING)
    private TypePayment preferentialPayment;

    @Enumerated(value = EnumType.STRING)
    private DocumentType documentType;
}
