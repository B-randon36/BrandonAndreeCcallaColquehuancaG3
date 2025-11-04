package pe.edu.upeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysventas.model.HistorialVenta;

@Repository
public interface HistorialVentaRepository extends JpaRepository<HistorialVenta, Long>, ICrudGenericoRepository<HistorialVenta, Long> {}