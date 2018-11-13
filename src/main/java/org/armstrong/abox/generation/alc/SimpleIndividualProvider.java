package org.armstrong.abox.generation.alc;

import org.armstrong.abox.reasoner.OWLReasonerFacade;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
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
public final class SimpleIndividualProvider implements IndividualProvider {

	private static IndividualProvider instance;
	private int counter = 0;
	private String baseName;
	private OWLReasonerFacade owlReasoner;

	private SimpleIndividualProvider(OWLReasonerFacade owlReasoner, String baseName) {
		super();
		this.owlReasoner = owlReasoner;
		this.baseName = baseName;
	}

	public static IndividualProvider getInstance(OWLReasonerFacade owlReasoner, String baseName) {
		if (instance == null) {
			instance = new SimpleIndividualProvider(owlReasoner, baseName);
		}

		return instance;
	}

	@Override
	public OWLNamedIndividual getFreshIndividual(ExemplarType exemplarType) {	  
		counter++;
		return owlReasoner.getFreshIndividual(baseName + exemplarType.getName() + Integer.toString(counter));
		
	}
}
