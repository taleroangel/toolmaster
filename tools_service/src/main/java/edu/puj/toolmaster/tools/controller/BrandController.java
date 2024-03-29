package edu.puj.toolmaster.tools.controller;

import edu.puj.toolmaster.tools.entities.Brand;
import edu.puj.toolmaster.tools.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para las marcas
 */
@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService service;

    /**
     * Obtener todas las marcas
     * @return Listado de marcas sin paginar
     */
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Brand> getAllBrands() {
        return service.getAllBrands();
    }
}
