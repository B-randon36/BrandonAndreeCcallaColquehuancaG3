package pe.edu.upeu.sysventas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class HistorialProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorialProducto;

    private Long idProducto;
    private String nombreProducto;
    private String accion;
    private LocalDateTime fechaHora;
}