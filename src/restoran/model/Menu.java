package restoran.model;

import restoran.util.Rupiah;

/**
 * Menu makanan atau minuman yang dijual restoran.
 * Semua atribut private dan hanya dapat diubah lewat setter yang memvalidasi nilai.
 */
public class Menu {

    private String kode;
    private String nama;
    private double harga;
    private int stok;
    private boolean tersedia;

    /** Konstruktor default: menu kosong dengan nilai awal yang valid. */
    public Menu() {
        this("M000", "Belum diisi", 1000, 0);
    }

    /**
     * Konstruktor berparameter. Nilai divalidasi lewat setter.
     *
     * @param kode  kode unik menu
     * @param nama  nama menu
     * @param harga harga satuan, harus lebih dari 0
     * @param stok  stok awal, tidak boleh negatif
     */
    public Menu(String kode, String nama, double harga, int stok) {
        this.setKode(kode);
        this.setNama(nama);
        this.setHarga(harga);
        this.setStok(stok);
    }

    // ===== Getter dan setter (setter memuat validasi) =====

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode menu tidak boleh kosong.");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama menu tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga menu harus lebih dari 0.");
        }
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok menu tidak boleh negatif.");
        }
        this.stok = stok;
        this.tersedia = stok > 0;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }

    // ===== Method perilaku =====

    /**
     * Mengecek apakah stok cukup untuk jumlah yang diminta.
     *
     * @param jumlah jumlah porsi
     * @return true jika menu tersedia dan stok mencukupi
     */
    public boolean cukupStok(int jumlah) {
        return tersedia && jumlah > 0 && jumlah <= stok;
    }

    /**
     * Mengurangi stok saat pesanan disimpan.
     *
     * @param jumlah jumlah porsi yang dikurangi
     */
    public void kurangiStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pengurangan stok harus lebih dari 0.");
        }
        if (jumlah > stok) {
            throw new IllegalArgumentException("Stok " + nama + " tidak cukup (tersisa " + stok + ").");
        }
        setStok(stok - jumlah);
    }

    /**
     * Menambah stok, dipakai saat pesanan dibatalkan.
     *
     * @param jumlah jumlah porsi yang dikembalikan
     */
    public void tambahStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah penambahan stok harus lebih dari 0.");
        }
        setStok(stok + jumlah);
    }

    /**
     * Menghitung subtotal untuk sejumlah porsi.
     *
     * @param jumlah jumlah porsi
     * @return harga x jumlah
     */
    public double hitungSubtotal(int jumlah) {
        return harga * jumlah;
    }

    /** Mencetak informasi menu dalam satu baris. */
    public void tampilkanInfo() {
        System.out.printf("%-6s | %-22s | %-10s | stok %-3d | %s%n",
                kode, nama, Rupiah.format(harga), stok, tersedia ? "Tersedia" : "Habis");
    }
}
