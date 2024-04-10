import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Calculadora5Test {

	Calculadora calc;

	@BeforeAll
	public static void setUpClass() {
		System.out.println("Before all tests");
	}
	
	@BeforeEach
	public void setUp() {
		System.out.println("Before test");
		this.calc = new Calculadora();
	}

	@Test
	public void testSuma() {
		double res = calc.suma(1, 1);
		assertEquals(2, res, 0, "1+1 should be equal to 2");
	}

	@Test
	public void testResta() {
		double res = calc.resta(1, 1);
		assertEquals(0, res, 0, "1-1 should be equal to 0");
	}
	
	@AfterEach
	public void teardown() {
		System.out.println("After test");
	}
	
	@AfterAll
	public static void teardownClass() {
		System.out.println("After all tests");
	}
}