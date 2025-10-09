package pe.edu.upeu.asistencia.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.repositorio.ParticipanteIRepositorio;

import java.util.List;

@Service
public class ParticipanteServicioImp  implements ParticipanteServicioI {

    @Autowired
    ParticipanteIRepositorio participanteIRepositorio;

    @Override
    public void save(Participante participante) {
       participanteIRepositorio.save(participante);
    }

    @Override
    public List<Participante> findAll() {
        return participanteIRepositorio.findAll();
    }

    @Override
    public Participante update(Participante participante) {

        return participanteIRepositorio.save(participante);
    }

    @Override
    public void delete(String dni){

        participanteIRepositorio.deleteById(dni);
    }

    @Override
    public Participante findById(String dni) {

        return participanteIRepositorio.findById(dni).orElseThrow();
    }
}
