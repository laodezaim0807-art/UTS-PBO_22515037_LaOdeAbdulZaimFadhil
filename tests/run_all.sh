#!/bin/bash
# Menjalankan semua skenario uji. Jalankan dari folder root repositori.
set -e
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name '*.java')
for f in tests/s*.txt; do
  b=$(basename "$f" .txt)
  java -cp out restoran.Main < "$f" > "tests/$b.out" 2>&1
  echo "$b selesai, jumlah [ERROR]: $(grep -c ERROR tests/$b.out)"
done
