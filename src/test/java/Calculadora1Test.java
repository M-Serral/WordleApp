import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Calculadora1Test {
	
	@Test
	public void testSuma() {
		// Arrange / Given
		Calculadora calculadora = new Calculadora();
		// Act / When
		double res = calculadora.suma(1, 1);
		// Assert / Then
		assertEquals(2, res, 0);
	}
	
}