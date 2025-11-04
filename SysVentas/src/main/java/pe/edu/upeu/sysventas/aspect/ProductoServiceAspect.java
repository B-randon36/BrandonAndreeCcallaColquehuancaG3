package pe.edu.upeu.sysventas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysventas.model.HistorialVenta;
import pe.edu.upeu.sysventas.model.HistorialProducto;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.repository.ProductoRepository;
import pe.edu.upeu.sysventas.service.IHistorialProductoService;

import java.time.LocalDateTime;

@Aspect
@Component
public class ProductoServiceAspect {

    @Autowired
    private IHistorialProductoService historialProductoService;
    @Autowired
    private ProductoRepository productoRepository;

    // Se ejecuta después de que save() o update() terminen exitosamente
    @AfterReturning(pointcut = "execution(* pe.edu.upeu.sysventas.service.ProductoIService.save(..)) || execution(* pe.edu.upeu.sysventas.service.ProductoIService.update(..))", returning = "productoGuardado")
    public void logAfterSaveOrUpdate(JoinPoint joinPoint, Producto productoGuardado) {
        String accion = joinPoint.getSignature().getName().equals("save") ? "CREACIÓN" : "ACTUALIZACIÓN";
        guardarHistorial(productoGuardado.getIdProducto(), productoGuardado.getNombre(), accion);
    }

    // Se ejecuta ANTES de que delete(id) termine, para poder capturar el nombre del producto
    @Before("execution(* pe.edu.upeu.sysventas.service.ProductoIService.deleteById(Long)) && args(id)")
    public void logBeforeDelete(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            guardarHistorial(id, producto.getNombre(), "ELIMINACIÓN");
        });
    }

    @Transactional
    private void guardarHistorial(Long idProducto, String nombreProducto, String accion) {
        try {
            if (historialProductoService == null) {
                System.err.println("ERROR CRÍTICO en Aspect: historialProductoService no fue inyectado. El historial no se puede guardar.");
                return;
            }

            HistorialProducto historial = new HistorialProducto();
            historial.setIdProducto(idProducto);
            historial.setNombreProducto(nombreProducto);
            historial.setAccion(accion);
            historial.setFechaHora(LocalDateTime.now());
            historialProductoService.save(historial);
            System.out.println("Historial de producto guardado: " + accion + " para el producto '" + nombreProducto + "' (ID: " + idProducto + ")");
        } catch (Exception e) {
            System.err.println("Error al guardar el historial del producto: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace completo para un mejor diagnóstico
        }
    }
}