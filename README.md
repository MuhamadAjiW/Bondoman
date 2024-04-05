# BondoMan

> Tugas Besar 1 IF3210 Pengembangan Aplikasi pada Platform Khusus - Kelompok GUY

## Deskripsi Aplikasi

**BondoMan** merupakan aplikasi manajemen keuangan yang dibangun khusus untuk platform Android. Aplikasi ini dibangun dengan menggunakan Android Studio. Bahasa pemrograman yang digunakan adalah Kotlin dengan minimum SDK 29 dan target SDK 32.<br/>

Dalam aplikasi ini, pengguna dapat melakukan hal-hal berikut.

- Menavigasi aplikasi melalui header dan navbar
- Login dan logout
- Menambahkan, mengubah, dan menghapus transaksi
- Melihat daftar transaksi yang sudah dilakukan
- Scan nota
- Melihat graf rangkuman transaksi
- Menyimpan daftar transaksi dalam format .xlsx, .xls
- Mengirim file daftar transaksi (dalam format .xlsx, .xls) melalui Gmail
- Melakukan randomize transaksi
- Membuat twibbon

## Library

- Android Core
- Room
- Retrofit
- Fused Location Client
- Recycler View
- Data Faker
- dll.


## Screenshot Aplikasi

### Splash Screen
![alt text](image.png)

### Login
![alt text](image-1.png)

### Transaksi
![alt text](image-2.png)

### Tambah Transaksi
![alt text](image-3.png)

### Scan Nota
![alt text](image-4.png)

![alt text](image-7.png)

### Graf

#### Potrait

![alt text](image-5.png)

#### Landscape

![alt text](image-6.png)

### Settings
![alt text](image-8.png)

### Twibbon
![alt text](screenshots/settings-ss.png)

## Bonus

### Twibbon

Lihat pada bagian [Twibbon](#twibbon).

### Accessibility Testing

#### Login

##### Suggestion sebelum Diperbaiki
![alt text](screenshots/login.png)

##### Perbaikan yang Dilakukan
- Mengubah layout tengah menjadi match_parent lalu menambahkan padding horizontal
- Mengubah width height EditText password menjadi wrap_content namun memberikan atribut maxHeight dan scrollHorizontally true
##### Suggestion setelah Diperbaiki
![alt text](screenshots/login_after.png)
#### Transaksi

##### Suggestion sebelum Diperbaiki
![alt text](screenshots/transaction.png)
##### Perbaikan yang Dilakukan
- Menambah id pada contentDescription yang sama
##### Suggestion setelah Diperbaiki
![alt text](screenshots/transaction_after.png)
#### Tambah Transaksi

##### Suggestion sebelum Diperbaiki
![alt text](screenshots/add_transaction.png)
##### Perbaikan yang Dilakukan
- Mengubah width height EditText dan Spinner menjadi wrap_content namun memberikan atribut maxHeight dan scrollHorizontally true
##### Suggestion setelah Diperbaiki
![alt text](screenshots/add_transaction_after.png)

#### Scan Nota

##### Suggestion sebelum Diperbaiki
![alt text](screenshots/scan.png)
##### Perbaikan yang Dilakukan
- Menambah contentDescription pada header
- Mengubah width height Button menjadi wrap_content namun memberikan atribut maxHeight dan maxWidth
##### Suggestion setelah Diperbaiki
![alt text](screenshots/scan_after.png)

#### Graf

##### Suggestion sebelum Diperbaiki
![alt text](screenshots/stats.png)
##### Perbaikan yang Dilakukan
- Menambah contentDescription pada header
##### Suggestion setelah Diperbaiki
![alt text](screenshots/stats_after.png)

#### Settings

##### Suggestion sebelum Diperbaiki
![alt text](screenshots/settings.png)
##### Perbaikan yang Dilakukan
- Menambah contentDescription pada header
##### Suggestion setelah Diperbaiki
![alt text](screenshots/settings_after.png)

## Pembagian Kerja

| Pekerjaan                                                                | NIM                          |
| ------------------------------------------------------------------------ | ---------------------------- |
| Header dan Navbar                                                        | 13521095                     |
| Login - Halaman Login                                                    | 13521129                     |
| Logout - Halaman Pengaturan                                              | 13521129                     |
| Melakukan Penambahan, Pengubahan, dan Penghapusan Transaksi              | 13521095                     |
| Melihat Daftar Transaksi yang Sudah Dilakukan                            | 13521129                     |
| Melakukan Scan Nota - Halaman Scan Nota                                  | 13521149                     |
| Melihat Graf Rangkuman Transaksi - Halaman Graf                          | 13521129                     |
| Menyimpan Daftar Transaksi dalam Format .xlsx, .xls - Halaman Pengaturan | 13521095                     |
| Intent GMail - Halaman Pengaturan                                        | 13521095                     |
| Background Service - Mengecek expiry JWT                                 | 13521129                     |
| Network Sensing - Deteksi Sinyal                                         | 13521149                     |
| Broadcast Receiver - Randomize Transaksi dari Pengaturan                 | 13521095, 13521129           |
| Bonus: Twibbon                                                           | 13521149                     |
| Bonus: Accessibility Testing                                             | 13521095, 13521129, 13521149 |

## Durasi Persiapan dan Pengerjaan

| NIM      | Durasi Persiapan (jam) | Durasi Pengerjaan (jam) |
| -------- | ---------------------- | ----------------------- |
| 13521095 | 24                     | 48                      |
| 13521129 | 24                     | 48                      |
| 13521149 | 24                     | 48                      |

## Authors

| NIM      | Nama                 |
| -------- | -------------------- |
| 13521095 | Muhamad Aji Wibisono |
| 13521129 | Chiquita Ahsanunnisa |
| 13521149 | Rava Maulana Azzikri |
