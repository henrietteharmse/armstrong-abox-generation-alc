package org.armstrong.abox.reasoner;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
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
public interface OWLReasonerFacade {

	OWLClassAssertionAxiom addOWLClassAssertionAxiom(OWLClassExpression owlClassExpression,
			OWLNamedIndividual individual);
	
	OWLClassAssertionAxiom addOWLClassAssertionAxiom(OWLClassExpression owlClassExpression,
			OWLNamedIndividual individual, boolean flush);


	OWLClassAssertionAxiom addOWLClassAssertionAxiom(Set<OWLClassExpression> owlClassExpressions,
			OWLNamedIndividual individual);
	
	OWLClassAssertionAxiom addOWLClassAssertionAxiom(Set<OWLClassExpression> owlClassExpressions,
			OWLNamedIndividual individual, boolean flush);	

	OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(OWLClassExpression owlClassExpression,
			OWLNamedIndividual individual);
	
	OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(OWLClassExpression owlClassExpression,
			OWLNamedIndividual individual, boolean flush);
	
	OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(Set<OWLClassExpression> owlClassExpressions,
			OWLNamedIndividual individual);
	
	OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(Set<OWLClassExpression> owlClassExpressions,
			OWLNamedIndividual individual, boolean flush);

	void removeAxiom(OWLAxiom owlAxiom);

	void saveOntology(IRI ontologyFile) throws OWLOntologyStorageException;

	OWLClassExpression getOWLClassExpression(String name, ClassExpressionType classExpressionType);
	
	OWLClassExpression getOWLClassExpression(String propertyName, String className,
      ClassExpressionType classExpressionType);
	
	OWLClassExpression getOWLClassExpression(String className1, ClassExpressionType classExpressionType1,
	    String className2, ClassExpressionType classExpressionType2, 
	    ClassExpressionType operationExpressionType);
	
	OWLClassExpression getOWLClassExpression(OWLClassExpression owlClassExpression1,
      OWLClassExpression owlClassExpression2, ClassExpressionType operationExpressionType);
	
	OWLClass getOWLThing();

	OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLClassExpression owlClassExpression,
			OWLNamedIndividual individual);
	
	OWLClassAssertionAxiom getOWLNegatedClassAssertionAxiom(OWLClassExpression owlClassExpression,
			OWLNamedIndividual individual);

	OWLClassAssertionAxiom getOWLNegatedClassAssertionAxiom(Set<OWLClassExpression> owlClassExpressions,
			OWLNamedIndividual individual);
	
	OWLClassExpression createConcept(Set<OWLClassExpression> owlClassExpressions, 
	    Set<OWLClassExpression> setM);
	
	OWLSubClassOfAxiom createAxiom(Set<OWLClassExpression> leftHandSide, 
	    Set<OWLClassExpression> rightHandSide, Set<OWLClassExpression> setM);
	
	OWLClassExpression getIntersectionOf(Set<OWLClassExpression> owlClassExpressions);
	
	OWLNamedIndividual getFreshIndividual(String shortname);
	
	boolean isConsistent();
	
	boolean isEntailed(OWLAxiom owlAxiom);
	
	boolean isTautology(OWLSubClassOfAxiom owlSubClassOfAxiom);
	
	boolean isContradiction(OWLSubClassOfAxiom owlSubClassOfAxiom);
	
	boolean isSatisfiable(OWLClassExpression owlClassExpression);
		
	boolean isThing(OWLClassExpression classExpression);
	
	boolean isNothing(OWLClassExpression classExpression);
	
	
	Map<OWLClassExpression, Set<OWLClassExpression>> getDisjointnessMap(
      Set<OWLClassExpression> classExpressions);
	
	OWLAnnotationAssertionAxiom addAnnotation(OWLNamedIndividual namedIndividual, String comment);
}
