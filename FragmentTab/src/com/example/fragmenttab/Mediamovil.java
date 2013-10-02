package com.example.fragmenttab;

import java.util.ArrayList;
import java.util.List;

public class Mediamovil {

	public Mediamovil() {

	}

	public List<Double> returnMediamovil(List<Double> a, int x1) {

		int p = (x1 - 1) / 2;
		int q = p + 1;
		List<Double> b = new ArrayList<Double>();

		// primi q valori
		for (int i = 0; i < p; i++)
			b.add(a.get(i));

		double valor1 = 0;
		for (int k = 0; k < x1; k++) {
			valor1 += a.get(k);
		}
		b.add(valor1);

		for (int i = q; i < (a.size() - p); i++)
			b.add(b.get(i - 1) + a.get(i + p) - a.get(i - q));

		// ultimi p valori
		for (int i = a.size() - p; i < a.size(); i++)
			b.add(a.get(i));

		for (int m = p; m < b.size() - p; m++) {
			b.set(m, b.get(m) / x1);
		}

		return b;

	}

}
