
package com.colineal.colinealcrud;

import com.colineal.dao.*;
import com.colineal.entities.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaVentas extends JFrame {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

    // Componentes para registrar ventas
    private JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private JComboBox<Producto> cmbProducto = new JComboBox<>();
    private JTextField txtCantidad = new JTextField("1", 5);
    private JLabel lblPrecio = new JLabel("$0.00");
    private JLabel lblTotal = new JLabel("$0.00");

    private DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Producto", "Precio", "Cantidad", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private JTable tabla = new JTable(modelo);
    private List<DetalleVenta> detalles = new ArrayList<>();

    // Componentes para consultar ventas
    private DefaultTableModel modeloVentas = new DefaultTableModel(
        new String[]{"ID", "Fecha", "Cliente", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private JTable tablaVentas = new JTable(modeloVentas);

    private DefaultTableModel modeloDetalles = new DefaultTableModel(
        new String[]{"Producto", "Precio", "Cantidad", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private JTable tablaDetalles = new JTable(modeloDetalles);

    public VentanaVentas() {
        setTitle("Ventas - Colineal");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();

        // Pestaña para registrar ventas
        JPanel panelRegistro = new JPanel(new BorderLayout(8, 8));
        panelRegistro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(3, 4, 8, 8));

        formulario.add(new JLabel("Cliente:"));
        formulario.add(cmbCliente);

        formulario.add(new JLabel("Producto:"));
        formulario.add(cmbProducto);

        formulario.add(new JLabel("Precio unitario:"));
        formulario.add(lblPrecio);

        formulario.add(new JLabel("Cantidad:"));
        formulario.add(txtCantidad);

        JButton btnAgregar = new JButton("Agregar producto");
        JButton btnQuitar = new JButton("Quitar producto");
        JButton btnRegistrar = new JButton("Registrar venta");
        JButton btnCancelar = new JButton("Cancelar");

        formulario.add(btnAgregar);
        formulario.add(btnQuitar);
        formulario.add(btnRegistrar);
        formulario.add(btnCancelar);

        panelRegistro.add(formulario, BorderLayout.NORTH);
        panelRegistro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pieRegistro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pieRegistro.add(new JLabel("TOTAL:"));
        pieRegistro.add(lblTotal);
        panelRegistro.add(pieRegistro, BorderLayout.SOUTH);
        // Pestaña para consultar ventas
        JPanel panelConsulta = new JPanel(new BorderLayout(8, 8));
        panelConsulta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tituloVentas = new JLabel("Ventas registradas");

        // Filtro por cliente
        JComboBox<Cliente> cmbBuscarCliente = new JComboBox<>();
        cmbBuscarCliente.addItem(null);

        for (Cliente cliente : clienteDAO.obtenerTodos()) {
            cmbBuscarCliente.addItem(cliente);
        }

        JButton btnBuscarCliente = new JButton("Buscar por cliente");
        JButton btnVerTodas = new JButton("Ver todas");
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Cliente:"));
        panelBusqueda.add(cmbBuscarCliente);
        panelBusqueda.add(btnBuscarCliente);
        panelBusqueda.add(btnVerTodas);

        JPanel encabezadoConsulta = new JPanel(new BorderLayout());
        encabezadoConsulta.add(tituloVentas, BorderLayout.NORTH);
        encabezadoConsulta.add(panelBusqueda, BorderLayout.SOUTH);

panelConsulta.add(encabezadoConsulta, BorderLayout.NORTH);

        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 8, 8));

        JPanel panelListaVentas = new JPanel(new BorderLayout(4, 4));
        panelListaVentas.add(new JLabel("Selecciona una venta:"), BorderLayout.NORTH);
        panelListaVentas.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        JPanel panelListaDetalles = new JPanel(new BorderLayout(4, 4));
        panelListaDetalles.add(new JLabel("Productos de la venta seleccionada:"), BorderLayout.NORTH);
        panelListaDetalles.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        panelTablas.add(panelListaVentas);
        panelTablas.add(panelListaDetalles);

        panelConsulta.add(panelTablas, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar ventas");
        panelConsulta.add(btnActualizar, BorderLayout.SOUTH);

        pestañas.addTab("Registrar venta", panelRegistro);
        pestañas.addTab("Consultar ventas", panelConsulta);

        add(pestañas, BorderLayout.CENTER);

        // Eventos
        cmbProducto.addActionListener(e -> actualizarPrecio());

        btnAgregar.addActionListener(e -> agregarProducto());
        btnQuitar.addActionListener(e -> quitarProducto());
        btnRegistrar.addActionListener(e -> registrarVenta());

        btnCancelar.addActionListener(e -> {
            detalles.clear();
            mostrarDetalles();
        });

        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetallesVenta();
            }
        });

        btnActualizar.addActionListener(e -> cargarVentas());

        btnBuscarCliente.addActionListener(e -> {
        Cliente cliente = (Cliente) cmbBuscarCliente.getSelectedItem();

        if (cliente == null) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona un cliente para buscar.");
            return;
        }

        cargarVentasPorCliente(cliente.getId());
    });

btnVerTodas.addActionListener(e -> {
    tablaVentas.clearSelection();
    modeloDetalles.setRowCount(0);
    cargarVentas();
});
        cargarDatos();
        cargarVentas();
    }

    private void cargarDatos() {
        cmbCliente.removeAllItems();
        for (Cliente cliente : clienteDAO.obtenerTodos()) {
            cmbCliente.addItem(cliente);
        }

        cmbProducto.removeAllItems();
        for (Producto producto : productoDAO.obtenerTodos()) {
            cmbProducto.addItem(producto);
        }

        actualizarPrecio();
    }

    private void actualizarPrecio() {
        Producto producto = (Producto) cmbProducto.getSelectedItem();

        if (producto != null) {
            lblPrecio.setText(String.format("$%.2f", producto.getPrecio()));
        } else {
            lblPrecio.setText("$0.00");
        }
    }

    private void agregarProducto() {
        Producto producto = (Producto) cmbProducto.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        int cantidad;

        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad válida.");
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.");
            return;
        }

        if (cantidad > producto.getCantidad()) {
            JOptionPane.showMessageDialog(this, "No hay suficiente stock.");
            return;
        }

        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto().getId().equals(producto.getId())) {
                int nuevaCantidad = detalle.getCantidad() + cantidad;

                if (nuevaCantidad > producto.getCantidad()) {
                    JOptionPane.showMessageDialog(this, "No hay suficiente stock.");
                    return;
                }

                detalle.setCantidad(nuevaCantidad);
                mostrarDetalles();
                return;
            }
        }

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());

        detalles.add(detalle);
        mostrarDetalles();
    }

    private void mostrarDetalles() {
        modelo.setRowCount(0);

        for (DetalleVenta detalle : detalles) {
            modelo.addRow(new Object[]{
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getPrecioUnitario(),
                detalle.getCantidad(),
                detalle.calcularSubtotal()
            });
        }

        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;

        for (DetalleVenta detalle : detalles) {
            total += detalle.calcularSubtotal();
        }

        lblTotal.setText(String.format("$%.2f", total));
    }

    private void quitarProducto() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.");
            return;
        }

        detalles.remove(fila);
        mostrarDetalles();
    }

    private void registrarVenta() {
        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.");
            return;
        }

        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto.");
            return;
        }

        try {
            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setFecha(java.time.LocalDate.now());

            ventaDAO.crear(venta);

            for (DetalleVenta detalle : detalles) {
                detalle.setVenta(venta);
                detalleDAO.crear(detalle);
            }

            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");

            detalles.clear();
            modelo.setRowCount(0);
            actualizarTotal();

            cargarDatos();
            cargarVentas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo completar la venta.\n" + e.getMessage()
            );
        }
    }

    private void cargarVentas() {
        modeloVentas.setRowCount(0);
        modeloDetalles.setRowCount(0);

        List<Venta> ventas = ventaDAO.obtenerTodas();
        List<DetalleVenta> todosLosDetalles = detalleDAO.obtenerTodos();

        for (Venta venta : ventas) {
            double total = 0;

            for (DetalleVenta detalle : todosLosDetalles) {
                if (detalle.getVenta() != null
                        && detalle.getVenta().getId().equals(venta.getId())) {
                    total += detalle.calcularSubtotal();
                }
            }

            modeloVentas.addRow(new Object[]{
                venta.getId(),
                venta.getFecha(),
                venta.getCliente().getNombre(),
                String.format("$%.2f", total)
            });
        }
    }
        private void cargarVentasPorCliente(Integer idCliente) {
            modeloVentas.setRowCount(0);
            modeloDetalles.setRowCount(0);

            try {
                List<Venta> ventas = ventaDAO.buscarPorCliente(idCliente);
                List<DetalleVenta> todosLosDetalles = detalleDAO.obtenerTodos();

                for (Venta venta : ventas) {
                    double total = 0;

                    for (DetalleVenta detalle : todosLosDetalles) {
                        if (detalle.getVenta() != null
                                && detalle.getVenta().getId().equals(venta.getId())) {
                            total += detalle.calcularSubtotal();
                        }
                    }

                    modeloVentas.addRow(new Object[]{
                        venta.getId(),
                        venta.getFecha(),
                        venta.getCliente().getNombre(),
                        String.format("$%.2f", total)
                    });
                }

                if (ventas.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this, "Este cliente no tiene ventas registradas.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al consultar las ventas del cliente.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    private void cargarDetallesVenta() {
        modeloDetalles.setRowCount(0);

        int fila = tablaVentas.getSelectedRow();

        if (fila == -1) {
            return;
        }

        Integer idVenta = (Integer) modeloVentas.getValueAt(fila, 0);

        for (DetalleVenta detalle : detalleDAO.obtenerTodos()) {
            if (detalle.getVenta() != null
                    && detalle.getVenta().getId().equals(idVenta)) {
                modeloDetalles.addRow(new Object[]{
                    detalle.getProducto().getNombre(),
                    String.format("$%.2f", detalle.getPrecioUnitario()),
                    detalle.getCantidad(),
                    String.format("$%.2f", detalle.calcularSubtotal())
                });
            }
        }
    }
}
