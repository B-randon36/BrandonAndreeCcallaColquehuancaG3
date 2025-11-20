package pe.edu.upeu.sysventas.repository;

import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysventas.model.Historial;

@Repository
public interface HistorialRepository extends ICrudGenericoRepository<Historial, Long> {
}
