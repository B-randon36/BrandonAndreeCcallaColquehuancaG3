package pe.edu.upeu.sysventas.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysventas.enums.ActionType;
import pe.edu.upeu.sysventas.model.Cliente;
import pe.edu.upeu.sysventas.model.Historial;
import pe.edu.upeu.sysventas.repository.ClienteRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.IClienteService;
import pe.edu.upeu.sysventas.service.IHistorialService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImp extends CrudGenericoServiceImp<Cliente,String> implements IClienteService {
    private final ClienteRepository clienteRepository;
    private final IHistorialService historialService;

    Logger logger= LoggerFactory.getLogger(ClienteServiceImp.class);

    @Override
    protected ICrudGenericoRepository<Cliente, String> getRepo() {
        return clienteRepository;
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        // Determinar si es una creación o una edición
        Optional<Cliente> existingCliente = clienteRepository.findById(cliente.getDniruc());
        ActionType actionType = existingCliente.isPresent() ? ActionType.EDITAR : ActionType.CREAR;
        String details = (actionType == ActionType.CREAR ? "Se creó el cliente: " : "Se editó el cliente: ") + cliente.getNombres();

        // Guardar el cliente
        Cliente savedCliente = super.save(cliente);

        // Registrar en el historial
        historialService.save(
                Historial.builder()
                        .entityName("Cliente")
                        .actionType(actionType)
                        .timestamp(LocalDateTime.now())
                        .details(details)
                        .build()
        );
        return savedCliente;
    }
    
    @Override
    @Transactional
    public Cliente update(String id, Cliente cliente) {
        Cliente updatedCliente = super.update(id, cliente);
        historialService.save(
                Historial.builder()
                        .entityName("Cliente")
                        .actionType(ActionType.EDITAR)
                        .timestamp(LocalDateTime.now())
                        .details("Se editó el cliente: " + updatedCliente.getNombres())
                        .build()
        );
        return updatedCliente;
    }

    @Override
    @Transactional
    public void delete(String id) {
        Cliente cliente = findById(id);
        super.delete(id);
        historialService.save(
                Historial.builder()
                        .entityName("Cliente")
                        .actionType(ActionType.ELIMINAR)
                        .timestamp(LocalDateTime.now())
                        .details("Se eliminó el cliente: " + cliente.getNombres())
                        .build()
        );
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletCliente() {
        List<ModeloDataAutocomplet> listarclientes = new ArrayList<>();
        try {
            for (Cliente cliente : clienteRepository.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(cliente.getDniruc());
                data.setNameDysplay(cliente.getNombres());
                data.setOtherData(cliente.getTipoDocumento().name());
                listarclientes.add(data);
            }
        } catch (Exception e) {
            logger.error("Error durante la operación", e);
        }
        return listarclientes;
    }

}
