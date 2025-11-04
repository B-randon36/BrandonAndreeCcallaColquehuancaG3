package pe.edu.upeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysventas.model.HistorialProducto;

@Repository
public interface HistorialProductoRepository extends JpaRepository<HistorialProducto, Long>, ICrudGenericoRepository<HistorialProducto, Long> {}