/*
Nonbre del Proyecto: Reto número 5
Descripción del proyecto: Aplicación que permite agregar, eliminar y actualizar la información de una base de datos
Desarrollador: Andrea Gómez
Fecha: 18 / 09 / 2021
Versión: 1.0
*/
package controlador;

//Importaciones requeridas
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;
import modelo.RepositorioProducto;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import vista.Interfaz;

/*
 * @author RYZEN
 */

//Clase que controla el funcionamiento de los botones y la tabla respectiva

public class ControladorProducto implements ActionListener{
    
    RepositorioProducto repoProducto;
    Interfaz vista;
    DefaultTableModel defaultTableModel;
    
    private int codigo;
    private String nombre;
    private float precio;
    private int inventario;

    //Constructores
    public ControladorProducto() {
        super();
    }

    public ControladorProducto(RepositorioProducto repoProducto, Interfaz vista) {
        super();
        this.repoProducto = repoProducto;
        this.vista = vista;
        vista.setVisible(true);
        agregarEventos();
        listarTabla();
    }

    //Eventos de los botones
    private void agregarEventos(){
        vista.getBtnAgregar().addActionListener(this);
        vista.getBtnBorrar().addActionListener(this);
        vista.getBtnActualizar().addActionListener(this);
        vista.getBtnInforme().addActionListener(this);
        vista.getTblTabla().addMouseListener(new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e){
                llenarCampos(e);
            }
        });
    }
    
    //Listar la tabla
    public void listarTabla(){
        String[] titulos = new String[]{"Codigo", "Nombre", "Precio", "Inventario"};
        defaultTableModel = new DefaultTableModel(titulos, 0);
        List<Producto> listaProductos = (List<Producto>) repoProducto.findAll();
        
        for(Producto produc : listaProductos){
            defaultTableModel.addRow(new Object[]{produc.getCodigo(), produc.getNombre(), produc.getPrecio(), produc.getInventario()});
        }
        
        vista.getTblTabla().setModel(defaultTableModel);
        vista.getTblTabla().setPreferredSize(new Dimension(350, defaultTableModel.getRowCount()*16));
    }
    
    //Sea seleccionado una fila de la tabla llenar los textfields para poder editarlos
    private void llenarCampos(MouseEvent e){
        JTable target = (JTable) e.getSource();
        vista.getTxtCodigo().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 0).toString());
        vista.getTxtNombre().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 1).toString());
        vista.getTxtPrecio().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 2).toString());
        vista.getTxtInventario().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 3).toString());
    }
    
    //Validar campos vacios
    private boolean validarDatos(){
        if ("".equals(vista.getTxtNombre().getText()) || "".equals(vista.getTxtPrecio().getText()) || "".equals(vista.getTxtInventario().getText())){
            JOptionPane.showMessageDialog(null, "Todos los campos son abligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }else{
            return true;
        }
    }
    
    // Cargar datos y validar si precio e inventario son numéricos
    private boolean cargarDatos(){
        try{
            codigo = Integer.parseInt("".equals(vista.getTxtCodigo().getText()) ? "0" : vista.getTxtCodigo().getText());
            nombre = vista.getTxtNombre().getText();
            precio = Float.parseFloat(vista.getTxtPrecio().getText());
            inventario = Integer.parseInt(vista.getTxtInventario().getText());
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    //Limpiar los textfields luego de haber realizado determinada operación
    private void limpiarCampos(){
        vista.getTxtCodigo().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtInventario().setText("");
    }
    
    //CRUD
    
    //Agregar productos a la base de datos
    private void agregar(){
        try{
            if(validarDatos()){
                if (cargarDatos()){
                    Producto producto = new Producto(nombre, precio, inventario);
                    repoProducto.save(producto);
                    
                    JOptionPane.showMessageDialog(null, "Producto fue agregado con éxito.");
                    limpiarCampos();
                }else{
                    JOptionPane.showMessageDialog(null, "Los campos precio e inventario deben ser numéricos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch (DbActionExecutionException e){
            JOptionPane.showMessageDialog(null, "El producto ya existe.");
        }finally{
            listarTabla();
        }
    }
    
    //Actualizar los productos dentro de la base de datos
    private void actualizar(){
        try{
            if(validarDatos()){
                if (cargarDatos()){
                    Producto producto = new Producto(codigo, nombre, precio, inventario);
                    repoProducto.save(producto);
                    
                    JOptionPane.showMessageDialog(null, "Producto fue actualizado con éxito.");
                    limpiarCampos();
                }else{
                    JOptionPane.showMessageDialog(null, "Los campos precio e inventario deben ser numéricos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch (DbActionExecutionException e){
            JOptionPane.showMessageDialog(null, "El producto ya existe.");
        }finally{
            listarTabla();
        }
    }
    
    //Borrar los productos de la base de datos
    private void borrar(){
        try{
            if(validarDatos()){
                if (cargarDatos()){
                    Producto producto = new Producto(codigo, nombre, precio, inventario);
                    repoProducto.delete(producto);
                    
                    JOptionPane.showMessageDialog(null, "Producto fue borrado con éxito.");
                    limpiarCampos();
                }else{
                    JOptionPane.showMessageDialog(null, "Los campos precio e inventario deben ser numéricos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch (DbActionExecutionException e){
            JOptionPane.showMessageDialog(null, "No se pudo eliminar le producto.");
        }finally{
            listarTabla();
        }
    }
    
    //Generar informe
    private void generarInforme(){
        String precioMayor = precioMayor();
        String precioMenor = precioMenor();
        String promedio = promedio();
        String total = totalInventario();
        
        JOptionPane.showMessageDialog(null, "Producto precio mayor: "+precioMayor+"\nProducto precio menor: "+precioMenor+""
                + "\nPromedio precios: "+promedio+"\nValor del inventario: "+total);
    }
    
    //Determinar el producto con precio mayor de la base de datos
    private String precioMayor(){
        String nombre = "";
        double pre = 0;
        List<Producto> listaProductos = (List<Producto>) repoProducto.findAll();
        
        for(Producto produc : listaProductos){
            if (produc.getPrecio() > pre){
                nombre = produc.getNombre();
                pre = produc.getPrecio();
            }
        }
        
        return nombre;
    }
    
    //Determinar el producto con precio menor de la base de datos
    private String precioMenor(){
        String nombre = "";
        double pre = 0;
        List<Producto> listaProductos = (List<Producto>) repoProducto.findAll();
        
        for(Producto produc : listaProductos){
            if (produc.getPrecio() < pre){
                nombre = produc.getNombre();
            }
            
            pre = produc.getPrecio();
        }
        
        return nombre;
    }
    
    //Promedio de precios dela base de datos
    private String promedio(){
        double suma = 0;
        double resultado = 0;
        List<Producto> listaProductos = (List<Producto>) repoProducto.findAll();
        
        for(Producto produc : listaProductos){
           suma += produc.getPrecio();
        }
        
        resultado = suma/listaProductos.size();
        return String.format("%.1f", resultado);
    }
    
    //Calcular el valor del inventario (precio por número de inventario)
    private String totalInventario(){
        double suma = 0;
        double resultado = 0;
        List<Producto> listaProductos = (List<Producto>) repoProducto.findAll();
        
        for(Producto produc : listaProductos){
           suma += produc.getInventario()* produc.getPrecio();
        }
        
        resultado = suma/listaProductos.size();
        return String.format("%.1f", resultado);
    }
    
    //Acciones de los botones según el seleccionado
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == vista.getBtnAgregar()){
            agregar();
        }
        
        if (ae.getSource() == vista.getBtnActualizar()){
            actualizar();
        }
        
        if (ae.getSource() == vista.getBtnBorrar()){
            borrar();
        }
        
        if (ae.getSource() == vista.getBtnInforme()){
            generarInforme();
        }
    }
}
