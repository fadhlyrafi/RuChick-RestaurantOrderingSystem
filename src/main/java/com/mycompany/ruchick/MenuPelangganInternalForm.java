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
import java.sql.Blob;
import java.sql.SQLException;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Muhamad Fadhly
 */
public class MenuPelangganInternalForm extends javax.swing.JInternalFrame {

    /**
     * Creates new form MenuPelangganInternalForm
     */
    public static String kategori;
    public static int order_id;
    public int rowCount = 1;
    public int colCount = 3;
    private RuchickMenu parent;
    public MenuPelangganInternalForm(String kategori, int order_id, RuchickMenu parent) {
        try {
            // Koneksi mySQL
            Connection penghubungdatabase = (Connection)koneksi_database.konfigurasi_database();

            // Query untuk mengambil data dari menu_items
            String sql_select = "SELECT menu_item_id, name, image_id, price, description, category, stock, units FROM menu_items";
            if (!kategori.isEmpty()) {
                sql_select = "SELECT menu_item_id, name, image_id, price, description, category, stock, units FROM menu_items WHERE category = '" + kategori +"'";
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
        this.order_id = order_id;
        this.parent = parent;
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
            String sql_select = "SELECT menu_item_id, name, image_id, price, description, category, stock, units FROM menu_items";
            if (!kategori.isEmpty()) {
                sql_select = "SELECT menu_item_id, name, image_id, price, description, category, stock, units FROM menu_items WHERE category = '" + kategori + "'";
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
                final int idMenu = resultSet.getInt("menu_item_id");
                final String name = resultSet.getString("name");
                final int price = resultSet.getInt("price");
                final String description = resultSet.getString("description");
                Blob image = resultSet.getBlob("image_id");
                byte[] imageBytes = image.getBytes(1, (int) image.length());
                ImageIcon originalIcon = new ImageIcon(imageBytes);
                Image resizedImage = originalIcon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImage);
                
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk fleksibilitas
                Color noSelected = new Color(139,146,178);
                itemPanel.setBorder(new CompoundBorder(  
                        new LineBorder(noSelected, 3),
                        new EmptyBorder(0, 0, 0, 0)
                ));
                itemPanel.setBackground(new Color(245, 245, 245)); // Background warna terang

                // Menambahkan MouseListener ke itemPanel
                itemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {     
                    // Membuat panel untuk JOptionPane kustom
                    JPanel dialogPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints dialogGbc = new GridBagConstraints();
                    dialogGbc.insets = new Insets(10, 10, 10, 10);
                    dialogGbc.fill = GridBagConstraints.HORIZONTAL;
                    dialogGbc.gridx = 0;
                    dialogGbc.gridy = 0;

                    JPanel imagePanel = new JPanel(); // Panel untuk menampilkan gambar
                    JLabel imageLabel = new JLabel(); // Label untuk gambar
                    ImageIcon largeIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Membuat gambar lebih besar
                    imageLabel.setIcon(largeIcon); // Menetapkan gambar ke label
                    imagePanel.add(imageLabel); // Menambahkan label ke panel
                    dialogPanel.add(imagePanel, dialogGbc); // Menambahkan panel gambar ke panel utama
                    
                    dialogGbc.gridy++;
                    JLabel dialogNameLabel = new JLabel(name+" ("+description+")");
                    dialogPanel.add(dialogNameLabel, dialogGbc);
                    
                    //tampilkan harga
                    dialogGbc.gridy++;
                    JLabel dialogPriceLabel = new JLabel("Rp " + price);
                    dialogPanel.add(dialogPriceLabel, dialogGbc);
                    
                    dialogGbc.gridy++;
                    JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel quantityLabel = new JLabel("Jumlah:");
                    SpinnerNumberModel SpinnerModel = new SpinnerNumberModel(1,1, Integer.MAX_VALUE, 1);
                    JSpinner quantitySpinner = new JSpinner(SpinnerModel);
                    quantityPanel.add(quantityLabel);
                    quantityPanel.add(quantitySpinner);
                    dialogPanel.add(quantityPanel, dialogGbc);
                    
                    boolean validInput = false;
                    while (!validInput) {
                        // Menampilkan JOptionPane dengan panel yang telah dibuat
                        int option = JOptionPane.showConfirmDialog(null, dialogPanel, "Order Menu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (option == JOptionPane.OK_OPTION) {
                            try {
                                int quantity = (int) quantitySpinner.getValue();
                                if (quantity <= 0) {
                                    JOptionPane.showMessageDialog(null, "Harus memilih setidaknya 1", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    // Simpan data ke database
                                    Connection connection = koneksi_database.konfigurasi_database();
                                    String insertOrderSQL = "INSERT INTO order_details (menu_item_id, order_id, quantityOrdered, priceEach) VALUES (?, ?, ?, ?)";
                                    PreparedStatement insertStatement = connection.prepareStatement(insertOrderSQL);
                                    insertStatement.setInt(1, idMenu);
                                    insertStatement.setInt(2, order_id);
                                    insertStatement.setInt(3, quantity);
                                    insertStatement.setDouble(4, price);
                                    JOptionPane.showMessageDialog(null, insertStatement);
                                    insertStatement.execute();
                                    JOptionPane.showMessageDialog(null, "Berhasil ditambahkan");
                                    parent.baca_data_order();
                                    validInput = true;
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Invalid quantity entered. Please enter a valid number.");
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                            }
                        } else {
                            validInput = true; // Pengguna membatalkan dialog
                        }
                    } }


                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Color selectedColor = new Color(40, 40, 100);
                        itemPanel.setBorder(new CompoundBorder(
                                new LineBorder(selectedColor, 3),
                                new EmptyBorder(0, 0, 0, 0)
                        ));
                        itemPanel.setBackground(new Color(243,243,244));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        Color noSelected = new Color(139,146,178);
                        itemPanel.setBorder(new CompoundBorder(
                                new LineBorder(noSelected, 3),
                                new EmptyBorder(0, 0, 0, 0)
                        ));
                        itemPanel.setBackground(new Color(245, 245, 245));
                    }
                    
                });

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.NORTHWEST;

                
                JLabel imageLabel = new JLabel(resizedIcon);

                itemPanel.add(imageLabel, gbc);
                gbc.gridy++;
                
                itemPanel.add(createLabel(name, true), gbc);
                gbc.gridy++;

                itemPanel.add(createLabel("Rp " + price, false), gbc);
                gbc.gridy++;

                // Mengatur ukuran tetap untuk itemPanel
                Dimension panelSize = new Dimension(180, 260); // Atur ukuran sesuai kebutuhan Anda
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
        setPreferredSize(new java.awt.Dimension(713, 618));

        scrollPane.setBackground(new java.awt.Color(255, 255, 255));
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scrollPane.setPreferredSize(new java.awt.Dimension(703, 464));

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelContainer.setLayout(new java.awt.GridLayout(rowCount, 3, 10, 10));
        scrollPane.setViewportView(panelContainer);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelContainer;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
