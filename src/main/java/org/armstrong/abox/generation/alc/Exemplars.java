package org.armstrong.abox.generation.alc;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.closedsets.OrderedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * ArmstrongABoxes: A library for generating Armstrong ABoxes.
 * 
 * MIT License
 *
 * Copyright (c) 2017 Henriette Harmse
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Exemplars {
	
  private static Logger logger = LoggerFactory.getLogger(Exemplars.class);
  
	private IndexedSet<ViolatingExemplar> violatingExemplars = new OrderedSet<ViolatingExemplar>();
	private IndexedSet<SatisfyingExemplar> satisfyingExemplars = new OrderedSet<SatisfyingExemplar>();

	public Exemplars() {
		super();
	}

	void add(Exemplar exemplar) {
		if (exemplar.isViolating()) {
			violatingExemplars.add((ViolatingExemplar)exemplar);
			logger.trace("Adding violating exemplar: " + exemplar);
		}
		else if (exemplar.isSatisfying()) {
			satisfyingExemplars.add((SatisfyingExemplar)exemplar);
			logger.trace("Adding satisfying exemplar: " + exemplar);
		}
		else
			throw new UnsupportedOperationException("Unknown Exemplar class: " + exemplar.getClass());
	}

	public IndexedSet<ViolatingExemplar> getViolatingExemplars() {
		return violatingExemplars;
	}

	public IndexedSet<SatisfyingExemplar> getSatisfyingExemplars() {
		return satisfyingExemplars;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Satisfying Exemplars\n");
		builder.append("--------------------\n");
		for (SatisfyingExemplar satisfyingExemplar : satisfyingExemplars) {
			builder.append("\n");
			builder.append(satisfyingExemplar);
		}

		builder.append("Violating Exemplars\n");
		builder.append("--------------------\n");
		for (ViolatingExemplar violatingExemplar: violatingExemplars) {
			builder.append("\n");
			builder.append(violatingExemplar);
		}		
		return builder.toString();
	}		
}
