/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.ruchick;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 *
 * @author Muhamad Fadhly
 */
public class MenuPelangganInternalForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form MenuPelangganInternalForm
     */
    public String kategori;
    public MenuPelangganInternalForm(String kategori) {
        this.kategori = kategori;
        initComponents();
        tambahPanelDariDatabase();
    }
    
    public void tambahPanelDariDatabase() {
        try {
            // Koneksi mySQL
            Connection penghubungdatabase = (Connection)koneksi_database.konfigurasi_database();

            // Query untuk mengambil data dari menu_items
            String sql_select = "SELECT menu_item_id, name, price, description, category, stock, units FROM menu_items";
            if (!kategori.isEmpty()) {
                sql_select = "SELECT menu_item_id, name, price, description, category, stock, units FROM menu_items WHERE category = '" + kategori +"'";
            }
            PreparedStatement query_select = penghubungdatabase.prepareStatement(sql_select);
            ResultSet resultSet = query_select.executeQuery();

            // Membuat panel untuk setiap item
            while (resultSet.next()) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk fleksibilitas
                itemPanel.setSize(new Dimension(50, 250));
                itemPanel.setBorder(new CompoundBorder(
                        new LineBorder(Color.GRAY, 1),
                        new EmptyBorder(10, 10, 10, 10)
                ));
                itemPanel.setBackground(new Color(245, 245, 245)); // Background warna terang

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.EAST;

                // Mengambil data dari result set
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("description");

                // Menambahkan data ke panel dengan label yang estetik
//                itemPanel.add(createLabel("Nama:", true), gbc);
//                gbc.gridx = 1;
                itemPanel.add(createLabel(name, false), gbc);
                gbc.gridx = 0;
                gbc.gridy++;

//                itemPanel.add(createLabel("Harga:", true), gbc);
//                gbc.gridx = 1;
                itemPanel.add(createLabel("Rp " + price, false), gbc);
                gbc.gridx = 0;
                gbc.gridy++;

//                itemPanel.add(createLabel("Deskripsi:", true), gbc);
//                gbc.gridx = 1;
                JTextArea descriptionArea = createTextArea(description);
                itemPanel.add(descriptionArea, gbc);
                gbc.gridx = 0;
                
                // Menambahkan panel item ke container
                panelContainer.add(itemPanel);
                panelContainer.add(Box.createVerticalStrut(10)); // Spasi antar itemPanel
            }

            // Refresh panel container untuk menampilkan perubahan
            panelContainer.revalidate();
            panelContainer.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Kesalahan: " + e);
        }
    }

    private JLabel createLabel(String text, boolean isTitle) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        if (isTitle) {
            label.setFont(new Font("Arial", Font.BOLD, 14));
        } else {
            label.setFont(new Font("Arial", Font.PLAIN, 14));
        }
        return label;
    }
    
    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder()); // Menghilangkan border
        textArea.setBackground(new Color(245, 245, 245)); // Sesuaikan dengan warna background
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Sesuaikan font dengan label
        textArea.setForeground(Color.BLACK); // Sesuaikan warna teks dengan label
        return textArea;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        panelContainer = new javax.swing.JPanel();

        scrollPane.setBackground(new java.awt.Color(255, 255, 255));
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelContainer.setLayout(new java.awt.GridLayout(10, 2, 10, 10));
        scrollPane.setViewportView(panelContainer);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelContainer;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
