package MutantModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DnaRequest {
    @Column(unique = true)
    private String dna;  // Cambiado a String para facilitar la persistencia
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    private Boolean esMutant = false;
}
