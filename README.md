# Spring Boot MyBatis 示範專案

## 專案說明
這是一個使用 Spring Boot 和 MyBatis 實作的咖啡訂單管理系統示範專案。本專案展示了如何使用 MyBatis 進行物件關聯對應（ORM）的實作，並包含完整的單元測試案例。

## 技術架構
- Spring Boot 3.4.5
- MyBatis 3.0.3
- Java 21
- H2 資料庫
- Lombok
- Joda Money

## 專案結構
```
src/main/java/tw/fengqing/spring/data/mybatisdemo/
├── MybatisDemoApplication.java    # 應用程式主類別
├── model/                         # 資料模型
│   └── Coffee.java               # 咖啡實體類別
├── mapper/                        # 資料存取層
│   └── CoffeeMapper.java         # 咖啡資料存取介面
└── test/                         # 測試程式碼
    └── MybatisDemoApplicationTests.java  # 單元測試類別
```

## 核心程式碼說明

### 1. 咖啡實體類別 (Coffee.java)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coffee {
    private Long id;          // 咖啡編號
    private String name;      // 咖啡名稱
    private Money price;      // 咖啡價格
    private Date createTime;  // 建立時間
    private Date updateTime;  // 更新時間
}
```
**程式碼說明**：
- `@Data`：Lombok 註解，自動產生 getter/setter 等方法
- `@Builder`：Lombok 註解，提供建構器模式
- `@AllArgsConstructor`：Lombok 註解，產生包含所有欄位的建構子
- `@NoArgsConstructor`：Lombok 註解，產生無參數建構子

### 2. 咖啡資料存取介面 (CoffeeMapper.java)
```java
@Mapper
public interface CoffeeMapper {
    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Coffee coffee);

    @Select("select * from t_coffee where id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
    })
    Coffee findById(@Param("id") Long id);
}
```
**程式碼說明**：
- `@Mapper`：標記為 MyBatis 的 Mapper 介面
- `@Insert`：定義新增資料的 SQL 語句
- `@Options`：設定自動產生主鍵並回填到實體物件
- `@Select`：定義查詢資料的 SQL 語句
- `@Results`：定義查詢結果的欄位對應關係

### 3. 單元測試類別 (MybatisDemoApplicationTests.java)
```java
@SpringBootTest
@Slf4j
class MybatisDemoApplicationTests {
    @Autowired
    private CoffeeMapper coffeeMapper;

    @Test
    void testCoffeeCRUD() {
        // 建立測試資料
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("TWD"), 100.0))
                .build();

        // 測試儲存
        int count = coffeeMapper.save(espresso);
        assertEquals(1, count, "儲存咖啡應該返回 1");
        assertNotNull(espresso.getId(), "儲存後應該有編號");
        
        // 測試查詢
        Coffee found = coffeeMapper.findById(espresso.getId());
        assertNotNull(found, "應該能找到儲存的咖啡");
        assertEquals(espresso.getName(), found.getName(), "咖啡名稱應該相同");
        assertEquals(espresso.getPrice(), found.getPrice(), "咖啡價格應該相同");
    }
}
```
**程式碼說明**：
- `@SpringBootTest`：啟動完整的 Spring 應用程式上下文
- `@Slf4j`：啟用 Lombok 日誌功能
- 測試案例包含：
  - 資料儲存測試
  - 資料查詢測試
  - 屬性驗證測試

## 資料庫設定
專案使用 H2 記憶體資料庫，資料表結構如下：
```sql
create table t_coffee (
    id bigint not null auto_increment,
    name varchar(255),
    price bigint not null,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);
```

## 建置與執行

### 前置需求
- JDK 21 或以上版本
- Maven 3.6 或以上版本

### 建置步驟
1. 複製專案
```bash
git clone https://github.com/SpringMicroservicesCourse/mybatis-demo
```

2. 進入專案目錄
```bash
cd mybatis-demo
```

3. 使用 Maven 建置專案
```bash
mvn clean package
```

4. 執行應用程式
```bash
mvn spring-boot:run
```

### 執行測試
```bash
mvn test
```

## 注意事項
1. 資料庫連線設定在 `application.properties` 中，預設使用 H2 記憶體資料庫
2. 使用 `@Options(useGeneratedKeys = true, keyProperty = "id")` 確保自動產生主鍵並回填
3. 使用 `@Results` 註解處理資料庫欄位與 Java 物件的對應關係

## 常見問題
1. 如果遇到 "No active profile set" 警告，這是正常的，表示使用預設設定檔
2. 如果遇到 "Sharing is only supported for boot loader classes" 警告，這是 JVM 相關的警告，不影響程式執行

## 後續擴充建議
1. 新增更新和刪除功能
2. 實作批量操作功能
3. 加入交易管理機制
4. 實作更複雜的查詢功能
5. 加入資料驗證機制 

## 授權說明

本專案採用 MIT 授權條款，詳見 LICENSE 檔案。 