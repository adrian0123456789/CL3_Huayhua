package com.cibertec.edu.contollers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.repositories.ProductoDAO;
import com.cibertec.edu.services.ProductoService;
import com.cibertec.edu.services.ProductoServiceImpl;

import net.sf.jasperreports.engine.JRException;

@Controller
public class MainController {
	
	@Autowired
	private ProductoDAO productoRepository;
	private ProductoServiceImpl productoService;
	
	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/registro")
	public String registro(@ModelAttribute("producto") Producto producto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return "registro";
		} else {
			return "acceso_denegado";
		}
	}
	
	@PostMapping("/registro")
    public String registrarEstudiante(@Validated @ModelAttribute("producto") Producto producto, BindingResult bindingResult) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	if (bindingResult.hasErrors()) {
                return "registro";
            }
        	
        	productoRepository.save(producto);
        	
            return "redirect:/";
        } else {
        	return "acceso-denegado";
        }    	
    }
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}	
	
	@GetMapping({"/"})
	public String index(Model model) {
		model.addAttribute("titulo", "JASPER + SPRING BOOT");
		return "home";
	}
	
	@ModelAttribute("producto")
	public List<Producto> obtenerEstudiantes(){
		List<Producto> producto = productoService.getAllProducto();		
		return producto;
	}
	
	@GetMapping(value = "/reporte", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> reporteProducto() throws IOException, JRException {
		try {
			InputStream report = this.productoService.getReportProducto();
			byte[] data = report.readAllBytes();
			report.close();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_PDF);
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_estudiantes.pdf\"");
			header.setContentLength(data.length);
			
			return new ResponseEntity<byte[]>(data,header, HttpStatus.CREATED);			
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("IO Error retornando archivo");
		}
	}
	
}
