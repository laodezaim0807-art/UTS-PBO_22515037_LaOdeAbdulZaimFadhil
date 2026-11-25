package restoran.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import restoran.util.Rupiah;

/**
 * Pesanan yang menghubungkan satu {@link Pelanggan} dengan satu atau lebih {@link Menu}.
 * Pajak 10 persen dihitung otomatis dari subtotal.
 */
public class Pesanan {

    /** Tarif pajak restoran (10 persen). */
    public static final double TARIF_PAJAK = 0.10;
    public static final String STATUS_DIPROSES = "DIPROSES";
    public static final String STATUS_SELESAI = "SELESAI";

    private String kodePesanan;
    private Pelanggan pelanggan;
    private final Map<Menu, Integer> items = new LinkedHashMap<>();
    private String status;

    /** Konstruktor default: pesanan kosong atas nama pelanggan default. */
    public Pesanan() {
        this("PSN-000", new Pelanggan());
    }

    /**
     * Konstruktor berparameter.
     *
     * @param kodePesanan kode pesanan
     * @param pelanggan   pelanggan yang memesan, tidak boleh null
     */
    public Pesanan(String kodePesanan, Pelanggan pelanggan) {
        this.setKodePesanan(kodePesanan);
        this.setPelanggan(pelanggan);
        this.status = STATUS_DIPROSES;
    }

    // ===== Getter dan setter =====

    public String getKodePesanan() {
        return kodePesanan;
    }

    public void setKodePesanan(String kodePesanan) {
        if (kodePesanan == null || kodePesanan.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode pesanan tidak boleh kosong.");
        }
        this.kodePesanan = kodePesanan.trim().toUpperCase();
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pesanan harus memiliki pelanggan.");
        }
        this.pelanggan = pelanggan;
    }

    /** @return daftar menu dan jumlahnya, hanya untuk dibaca */
    public Map<Menu, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!STATUS_DIPROSES.equals(status) && !STATUS_SELESAI.equals(status)) {
            throw new IllegalArgumentException("Status pesanan tidak dikenal: " + status);
        }
        this.status = status;
    }

    // ===== Method perilaku =====

    /**
     * Menambahkan menu ke pesanan. Jika menu sudah ada, jumlahnya ditambah.
     * Stok belum dikurangi di sini; pengurangan terjadi saat pesanan disimpan.
     *
     * @param menu   menu yang dipesan
     * @param jumlah jumlah porsi, harus lebih dari 0
     */
    public void tambahMenu(Menu menu, int jumlah) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu tidak ditemukan.");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah porsi harus lebih dari 0.");
        }
        if (!menu.isTersedia()) {
            throw new IllegalArgumentException("Menu " + menu.getNama() + " sedang habis.");
        }
        int total = items.getOrDefault(menu, 0) + jumlah;
        if (total > menu.getStok()) {
            throw new IllegalArgumentException("Stok " + menu.getNama()
                    + " tidak cukup (tersisa " + menu.getStok() + ", diminta " + total + ").");
        }
        items.put(menu, total);
    }

    /** @return true jika belum ada menu di pesanan */
    public boolean isKosong() {
        return items.isEmpty();
    }

    /** @return jumlah seluruh porsi di pesanan */
    public int hitungJumlahPorsi() {
        int jumlah = 0;
        for (int qty : items.values()) {
            jumlah += qty;
        }
        return jumlah;
    }

    /** @return jumlah harga semua menu sebelum pajak */
    public double hitungSubtotal() {
        double subtotal = 0;
        for (Map.Entry<Menu, Integer> e : items.entrySet()) {
            subtotal += e.getKey().hitungSubtotal(e.getValue());
        }
        return subtotal;
    }

    /** @return pajak 10 persen dari subtotal */
    public double hitungPajak() {
        return hitungSubtotal() * TARIF_PAJAK;
    }

    /** @return subtotal ditambah pajak */
    public double hitungTotal() {
        return hitungSubtotal() + hitungPajak();
    }

    /** Mengubah status pesanan menjadi selesai. */
    public void ubahStatus() {
        if (STATUS_SELESAI.equals(status)) {
            throw new IllegalStateException("Pesanan " + kodePesanan + " sudah selesai.");
        }
        this.status = STATUS_SELESAI;
    }

    /** Mencetak rincian pesanan seperti struk. */
    public void tampilkanInfo() {
        System.out.println("Kode      : " + kodePesanan);
        System.out.println("Pelanggan : " + pelanggan.getNama() + " (" + pelanggan.getIdPelanggan() + ")");
        System.out.println("Status    : " + status);
        for (Map.Entry<Menu, Integer> e : items.entrySet()) {
            Menu m = e.getKey();
            int qty = e.getValue();
            System.out.printf("  %-22s %2d x %-10s = %s%n", m.getNama(), qty,
                    Rupiah.format(m.getHarga()), Rupiah.format(m.hitungSubtotal(qty)));
        }
        System.out.println("Subtotal  : " + Rupiah.format(hitungSubtotal()));
        System.out.println("Pajak 10% : " + Rupiah.format(hitungPajak()));
        System.out.println("Total     : " + Rupiah.format(hitungTotal()));
    }
}
