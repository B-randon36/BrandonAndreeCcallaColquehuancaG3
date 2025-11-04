package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.repository.ProductoRepository;
import pe.edu.upeu.sysventas.service.ProductoIService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoServiceImp extends CrudGenericoServiceImp<Producto, Long> implements ProductoIService {
    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImp.class);
    private final ProductoRepository pRepo;

    @Override
    protected ICrudGenericoRepository<Producto, Long> getRepo() {
        return pRepo;
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletProducto(String nombre) {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (Producto producto : pRepo.listAutoCompletProducto(nombre + "%")) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(producto.getNombre());
                data.setNameDysplay(String.valueOf(producto.getIdProducto()));
                data.setOtherData(producto.getPu() + ":" + producto.getStock());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletProducto() {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (Producto producto : pRepo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(producto.getIdProducto()));
                data.setNameDysplay(producto.getNombre());
                data.setOtherData(producto.getPu() + ":" + producto.getStock());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }
}