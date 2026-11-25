package restoran.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import restoran.adt.RiwayatStack;
import restoran.util.Rupiah;

/**
 * Kelas pengelola restoran. Menyimpan daftar menu, pelanggan, pesanan,
 * dan {@link RiwayatStack} untuk membatalkan pesanan terakhir.
 */
public class Restoran {

    /** Kapasitas riwayat pesanan yang dapat dibatalkan. */
    public static final int KAPASITAS_RIWAYAT = 5;

    private String nama;
    private String alamat;
    private final List<Menu> daftarMenu = new ArrayList<>();
    private final List<Pelanggan> daftarPelanggan = new ArrayList<>();
    private final List<Pesanan> daftarPesanan = new ArrayList<>();
    private final RiwayatStack riwayat = new RiwayatStack(KAPASITAS_RIWAYAT);
    private int nomorPesananTerakhir = 0;

    /** Konstruktor default. */
    public Restoran() {
        this("Restoran Baru", "Alamat belum diisi");
    }

    /**
     * Konstruktor berparameter.
     *
     * @param nama   nama restoran
     * @param alamat alamat restoran
     */
    public Restoran(String nama, String alamat) {
        this.setNama(nama);
        this.setAlamat(alamat);
    }

    // ===== Getter dan setter =====

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama restoran tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat restoran tidak boleh kosong.");
        }
        this.alamat = alamat.trim();
    }

    public List<Menu> getDaftarMenu() {
        return Collections.unmodifiableList(daftarMenu);
    }

    public List<Pelanggan> getDaftarPelanggan() {
        return Collections.unmodifiableList(daftarPelanggan);
    }

    public List<Pesanan> getDaftarPesanan() {
        return Collections.unmodifiableList(daftarPesanan);
    }

    public RiwayatStack getRiwayat() {
        return riwayat;
    }

    // ===== Menu dan pelanggan =====

    /**
     * Menambah menu baru. Kode menu harus unik.
     *
     * @param menu menu yang ditambahkan
     */
    public void tambahMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu tidak boleh null.");
        }
        if (cariMenuByKode(menu.getKode()) != null) {
            throw new IllegalArgumentException("Kode menu " + menu.getKode() + " sudah dipakai.");
        }
        daftarMenu.add(menu);
    }

    /**
     * Menambah pelanggan baru. ID pelanggan harus unik.
     *
     * @param pelanggan pelanggan yang ditambahkan
     */
    public void tambahPelanggan(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pelanggan tidak boleh null.");
        }
        if (cariPelangganById(pelanggan.getIdPelanggan()) != null) {
            throw new IllegalArgumentException("ID pelanggan " + pelanggan.getIdPelanggan() + " sudah dipakai.");
        }
        daftarPelanggan.add(pelanggan);
    }

    // ===== Pencarian =====

    /** @return menu dengan kode tersebut, atau null */
    public Menu cariMenuByKode(String kode) {
        if (kode == null) {
            return null;
        }
        for (Menu m : daftarMenu) {
            if (m.getKode().equalsIgnoreCase(kode.trim())) {
                return m;
            }
        }
        return null;
    }

    /** @return menu yang kodenya sama atau namanya memuat kata kunci */
    public List<Menu> cariMenu(String kataKunci) {
        List<Menu> hasil = new ArrayList<>();
        String kunci = kataKunci == null ? "" : kataKunci.trim().toLowerCase();
        if (kunci.isEmpty()) {
            return hasil;
        }
        for (Menu m : daftarMenu) {
            if (m.getKode().toLowerCase().equals(kunci) || m.getNama().toLowerCase().contains(kunci)) {
                hasil.add(m);
            }
        }
        return hasil;
    }

    /** @return pelanggan dengan ID tersebut, atau null */
    public Pelanggan cariPelangganById(String id) {
        if (id == null) {
            return null;
        }
        for (Pelanggan p : daftarPelanggan) {
            if (p.getIdPelanggan().equalsIgnoreCase(id.trim())) {
                return p;
            }
        }
        return null;
    }

    /** @return pelanggan yang ID-nya sama atau namanya memuat kata kunci */
    public List<Pelanggan> cariPelanggan(String kataKunci) {
        List<Pelanggan> hasil = new ArrayList<>();
        String kunci = kataKunci == null ? "" : kataKunci.trim().toLowerCase();
        if (kunci.isEmpty()) {
            return hasil;
        }
        for (Pelanggan p : daftarPelanggan) {
            if (p.getIdPelanggan().toLowerCase().equals(kunci) || p.getNama().toLowerCase().contains(kunci)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    /** @return pesanan dengan kode tersebut, atau null */
    public Pesanan cariPesanan(String kode) {
        if (kode == null) {
            return null;
        }
        for (Pesanan p : daftarPesanan) {
            if (p.getKodePesanan().equalsIgnoreCase(kode.trim())) {
                return p;
            }
        }
        return null;
    }

    // ===== Pesanan =====

    /**
     * Membuat pesanan sementara (belum tersimpan) untuk pelanggan tertentu.
     *
     * @param idPelanggan ID pelanggan yang memesan
     * @return pesanan kosong berkode "DRAFT"
     */
    public Pesanan buatDraftPesanan(String idPelanggan) {
        Pelanggan pelanggan = cariPelangganById(idPelanggan);
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pelanggan dengan ID " + idPelanggan + " tidak ditemukan.");
        }
        return new Pesanan("DRAFT", pelanggan);
    }

    /**
     * Menyimpan pesanan. Aturan bisnis: pesanan memuat minimal 1 menu,
     * dan riwayat tidak boleh penuh. Saat berhasil, stok berkurang, total belanja
     * pelanggan bertambah, dan pesanan di-push ke riwayat.
     *
     * @param pesanan pesanan sementara yang sudah berisi menu
     */
    public void simpanPesanan(Pesanan pesanan) {
        if (pesanan == null || pesanan.isKosong()) {
            throw new IllegalArgumentException("Pesanan harus memuat minimal 1 menu.");
        }
        if (riwayat.isFull()) {
            throw new IllegalStateException("Riwayat pesanan penuh (" + riwayat.size() + "/"
                    + riwayat.kapasitas() + "). Batalkan pesanan terakhir atau konfirmasi riwayat dahulu.");
        }
        // Cek ulang stok karena stok bisa berubah sejak menu ditambahkan ke draft.
        for (Map.Entry<Menu, Integer> e : pesanan.getItems().entrySet()) {
            if (!e.getKey().cukupStok(e.getValue())) {
                throw new IllegalStateException("Stok " + e.getKey().getNama() + " tidak cukup lagi.");
            }
        }
        for (Map.Entry<Menu, Integer> e : pesanan.getItems().entrySet()) {
            e.getKey().kurangiStok(e.getValue());
        }
        nomorPesananTerakhir++;
        pesanan.setKodePesanan(String.format("PSN-%03d", nomorPesananTerakhir));
        pesanan.getPelanggan().tambahBelanja(pesanan.hitungTotal());
        daftarPesanan.add(pesanan);
        riwayat.push(pesanan);
    }

    /**
     * Membatalkan pesanan terakhir (pop). Stok dan total belanja dikembalikan.
     *
     * @return pesanan yang dibatalkan, atau null jika riwayat kosong
     */
    public Pesanan batalkanPesananTerakhir() {
        Pesanan pesanan = riwayat.pop();
        if (pesanan == null) {
            return null;
        }
        for (Map.Entry<Menu, Integer> e : pesanan.getItems().entrySet()) {
            e.getKey().tambahStok(e.getValue());
        }
        pesanan.getPelanggan().kurangiBelanja(pesanan.hitungTotal());
        daftarPesanan.remove(pesanan);
        return pesanan;
    }

    /** Mengosongkan riwayat. Pesanan tetap tercatat tetapi tidak dapat dibatalkan lagi. */
    public void konfirmasiRiwayat() {
        riwayat.clear();
    }

    // ===== Laporan =====

    /** @return jumlah total semua pesanan yang tercatat, termasuk pajak */
    public double hitungTotalPendapatan() {
        double total = 0;
        for (Pesanan p : daftarPesanan) {
            total += p.hitungTotal();
        }
        return total;
    }

    /** @return jumlah pajak dari semua pesanan yang tercatat */
    public double hitungTotalPajak() {
        double total = 0;
        for (Pesanan p : daftarPesanan) {
            total += p.hitungPajak();
        }
        return total;
    }

    /** Mencetak ringkasan restoran. */
    public void tampilkanLaporan() {
        int selesai = 0;
        for (Pesanan p : daftarPesanan) {
            if (Pesanan.STATUS_SELESAI.equals(p.getStatus())) {
                selesai++;
            }
        }
        System.out.println("Restoran          : " + nama + " (" + alamat + ")");
        System.out.println("Jumlah menu       : " + daftarMenu.size());
        System.out.println("Jumlah pelanggan  : " + daftarPelanggan.size());
        System.out.println("Jumlah pesanan    : " + daftarPesanan.size()
                + " (selesai " + selesai + ", diproses " + (daftarPesanan.size() - selesai) + ")");
        System.out.println("Total pajak       : " + Rupiah.format(hitungTotalPajak()));
        System.out.println("Total pendapatan  : " + Rupiah.format(hitungTotalPendapatan()));
        System.out.println("Riwayat (stack)   : " + riwayat.size() + "/" + riwayat.kapasitas());
    }
}
