# UTS Pemrograman Berorientasi Objek

- Nama: La Ode Abdul Za'im Fadhil
- NIM: 22515037
- Topik: Restoran (digit terakhir 7)
- ADT: Stack (digit kedua dari belakang 3, 3 mod 3 = 0)

Aplikasi console Java untuk mengelola menu, pelanggan, dan pesanan restoran.
Stack dipakai sebagai riwayat pesanan terakhir, sehingga pesanan terakhir dapat dibatalkan (LIFO).

## Struktur

```
src/restoran/Main.java              menu console dan input
src/restoran/model/Menu.java        entitas menu
src/restoran/model/Pelanggan.java   entitas pelanggan
src/restoran/model/Pesanan.java     entitas pesanan (pajak 10% otomatis)
src/restoran/model/Restoran.java    pengelola data dan aturan bisnis
src/restoran/adt/RiwayatStack.java  ADT Stack berbasis array
src/restoran/util/Rupiah.java       format rupiah
tests/                              input dan hasil 11 skenario uji
```

## Menjalankan

Butuh JDK 17 atau lebih baru.

```
mkdir out
javac -encoding UTF-8 -d out $(find src -name '*.java')
java -cp out restoran.Main
```

Pilih menu 11 untuk memuat data contoh.

## Aturan bisnis

- Pesanan memuat minimal 1 menu.
- Pajak 10 persen dihitung otomatis (`Pesanan.TARIF_PAJAK`).
- Stok tidak boleh negatif dan tidak boleh kurang dari jumlah pesanan.
- Harga menu harus lebih dari 0.
- Riwayat Stack berkapasitas 5. Jika penuh, pesanan baru ditolak sampai ada pembatalan atau konfirmasi riwayat.

## Pengujian

```
bash tests/run_all.sh
```

Skrip mengisi input dari `tests/sNN_*.txt` dan menulis hasil ke `tests/sNN_*.out`.
