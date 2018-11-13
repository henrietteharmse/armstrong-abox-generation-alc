package org.armstrong.abox.generation.alc;

import org.armstrong.abox.reasoner.OWLReasonerFacade;
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
public class SatisfyingExemplar extends AbstractExemplar {
  private boolean subsumptionIsTautology = false;
  
  private SatisfyingExemplar(OWLSubClassOfAxiom subClassAxiom, OWLNamedIndividual individual,
      OWLClassAssertionAxiom subsumeeAssertionAxiom, OWLClassAssertionAxiom subsumerAssertionAxiom) {
    super(subClassAxiom, individual, subsumeeAssertionAxiom, subsumerAssertionAxiom);
  }

  private SatisfyingExemplar(OWLSubClassOfAxiom subClassAxiom) {
    super(subClassAxiom);
    subsumptionIsTautology = true;
  }
  
  public static SatisfyingExemplar build(OWLReasonerFacade owlReasoner,
      IndividualProvider individualGenerator, OWLSubClassOfAxiom subClassAxiom) {
    OWLNamedIndividual individual;

    if (owlReasoner.isTautology(subClassAxiom)) {
      return new SatisfyingExemplar(subClassAxiom);
    } 
    
    individual = individualGenerator.getFreshIndividual(ExemplarType.SATISFYING);

    OWLClassAssertionAxiom subsumeeAssertionAxiom = owlReasoner
        .addOWLClassAssertionAxiom(subClassAxiom.getSubClass(), individual);
    OWLClassAssertionAxiom subsumerAssertionAxiom = owlReasoner
        .addOWLClassAssertionAxiom(subClassAxiom.getSuperClass(), individual, true);
    
    owlReasoner.addAnnotation(individual, getComment(subClassAxiom));
    return new SatisfyingExemplar(subClassAxiom, individual, subsumeeAssertionAxiom, subsumerAssertionAxiom);
  }
  

  @Override
  public boolean isViolating() {
    return false;
  }

  @Override
  public boolean isSatisfying() {
    return true;
  }

  public boolean subsumptionIsTautology() {
    return subsumptionIsTautology;
  }

  @Override
  public String toString() {
    return "SatisfyingExemplar [" + super.toString() + "]";
  }

}
