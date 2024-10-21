package MutantService1;

import MutantModel.DnaRequest;
import MutantRepository.DnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MutantService {

    @Autowired
    private DnaRepository dnaRepository;

    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) {
            throw new NullPointerException("El ADN está vacío");
        }

        int N = dna.length;
        for (String row : dna) {
            if (row.length() != N) {
                throw new ArrayIndexOutOfBoundsException("El ADN no tiene un tamaño de NxN");
            }
            for (char c : row.toCharArray()) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    throw new IllegalArgumentException("Alguna de las letras del ADN no corresponde con una de las válidas");
                }
            }
        }

        int sequenceCount = 0;

        // Buscar secuencias en dirección horizontal
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N - 3; j++) {
                if (dna[i].charAt(j) == dna[i].charAt(j + 1) &&
                        dna[i].charAt(j) == dna[i].charAt(j + 2) &&
                        dna[i].charAt(j) == dna[i].charAt(j + 3)) {
                    sequenceCount++;
                }
            }
        }

        // Buscar secuencias en dirección vertical
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N - 3; i++) {
                if (dna[i].charAt(j) == dna[i + 1].charAt(j) &&
                        dna[i].charAt(j) == dna[i + 2].charAt(j) &&
                        dna[i].charAt(j) == dna[i + 3].charAt(j)) {
                    sequenceCount++;
                }
            }
        }

        // Buscar secuencias en dirección diagonal
        for (int i = 0; i < N - 3; i++) {
            for (int j = 0; j < N - 3; j++) {
                if (dna[i].charAt(j) == dna[i + 1].charAt(j + 1) &&
                        dna[i].charAt(j) == dna[i + 2].charAt(j + 2) &&
                        dna[i].charAt(j) == dna[i + 3].charAt(j + 3)) {
                    sequenceCount++;
                }
            }
        }

        // Buscar secuencias en dirección diagonal inversa
        for (int i = 0; i < N - 3; i++) {
            for (int j = 3; j < N; j++) {
                if (dna[i].charAt(j) == dna[i + 1].charAt(j - 1) &&
                        dna[i].charAt(j) == dna[i + 2].charAt(j - 2) &&
                        dna[i].charAt(j) == dna[i + 3].charAt(j - 3)) {
                    sequenceCount++;
                }
            }
        }

        // Devuelve true si hay más de una secuencia mutante
        return sequenceCount > 1;
    }

    public DnaRequest save(DnaRequest dnaRequest) {
        Optional<DnaRequest> existingDna = dnaRepository.findByDna(dnaRequest.getDna());
        if (existingDna.isPresent()) {
            throw new DataIntegrityViolationException("EL ADN ingresado ya se encuentra en la base de datos");
        }
        return dnaRepository.save(dnaRequest);
    }

    public Map<String, Object> getStats() {
        long countMutantDna = dnaRepository.countByEsMutant(true);
        long countHumanDna = dnaRepository.countByEsMutant(false);
        double ratio = countHumanDna > 0 ? (double) countMutantDna / countHumanDna : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("count_mutant_dna", countMutantDna);
        stats.put("count_human_dna", countHumanDna);
        stats.put("ratio", ratio);

        return stats;
    }
}
