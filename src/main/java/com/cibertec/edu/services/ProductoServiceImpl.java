package com.cibertec.edu.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.models.ProductoReport;
import com.cibertec.edu.repositories.ProductoDAO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	@Autowired
	private ProductoDAO productoRepository;

	@Override
	public List<Producto> getAllProducto() {
		
		return this.productoRepository.findAll();
	}

	@Override
	public InputStream getReportProducto() throws Exception {
		
		try {
			//DATASOURCE
			List<Producto> listaEstudiantes = this.getAllProducto();
			List<ProductoReport> listaData = new ArrayList<ProductoReport>();
			listaData.add(new ProductoReport());
			listaData.get(0).setEstudiantesList(listaEstudiantes);
			JRBeanCollectionDataSource dts = new JRBeanCollectionDataSource(listaData);
			//PARAMETROS
			Map<String,Object> parameters = new HashMap<>();
			parameters.put("IMAGE_PATH", "https://www.logolynx.com/images/logolynx/2d/2d0872912b6970a81b093aaf1bd027c7.jpeg");
			
			//GENERACION DEL REPORTE
			JasperReport jasperReportObj = getJasperReportCompiled();
			JasperPrint jPrint = JasperFillManager.fillReport(jasperReportObj, parameters, dts);
			InputStream result = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jPrint));
			return result;			
		} catch (JRException ex) {
			throw ex;
		}
	}
	private JasperReport getJasperReportCompiled() {
		try {
			InputStream studentReportStream = getClass().getResourceAsStream("/jasper/producto_report.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(studentReportStream);
			return jasper;
		} catch (Exception e) {
			return null;
		}
	}
}
