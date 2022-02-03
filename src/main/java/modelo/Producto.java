/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 *
 * @author RYZEN
 */

@Table("productos")

public class Producto {
    @Id
    @Column("codigo")
    private int codigo;
    @Column("nombre")
    private String nombre;
    @Column("precio")
    private float precio;
    @Column("inventario")
    private int inventario;

    public Producto() {
    }

    public Producto(String nombre, float precio, int inventario) {
        this.nombre = nombre;
        this.precio = precio;
        this.inventario = inventario;
    }
    
    public Producto(int codigo, String nombre, float precio, int inventario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.inventario = inventario;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public int getInventario() {
        return inventario;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }
}
