import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Calculadora4Test {

	Calculadora calc;

	@BeforeEach
	public void setUp() {
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
}