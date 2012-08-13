package unit;

import static asserts.ModelAssert.assertDifference;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.Order;

import model.UnUseModel;
import models.Product;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import asserts.CallBack;
import factory.BuildCallBack;
import factory.FactoryBoy;
import factory.OrderFactory;
import factory.ProductFactory;

public class ProductTest extends UnitTest {
	
	Product product = null;
	
	@Before
	public void setUp() {
		FactoryBoy.init(Product.class);
	}
	
	@Test(expected=RuntimeException.class)
	public void testFindUnExistsModelFactory() {
		FactoryBoy.findModelFactory(UnUseModel.class);
	}
	
	@Test
	public void testFindModelFactory() {
		assertEquals(ProductFactory.class, FactoryBoy.findModelFactory(Product.class).getClass());
	}
 
	@Test
	public void testCreateProduct() throws Exception {
		assertDifference(Product.class, 1, new CallBack() {
			@Override
            public void run() {
				product = FactoryBoy.build(Product.class);
				product.save();	            
            }
		});
	}
	
	@Test
	public void testUpdateProduct() {
		product = FactoryBoy.create(Product.class);
		product.name = "New Name";
		product.save();
		assertTrue(true);
	}
	
	@Test
	public void testFindByName() {
		Product product = FactoryBoy.create(Product.class, new BuildCallBack<Product>() {
			@Override
            public Product build(Product target) {
				target.name = "HHKB";
	            return target;
            }
		});
		
		Product p = Product.find("byName", "HHKB").first();
		assertEquals(product.id, p.id);
	}
	
	@Test
	public void testDeleteProduct() throws Exception {
		product = FactoryBoy.create(Product.class);
		assertDifference(Product.class, -1, new CallBack() {
			@Override
            public void run() {
				product.delete();	            
            }
		});
	}
	
	/*
	@Test
	public void testModelFactorySequence() {
		int orderSequence = FactoryBoy.sequence(Order.class);
		int productSequence = ProductFactory.sequence();
		assertEquals(productSequence, orderSequence);
		
		assertEquals(orderSequence + 1, OrderFactory.sequence());
		
		int orderSequence1 = OrderFactory.sequence();
		int productSequence1 = ProductFactory.sequence();
		assertEquals(productSequence1 + 1, orderSequence1);
	}
	*/
	
	@Test
	public void testGetNamedProduct() throws Exception {
		product = FactoryBoy.create(Product.class, "hhkb");
		assertEquals("HHKB", product.name);
		assertEquals(new BigDecimal("2000.00"), product.price);
		
		Product product2 =FactoryBoy.create(Product.class, "hhkb2"); 
		assertEquals("HHKB Pro2", product2.name);
		assertEquals(new BigDecimal("2000.00"), product2.price);
	}
	
	@Test
	public void testBatchCreateProducts() throws Exception {
		assertDifference(Product.class, 5, new CallBack() {
			@Override
            public void run() {
				List<Product> products = FactoryBoy.batchCreate(5, Product.class);
				assertEquals(5, products.size());
			}
		});
	}
	
}
