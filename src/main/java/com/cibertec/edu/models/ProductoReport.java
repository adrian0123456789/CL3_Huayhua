package com.cibertec.edu.models;

import java.util.ArrayList;
import java.util.List;

public class ProductoReport {

	private List<Producto> productoList;
	public ProductoReport() {
		super();
		this.productoList = new ArrayList<>();
	}

	public List<Producto> getEstudiantesList() {
		return productoList;
	}

	public void setEstudiantesList(List<Producto> estudiantesList) {
		this.productoList = estudiantesList;
	}
	
}
