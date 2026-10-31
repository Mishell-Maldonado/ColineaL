
package com.colineal.colinealcrud;

import javax.swing.*;

public class ColinealCRUD {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame menu = new JFrame("Sistema Colineal");
            menu.setSize(480, 160);
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menu.setLayout(new java.awt.GridBagLayout());

            JButton btnProductos = new JButton("Productos");
            JButton btnClientes = new JButton("Clientes");
            JButton btnVentas = new JButton("Ventas");

            btnProductos.setPreferredSize(new java.awt.Dimension(130, 40));
            btnClientes.setPreferredSize(new java.awt.Dimension(130, 40));
            btnVentas.setPreferredSize(new java.awt.Dimension(130, 40));

            btnProductos.addActionListener(e ->
                    new VentanaProductos().setVisible(true));

            btnClientes.addActionListener(e ->
                    new VentanaClientes().setVisible(true));

            btnVentas.addActionListener(e ->
                    new VentanaVentas().setVisible(true));

            JPanel panel = new JPanel();
            panel.add(btnProductos);
            panel.add(btnClientes);
            panel.add(btnVentas);

            menu.add(panel);
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        });
    }
}
