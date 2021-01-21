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

Bu çalışmada kapsamına Amazon sitesi analizi, büyük veri analizi esas alarak ve Hadoop mimarisi, Mahout ve

Pig kullanarak gerçekleştirilmiş. Amazon sitesindeki bulunan kamera ürününe ait yorumların incelenerek

bilgilendirici bir sonuç üretilmiştir. Ürün olarak kamera seçildiğine rağmen, Amazon sitesindeki herhangi bir

ürün için geçerli olabilir, veri seti bulmak şartıyla.

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

**Mapper** :

Aşağıdaki kod parçacığında gösterilen map fonksiyonu input olarak product_id gösterilmiştir.

public void map(LongWritable key, Text value, Mapper<LongWritable, Text,
Text, IntWritable>.Context context) throws IOException,
InterruptedException {
try {
String[] input = value.toString().split("\\t");
String productId = input[ 3 ].trim();
this.word.set(productId);
context.write(this.word, this.one);
} catch (Exception var6) {
System.out.println("Something went wrong in Mapper Task: ");
var6.printStackTrace();
}
}

**Reducer :**

Ürünlerin anahtarları baz alarak reduce işlemi yapıldı dolaysıyla her uruna ait urun sayısı bulunmaktadır.

public void reduce(Text key, Iterable<IntWritable> values, Reducer<Text,
IntWritable, Text, IntWritable>.Context context) {
try {
int sum = 0 ;

IntWritable val;
for(Iterator var5 = values.iterator(); var5.hasNext(); sum +=
val.get()) {
val = (IntWritable)var5.next();
}

IntWritable count = new IntWritable(sum);
context.write(key, count);
} catch (Exception var7) {
System.out.println("Something went wrong in Reducer Task: ");
var7.printStackTrace();
}
}


**Mapreduce'u çalıştırma komutu:**

sudo hadoop jar <jar dosyası bulunduğu klasörün yolu>/AmazonAnalysis.jar

Çıktı :

**Çıktı :**

### 2. Her ürün için ortalama ürün değerlendirme puanı bulmak

Hadoop MapReduce teknolojisi ile veri setindeki toplam urun sayısı bulunmaktadır.

İnput : Veri Seti

Çıktı : ProductID, Product Count, Product Average Rating.

Metot: MapReduce, Reduce metodu kombiner olarak da kullanılmıştır.

**Mapper** :

public void map(LongWritable key, Text value, Context context)
throws IOException, InterruptedException {

try {

String input[] = value.toString().split("\\t");
String productId = input[ 3 ].trim();

if (!productId.isEmpty()) {
id.set((productId));
outCountAverage.setCount(Long.valueOf( 1 ));

outCountAverage.setAverage(Float.valueOf(input[ 7 ].trim()));
context.write(id, outCountAverage);
}


**Reducer :**

public void reduce(Text key, Iterable<CountAverageTuple> value,
Context context)
throws IOException, InterruptedException {

try {
long count = 0 ;
float sum = 0 ;

for (CountAverageTuple val: value) {
count += val.getCount();
sum += val.getCount() * val.getAverage();
}

result.setCount(count);
result.setAverage(sum/count);
context.write(key, result);

}

**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.AverageProductRating.ProductAverageRatingMain /input /output/AverageProductRating

**Çıktı** :

### 3. Sayıya göre sıralanmış en çok yorum alan ürünü bulmak

İnput : 1. analizin çıktı dosyası <-> ProductCount

Çıktı: Product Count, ProductID.

Metot :

```
 Secondry Sorting. WritableComparator sınıfını genişleterek ilk 10 ürünü azalan sırada alacak şekilde
gerçekleştirilmiştir. TopN filtreleme modeli, ilk 10 ürünü bulmak için kullanılır.
 Filtreme Patern TopN
```

**Mapper** :

public void map(LongWritable key, Text value, Context context){

String[] row = value.toString().split("\\t");
String productId = row[ 0 ].trim();
int count = Integer.parseInt(row[ 1 ].trim());
try{
Text id = new Text(productId);
IntWritable prodRating = new IntWritable(count);
context.write(prodRating, id);
}

**Reducer** :

public void reduce(IntWritable key, Iterable<Text> value, Context
context)
throws IOException, InterruptedException{
try {
for(Text val: value){
if(count<N)
{
context.write(key,val);
}
count++;
}

}

**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.topNProducts.TopNProductsMain /output/ProductCount /output/TopNProducts

**Çıktı** :


### 4. TopN Değerlendirmede Bulunan Ürünlerin Toplam Değerlendirmeleri Bulmak

İnput : 2. Ve 3. analizin çıktı dosyaları <-> TopNProducts, AverageProductRating

Çıktı: ProductID, Product Count, Product Average

Metot : Reduce Side Inner Join teknolojisi kesişen ürünleri elde etmek için kullanıldı.

**Top products Mapper:**

public void map(LongWritable key, Text value, Context context){

String[] row = value.toString().split("\\t");
String productId = row[ 0 ].trim();
int count = Integer.parseInt(row[ 1 ].trim());
try{
Text id = new Text(productId);
IntWritable prodRating = new IntWritable(count);
context.write(prodRating, id);

}

**Rating Mapper** :

protected void map(LongWritable key, Text value, Mapper.Context context) throws
IOException, InterruptedException {

Text productId = new Text();
Text rating = new Text();

try {
String[] input = value.toString().split("\\t");
productId.set(input[ 0 ].trim());
rating.set("*" + input[ 2 ].trim());

context.write(productId, rating);

}

**Reducer** :

protected void reduce(Text key, Iterable<Text> values, Context context) throws
IOException, InterruptedException {

try {
Set<String> listA = new HashSet<String>();
Set<String> listB = new HashSet<String>();

for (Text text: values) {
if (text.toString().startsWith("#"))
listA.add(text.toString().substring( 1 ));
else if (text.toString().startsWith("*"))
listB.add(text.toString().substring( 1 ));
}


if(!listA.isEmpty() && !listB.isEmpty()) {
for (String A: listA) {
for (String B: listB) {
context.write(new Text(A), new Text(B));
}
}
}
}

**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.reduceSideInnerJoin.JoinMain /output/TopNProducts /output/AverageProductRating
/output/ReduceSideInnerJoin

**Çıktı** :

### 5. Her ürün için yorum yapan tüm kullanıcıları bulmak

İnput : Veri Seti

Çıktı: : ProductID, User ID.

Metot : Kullanıcı bilgileri elde etmek için Inverted Index summarization pattern kullanıldı.

**Mapper** :

protected void map(LongWritable key, Text value, Context context) throws IOException,
InterruptedException {

if(key.get()== 0 ){
return;
}
try{
String[] tokens = value.toString().split("\\t");
userId.set(tokens[ 1 ]);
productId.set(tokens[ 3 ]);
context.write(productId, userId);

## }


**Reducer** :

public void reduce(Text key, Iterable<Text> values, Context context)
throws IOException, InterruptedException {

try {
StringBuilder sb = new StringBuilder();
boolean first = true;

for(Text id: values){
if(first){
first = false;
}
else{
sb.append(" ");
}
sb.append(id.toString());
}

result.set(sb.toString());
context.write(key, result);

}

**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.invertedIndexPattern.InvertedIndexMain /input /output/InvertedIndex

**Çıktı** :

### 6. Ürünün yorumları tarihe göre bölümlendirilmiş tüm kayıtları bulmak

İnput : Veri Seti

Çıktı: Ayrı bölümlere bölünmüş tüm veriler.

Metot : Bu analizi bölümlemek için özel bir sınıf genişletirmiştir.


**Custom Partitioner class :**

public static class YearPartitionPartitioner extends Partitioner<Text, Text> {
@Override
public int getPartition(Text key, Text value, int numPartitions){
int n= 1 ;
if(numPartitions== 0 ) return 0 ;
else if(key.equals(("99"))) return n % numPartitions;
else if(key.equals(new Text("00"))) return 2 % numPartitions;
else if(key.equals(new Text("01"))) return 3 % numPartitions ;
else if(key.equals(new Text("02"))) return 4 % numPartitions;
else if(key.equals(new Text("03"))) return 5 % numPartitions;
else if(key.equals(new Text("04"))) return 6 % numPartitions;
else if(key.equals(new Text("05"))) return 7 % numPartitions;
else if(key.equals(new Text("06"))) return 8 % numPartitions;
else if(key.equals(new Text("07"))) return 9 % numPartitions;
else if(key.equals(new Text(" 08 "))) return 10 % numPartitions;

else if (key.equals(new Text("09"))) return 11 % numPartitions;
else if (key.equals(new Text("10"))) return 12 % numPartitions;
else if (key.equals(new Text("11"))) return 13 % numPartitions;
else
return 14 % numPartitions;
}
}

**Mapper** :

protected void map(LongWritable key, Text value, Mapper.Context context) throws
IOException, InterruptedException{

if(key.get()== 0 ){
return;
}

String[] line = value.toString().split("\\t");
String[] yearPart = line[ 14 ].split("-");
String yearVal = yearPart[ 2 ].trim();

year.set(yearVal);
inputRec.set(value);

context.write(year, inputRec);
}

**Reducer** :

protected void reduce(Text key, Iterable<Text> values, Reducer.Context context) throws
IOException, InterruptedException{
for(Text t: values){

context.write(t, NullWritable.get());
}
}


**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar sau.YearPartitioner /input
/output/YearPartitioner

**Çıktı** :

**1. bölümün çıktı dosyasının başı**

### 7. Ürünleri yıldız değerlendirmelerine göre kullanıcıya önermek

İlk olarak UserID, ProductID ve start rating’i almak için veriler temizlenir.

İnput : Temizlenmiş veriler.

Çıktı: UserID, ProductID, start rating.

Metot : Apache Mahout, MapReduce zinciri kullanıcıya ürün önermek amacıyla kullanılmıştır.


**Recommendation Data Mapper** :

protected void map(LongWritable key, Text value, Context context) throws IOException,
InterruptedException{

try {
if(key.get()== 0 ){
return;
}

else{
String[] line = value.toString().split("\\t");
Text output = new Text();
output.set(line[ 1 ] + "," + line[ 4 ] + "," + line[ 7 ]);
// 1: userID, 4:productID, 7:Rating

context.write(NullWritable.get(), output);
}

}

**User Data Mapper :**

protected void map(LongWritable key, Text value, Context context) throws IOException,
InterruptedException {

try {
String[] line = value.toString().split(",");
String userId = line[ 0 ].trim();
context.write(new Text(userId), NullWritable.get());

}

**Reducer** :

protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws
IOException, InterruptedException {

try {

Long userId = Long.valueOf(key.toString());
List<RecommendedItem> recs = genericRecommender.recommend(userId, 2 );

if (!recs.isEmpty()) {

Text res = new Text();
for (RecommendedItem recommendedItem : recs) {

res.set(key.toString() + "Recommend Item Id: " +
recommendedItem.getItemID() +
" Strength of preference: " + recommendedItem.getValue());
}
context.write(NullWritable.get(), res);
}
}


**Main :**

Path inputPath = new Path(args[ 0 ]);
Path dataOutputPath = new Path(args[ 1 ]);
Path recommedationOutputPath = new Path(args[ 2 ]);

FileInputFormat.setInputPaths(getDataJob, inputPath);
FileOutputFormat.setOutputPath(getDataJob, dataOutputPath);

if (fs.exists(dataOutputPath)) {
fs.delete(dataOutputPath, true);
}
mahoutGetDataJobSuccesful = getDataJob.waitForCompletion(true);

if(mahoutGetDataJobSuccesful) {

Job recommendationJob = Job.getInstance(conf, "Recommendation");
String path = dataOutputPath.toString();
recommendationJob.getConfiguration().set("DataPath", path);

**Komut** :

sudo hadoop jar /home/cloudera/Desktop/bigdata/out/AmazonAnalysis.jar
sau.mahoutRecommendation.RecommendationMain /input /output/MahoutRecommendation/data
/output/MahoutRecommendation/recommendation

**Çıktı** :

### 8. Her ürün için tarihe göre gruplandırılmış yorumların sayısını bulmak

Apache Pig, her ürün için tarihe göre gruplandırılmış inceleme sayısını bulmak için kullanılmış.

İnput : Veri Seti

Çıktı: Date, Product Count.

Metot : Apache Pig.


raw_data = load '/input' using PigStorage('\t') AS (marketplace, customer_id,
review_id, product_id, product_parent, product_title, product_category,
star_rating,
helpful_votes, total_votes, vine, verified_purchase,
review_headline, review_body, review_date);

data = STREAM raw_data THROUGH `tail -n +2`
AS (marketplace, customer_id, review_id, product_id, product_parent,
product_title, product_category, star_rating,
helpful_votes, total_votes, vine, verified_purchase, review_headline,
review_body, review_date);

daily = GROUP data by review_date;

daily_reviews = FOREACH daily GENERATE group as review_date,
COUNT(data.review_id) as count;

order_by_data = ORDER daily_reviews BY count DESC;

store order_by_data INTO '/output';

**Komut** :

pig /home/cloudera/workspace/AmazonAnalysis/src/sau/pig/DailyReviewsCount.pig

**Çıktı** :

#### THE END


