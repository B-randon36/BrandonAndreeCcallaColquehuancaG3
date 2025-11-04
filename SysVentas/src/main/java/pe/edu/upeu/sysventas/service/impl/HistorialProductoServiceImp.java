package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.model.HistorialProducto;
import pe.edu.upeu.sysventas.repository.HistorialProductoRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.IHistorialProductoService;

@Service
@RequiredArgsConstructor
public class HistorialProductoServiceImp extends CrudGenericoServiceImp<HistorialProducto, Long> implements IHistorialProductoService {
    private final HistorialProductoRepository repo;
    @Override
    protected ICrudGenericoRepository<HistorialProducto, Long> getRepo() { return repo; }

    @Override
    public void deleteAll() {
        repo.deleteAll();
    }
}