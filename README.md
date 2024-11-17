# Aplikasi Cek Cuaca  
Tugas 6 - Adrian Akhmad Firdaus (2210010491)  

---

## Deskripsi  

Aplikasi ini digunakan untuk menampilkan informasi cuaca secara real-time berdasarkan lokasi yang dipilih pengguna. Data cuaca diambil dari API eksternal OpenWeatherMap dan ditampilkan dalam antarmuka yang mencakup ikon cuaca, deskripsi kondisi cuaca, serta suhu. Aplikasi juga memiliki fitur untuk menyimpan lokasi favorit dan data cuaca yang dapat dimuat kembali kapan saja.

---

## Fitur Aplikasi  

1. **Pengecekan Cuaca Real-Time**:  
   - Mengambil data cuaca dari API OpenWeatherMap berdasarkan input lokasi pengguna.  
   - Menampilkan deskripsi cuaca, ikon cuaca, dan suhu.  

2. **Daftar Lokasi Favorit**:  
   - Menyimpan lokasi favorit yang sering dicek ke dalam file untuk dipilih kembali melalui `JComboBox`.  

3. **Simpan dan Muat Data Cuaca**:  
   - Menyimpan data cuaca ke dalam file CSV.  
   - Memuat data cuaca yang tersimpan dan menampilkannya di `JTable`.  

---

## Komponen GUI  

Aplikasi ini menggunakan komponen berikut:  
- **JFrame**: Jendela utama aplikasi.  
- **JPanel**: Panel untuk menata elemen GUI.  
- **JLabel**: Label untuk menampilkan informasi cuaca dan ikon.  
- **JTextField**: Input nama kota.  
- **JButton**: Tombol untuk mengecek cuaca, menyimpan data, dan memuat data.  
- **JComboBox**: Memilih lokasi favorit.  
- **JTable**: Menampilkan data cuaca yang dimuat dari file.  

---

## Logika Program  

1. **Mengambil Data Cuaca dari API**  
   Menggunakan HTTP request untuk mengakses API OpenWeatherMap. Respon dalam format JSON diproses untuk mendapatkan deskripsi cuaca, kode ikon, dan suhu.  

   **Kode Implementasi**:  
   ```java
   private String ambilDataCuaca(String kota) {
       String result = "";
       try {
           String urlString = API_URL + "?q=" + kota + ",id&appid=" + API_KEY + "&units=metric&lang=id";
           URL url = new URL(urlString);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");

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
   ```  

2. **Menampilkan Data Cuaca**  
   Setelah data diambil dari API, JSON diproses untuk menampilkan informasi cuaca dalam bentuk deskripsi, ikon, dan suhu.  

   **Kode Implementasi**:  
   ```java
   private void tampilkanCuaca(String kota) {
       String jsonResponse = ambilDataCuaca(kota);
       JSONObject json = new JSONObject(jsonResponse);

       String weatherDesc = json.getJSONArray("weather").getJSONObject(0).getString("description");
       String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");
       double temperature = json.getJSONObject("main").getDouble("temp");

       lblKondisiCuaca.setText(weatherDesc);
       lblSuhu.setText(temperature + "°C");

       String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
       try {
           ImageIcon weatherIcon = new ImageIcon(new URL(iconUrl));
           lblIconCuaca.setIcon(weatherIcon);
       } catch (Exception e) {
           lblIconCuaca.setText("Gagal memuat ikon");
       }
   }
   ```  

3. **Simpan dan Muat Data Cuaca**  
   Data cuaca yang ditampilkan dapat disimpan ke dalam file CSV dan dimuat kembali untuk ditampilkan di tabel.  

   **Kode Implementasi**:  
   ```java
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

   private void btnMuatDataActionPerformed(java.awt.event.ActionEvent evt) {
       try (BufferedReader reader = new BufferedReader(new FileReader("data/cuaca_data.csv"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] data = line.split(",");
               if (data.length == 3) {
                   modelTabelCuaca.addRow(new Object[]{data[0], data[1], data[2]});
               }
           }
           JOptionPane.showMessageDialog(this, "Data cuaca berhasil dimuat ke tabel.");
       } catch (IOException e) {
           JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data cuaca.");
           e.printStackTrace();
       }
   }
   ```  

---

## Events  

1. **ActionListener pada Tombol Cek Cuaca**  
   Menampilkan cuaca berdasarkan input kota dan menyimpan kota baru ke daftar favorit jika belum ada.  

   **Kode Implementasi**:  
   ```java
   private void btnCekCuacaActionPerformed(java.awt.event.ActionEvent evt) {
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
               simpanKotaKeFile();
           }
       } else {
           JOptionPane.showMessageDialog(this, "Silakan masukkan nama kota!");
       }
   }
   ```  

2. **ItemListener pada JComboBox**  
   Mengubah data cuaca saat lokasi favorit dipilih.  

   **Kode Implementasi**:  
   ```java
   private void cmbKotaItemStateChanged(java.awt.event.ItemEvent evt) {
       if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
           String selectedCity = (String) cmbKota.getSelectedItem();
           txtKota.setText(selectedCity);
           tampilkanCuaca(selectedCity);
       }
   }
   ```  

---

## Cara Menjalankan Aplikasi  

1. Clone atau unduh proyek ini.  
2. Pastikan memiliki koneksi internet untuk mengakses API OpenWeatherMap.  
3. Jalankan file utama `CekCuacaForm.java` melalui IDE seperti NetBeans atau IntelliJ.  
4. Masukkan nama kota atau pilih dari daftar lokasi favorit, kemudian tekan tombol **Cek Cuaca**.  
5. Simpan data cuaca ke dalam file CSV atau muat data cuaca yang tersimpan untuk ditampilkan di tabel.  

## Tampilan Aplikasi Saat Dijalankan 
![image](https://github.com/user-attachments/assets/db966fb9-d721-455d-a2d1-b55bce22c45d)


---

## Indikator Penilaian  

| No  | Komponen           | Persentase |
|-----|---------------------|------------|
| 1   | Komponen GUI       | 10%        |
| 2   | Logika Program     | 20%        |
| 3   | Events             | 10%        |
| 4   | Kesesuaian UI      | 20%        |
| 5   | Memenuhi Variasi   | 40%        |
| **TOTAL** |               | **100%**   |  

---

## Pembuat  

- **Nama**: Adrian Akhmad Firdaus  
- **NPM**: 2210010491  
- **Kelas**: 5A Reguler Pagi  
- **Tugas**: Tugas 6 - Aplikasi Cek Cuaca  
- **Fakultas**: Fakultas Teknologi Informasi (FTI)  
- **Universitas**: Universitas Islam Kalimantan Muhammad Arsyad Al Banjari Banjarmasin  
