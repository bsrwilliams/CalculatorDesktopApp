package calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalculatorGUI implements ActionListener {
	private JFrame frame;
	private JTextField led;
	private JLabel label;
	private final String[] buttons = {"(", ")", "%", "/", "7","8","9","*","4","5","6","+","1","2","3","-","^","0",".","="};
	private final char[] operators = {'^', '/', '*', '+', '-', '%'};

	public CalculatorGUI() {
		// JFrame Configurations
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setTitle("Calculator");
		
		
		// JPanel Configurations
		JPanel ledPanel = new JPanel();
		ledPanel.setLayout(new GridLayout(2, 1)); 
	
		JPanel mainPanel = new JPanel(new GridLayout(5,4));
		JPanel lowerPanel = new JPanel(new GridLayout(1, 2));
		lowerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		ledPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		
		// LED Display Configuration
		this.led = new JTextField();
		this.led.setPreferredSize(new Dimension(250, 75));
		this.led.setMargin(new Insets(10, 10, 10, 10));
		this.led.setHorizontalAlignment(JTextField.RIGHT);
		this.led.setEditable(false);
		
		//JLabel Configuration
		this.label = new JLabel();
		this.label.setHorizontalAlignment(JLabel.RIGHT);
		this.label.setText("");
		this.label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		
		// JTextField (LED Display) styling
		this.led.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		this.led.setBackground(Color.WHITE);
		
		ledPanel.add(led); // Add LED Display to ledPanel
		ledPanel.add(this.label); // Add label to ledPanel
		
		// Button Configurations 
		for (int i = 0; i < this.buttons.length; i++) {
			JButton button = new JButton(this.buttons[i]);
			button.addActionListener(this);
			mainPanel.add(button);
		}
		JButton del = new JButton("DEL");
		del.addActionListener(this);
		JButton c = new JButton("C");
		c.addActionListener(this);
		
		lowerPanel.add(del);
		lowerPanel.add(c);
		
		
		// GridLayout Configurations
		GridLayout mainLayout = new GridLayout(5, 4); // (Row, Column)
		GridLayout secondaryLayout = new GridLayout(1,2);
		
		mainLayout.setHgap(5); // Horizontal margin
		mainLayout.setVgap(5); // Vertical margin
		mainPanel.setLayout(mainLayout); // Set JPanel layout
		
		secondaryLayout.setHgap(5); // Horizontal margin
		lowerPanel.setLayout(secondaryLayout); // Set JPanel layout
		
		
		this.frame.add(ledPanel, BorderLayout.PAGE_START);
		this.frame.add(lowerPanel, BorderLayout.CENTER);
		this.frame.add(mainPanel, BorderLayout.SOUTH);
		this.frame.pack();
		this.frame.setSize(275, 400);
		this.frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new CalculatorGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String output = this.led.getText();
		String keyPressed = e.getActionCommand();
		
		switch (keyPressed) {
			case "=": this.label.setText("= "+ this.result(output));  
				break;
			case "C": output = ""; this.label.setText(""); 
				break;
			case "DEL": output = output.substring(0, output.length()-1);
				break;	
			default: output += keyPressed;
				break;
		}
		this.led.setText(output);
	}
	
	/*
	 * Passes equation to the calculation method following BIDMAS rules 
	 * 
	 * @param text The equation as a String
	 * @return output The result of the equation
	 */
	public String result(String text) {
		String output = text;
		
		// Evaluating equation whilst conforming to B I D M A S
		int openBraceCount = this.countChar('(', text);
		int closeBraceCount = this.countChar(')', text);
		
		if (openBraceCount == closeBraceCount) {
			// Contains brackets
			if (openBraceCount != 0) {
				// Isolate and extract values between brackets
				String temp = "";
				int indexA = 0;
				// Get first index of ')' then work backwards to find first index of '('
				// Works for nested brackets
				int indexB = text.indexOf(')');
				for (int i = indexB; i != 0; i--) {
					if (text.charAt(i) == '(') {
						indexA = i;
						break;
					}
				}
				// Replace equation with result 
				temp = text.substring(indexA+1, indexB);
				String result = result(temp);
				text = text.replace("(" + temp + ")", result);
				output = result(text);
			} else {
				if (output.contains("--") == true) {
					output = text.replace("--", "+");
				}
				for (int i = 0; i < this.operators.length; i++) {
					char operator = this.operators[i];
					while (output.indexOf(operator) != -1) {
						// Checks whether a value that has '-' is negative or if it is an equation 
						if (output.startsWith("-") == true) {
							boolean isFinal = true;
							// Loop through output checking for any operators 
							for (int j = 1; j < output.length(); j++) {
								for (int k = 0; k < this.operators.length; k++) {
									if (output.charAt(j) == this.operators[k]) {
										isFinal = false;
									}
								}
							}
							
							if (isFinal == true) {
								if (output.charAt(output.length()-1) == '0') {
									int result = (int) Double.parseDouble(output);
									return Integer.toString(result);
								} 
								return output;
							} else {
								output = calculate(operator, output);
							}
						} else { 
							output = calculate(operator, output);
						}
					}
				}
			}
		} else {
			// Calculate difference between '(' and ')'
			int diference = 0;
			if (Math.max(openBraceCount, closeBraceCount) == openBraceCount) {
				diference = openBraceCount - closeBraceCount;
				return "Missing one or more ')'. Amount: " + diference;
			} else {
				diference = closeBraceCount - openBraceCount;
				return "Missing one or more '('. Amount: " + diference;
			}
		}
		
		// Return output as a whole number otherwise return to one decimal point
		if (output.charAt(output.length()-1) == '0') {
			int result = (int) Double.parseDouble(output);
			return Integer.toString(result);
		} else {
			return output;
		}
	}
	
	// Utility methods
	
	/*
	 * Counts the appearances of a character in a string
	 * 
	 * @param c Character to be checked
	 * @param text String to search for character
	 *  
	 * @return count
	 */
	private int countChar(char c, String text) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}
	
	/*
	 * Reverses a String
	 * @param str String to be reversed 
	 * 
	 * @return reversed The string reversed
	 */
	private static String reverse(String str) {
		String reversed = "";
		for (int i = str.length()-1; i >= 0; i--) {
			reversed += str.charAt(i);
		}
		return reversed;
	}  

	/*
	 * Calculates Modular Arithmetic with exponents
	 * 
	 * @param a Base
	 * @param j Exponent
	 * @param n Modulo
	 * 
	 * @return result The resulting calculation 
	 */
	private static long modularArithmetic(long a, long j, long n) {
		if (j < 1) {return 1;}
		
		long result = modularArithmetic(a, j/2, n);
		result = result * result % n;
		return (j % 2 == 0) ? result : (result * a % n);
	}
	
	/*
	 * Extracts and simplifies an equation in a string
	 * 
	 * @param c Operator separating equation
	 * @param str Equation to be calculated
	 * 
	 * @return str The resulting string
	 */
	private static String calculate(char c, String str) {
		double result = 0;
		String input = str;
		
		String leftSide = "";
		if (str.startsWith("-")) {
			input = input.substring(1);
		}
		
		// Loop from index of operator 'c' back until occurrence of another operator or start of equation
		int indexA = str.indexOf(c)-1;
		for (int i = indexA; i >= 0; i--) {
			char current = str.charAt(i);
			if (Character.isDigit(current) == true) {
				leftSide += current;
			} else {
				if (current == '.') {
					leftSide += current;
					continue;
				// Differentiate between negative numbers and subtract ('-') operator
				} else if ((current == '-') && (Character.isDigit(str.charAt(i+1)) == false)) {
					leftSide += current;
					continue;
				} else {
					break;
				}
			}
			indexA = i;
		}
		leftSide = reverse(leftSide);
		if (str.startsWith("-")) {
			leftSide = "-" + leftSide;
		}
		
		String rightSide = "";
		int indexB = str.indexOf(c)+1;

		// Loop from index of operator 'c' to occurrence of another operator or end of equation
		for (int i = indexB; i < str.length(); i++) {
			char current = str.charAt(i);
			if (Character.isDigit(current) == true) {
				rightSide += current;
			} else {
				if (current == '.') {
					rightSide += current;
					continue;
				// Differentiate between negative numbers and subtract ('-') operator
				} else if ((current == '-') && (Character.isDigit(str.charAt(i-1)) == false)) {
					rightSide += current;
					continue;
				} else {
					break;
				}
			}
			indexB = i;
		}		
		double a = Double.parseDouble(leftSide);
		double b = Double.parseDouble(rightSide);
		long n = 0;
		
		String replace = str.substring(indexA, indexB+1);
		
		String modulo = "";
		if ((indexB+1 < input.length()) && (input.charAt(indexB+1) == '%')) {
			long base = (long) a;
			long exponent = (long) b;
			for (int i = indexB+2; i < input.length(); i++) {
				char current = str.charAt(i);
				if (Character.isDigit(current)) {
					modulo += current;
				} else {
					break;
				}
				indexB = i;
			}
			n = Long.parseLong(modulo);
			long solution = modularArithmetic(base,exponent,n);
			replace = str.substring(indexA, indexB+1);
			return str.replace(replace, Long.toString(solution));
		} else {
			switch (c) {
				case '+': result = a + b; 
					break;
				case '-': result = a - b;
					break;
				case '/': result = a / b;
					break;
				case '*': result = a * b;
					break;
				case '^': result = Math.pow(a, b);
					break;
				case '%': result = a % b;
					break;
			}
			
			if (str.startsWith("-")) {
				indexA--;
			}
			replace = str.substring(indexA, indexB+1);
		}
		return str.replace(replace, Double.toString(result));
	}
}
