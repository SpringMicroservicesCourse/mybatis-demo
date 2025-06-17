package tw.fengqing.spring.data.mybatisdemo;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.fengqing.spring.data.mybatisdemo.mapper.CoffeeMapper;
import tw.fengqing.spring.data.mybatisdemo.model.Coffee;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MybatisDemoApplicationTests {

	@Autowired
	private CoffeeMapper coffeeMapper;

	@Test
	void testCoffeeCRUD() {
		// 創建測試資料
		Coffee espresso = Coffee.builder()
				.name("espresso")
				.price(Money.of(CurrencyUnit.of("TWD"), 100.0))
				.build();

		// 測試保存
		int count = coffeeMapper.save(espresso);
		assertEquals(1, count, "保存咖啡應該返回 1");
		assertNotNull(espresso.getId(), "保存後應該有 ID");
		log.info("Saved Coffee: {}", espresso);

		// 測試查詢
		Coffee found = coffeeMapper.findById(espresso.getId());
		assertNotNull(found, "應該能找到保存的咖啡");
		assertEquals(espresso.getName(), found.getName(), "咖啡名稱應該相同");
		assertEquals(espresso.getPrice(), found.getPrice(), "咖啡價格應該相同");
		log.info("Found Coffee: {}", found);

		// 創建第二個咖啡
		Coffee latte = Coffee.builder()
				.name("latte")
				.price(Money.of(CurrencyUnit.of("TWD"), 125.0))
				.build();

		// 再次測試保存
		count = coffeeMapper.save(latte);
		assertEquals(1, count, "保存第二個咖啡應該返回 1");
		assertNotNull(latte.getId(), "保存後應該有 ID");
		log.info("Saved Coffee: {}", latte);

		// 測試查詢第二個咖啡
		found = coffeeMapper.findById(latte.getId());
		assertNotNull(found, "應該能找到第二個保存的咖啡");
		assertEquals(latte.getName(), found.getName(), "第二個咖啡名稱應該相同");
		assertEquals(latte.getPrice(), found.getPrice(), "第二個咖啡價格應該相同");
		log.info("Found Coffee: {}", found);
	}
}

