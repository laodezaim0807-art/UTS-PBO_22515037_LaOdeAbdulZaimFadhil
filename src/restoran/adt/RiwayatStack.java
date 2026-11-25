package restoran.adt;

import restoran.model.Pesanan;

/**
 * ADT Stack (LIFO) berbasis array untuk riwayat pesanan terakhir.
 * Dipakai untuk fitur batalkan pesanan terakhir.
 *
 * Atribut internal private. Kondisi penuh dan kosong ditangani lewat
 * nilai kembalian (false atau null), tidak lewat exception.
 */
public class RiwayatStack {

    /** Kapasitas bawaan jika tidak ditentukan. */
    public static final int KAPASITAS_DEFAULT = 5;

    private final Pesanan[] data;
    private int top;   // indeks elemen teratas, -1 berarti kosong

    /** Membuat stack dengan kapasitas bawaan. */
    public RiwayatStack() {
        this(KAPASITAS_DEFAULT);
    }

    /**
     * Membuat stack dengan kapasitas tertentu.
     *
     * @param kapasitas jumlah maksimum elemen, harus lebih dari 0
     */
    public RiwayatStack(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas stack harus lebih dari 0.");
        }
        this.data = new Pesanan[kapasitas];
        this.top = -1;
    }

    /**
     * Menaruh pesanan di puncak stack.
     * Prasyarat: stack tidak penuh. Efek: size bertambah 1.
     *
     * @param pesanan pesanan yang disimpan
     * @return true jika berhasil, false jika stack penuh atau pesanan null
     */
    public boolean push(Pesanan pesanan) {
        if (pesanan == null || isFull()) {
            return false;
        }
        data[++top] = pesanan;
        return true;
    }

    /**
     * Mengambil dan menghapus pesanan di puncak stack.
     * Prasyarat: stack tidak kosong. Efek: size berkurang 1.
     *
     * @return pesanan teratas, atau null jika stack kosong
     */
    public Pesanan pop() {
        if (isEmpty()) {
            return null;
        }
        Pesanan hasil = data[top];
        data[top--] = null;   // lepas referensi agar tidak tertahan
        return hasil;
    }

    /**
     * Melihat pesanan di puncak tanpa menghapusnya.
     *
     * @return pesanan teratas, atau null jika stack kosong
     */
    public Pesanan peek() {
        return isEmpty() ? null : data[top];
    }

    /** @return true jika stack tidak berisi elemen */
    public boolean isEmpty() {
        return top == -1;
    }

    /** @return true jika jumlah elemen sama dengan kapasitas */
    public boolean isFull() {
        return top == data.length - 1;
    }

    /** @return jumlah elemen saat ini */
    public int size() {
        return top + 1;
    }

    /** @return kapasitas maksimum stack */
    public int kapasitas() {
        return data.length;
    }

    /** Mengosongkan stack. Efek: size menjadi 0. */
    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }

    /** Mencetak isi stack dari puncak ke dasar. */
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("(riwayat kosong)");
            return;
        }
        for (int i = top; i >= 0; i--) {
            Pesanan p = data[i];
            System.out.printf("%s%d. %s | %s | total %s%n",
                    i == top ? "[TOP] " : "      ", i + 1, p.getKodePesanan(),
                    p.getPelanggan().getNama(),
                    restoran.util.Rupiah.format(p.hitungTotal()));
        }
    }
}
