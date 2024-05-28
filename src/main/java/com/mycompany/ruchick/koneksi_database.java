/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ruchick;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Muhamad Fadhly
 */
public class koneksi_database {
    public static Connection koneksi;
    // 0. buat database
    // 1. buat class koneksi mySQL
    public static Connection konfigurasi_database() throws SQLException {
        // Exception Handling -> memvalidasi database apakah berhasil atau tidak
        try {
            // Konfigurasi database
        // 2. Definisikan
        // 3. KOneksinya apakah berhasil atau tidak
            String url = "jdbc:mysql://localhost/db_restaurant";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            koneksi = DriverManager.getConnection(url, "root", "");
            System.out.println("Koneksi Berhasil");
        } catch (Exception e) {
            // Pesan jika database tidak terhubung
            JOptionPane.showMessageDialog(null, "Koneksi Gagal! :" + e);
            System.out.println("Koneksi Gagal!");
            
        }
        
        // Terakhir
        return koneksi;
    }
    public static void main(String[] args) throws SQLException {
        // Menghubungkan mySQL dengan java
        // Connection nama_variabel = nama_kelas koneksi;
        Connection penghubung_database = (Connection)koneksi_database.konfigurasi_database();
    }
}
