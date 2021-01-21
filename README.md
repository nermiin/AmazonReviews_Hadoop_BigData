# AMAZON MÜŞTERİ YORUMLARININ ANALİZİ


## İçerik

- GİRİŞ
   - Veri Setinin Linki
   - Verilere Giriş
- Veri Setinin Analizi
   - Analiz Neler Kapsıyor?
   - Kullanılan Teknikler / Teknolojiler
- Analiz Aşamaları
   - Veri Seti Hadoop Tarafına Aktarmak
   - 1. Veri setindeki toplam ürün sayısını bulmak
   - 2. Her ürün için ortalama ürün değerlendirme puanı bulmak
   - 3. Sayıya göre sıralanmış en çok yorum alan ürünü bulmak
   - 4. TopN Değerlendirmede Bulunan Ürünlerin Toplam Değerlendirmeleri Bulmak
   - 5. Her ürün için yorum yapan tüm kullanıcıları bulmak
   - 6. Ürünün yorumları tarihe göre bölümlendirilmiş tüm kayıtları bulmak
   - 7. Ürünleri yıldız değerlendirmelerine göre kullanıcıya önermek
   - 8. Her ürün için tarihe göre gruplandırılmış yorumların sayısını bulmak



## GİRİŞ

Bu çalışmada kapsamına Amazon sitesi analizi, büyük veri analizi esas alarak ve Hadoop mimarisi, Mahout ve Pig kullanarak gerçekleştirilmiş. 

Amazon sitesindeki bulunan kamera ürününe ait yorumların incelenerek bilgilendirici bir sonuç üretilmiştir. Ürün olarak kamera seçildiğine rağmen, Amazon 

sitesindeki herhangi bir ürün için geçerli olabilir, veri seti bulmak şartıyla.

### Veri Setinin Linki

Veri seti bu linkten indirebilir Amazon Camera Reviews.
[Amazon Camera Reviews](https://s3.amazonaws.com/amazon-reviews-pds/tsv/amazon_reviews_us_Camera_v1_00.tsv.gz "Amazon Camera Reviews")

Ayrıca [bu linkten](https://s3.amazonaws.com/amazon-reviews-pds/readme.html ) de erişim sağlanabilir.

### Verilere Giriş

#### VERİ FORMATI

Text dosyasındaki veriler ('\ t') ile ayrılmış. Bu veri seti 1 GB boyutundadır. Dosyadaki ilk satır başlıktır; 1 satır
1 kayda karşılık gelir.

#### VERİ SÜTUNLARI:

```
o marketplace: 2 harlı ülke kodu.
o customer_id: Tek bir yazar tarafından yazılan yorumları toplamak için bir anahtaradır.
o review_id: Yoruma ait anahtar.
o product_id: Yorum yapılan ürünün anahtarıdır. Çok dilli veri setinde, aynı ürün için farklı ülkelerdeki
incelemeler aynı şekilde gruplandırılabilir.
o product_parent: Aynı ürüne ilişkin yorumları toplamak için kullanılabilen anahtardır.
o product_title: Ürünün başlığı.
o product_category : Yorumları gruplamak için kullanılabilen geniş ürün kategorisi.
o star_rating : Yorumlara 1-5 arasında verilen yıldız sayısı.
o helpful_votes : Yararlı oyların sayısı.
o total_votes : Yorumun aldığı toplam oy sayısı.
o vine : Yorum, Vine programının bir parçası olarak yazılmıştır.
o verified_purchase : Yorumu yapan, doğrulanmış bir satın alma işlemi yapmış.
o review_headline : Yorumun başlığı.
o review_body : Yorumun metni.
o review_date : Yorum tarihi.
```

## Veri Setinin Analizi

### Analiz Neler Kapsıyor?

1. Veri setinde bulunan toplam ürün sayısını bulmak.
2. Her ürün için ortalama ürün değerlendirme puanı bulmak.
3. Sayıya göre sıralanmış en çok yorum alan ürünü bulmak.
4. En çok yorum alan ürünlerin arasında toplam ürün değerlendirmesini bulmak.
5. Her ürün için yorum yapan tüm kullanıcıları bulmak.
6. Ürünün yorumları tarihe göre bölümlendirilmiş tüm kayıtları bulmak.
7. Ürünleri yıldız değerlendirmelerine göre kullanıcıya önermek.
8. Her ürün için tarihe göre gruplandırılmış yorumların sayısını bulmak.
9. Her ürün yıldız değerlendirmesini için ürün sayısını bulmak.

### Kullanılan Teknikler / Teknolojiler

1. Hadoop MapReduce.
2. Summarization Pattern – Numerical Summarization, Inverted Index.
3. Joins – Reduce Side Inner Join.
4. Partitioning.
5. Secondary Sorting.
6. MapReduce Chaining.
7. Filtering Pattern – TopN filtering pattern.
8. Mahout Recommendation.
9. Apache Pig.

## Analiz Aşamaları

### Veri Seti Hadoop Tarafına Aktarmak


### 1. Veri setindeki toplam ürün sayısını bulmak

Hadoop MapReduce teknolojisi ile veri setindeki toplam urun sayısı bulunmaktadır.

İnput : Veri Seti

Çıktı : ProductID, Product Count.

Metot : MapReduce.



**Mapreduce'u çalıştırma komutu:**

sudo hadoop jar <jar dosyası bulunduğu klasörün yolu>/AmazonAnalysis.jar



### 2. Her ürün için ortalama ürün değerlendirme puanı bulmak

Hadoop MapReduce teknolojisi ile veri setindeki toplam urun sayısı bulunmaktadır.

İnput : Veri Seti

Çıktı : ProductID, Product Count, Product Average Rating.

Metot: MapReduce, Reduce metodu kombiner olarak da kullanılmıştır.


**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.AverageProductRating.ProductAverageRatingMain /input /output/AverageProductRating


### 3. Sayıya göre sıralanmış en çok yorum alan ürünü bulmak

İnput : 1. analizin çıktı dosyası <-> ProductCount

Çıktı: Product Count, ProductID.

Metot : Secondry Sorting. WritableComparator sınıfını genişleterek ilk 10 ürünü azalan sırada alacak şekilde
gerçekleştirilmiştir. 

TopN filtreleme modeli, ilk 10 ürünü bulmak için kullanılır.Filtreme Patern TopN

**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.topNProducts.TopNProductsMain /output/ProductCount /output/TopNProducts


### 4. TopN Değerlendirmede Bulunan Ürünlerin Toplam Değerlendirmeleri Bulmak

İnput : 2. Ve 3. analizin çıktı dosyaları <-> TopNProducts, AverageProductRating

Çıktı: ProductID, Product Count, Product Average

Metot : Reduce Side Inner Join teknolojisi kesişen ürünleri elde etmek için kullanıldı.



**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.reduceSideInnerJoin.JoinMain /output/TopNProducts /output/AverageProductRating
/output/ReduceSideInnerJoin


### 5. Her ürün için yorum yapan tüm kullanıcıları bulmak

İnput : Veri Seti

Çıktı: : ProductID, User ID.

Metot : Kullanıcı bilgileri elde etmek için Inverted Index summarization pattern kullanıldı.


**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.invertedIndexPattern.InvertedIndexMain /input /output/InvertedIndex



### 6. Ürünün yorumları tarihe göre bölümlendirilmiş tüm kayıtları bulmak

İnput : Veri Seti

Çıktı: Ayrı bölümlere bölünmüş tüm veriler.

Metot : Bu analizi bölümlemek için özel bir sınıf genişletirmiştir.



**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar sau.YearPartitioner /input
/output/YearPartitioner




### 7. Ürünleri yıldız değerlendirmelerine göre kullanıcıya önermek

İlk olarak UserID, ProductID ve start rating’i almak için veriler temizlenir.

İnput : Temizlenmiş veriler.

Çıktı: UserID, ProductID, start rating.

Metot : Apache Mahout, MapReduce zinciri kullanıcıya ürün önermek amacıyla kullanılmıştır.



**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.mahoutRecommendation.RecommendationMain /input /output/MahoutRecommendation/data
/output/MahoutRecommendation/recommendation



### 8. Her ürün için tarihe göre gruplandırılmış yorumların sayısını bulmak

Apache Pig, her ürün için tarihe göre gruplandırılmış inceleme sayısını bulmak için kullanılmış.

İnput : Veri Seti

Çıktı: Date, Product Count.

Metot : Apache Pig.


**Komut** :

pig /home/cloudera/workspace/AmazonAnalysis/src/sau/pig/DailyReviewsCount.pig



#### THE END :)


