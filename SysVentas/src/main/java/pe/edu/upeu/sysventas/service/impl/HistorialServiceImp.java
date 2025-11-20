package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.model.Historial;
import pe.edu.upeu.sysventas.repository.HistorialRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.IHistorialService;

@Service
@RequiredArgsConstructor
public class HistorialServiceImp extends CrudGenericoServiceImp<Historial, Long> implements IHistorialService {

    private final HistorialRepository historialRepository;

    @Override
    protected ICrudGenericoRepository<Historial, Long> getRepo() {
        return historialRepository;
    }
}
