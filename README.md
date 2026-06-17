# Coffee Bliss Membership Card App

## Deskripsi
**Coffee Bliss Membership Card App** adalah aplikasi Android berbasis **Jetpack Compose** yang dikembangkan sebagai **Tugas Akhir Mobile Programming**.  
Aplikasi ini mensimulasikan sistem membership untuk coffee shop, mulai dari registrasi member, login, pencatatan transaksi, pengelolaan poin, penukaran reward, hingga tampilan kartu member digital dengan QR code.

Proyek ini dibuat untuk menunjukkan implementasi konsep mobile programming modern pada Android, khususnya:
- UI declarative dengan Jetpack Compose
- Room database
- arsitektur sederhana berbasis Repository dan ViewModel
- navigasi antar halaman dengan Navigation Compose
- state management dan penyimpanan sesi login lokal

## Anggota Tim
- Bayu Nismara Nagatama 5025231152
- Muhammad Rafi Budiman 5025231297


## Tujuan Aplikasi
Aplikasi ini dirancang untuk:
- mempermudah proses pendaftaran dan login member
- menyimpan data member secara lokal
- mencatat transaksi pembelian
- menghitung poin otomatis dari transaksi
- menyediakan fitur redeem reward berdasarkan jumlah poin
- menampilkan kartu member digital yang dapat dipindai melalui QR code
- menyediakan halaman profile untuk update data member

## 📲 Download & Link Proyek
| Kategori | Link |
|---|---|
| **Aplikasi** | [📥 Download APK](https://github.com/Gembut/UAS-PPB/blob/main/apk/CoffeeBliss.apk) |
| **Video Presentasi** | <a href="" target="_blank">🎬 Tonton di YouTube</a> |
| **Presentasi (PDF)** | [📊 Download PPT]() |
| **Laporan (Blog)** | [📝 Lihat Laporan]() |

> **Catatan:** Untuk menginstal APK, pastikan Anda telah mengizinkan "Install from Unknown Sources" di pengaturan HP Anda.

## Fitur Utama
### 1. Splash Screen
- Menampilkan identitas aplikasi saat pertama kali dibuka
- Mengecek status login member secara otomatis

### 2. Register Member
- Registrasi member baru dengan data:
  - nama lengkap
  - email
  - nomor telepon
  - password
- Validasi field kosong
- Validasi duplikasi berdasarkan email atau nomor telepon

### 3. Login Member
- Login menggunakan:
  - email atau nomor telepon
- Penyimpanan sesi login menggunakan `SharedPreferences`

### 4. Digital Membership Card
- Menampilkan informasi member:
  - nama
  - member ID otomatis
  - total poin
  - level member
- Level member dihitung otomatis dari jumlah poin:
  - `Bronze` poin < 200
  - `Silver` poin 200 - 499
  - `Gold` poin >= 500

### 5. QR Code Member
- Ikon QR pada kartu member dapat ditekan
- Menampilkan QR code berukuran besar untuk dipindai
- Menampilkan ringkasan identitas member pada dialog QR pass

### 6. Transaksi Pembelian
- Input nominal transaksi pembelian
- Poin dihitung otomatis dengan rumus:

```text
1 poin = Rp 10.000 transaksi
```

- Riwayat transaksi disimpan ke database lokal
- Waktu transaksi disimpan hingga detik di database
- Waktu yang ditampilkan di antarmuka hanya sampai menit

### 7. Reward Redemption
- Reward dibagi dalam 3 kategori:
  - minuman
  - makanan
  - merch

- Reward hanya bisa ditukar jika poin mencukupi
- Reward yang belum cukup poin akan ditampilkan lebih redup dan tombol redeem tidak aktif

### 8. Riwayat Poin dan Reward
- Menampilkan daftar transaksi pembelian
- Menampilkan aktivitas redeem reward
- Poin bertambah saat transaksi pembelian
- Poin berkurang saat reward ditukar

### 9. Edit Profile
- Member dapat mengubah:
  - nama
  - email
  - nomor telepon
- Logout

## Alur Penggunaan Aplikasi
1. User membuka aplikasi dan melihat splash screen
2. Jika belum login, user masuk ke halaman login/register
3. User melakukan registrasi sebagai member baru
4. User login menggunakan email atau nomor telepon
5. Setelah login, user masuk ke halaman member profile
6. User dapat:
   - melihat kartu member digital
   - membuka QR code member
   - menambahkan transaksi
   - menukar reward
   - membuka halaman profile
7. Data transaksi dan poin akan tersimpan di database lokal

## Struktur Fitur
### Layer Data
- `Member.kt`  
  Model data member
- `Transaction.kt`  
  Model data transaksi dan formatter tanggal tampilan
- `MemberDao.kt`  
  Query database untuk member
- `TransactionDao.kt`  
  Query database untuk transaksi
- `AppDatabase.kt`  
  Konfigurasi Room database
- `CoffeeRepository.kt`  
  Penghubung antara DAO dan ViewModel

### Layer Presentation
- `CoffeeViewModel.kt`  
  State management, login, register, transaksi, reward, dan profile update
- `CoffeeNavGraph.kt`  
  Navigasi antar screen

### Screen Utama
- `SplashScreen.kt`
- `LoginRegisterScreen.kt`
- `MemberDetailScreen.kt`
- `TransactionScreen.kt`
- `RewardScreen.kt`
- `ProfileScreen.kt`

## Teknologi yang Digunakan
- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **Room Database**
- **ViewModel**
- **StateFlow / Flow**
- **SharedPreferences**
- **ZXing** untuk pembuatan QR code

## Arsitektur Sederhana
Aplikasi ini menggunakan pola sederhana:

```text
UI (Compose Screen)
    -> ViewModel
        -> Repository
            -> Room DAO
                -> SQLite (local database)
```

Pendekatan ini dipilih agar kode lebih terstruktur, mudah dipelajari, dan sesuai untuk proyek tugas akhir.

## Database
Aplikasi menggunakan **Room Database** dengan 2 entitas utama:

### Tabel `members`
Menyimpan data:
- id
- name
- email
- phone 
- password
- points

### Tabel `transactions`
Menyimpan data:
- id
- memberId
- title
- amount
- pointEarned
- date

## Sistem Poin dan Level
### Perhitungan Poin
```text
points = amount / 10000
```

Contoh:
- transaksi `Rp 50.000` menghasilkan `5 pts`
- transaksi `Rp 120.000` menghasilkan `12 pts`

### Level Membership
- `Bronze` : poin < 200
- `Silver` : poin >= 200
- `Gold` : poin >= 500



## Cara Menjalankan Proyek

1. Buka project ini di Android Studio
2. Pastikan Android SDK sudah terpasang
3. Sync Gradle
4. Jalankan aplikasi pada emulator atau perangkat Android


## 🖼️ Infografis Aplikasi
| | | |
|---|---|---|
| <img width="388" height="857" alt="Screenshot 2026-06-16 224836" src="https://github.com/user-attachments/assets/21c7c4a8-cb6a-4e9f-95b5-caa57181ea35" /> | <img width="383" height="857" alt="Screenshot 2026-06-16 224935" src="https://github.com/user-attachments/assets/4cfbdda9-f0f2-4532-893c-aa1fed5e5bda" /> | <img width="390" height="862" alt="Screenshot 2026-06-16 225027" src="https://github.com/user-attachments/assets/6351040b-745a-4906-afe8-d580eac2d064" /> |
| <img width="394" height="859" alt="Screenshot 2026-06-16 225043" src="https://github.com/user-attachments/assets/5c807729-fbee-491c-b92e-765b9efc637f" /> | <img width="389" height="860" alt="Screenshot 2026-06-16 225106" src="https://github.com/user-attachments/assets/40053dc2-cdcd-4969-81ea-e0d6066337ff" /> | <img width="398" height="860" alt="Screenshot 2026-06-16 225119" src="https://github.com/user-attachments/assets/693ef3b3-6b19-48af-b547-3c9b678f93da" /> |
| <img width="393" height="860" alt="Screenshot 2026-06-16 225127" src="https://github.com/user-attachments/assets/dc5483f6-aec0-4531-9ae0-a8064393dd83" /> | <img width="394" height="862" alt="Screenshot 2026-06-16 225136" src="https://github.com/user-attachments/assets/972b1b19-6178-4fdf-8cf8-2e97907835c5" /> | <img width="390" height="865" alt="Screenshot 2026-06-16 225208" src="https://github.com/user-attachments/assets/99d00a5d-b9f1-4493-80ab-4458a6348f38" /> |
| <img width="382" height="856" alt="Screenshot 2026-06-16 225227" src="https://github.com/user-attachments/assets/fa98dc6f-25e5-44ce-ab2f-e8b703fdc089" /> | <img width="387" height="864" alt="Screenshot 2026-06-16 225244" src="https://github.com/user-attachments/assets/b75f6064-4314-4b0c-a566-9071e32ad21a" /> | <img width="389" height="860" alt="Screenshot 2026-06-16 225254" src="https://github.com/user-attachments/assets/b28bb930-786b-4d85-b088-39f8a6051ffb" /> |
| <img width="387" height="858" alt="Screenshot 2026-06-16 225303" src="https://github.com/user-attachments/assets/3d69f49a-3e11-4414-850a-1a1344e3f9f4" /> | <img width="386" height="859" alt="Screenshot 2026-06-16 225313" src="https://github.com/user-attachments/assets/831b8c13-f8d7-4c57-bfa7-ba677ae94f0d" /> |


