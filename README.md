# 📖 AcadeMate: Platform Penyewaan Tutor Mata Kuliah

AcadeMate adalah aplikasi mobile yang dikembangkan menggunakan **Kotlin** dan **Jetpack Compose** untuk memfasilitasi mahasiswa dalam mencari, memesan, dan menyewa mentor (tutor) mata kuliah dari berbagai fakultas. Aplikasi ini dirancang dengan antarmuka modern (Material 3) dan didukung oleh **Firebase** untuk manajemen autentikasi dan penyimpanan data *realtime*.

---

## 👥 Identitas Kelompok

| No. | Nama Lengkap | NIM |
| :---: | :--- | :--- |
| 1. | Hanidura Ayatulloh | 225150207111005 |
| 2. | Ivan Rifanda Sharif | 215150207111082 |
| 3. | Ni Nyoman Chandra Pramesti Iswari Wijaya | 225150207111106 |
| 4. | Scorpian Erickda | 225150200111061 |

---

## 🚀 Fitur Utama Aplikasi

Berdasarkan struktur file (`HomeScreen.kt`, `Riwayat.kt`, `FormMentor.kt`, dll.), fitur-fitur kunci yang ditawarkan AcadeMate meliputi:

1.  **Sistem Autentikasi:** Fitur Login (`Login.kt`) dan Registrasi/Sign Up (`SignUp.kt`) menggunakan **Firebase Authentication**.
2.  **Onboarding:** Tampilan pengenalan aplikasi awal (`SplashScreen.kt`, `onBoarding1.kt`, `onBoarding2.kt`).
3.  **Daftar Mata Kuliah:** Menampilkan daftar mata kuliah yang tersedia untuk pencarian mentor (`MataKuliah.kt`).
4.  **Informasi Mata Kuliah:** Detail deskripsi mata kuliah (`InformasiMatkul.kt`).
5.  **Daftar Mentor:** Menampilkan mentor yang tersedia untuk mata kuliah yang dipilih (`DaftarMentor.kt`).
6.  **Pemesanan Mentor:** Fitur detail mentor dan dialog konfirmasi sewa (terdapat di `InfromasiMentor.kt` yang juga menyimpan data sewa ke Firebase Realtime Database sebagai Riwayat).
7.  **Riwayat Sewa:** Halaman untuk melihat daftar transaksi penyewaan mentor yang telah dilakukan (`Riwayat.kt`).
8.  **Review dan Rating:** Memberikan ulasan dan penilaian kepada mentor (`ReviewMentor.kt`, `InputReviewMentor.kt`).
9.  **Pendaftaran Mentor:** Formulir bagi pengguna untuk mengajukan diri sebagai mentor (`FormMentor.kt`), dengan update status pengguna di Firebase.

---

## 🧱 Struktur Arsitektur

Aplikasi ini mengimplementasikan pola desain **Model-View-ViewModel (MVVM)**, yang dipadukan dengan prinsip **Clean Architecture** menggunakan **Flow** dan **Sealed Class (`Resource.kt`)** untuk manajemen *state* asinkron.

### 1. Presentation Layer (UI/View)
* **Teknologi:** Jetpack Compose (Composables).
* **Contoh File:** Semua file `.kt` di direktori `ui/presentation/` (misalnya `HomeScreen.kt`, `Login.kt`).
* **Tanggung Jawab:** Menampilkan data dari ViewModel dan menangani input pengguna.

### 2. ViewModel Layer
* **Teknologi:** `ViewModel`, `StateFlow`/`Channel` (untuk *one-shot events* dan *state*).
* **Contoh File:** `SignInViewModel.kt`, `SignUpViewModel.kt`, `InformasiMatkulViewModel.kt`, `CurrentMatkulViewModel.kt`, `UserViewModel.kt`.
* **Tanggung Jawab:** Menyimpan data yang berkaitan dengan UI secara *lifecycle-aware* dan mengekspos *state* ke UI.

### 3. Data Layer (Repository)
* **Teknologi:** Firebase (Auth, Realtime Database) dan Coroutines.
* **Contoh File:** `AuthRepository.kt`, `AuthRepositoryImpl.kt`, `MataKuliahRepository.kt`.
* **Tanggung Jawab:** Abstraksi sumber data. Berisi logika untuk berinteraksi dengan Firebase (Login, Register, Get Data Mata Kuliah).

### Injeksi Dependensi
Proyek menggunakan **Dagger Hilt** (`ApplicationFirebaseAuth.kt`, `AppModule.kt`) untuk memfasilitasi injeksi dependensi pada *Repository* dan *ViewModel*.

---

## 🛠️ Dependencies dan Teknologi Utama

| Kategori | Dependency Utama (Contoh) | Tujuan |
| :--- | :--- | :--- |
| **Backend** | `firebase-auth`, `firebase-database` | Autentikasi pengguna dan penyimpanan data *realtime*. |
| **UI** | `androidx.compose.material3` | Komponen Material Design 3. |
| **Navigation** | `androidx.navigation.compose` | Pengelolaan perpindahan antar layar (routing). |
| **Architecture** | `androidx.lifecycle:lifecycle-viewmodel-compose` | Integrasi ViewModel di Compose. |
| **Icons** | `material-icons-extended` | Koleksi ikon yang lebih lengkap (`CameraAlt`, `ExitToApp`, dll.). |
| **Dependency Injection** | `com.google.dagger:hilt-android` | Injeksi dependensi untuk komponen aplikasi. |

---

## 🗺️ Alur Navigasi (Routes)

Aplikasi ini menggunakan sistem navigasi berbasis rute (didefinisikan di `Route.kt` dan diimplementasikan di `Navigation.kt`) sebagai berikut:

| Rute | Tampilan | Fungsi Utama |
| :--- | :--- | :--- |
| **`splashScreen`** | `SplashScreen` | Tampilan awal sebelum masuk ke aplikasi. |
| **`onBoarding1`**, **`onBoarding2`** | `onBoarding1`, `onBoarding2` | Halaman perkenalan aplikasi. |
| **`login`** | `Login` | Halaman masuk/log in. |
| **`signup`** | `SignUp` | Halaman pendaftaran akun baru. |
| **`home`** | `HomeScreen` | Dashboard utama (dengan bottom bar). |
| **`matakuliah`** | `MataKuliah` | Daftar mata kuliah yang tersedia (dengan bottom bar). |
| **`informasiMatkul`** | `InformasiMatkul` | Detail mata kuliah dan tombol "Cari Mentor". |
| **`daftarmentorRPL`** | `DaftarMentor` | Daftar mentor spesifik untuk mata kuliah tertentu. |
| **`informasiMentor`** | `InformasiMentor` | Detail profil mentor dan dialog konfirmasi sewa. |
| **`search`** | `SearchScreen` | Halaman pencarian (dengan bottom bar). |
| **`profile`** | `Profil` | Halaman profil pengguna (dengan bottom bar). |
| **`formmentor`** | `FormMentor` | Formulir pengajuan diri menjadi mentor. |
| **`pemberitahuanbementor`** | `PemberitahuanBeMentor` | Notifikasi setelah submit formulir mentor. |
| **`riwayat`** | `RiwayatScreen` | Daftar riwayat penyewaan mentor. |
| **`inputreviewmentor`** | `InputReviewMentor` | Formulir untuk memberikan rating dan review. |
