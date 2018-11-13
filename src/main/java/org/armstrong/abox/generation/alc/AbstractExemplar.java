package org.armstrong.abox.generation.alc;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
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
public abstract class AbstractExemplar implements Exemplar {
	private OWLNamedIndividual individual;
	private OWLClassAssertionAxiom subsumeeAssertionAxiom;
	private OWLClassAssertionAxiom subsumerAssertionAxiom;
	private OWLSubClassOfAxiom subClassAxiom;	

	protected AbstractExemplar(OWLSubClassOfAxiom subClassAxiom) {
	  this.subClassAxiom = subClassAxiom;
	}
	
	protected AbstractExemplar(OWLSubClassOfAxiom subClassAxiom, OWLNamedIndividual individual,
			OWLClassAssertionAxiom subsumeeAssertionAxiom, OWLClassAssertionAxiom subsumerAssertionAxiom) {
		this.subClassAxiom = subClassAxiom;
		this.individual = individual;
		this.subsumeeAssertionAxiom = subsumeeAssertionAxiom;
		this.subsumerAssertionAxiom = subsumerAssertionAxiom;
	}

	@Override
	public OWLNamedIndividual getIndividual() {
		return individual;
	}

	@Override
	public OWLClassAssertionAxiom getSubsumerAssertionAxiom() {
		return subsumerAssertionAxiom;
	}

	@Override
	public OWLClassAssertionAxiom getSubsumeeAssertionAxiom() {
		return subsumeeAssertionAxiom;
	}

	@Override
	public OWLSubClassOfAxiom getSubClassOfAxiom() {
		return subClassAxiom;
	}

	protected static String getComment(OWLSubClassOfAxiom subClassOfAxiom) {
	  StringBuffer strBuffer = new StringBuffer();
	  
	  strBuffer.append("Subsumee = ");
	  strBuffer.append(subClassOfAxiom.getSubClass());
	  strBuffer.append("\n");
    strBuffer.append("Subsumer = ");
    strBuffer.append(subClassOfAxiom.getSuperClass());	  
	  return strBuffer.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Individual=" + individual.getIRI().getShortForm() +"\n");

		builder.append("SubsumeeAssertions are:\n");
		if (subsumeeAssertionAxiom != null)
  		subsumeeAssertionAxiom.nestedClassExpressions()
  			.filter(p->(p.getClassExpressionType() == ClassExpressionType.OWL_CLASS))
  			.forEach(p->builder.append(p + "\n"));

		builder.append("SubsumerAssertions are:\n");
		subsumerAssertionAxiom.nestedClassExpressions()
			.filter(p->(p.getClassExpressionType() == ClassExpressionType.OWL_CLASS))
			.forEach(p->builder.append(p + "\n"));
		
		return builder.toString();
	}	
	
	
}
