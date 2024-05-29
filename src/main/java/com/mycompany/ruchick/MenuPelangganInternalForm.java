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
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Muhamad Fadhly
 */
public class MenuPelangganInternalForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form MenuPelangganInternalForm
     */
    public String kategori;
    public int rowCount = 1;
    public int colCount = 3;
    public MenuPelangganInternalForm(String kategori) {
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
            while (resultSet.next()) {
                rowCount++;
            }
            if (rowCount <= 3) {
                colCount = 2;
            }
            rowCount = rowCount/3;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        this.kategori = kategori;
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        ui.setEastPane(null);
        ui.setSouthPane(null);
        ui.setWestPane(null);
        initComponents();
        tambahPanelDariDatabase();
    }
    
    public void tambahPanelDariDatabase() {
        try {
            // Koneksi mySQL
            Connection penghubungdatabase = (Connection) koneksi_database.konfigurasi_database();

            // Query untuk mengambil data dari menu_items
            String sql_select = "SELECT menu_item_id, name, price, description, category, stock, units FROM menu_items";
            if (!kategori.isEmpty()) {
                sql_select = "SELECT menu_item_id, name, price, description, category, stock, units FROM menu_items WHERE category = '" + kategori + "'";
            }
            PreparedStatement query_select = penghubungdatabase.prepareStatement(sql_select);
            ResultSet resultSet = query_select.executeQuery();

            panelContainer.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk panelContainer
            GridBagConstraints containerGbc = new GridBagConstraints();
            containerGbc.insets = new Insets(10, 10, 10, 10);
            containerGbc.fill = GridBagConstraints.HORIZONTAL;
            containerGbc.anchor = GridBagConstraints.NORTHWEST; // Memastikan panel terisi dari sebelah kiri atas

            int columnCount = 3; // Jumlah kolom
            int currentColumn = 0;
            int currentRow = 0;

            while (resultSet.next()) {
                int idMenu = resultSet.getInt("menu_item_id");
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk fleksibilitas
                itemPanel.setBorder(new CompoundBorder(
                        new LineBorder(Color.GRAY, 2),
                        new EmptyBorder(0, 0, 0, 0)
                ));
                itemPanel.setBackground(new Color(245, 245, 245)); // Background warna terang

                // Menambahkan MouseListener ke itemPanel
                itemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Aksi ketika panel ditekan
                        DaftarPesanan daftarPopUp = new DaftarPesanan();
                        JOptionPane.showMessageDialog(null, "Panel diklik: " + idMenu);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Color selectedColor = new Color(40, 40, 100);
                        itemPanel.setBorder(new CompoundBorder(
                                new LineBorder(selectedColor, 1),
                                new EmptyBorder(0, 0, 0, 0)
                        ));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        itemPanel.setBorder(new CompoundBorder(
                                new LineBorder(Color.GRAY, 1),
                                new EmptyBorder(0, 0, 0, 0)
                        ));
                    }
                });

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.NORTHWEST;

                // Mengambil data dari result set
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("description");

                itemPanel.add(createLabel(name, false), gbc);
                gbc.gridy++;

                itemPanel.add(createLabel("Rp " + price, false), gbc);
                gbc.gridy++;

                JTextArea descriptionArea = createTextArea(description);
                itemPanel.add(descriptionArea, gbc);

                // Mengatur ukuran tetap untuk itemPanel
                Dimension panelSize = new Dimension(200, 150); // Atur ukuran sesuai kebutuhan Anda
                itemPanel.setPreferredSize(panelSize);
                itemPanel.setMinimumSize(panelSize);
                itemPanel.setMaximumSize(panelSize);

                // Menentukan posisi gridx dan gridy untuk panel
                containerGbc.gridx = currentColumn;
                containerGbc.gridy = currentRow;

                // Menambahkan panel item ke container
                panelContainer.add(itemPanel, containerGbc);

                // Mengatur posisi kolom dan baris berikutnya
                currentColumn++;
                if (currentColumn >= columnCount) {
                    currentColumn = 0;
                    currentRow++;
                }
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
            label.setFont(new Font("Arial", Font.BOLD, 12));
        } else {
            label.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        return label;
    }
    
    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
//        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder()); // Menghilangkan border
        textArea.setBackground(new Color(245, 245, 245)); // Sesuaikan dengan warna background
        textArea.setFont(new Font("Arial", Font.PLAIN, 12)); // Sesuaikan font dengan label
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

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));

        scrollPane.setBackground(new java.awt.Color(255, 255, 255));
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new java.awt.Dimension(703, 464));

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelContainer.setLayout(new java.awt.GridLayout(rowCount, 3, 0, 10));
        scrollPane.setViewportView(panelContainer);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelContainer;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
