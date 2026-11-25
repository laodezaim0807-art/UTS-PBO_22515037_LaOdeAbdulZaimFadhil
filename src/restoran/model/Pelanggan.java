package restoran.model;

import restoran.util.Rupiah;

/**
 * Pelanggan restoran. Status member diperbarui otomatis berdasarkan total belanja.
 */
public class Pelanggan {

    /** Total belanja minimal agar pelanggan menjadi member. */
    public static final double BATAS_MEMBER = 100000;

    private String idPelanggan;
    private String nama;
    private String noTelepon;
    private double totalBelanja;
    private boolean member;

    /** Konstruktor default. */
    public Pelanggan() {
        this("P000", "Belum diisi", "00000000");
    }

    /**
     * Konstruktor berparameter. Total belanja dimulai dari 0.
     *
     * @param idPelanggan kode unik pelanggan
     * @param nama        nama pelanggan
     * @param noTelepon   nomor telepon, 8 sampai 15 digit
     */
    public Pelanggan(String idPelanggan, String nama, String noTelepon) {
        this.setIdPelanggan(idPelanggan);
        this.setNama(nama);
        this.setNoTelepon(noTelepon);
        this.totalBelanja = 0;
        this.member = false;
    }

    // ===== Getter dan setter =====

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        if (idPelanggan == null || idPelanggan.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pelanggan tidak boleh kosong.");
        }
        this.idPelanggan = idPelanggan.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        if (noTelepon == null || !noTelepon.trim().matches("\\d{8,15}")) {
            throw new IllegalArgumentException("Nomor telepon harus 8 sampai 15 digit angka.");
        }
        this.noTelepon = noTelepon.trim();
    }

    public double getTotalBelanja() {
        return totalBelanja;
    }

    public void setTotalBelanja(double totalBelanja) {
        if (totalBelanja < 0) {
            throw new IllegalArgumentException("Total belanja tidak boleh negatif.");
        }
        this.totalBelanja = totalBelanja;
        perbaruiStatusMember();
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    // ===== Method perilaku =====

    /**
     * Menambah total belanja setelah pesanan disimpan.
     *
     * @param jumlah nominal belanja, harus lebih dari 0
     */
    public void tambahBelanja(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Nominal belanja harus lebih dari 0.");
        }
        setTotalBelanja(totalBelanja + jumlah);
    }

    /**
     * Mengurangi total belanja saat pesanan dibatalkan.
     * Hasil tidak pernah di bawah 0.
     *
     * @param jumlah nominal yang dikurangi
     */
    public void kurangiBelanja(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Nominal pengurangan harus lebih dari 0.");
        }
        setTotalBelanja(Math.max(0, totalBelanja - jumlah));
    }

    /** Menetapkan status member: true jika total belanja mencapai batas. */
    public void perbaruiStatusMember() {
        this.member = totalBelanja >= BATAS_MEMBER;
    }

    /** Mencetak informasi pelanggan dalam satu baris. */
    public void tampilkanInfo() {
        System.out.printf("%-6s | %-18s | %-14s | belanja %-11s | %s%n",
                idPelanggan, nama, noTelepon, Rupiah.format(totalBelanja),
                member ? "Member" : "Non-member");
    }
}
