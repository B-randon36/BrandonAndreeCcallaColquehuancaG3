package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.model.HistorialVenta;
import pe.edu.upeu.sysventas.repository.HistorialVentaRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.IHistorialVentaService;

@Service
@RequiredArgsConstructor
public class HistorialVentaServiceImp extends CrudGenericoServiceImp<HistorialVenta, Long> implements IHistorialVentaService {

    private final HistorialVentaRepository repo;
    @Override
    protected ICrudGenericoRepository<HistorialVenta, Long> getRepo() { return repo; }
}