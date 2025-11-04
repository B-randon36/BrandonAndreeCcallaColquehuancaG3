package pe.edu.upeu.sysventas.service;

import pe.edu.upeu.sysventas.model.HistorialProducto;

public interface IHistorialProductoService extends ICrudGenericoService<HistorialProducto, Long> {
    void deleteAll();
}