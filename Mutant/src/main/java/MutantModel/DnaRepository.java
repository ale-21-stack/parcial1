package MutantModel;

import MutantModel.DnaRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRepository extends JpaRepository<DnaRequest, Long> {
    Optional<DnaRequest> findByDna(String dna);

    @Query("SELECT COUNT(d) FROM DnaRequest d WHERE d.esMutant = :esMutant")
    long countByEsMutant(@Param("esMutant") boolean esMutant);
}
