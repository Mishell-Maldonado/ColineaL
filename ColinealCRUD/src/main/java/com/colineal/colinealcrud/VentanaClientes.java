
package com.colineal.colinealcrud;

import com.colineal.dao.ClienteDAO;
import com.colineal.entities.Cliente;
import javax.swing.*;
import java.awt.*;

public class VentanaClientes extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtCedula = new JTextField(15);
    private JTextField txtTelefono = new JTextField(15);
    private JTextField txtCorreo = new JTextField(15);
    private ClienteDAO dao = new ClienteDAO();
    private String[] columnas = {"ID","Nombre", "Cédula", "Teléfono", "Correo"};
    private javax.swing.table.DefaultTableModel modelo =
            new javax.swing.table.DefaultTableModel(columnas, 0);
    private JTable tabla = new JTable(modelo) {
    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
};
    private JTextField txtBuscar = new JTextField(15);
    private JTextField txtIdSeleccionado = new JTextField();
    
    public VentanaClientes() {
        consultarClientes();
        setTitle("Clientes - Colineal");
        setSize(560, 480);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Cédula:"));
        formulario.add(txtCedula);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        JPanel botones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");

        botones.add(btnGuardar);
        botones.add(btnEliminar);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);

        JPanel lista = new JPanel(new BorderLayout(5, 5));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.add(new JLabel("Buscar por nombre o cédula:"));
        busqueda.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnVerTodos = new JButton("Ver todos");

        busqueda.add(btnBuscar);
        busqueda.add(btnVerTodos);
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(new JLabel("Clientes registrados:"), BorderLayout.NORTH);
        encabezado.add(busqueda, BorderLayout.CENTER);

        lista.add(encabezado, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(380, 120));
        scroll.setMinimumSize(new Dimension(350, 100));

        lista.add(scroll, BorderLayout.CENTER);
        add(lista, BorderLayout.CENTER);

        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnGuardar.addActionListener(e -> guardarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            consultarClientes();
        
        });
                
        setLocationRelativeTo(null);
    }

    private void guardarCliente() {
        try {
            Cliente c = new Cliente();
            c.setNombre(txtNombre.getText().trim());
            c.setCedula(txtCedula.getText().trim());
            c.setTelefono(txtTelefono.getText().trim());
            c.setCorreo(txtCorreo.getText().trim());

            if (c.getNombre().isEmpty() || c.getCedula().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nombre y cédula son obligatorios.");
                return;
            }

            dao.crear(c);
            JOptionPane.showMessageDialog(this, "Cliente guardado.");
            consultarClientes();
            txtNombre.setText("");
            txtCedula.setText("");
            txtTelefono.setText("");
            txtCorreo.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el cliente. Revisa la cédula.");
        }
    }

    private void consultarClientes() {
        try {
            modelo.setRowCount(0);
                for (Cliente c : dao.obtenerTodos()) {
                    modelo.addRow(new Object[]{
                    c.getId(), c.getNombre(), c.getCedula(), c.getTelefono(), c.getCorreo()
                   });
                }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al consultar clientes.");
        }
    }
    private void buscarCliente() {
    String texto = txtBuscar.getText().trim().toLowerCase();

    modelo.setRowCount(0);

    for (Cliente c : dao.obtenerTodos()) {
        if (c.getNombre().toLowerCase().contains(texto)
                || c.getCedula().contains(texto)) {

           modelo.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getCedula(),
                c.getTelefono(),
                c.getCorreo()
            });
        }
    }
}
   private void eliminarCliente() {
    int fila = tabla.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this,
                "Seleccione un cliente de la tabla.");
        return;
    }

    int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Desea eliminar el cliente seleccionado?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

    if (respuesta == JOptionPane.YES_OPTION) {
        try {
           Integer id = (Integer) modelo.getValueAt(fila, 0);
            dao.eliminar(id);
            consultarClientes();
            JOptionPane.showMessageDialog(this,
                    "Cliente eliminado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el cliente.\n"
                    + "Detalle: " + ex.getCause(),
                    "Error al eliminar",
                    JOptionPane.ERROR_MESSAGE);
        }
            }
    } 
}
