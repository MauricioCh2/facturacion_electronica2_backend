package org.example.backend.controller;

import org.example.backend.logic.DetalleEntity;
import org.example.backend.logic.FacturaConDetalles;
import org.example.backend.logic.FacturaEntity;
import org.example.backend.service.DetalleService;
import org.example.backend.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/factura-electronica/")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturacionController {
    @Autowired
    private FacturaService facturaService;
    @Autowired
    private DetalleService detalleService;

    @GetMapping("/facturas/{userId}")
    public List<FacturaEntity> getFacturasByUser(@PathVariable String userId) {
        return facturaService.findAllByProveedor(userId);
    }

    @PostMapping("/facturas")
    public FacturaEntity facturar(@RequestBody FacturaConDetalles facturaConDetalles) {
        // Aquí puedes acceder a la factura y los detalles con facturaConDetalles.getFactura() y facturaConDetalles.getDetalles()
        FacturaEntity facturaAux = facturaConDetalles.getFactura();
        ArrayList<DetalleEntity> detalles = (ArrayList<DetalleEntity>) facturaConDetalles.getDetalles();
        try {
            facturaService.save(facturaAux);
            for(DetalleEntity detalle: detalles){
                detalle.setIdFacDetalle(facturaAux.getIdFactura());
                detalleService.guardar(detalle);
            }
        } catch (Exception e) {
            System.out.println("Hubo un error al guardar la factura: " + e.getMessage());
            return null;
        }
        return facturaAux;
        //return new ResponseEntity<>(facturaConDetalles.getFactura(), HttpStatus.CREATED);
    }
}

