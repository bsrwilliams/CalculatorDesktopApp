package calculatortest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CalculatorTest {
private CalculatorGUI calculator;

	@Test
	void basicBrackets() {
		calculator = new CalculatorGUI();
		String result = calculator.result("(5*4)");
		assertEquals("20", result);
	}
	
	@Test
	void missingBrackets() {
		calculator = new CalculatorGUI();
		String result = calculator.result("(5*4");
		assertEquals("Missing one or more ')'. Amount: 1", result);
	}
	
	@Test
	void missingMultipleBrackets() {
		calculator = new CalculatorGUI();
		String result = calculator.result("(5*4+(3+1");
		assertEquals("Missing one or more ')'. Amount: 2", result);
	}

	@Test
	void basicIndices() {
		calculator = new CalculatorGUI();
		String result = calculator.result("2^4");
		assertEquals("16", result);
	}
	
	@Test
	void basicDivision() {
		calculator = new CalculatorGUI();
		String result = calculator.result("16/4");
		assertEquals("4", result);
	}
	
	@Test
	void basicMultiplication() {
		calculator = new CalculatorGUI();
		String result = calculator.result("25*4");
		assertEquals("100", result);
	}
	
	@Test
	void basicAddition() {
		calculator = new CalculatorGUI();
		String result = calculator.result("5+7");
		assertEquals("12", result);
	}
	
	@Test
	void basicSubtraction() {
		calculator = new CalculatorGUI();
		String result = calculator.result("9-4");
		assertEquals("5", result);
	}
	
	@Test
	void basicModulo() {
		calculator = new CalculatorGUI();
		String result = calculator.result("15%4");
		assertEquals("3", result);
	}
	
	@Test
	void complexBrackets() {
		calculator = new CalculatorGUI();
		String result = calculator.result("2+(3*1)-1");
		assertEquals("4", result);
	}
	
	@Test
	void complexBracketsTestTwo() {
		calculator = new CalculatorGUI();
		String result = calculator.result("2+((3*1)*3)-1");
		assertEquals("10", result);
	}
	
	@Test
	void negativeAdditionValues() {
		calculator = new CalculatorGUI();
		String result = calculator.result("-5+-3");
		assertEquals("-8", result);
	}
	
	@Test
	void doublerNegativeAdditionValues() {
		calculator = new CalculatorGUI();
		String result = calculator.result("-5--3");
		assertEquals("-2", result);
	}
	
	@Test
	void decimalEquations() {
		calculator = new CalculatorGUI();
		String result = calculator.result("2.2+3.5");
		assertEquals("5.7", result);
	}
	
	@Test
	void largeModularArithmetic() {
		calculator = new CalculatorGUI();
		String result = calculator.result("9^17%372389921");
		assertEquals("351127658", result);
	}	
}
