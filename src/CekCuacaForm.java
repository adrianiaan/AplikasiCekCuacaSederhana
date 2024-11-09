
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ADRIAN WIN
 */
public class CekCuacaForm extends javax.swing.JFrame {

    // Konstanta API dan path file
    private static final String API_KEY = "9d7f996d807b774f51d779d4e736fb32"; // API key untuk OpenWeatherMap
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather"; // URL API cuaca
    private static final String CITY_LIST_FILE = System.getProperty("user.dir") + "/data/kota_pilihan.txt"; // Path untuk daftar kota
    private static final String CUACA_DATA_FILE = System.getProperty("user.dir") + "/data/cuaca_data.csv"; // Path untuk data cuaca

    private DefaultTableModel modelTabelCuaca; // Model tabel untuk menampilkan data cuaca
    
    // Metode untuk mengecek dan membuat folder 'data' jika belum ada
    private void cekAtauBuatFolderData() {
        File folder = new File(System.getProperty("user.dir") + "/data");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    public CekCuacaForm() {
        initComponents();
        cekAtauBuatFolderData(); // Cek atau buat folder 'data'
        muatKotaDariFile(); // Muat data kota yang tersimpan dari file
        modelTabelCuaca = new DefaultTableModel(new String[]{"Kota", "Cuaca", "Suhu"}, 0);
        jTable1.setModel(modelTabelCuaca); // Pasang model tabel pada jTable1
    }

    // Ambil data cuaca dari API berdasarkan kota
    private String ambilDataCuaca(String kota) {
        String result = "";
        try {
            // Buat URL lengkap dengan parameter API dan kota
            String urlString = API_URL + "?q=" + kota + ",id&appid=" + API_KEY + "&units=metric&lang=id";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Periksa respon dari server
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                result = sb.toString();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mendapatkan data cuaca. Kode respons: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan koneksi: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    // Tampilkan data cuaca di GUI berdasarkan kota
    private void tampilkanCuaca(String kota) {
        String jsonResponse = ambilDataCuaca(kota);
        JSONObject json = new JSONObject(jsonResponse);

        // Ambil deskripsi cuaca dan suhu dari JSON
        String weatherDesc = json.getJSONArray("weather").getJSONObject(0).getString("description");
        String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");
        double temperature = json.getJSONObject("main").getDouble("temp");

        lblKondisiCuaca.setText(weatherDesc);
        lblSuhu.setText(temperature + "°C");

        // Ambil URL ikon cuaca dan tampilkan
        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        try {
            ImageIcon weatherIcon = new ImageIcon(new URL(iconUrl));
            lblIconCuaca.setIcon(weatherIcon);
        } catch (Exception e) {
            e.printStackTrace();
            lblIconCuaca.setText("Gagal memuat ikon");
        }
    }

    // Simpan kota yang ada di combobox ke dalam file
    private void simpanKotaKeFile() {
        // Membuat folder 'data' jika belum ada
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir(); // Membuat folder
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CITY_LIST_FILE))) {
            for (int i = 0; i < cmbKota.getItemCount(); i++) {
                writer.write(cmbKota.getItemAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Muat kota  dari file
    private void muatKotaDariFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CITY_LIST_FILE))) {
            String city;
            while ((city = reader.readLine()) != null) {
                cmbKota.addItem(city);
            }
        } catch (IOException e) {
            System.out.println("Tidak ada kota yang tersimpan.");
        }
    }

    // Simpan data cuaca ke dalam file CSV
    private void simpanDataCuacaKeFile(String kota, String deskripsiCuaca, double suhu) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUACA_DATA_FILE, true))) {
            writer.write(kota + "," + deskripsiCuaca + "," + suhu + "°C");
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Data cuaca berhasil disimpan!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data.");
            e.printStackTrace();
        }
    }
    
    private void isiKota() {
        String[] kotaIndonesia = {"Jakarta", "Surabaya", "Bandung", "Medan", "Yogyakarta", "Makassar"};
        for (String kota : kotaIndonesia) {
            cmbKota.addItem(kota);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        txtKota = new javax.swing.JTextField();
        btnCekCuaca = new javax.swing.JButton();
        cmbKota = new javax.swing.JComboBox<>();
        lblIconCuaca = new javax.swing.JLabel();
        lblKondisiCuaca = new javax.swing.JLabel();
        lblSuhu = new javax.swing.JLabel();
        btnMuatData = new javax.swing.JButton();
        btnSimpanData = new javax.swing.JButton();
        lblNamaKota = new javax.swing.JLabel();
        lblPilihKota = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplikasi Cuaca Sederhana", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Roboto", 1, 24), new java.awt.Color(0, 102, 255))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        txtKota.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        txtKota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKotaMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(txtKota, gridBagConstraints);

        btnCekCuaca.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnCekCuaca.setText("Cek Cuaca");
        btnCekCuaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekCuacaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnCekCuaca, gridBagConstraints);

        cmbKota.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cmbKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbKotaItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(cmbKota, gridBagConstraints);

        lblIconCuaca.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblIconCuaca, gridBagConstraints);

        lblKondisiCuaca.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblKondisiCuaca, gridBagConstraints);

        lblSuhu.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblSuhu, gridBagConstraints);

        btnMuatData.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnMuatData.setText("Muat Data Cuaca");
        btnMuatData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuatDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnMuatData, gridBagConstraints);

        btnSimpanData.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnSimpanData.setText("Simpan Data");
        btnSimpanData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnSimpanData, gridBagConstraints);

        lblNamaKota.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        lblNamaKota.setText("Masukan Nama Kota");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblNamaKota, gridBagConstraints);

        lblPilihKota.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        lblPilihKota.setText("Pilih Kota");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblPilihKota, gridBagConstraints);

        jTable1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel1.setText("Informasi Cuaca");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel2.setText("Kondisi Cuaca :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel3.setText("Suhu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCekCuacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekCuacaActionPerformed
        String kota = txtKota.getText();
        if (!kota.isEmpty()) {
            tampilkanCuaca(kota);

            boolean kotaAda = false;
            for (int i = 0; i < cmbKota.getItemCount(); i++) {
                if (cmbKota.getItemAt(i).equalsIgnoreCase(kota)) {
                    kotaAda = true;
                    break;
                }
            }

            if (!kotaAda) {
                cmbKota.addItem(kota);
                simpanKotaKeFile(); // Simpan kota ke file setelah menambahkannya
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama kota!");
        }
    }//GEN-LAST:event_btnCekCuacaActionPerformed

    private void cmbKotaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbKotaItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String selectedCity = (String) cmbKota.getSelectedItem();
            txtKota.setText(selectedCity);
            tampilkanCuaca(selectedCity);
        }
    }//GEN-LAST:event_cmbKotaItemStateChanged

    private void btnSimpanDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanDataActionPerformed
        String kota = txtKota.getText();
        String deskripsiCuaca = lblKondisiCuaca.getText();
        String suhuStr = lblSuhu.getText().replace("°C", "");

        if (!kota.isEmpty() && !deskripsiCuaca.isEmpty() && !suhuStr.isEmpty()) {
            double suhu = Double.parseDouble(suhuStr);
            simpanDataCuacaKeFile(kota, deskripsiCuaca, suhu);
        } else {
            JOptionPane.showMessageDialog(this, "Data cuaca tidak lengkap. Pastikan semua data sudah terisi.");
        }
    }//GEN-LAST:event_btnSimpanDataActionPerformed

    private void btnMuatDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuatDataActionPerformed
        try (BufferedReader reader = new BufferedReader(new FileReader("data/cuaca_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    // Menambahkan data ke dalam tabel
                    modelTabelCuaca.addRow(new Object[]{data[0], data[1], data[2]});
                }
            }
            JOptionPane.showMessageDialog(this, "Data cuaca berhasil dimuat ke tabel.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data cuaca.");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMuatDataActionPerformed

    private void txtKotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKotaMouseClicked
        // Cek apakah `txtKota` mengandung data yang valid sebelum dikosongkan
        String kota = txtKota.getText();
        if (!kota.isEmpty()) {
            boolean kotaAda = false;

            // Cek apakah kota sudah ada di `cmbKota`
            for (int i = 0; i < cmbKota.getItemCount(); i++) {
                if (cmbKota.getItemAt(i).equalsIgnoreCase(kota)) {
                    kotaAda = true;
                    break;
                }
            }

            // Tambahkan kota ke `cmbKota` jika belum ada, kemudian simpan
            if (!kotaAda) {
                cmbKota.addItem(kota);
                simpanKotaKeFile(); // Panggil metode simpan setelah menambahkan kota baru
            }
        }

        // Kosongkan teks pada `txtKota`
        txtKota.setText("");
    }//GEN-LAST:event_txtKotaMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CekCuacaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CekCuacaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CekCuacaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CekCuacaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CekCuacaForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCekCuaca;
    private javax.swing.JButton btnMuatData;
    private javax.swing.JButton btnSimpanData;
    private javax.swing.JComboBox<String> cmbKota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblIconCuaca;
    private javax.swing.JLabel lblKondisiCuaca;
    private javax.swing.JLabel lblNamaKota;
    private javax.swing.JLabel lblPilihKota;
    private javax.swing.JLabel lblSuhu;
    private javax.swing.JTextField txtKota;
    // End of variables declaration//GEN-END:variables
}
