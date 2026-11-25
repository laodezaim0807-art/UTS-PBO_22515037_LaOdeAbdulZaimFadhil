package restoran;

import java.util.List;

import java.util.NoSuchElementException;
import java.util.Scanner;
import restoran.adt.RiwayatStack;
import restoran.model.Menu;
import restoran.model.Pelanggan;
import restoran.model.Pesanan;
import restoran.model.Restoran;

/**
 * Titik masuk aplikasi console manajemen restoran mini.
 * Class ini hanya mengurus tampilan menu dan input; logika ada di class model.
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Restoran RESTORAN = new Restoran("Warung Kendari", "Jl. Mekar No. 7, Kendari");

    public static void main(String[] args) {
        System.out.println("=== Sistem Manajemen " + RESTORAN.getNama() + " ===");
        try {
            boolean jalan = true;
            while (jalan) {
                tampilkanMenuUtama();
                int pilihan = bacaInt("Pilih menu: ");
                try {
                    jalan = proses(pilihan);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // Pelanggaran aturan atau validasi: tampilkan pesan, program tetap jalan.
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nInput berakhir. Program dihentikan.");
        }
    }

    private static void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("---------- MENU UTAMA ----------");
        System.out.println(" 1. Tambah menu");
        System.out.println(" 2. Tampilkan semua menu");
        System.out.println(" 3. Tambah pelanggan");
        System.out.println(" 4. Tampilkan semua pelanggan");
        System.out.println(" 5. Buat pesanan");
        System.out.println(" 6. Tampilkan semua pesanan");
        System.out.println(" 7. Cari data");
        System.out.println(" 8. Riwayat pesanan (Stack)");
        System.out.println(" 9. Laporan ringkasan");
        System.out.println("10. Selesaikan pesanan");
        System.out.println("11. Muat data contoh");
        System.out.println(" 0. Keluar");
    }

    /** @return false jika program harus berhenti */
    private static boolean proses(int pilihan) {
        switch (pilihan) {
            case 1: tambahMenu(); break;
            case 2: tampilkanSemuaMenu(); break;
            case 3: tambahPelanggan(); break;
            case 4: tampilkanSemuaPelanggan(); break;
            case 5: buatPesanan(); break;
            case 6: tampilkanSemuaPesanan(); break;
            case 7: cariData(); break;
            case 8: menuRiwayat(); break;
            case 9: RESTORAN.tampilkanLaporan(); break;
            case 10: selesaikanPesanan(); break;
            case 11: muatDataContoh(); break;
            case 0:
                System.out.println("Terima kasih. Program selesai.");
                return false;
            default:
                System.out.println("[ERROR] Pilihan " + pilihan + " tidak ada di menu.");
        }
        return true;
    }

    // ===== Menu, pelanggan =====

    private static void tambahMenu() {
        System.out.println("-- Tambah menu --");
        String kode = bacaTeks("Kode menu   : ");
        String nama = bacaTeks("Nama menu   : ");
        double harga = bacaDouble("Harga       : ");
        int stok = bacaInt("Stok        : ");
        RESTORAN.tambahMenu(new Menu(kode, nama, harga, stok));
        System.out.println("Menu " + nama + " berhasil ditambahkan.");
    }

    private static void tampilkanSemuaMenu() {
        System.out.println("-- Daftar menu --");
        if (RESTORAN.getDaftarMenu().isEmpty()) {
            System.out.println("(belum ada menu)");
            return;
        }
        for (Menu m : RESTORAN.getDaftarMenu()) {
            m.tampilkanInfo();
        }
    }

    private static void tambahPelanggan() {
        System.out.println("-- Tambah pelanggan --");
        String id = bacaTeks("ID pelanggan : ");
        String nama = bacaTeks("Nama         : ");
        String telepon = bacaTeks("No. telepon  : ");
        RESTORAN.tambahPelanggan(new Pelanggan(id, nama, telepon));
        System.out.println("Pelanggan " + nama + " berhasil ditambahkan.");
    }

    private static void tampilkanSemuaPelanggan() {
        System.out.println("-- Daftar pelanggan --");
        if (RESTORAN.getDaftarPelanggan().isEmpty()) {
            System.out.println("(belum ada pelanggan)");
            return;
        }
        for (Pelanggan p : RESTORAN.getDaftarPelanggan()) {
            p.tampilkanInfo();
        }
    }

    // ===== Pesanan =====

    private static void buatPesanan() {
        System.out.println("-- Buat pesanan --");
        String idPelanggan = bacaTeks("ID pelanggan : ");
        Pesanan pesanan = RESTORAN.buatDraftPesanan(idPelanggan);
        System.out.println("Pelanggan: " + pesanan.getPelanggan().getNama());
        System.out.println("Masukkan kode menu satu per satu. Ketik SELESAI untuk menyimpan.");
        while (true) {
            String kode = bacaTeks("Kode menu    : ");
            if (kode.equalsIgnoreCase("SELESAI")) {
                break;
            }
            Menu menu = RESTORAN.cariMenuByKode(kode);
            if (menu == null) {
                System.out.println("[ERROR] Menu dengan kode " + kode + " tidak ditemukan.");
                continue;
            }
            int jumlah = bacaInt("Jumlah       : ");
            try {
                pesanan.tambahMenu(menu, jumlah);
                System.out.println("  + " + jumlah + " " + menu.getNama() + " ditambahkan.");
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
        RESTORAN.simpanPesanan(pesanan);
        System.out.println("Pesanan berhasil disimpan.");
        pesanan.tampilkanInfo();
    }

    private static void tampilkanSemuaPesanan() {
        System.out.println("-- Daftar pesanan --");
        if (RESTORAN.getDaftarPesanan().isEmpty()) {
            System.out.println("(belum ada pesanan)");
            return;
        }
        for (Pesanan p : RESTORAN.getDaftarPesanan()) {
            p.tampilkanInfo();
            System.out.println();
        }
    }

    private static void selesaikanPesanan() {
        String kode = bacaTeks("Kode pesanan : ");
        Pesanan p = RESTORAN.cariPesanan(kode);
        if (p == null) {
            System.out.println("[ERROR] Pesanan " + kode + " tidak ditemukan.");
            return;
        }
        p.ubahStatus();
        System.out.println("Pesanan " + p.getKodePesanan() + " ditandai SELESAI.");
    }

    // ===== Pencarian =====

    private static void cariData() {
        System.out.println("-- Cari data --");
        System.out.println("1. Menu (kode atau nama)");
        System.out.println("2. Pelanggan (ID atau nama)");
        System.out.println("3. Pesanan (kode)");
        int jenis = bacaInt("Pilih: ");
        if (jenis < 1 || jenis > 3) {
            System.out.println("[ERROR] Pilihan " + jenis + " tidak ada.");
            return;
        }
        String kunci = bacaTeks("Kata kunci: ");
        if (jenis == 1) {
            List<Menu> hasil = RESTORAN.cariMenu(kunci);
            if (hasil.isEmpty()) {
                System.out.println("Menu \"" + kunci + "\" tidak ditemukan.");
            }
            for (Menu m : hasil) {
                m.tampilkanInfo();
            }
        } else if (jenis == 2) {
            List<Pelanggan> hasil = RESTORAN.cariPelanggan(kunci);
            if (hasil.isEmpty()) {
                System.out.println("Pelanggan \"" + kunci + "\" tidak ditemukan.");
            }
            for (Pelanggan p : hasil) {
                p.tampilkanInfo();
            }
        } else {
            Pesanan p = RESTORAN.cariPesanan(kunci);
            if (p == null) {
                System.out.println("Pesanan \"" + kunci + "\" tidak ditemukan.");
            } else {
                p.tampilkanInfo();
            }
        }
    }

    // ===== Operasi ADT Stack =====

    private static void menuRiwayat() {
        RiwayatStack riwayat = RESTORAN.getRiwayat();
        System.out.println("-- Riwayat pesanan (Stack) --");
        System.out.println("1. Batalkan pesanan terakhir (pop)");
        System.out.println("2. Lihat pesanan terakhir (peek)");
        System.out.println("3. Tampilkan isi riwayat");
        System.out.println("4. Status stack (size, isEmpty, isFull)");
        System.out.println("5. Konfirmasi riwayat (kosongkan stack)");
        int pilihan = bacaInt("Pilih: ");
        switch (pilihan) {
            case 1:
                Pesanan batal = RESTORAN.batalkanPesananTerakhir();
                if (batal == null) {
                    System.out.println("[ERROR] Riwayat kosong, tidak ada pesanan untuk dibatalkan.");
                } else {
                    System.out.println("Pesanan " + batal.getKodePesanan() + " dibatalkan. Stok dan total belanja dikembalikan.");
                }
                break;
            case 2:
                Pesanan atas = riwayat.peek();
                if (atas == null) {
                    System.out.println("[ERROR] Riwayat kosong.");
                } else {
                    atas.tampilkanInfo();
                }
                break;
            case 3:
                riwayat.tampilkan();
                break;
            case 4:
                System.out.println("size     : " + riwayat.size() + " dari " + riwayat.kapasitas());
                System.out.println("isEmpty  : " + riwayat.isEmpty());
                System.out.println("isFull   : " + riwayat.isFull());
                break;
            case 5:
                RESTORAN.konfirmasiRiwayat();
                System.out.println("Riwayat dikosongkan. Pesanan tetap tercatat.");
                break;
            default:
                System.out.println("[ERROR] Pilihan " + pilihan + " tidak ada.");
        }
    }

    // ===== Data contoh =====

    private static void muatDataContoh() {
        if (!RESTORAN.getDaftarMenu().isEmpty() || !RESTORAN.getDaftarPelanggan().isEmpty()) {
            System.out.println("[ERROR] Data contoh hanya dapat dimuat saat data masih kosong.");
            return;
        }
        RESTORAN.tambahMenu(new Menu("M01", "Nasi Goreng Spesial", 25000, 10));
        RESTORAN.tambahMenu(new Menu("M02", "Ayam Bakar", 30000, 8));
        RESTORAN.tambahMenu(new Menu("M03", "Sup Ikan Kuah Kuning", 28000, 6));
        RESTORAN.tambahMenu(new Menu("M04", "Es Teh Manis", 5000, 30));
        RESTORAN.tambahMenu(new Menu("M05", "Jus Alpukat", 15000, 2));
        RESTORAN.tambahPelanggan(new Pelanggan("P01", "Ahmad Rizky", "081234567890"));
        RESTORAN.tambahPelanggan(new Pelanggan("P02", "Sari Wulandari", "082198765432"));
        RESTORAN.tambahPelanggan(new Pelanggan("P03", "Laode Hasan", "085311122233"));
        System.out.println("Data contoh dimuat: 5 menu dan 3 pelanggan.");
    }

    // ===== Helper input (semua input divalidasi, tidak ada crash) =====

    private static String bacaTeks(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SCANNER.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("[ERROR] Input tidak boleh kosong.");
        }
    }

    private static int bacaInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Input harus berupa bilangan bulat.");
            }
        }
    }

    private static double bacaDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Input harus berupa angka.");
            }
        }
    }
}
