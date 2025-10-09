package pe.edu.upeu.asistencia.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "participante")
public class Participante {
    @Id

    private String dni;
    private String nombre;
    private String apellidos;
    private Boolean estado;
    @Enumerated(EnumType.STRING)
    private Carrera carrera;
    @Enumerated(EnumType.STRING)
    private TipoParticipante tipoParticipante;


}
