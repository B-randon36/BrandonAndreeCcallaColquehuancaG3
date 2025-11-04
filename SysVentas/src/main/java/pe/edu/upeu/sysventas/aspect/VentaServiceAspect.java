package pe.edu.upeu.sysventas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.model.HistorialVenta;
import pe.edu.upeu.sysventas.model.Venta;
import pe.edu.upeu.sysventas.service.IHistorialVentaService;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
public class VentaServiceAspect {

    @Autowired
    private IHistorialVentaService historialVentaService;

    // Pointcut para el método save() de cualquier implementación de VentaIService
    @Pointcut("execution(* pe.edu.upeu.sysventas.service.VentaIService.save(..))")
    public void ventaSavePointcut() {}

    // Pointcut para el método update()
    @Pointcut("execution(* pe.edu.upeu.sysventas.service.VentaIService.update(..))")
    public void ventaUpdatePointcut() {}

    // Pointcut para el método delete() o deleteById()
    @Pointcut("execution(* pe.edu.upeu.sysventas.service.VentaIService.delete*(..)) && args(id)")
    public void ventaDeletePointcut(Long id) {}

    // Se ejecuta después de que save() o update() terminen exitosamente
    @AfterReturning(pointcut = "ventaSavePointcut() || ventaUpdatePointcut()", returning = "ventaGuardada")
    public void logAfterSaveOrUpdate(JoinPoint joinPoint, Venta ventaGuardada) {
        String accion = joinPoint.getSignature().getName().equals("save") ? "CREACIÓN" : "ACTUALIZACIÓN";
        guardarHistorial(ventaGuardada, accion + " de Venta");
        System.out.println("Historial de " + accion + " de venta guardado para el ID: " + ventaGuardada.getIdVenta());
    }

    // Se ejecuta después de que delete() o deleteById() terminen exitosamente
    @AfterReturning(pointcut = "ventaDeletePointcut(id)")
    public void logAfterDelete(JoinPoint joinPoint, Long id) {
        Venta ventaReferencia = new Venta();
        ventaReferencia.setIdVenta(id); // Solo necesitamos el ID para la referencia
        guardarHistorial(ventaReferencia, "ELIMINACIÓN de Venta");
        System.out.println("Historial de eliminación de venta guardado para el ID: " + id);
    }

    private void guardarHistorial(Venta venta, String accion) {
        HistorialVenta historial = new HistorialVenta();
        historial.setVenta(venta);
        historial.setAccion(accion);
        historial.setFechaHora(LocalDateTime.now());
        historialVentaService.save(historial);
    }
}