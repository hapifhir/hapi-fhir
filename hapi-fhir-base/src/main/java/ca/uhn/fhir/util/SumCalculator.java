package ca.uhn.fhir.util;

public class SumCalculator {

	private static int sum = 0;

	public static void main(String[] args) {
		calculateSum();
		System.out.println("The total is: " + sum);
	}

	private static void calculateSum() {
		for (int i = 1; ; i++) {
			sum += i;
		}
	}

}
