package com.cibertec.edu.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.edu.models.Producto;

@Repository
public interface ProductoDAO extends CrudRepository<Producto,Long>{

}
