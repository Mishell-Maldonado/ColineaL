package com.colineal.colinealcrud;

import com.colineal.dao.CategoriaDao;
import com.colineal.entities.Categoria;
import java.util.List;
import com.colineal.dao.ProductoDAO;
import com.colineal.entities.Producto;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaProductos extends JFrame {

    private final ProductoDAO dao = new ProductoDAO();
    private final CategoriaDao daoCategoria = new CategoriaDao();
    private JTextField txtId, txtNombre, txtDescripcion;
    private JTextField txtCantidad, txtPrecio, txtBuscar;
    private JComboBox<Categoria> cmbCategoria;
    private JTable tabla;
    private DefaultTableModel modelo;

    private final Color cafe = new Color(96, 67, 47);
    private final Color beige = new Color(243, 234, 222);

   public VentanaProductos() {
       
        setTitle("COLINEAL - Gestión de productos");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        construirVentana();
        daoCategoria.crearIniciales();
        cmbCategoria.setModel(
               new DefaultComboBoxModel<>(
                daoCategoria.obtenerTodas().toArray(new Categoria[0])
         )
    );
    consultarProductos();
    SwingUtilities.invokeLater(() ->
         txtNombre.requestFocusInWindow());
   }

    private void construirVentana() {
       
        
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(beige);

        JLabel titulo = new JLabel("COLINEAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 30));
        titulo.setForeground(cafe);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        add(titulo, BorderLayout.NORTH);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        cmbCategoria = new JComboBox<>();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();

        JPanel formulario = new JPanel(new GridLayout(3, 4, 10, 10));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(beige),
                "Datos del producto"));

       agregarCampo(formulario, "ID:", txtId);
       agregarCampo(formulario, "Categoría:", cmbCategoria);

       agregarCampo(formulario, "Nombre:", txtNombre);
       agregarCampo(formulario, "Cantidad:", txtCantidad);

       agregarCampo(formulario, "Precio ($):", txtPrecio);
       agregarCampo(formulario, "Descripción:", txtDescripcion);
       
        JButton guardar = boton("Guardar", cafe);
        JButton actualizar = boton("Actualizar", cafe);
        JButton filtrarCategoria = boton("Filtrar categoría", cafe);
        JButton eliminar = boton("Eliminar", new Color(150, 65, 55));
        JButton limpiar = boton("Limpiar", new Color(125, 105, 88));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        botones.setBackground(Color.WHITE);
        botones.add(guardar);
        botones.add(actualizar);
        botones.add(eliminar);
        botones.add(limpiar);

        JPanel arriba = new JPanel(new BorderLayout(8, 8));
        arriba.setBackground(beige);
        arriba.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Descripción",
                    "Categoría", "Cantidad", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(cafe);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        txtBuscar = new JTextField(18);
        JButton buscar = boton("Buscar", cafe);
        JButton consultar = boton("Ver todos", cafe);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.setBackground(Color.WHITE);
       busqueda.add(new JLabel("Buscar por nombre:"));
       busqueda.add(txtBuscar);
       busqueda.add(buscar);
       busqueda.add(filtrarCategoria);
       busqueda.add(consultar);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder("Inventario de productos"));
        panelTabla.add(busqueda, BorderLayout.NORTH);
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(10, 15));
        centro.setBackground(beige);
        centro.add(arriba, BorderLayout.NORTH);
        centro.add(panelTabla, BorderLayout.CENTER);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(centro, BorderLayout.CENTER);

        guardar.addActionListener(e -> guardarProducto());
        actualizar.addActionListener(e -> actualizarProducto());
        eliminar.addActionListener(e -> eliminarProducto());
        limpiar.addActionListener(e -> limpiarCampos());
        consultar.addActionListener(e -> consultarProductos());
        filtrarCategoria.addActionListener(e -> filtrarPorCategoria());
        buscar.addActionListener(e -> buscarProducto());
        txtBuscar.addActionListener(e -> buscarProducto());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int f = tabla.getSelectedRow();
                txtId.setText(modelo.getValueAt(f, 0).toString());
                txtNombre.setText(modelo.getValueAt(f, 1).toString());
                txtDescripcion.setText(modelo.getValueAt(f, 2).toString());
                String nombreCategoria = modelo.getValueAt(f, 3).toString();

for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
    Categoria categoria = cmbCategoria.getItemAt(i);

    if (categoria.getNombre().equals(nombreCategoria)) {
        cmbCategoria.setSelectedIndex(i);
        break;
    }
}
                txtCantidad.setText(modelo.getValueAt(f, 4).toString());
                txtPrecio.setText(
                modelo.getValueAt(f, 5).toString()
                .replace("$", "").trim());
            }
        });
    }

    private void agregarCampo(JPanel panel, String nombre, java.awt.Component campo) {
    panel.add(new JLabel(nombre));
    panel.add(campo);
}

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    
private Producto leerProducto() {
    String nombre = txtNombre.getText().trim();
    String cantidadTexto = txtCantidad.getText().trim();
    String precioTexto = txtPrecio.getText().trim();
    
    Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

    if (categoria == null) {
         throw new IllegalArgumentException(
            "Seleccione una categoría.");
}

    if (nombre.isEmpty()) {
        throw new IllegalArgumentException(
                "El nombre del producto es obligatorio.");
    }

    if (cantidadTexto.isEmpty() ||
            !cantidadTexto.matches("\\d+")) {
        throw new IllegalArgumentException(
                "La cantidad debe ser un entero positivo o cero.");
    }

    if (precioTexto.isEmpty() ||
        !precioTexto.matches("\\d+([.,]\\d{1,2})?")) {
    throw new IllegalArgumentException(
            "Ingrese un precio válido, por ejemplo: 120.50 o 120,50");
}

    int cantidad = Integer.parseInt(cantidadTexto);
    double precio = Double.parseDouble(
        precioTexto.replace(',', '.'));

    Producto p = new Producto();
    p.setNombre(nombre);
    p.setDescripcion(txtDescripcion.getText().trim());
    p.setCategoria(categoria);
    p.setCantidad(cantidad);
    p.setPrecio(precio);

    return p;
}   
private void guardarProducto() {
    try {
        Producto p = leerProducto();
        dao.crear(p);
        consultarProductos();
        limpiarCampos();

        JOptionPane.showMessageDialog(this,
                "Producto guardado correctamente.");
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "No se pudo guardar el producto.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
private void consultarProductos() {
    try {
        modelo.setRowCount(0);

        for (Producto p : dao.obtenerTodos()) {
            String nombreCategoria = "";

            if (p.getCategoria() != null) {
                nombreCategoria = p.getCategoria().getNombre();
            }

            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                nombreCategoria,
                p.getCantidad(),
                "$ " + String.format("%.2f", p.getPrecio())
            });
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Error al consultar productos.");
    }
}
private void filtrarPorCategoria() {
    Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

    if (categoria == null) {
        JOptionPane.showMessageDialog(this,
                "Seleccione una categoría en el formulario.");
        return;
    }

    try {
        modelo.setRowCount(0);

        List<Producto> productos =
                dao.buscarPorCategoria(categoria.getNombre());

        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getCategoria() != null
                        ? p.getCategoria().getNombre() : "",
                p.getCantidad(),
                "$ " + String.format("%.2f", p.getPrecio())
            });
        }

        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en esta categoría.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Error al filtrar productos por categoría.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void buscarProducto() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        try {
            for (Producto p : dao.obtenerTodos()) {
                if (p.getNombre().toLowerCase().contains(texto)) {
                    modelo.addRow(new Object[]{p.getId(), p.getNombre(),
                        p.getDescripcion(), p.getCategoria(),
                        p.getCantidad(), p.getPrecio()});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar productos.");
        }
    }

    private void actualizarProducto() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.");
                return;
            }
            Producto p = leerProducto();
            p.setId(Integer.parseInt(txtId.getText()));
            dao.actualizar(p);
            consultarProductos();
            limpiarCampos();
        } catch (IllegalArgumentException ex) {
    JOptionPane.showMessageDialog(this, ex.getMessage(),
            "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
} catch (Exception ex) {
    JOptionPane.showMessageDialog(this,
            "No se pudo actualizar el producto.",
            "Error", JOptionPane.ERROR_MESSAGE);
}
    }

    
   private void eliminarProducto() {
    if (txtId.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Seleccione un producto de la tabla.");
        return;
    }

    int r = JOptionPane.showConfirmDialog(this,
            "¿Desea eliminar el producto seleccionado?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

    if (r == JOptionPane.YES_OPTION) {
        try {
            dao.eliminar(Integer.parseInt(txtId.getText()));
            consultarProductos();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Producto eliminado correctamente.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el producto.");
        }
    }
}

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        cmbCategoria.setSelectedIndex(-1);
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtBuscar.setText("");
        tabla.clearSelection();
        txtNombre.requestFocusInWindow();
    }
}
