# YouRSearchEngine

Search Engine sederhana, yang diimplentasikan menggunakan **Java** (backend), **JavaFX**, **FXML** (User Interface).
Fitur-fitur yang diimplementasikan pada search engine ini adalah:

1. Data Preprocessing 
   - case folding
   - lemmatization, dengan memanfaatkan library **Stanford CoreNLP** (https://stanfordnlp.github.io/CoreNLP/)
   - normalization
   - porter stemmer
2. Inverted Index
3. Precision / Recall
4. User Interface
5. Boolean Query

## Library eksternal yang dibutuhkan
Terdapat dua file jar eksternal yang di download terpisah yaitu:
1. stanford-corenlp-3.9.2.jar
2. stanford-corenlp-3.9.2-models.jar

File file tersebut dapat diunggah di http://central.maven.org/maven2/edu/stanford/nlp/stanford-corenlp/3.9.2/

## Requirements
1. Pastikan komputer anda terinstal java minimal versi **JDK 1.7**. Untuk memeriksa nya, ketik `java -version` di command prompt, 
   apabila java sudah terinstall dengan benar, command prompt akan mengeluarkan
   ```
   java version "<versi_jdk>"
   Java(TM) SE Runtime Environment (build <angka random,tergantung versi JDK yang diinstall>)
   Java HotSpot(TM) Client VM (build 25.231-b11, mixed mode, sharing)
   ```
   atau sejenisnya
   
## Cara penggunaan
