package org.armstrong.abox.generation.alc;

import java.util.Set;

import org.armstrong.abox.reasoner.OWLReasonerFacade;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
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
public class ViolatingExemplar extends AbstractExemplar {
  private static Logger logger = LoggerFactory.getLogger(ViolatingExemplar.class);
  
  private ViolatingExemplar(OWLSubClassOfAxiom subClassAxiom, OWLNamedIndividual individual,
      OWLClassAssertionAxiom subsumeeAssertionAxiom, OWLClassAssertionAxiom subsumerAssertionAxiom) {
    super(subClassAxiom, individual, subsumeeAssertionAxiom, subsumerAssertionAxiom);
  }

  public static ViolatingExemplar build(OWLReasonerFacade owlReasoner,
      Set<OWLClassExpression> setM, IndividualProvider individualGenerator,
      OWLSubClassOfAxiom subClassAxiom) {

    boolean subsumptionIsContradiction = owlReasoner.isContradiction(subClassAxiom);
    boolean subsumptionIsTautology = owlReasoner.isTautology(subClassAxiom);
    
    if (subsumptionIsContradiction && subsumptionIsTautology)
       logger.trace("~~~ ###" + subClassAxiom + " is a CONTRADICTION and a TAUTOLOGY!");
    else if (subsumptionIsContradiction)
      logger.trace("~~~ " + subClassAxiom + " is a CONTRADICTION.");
    else if (subsumptionIsTautology)
      logger.trace("~~~ " + subClassAxiom + " is a TAUTOLOGY.");
    
    
    OWLNamedIndividual individual = individualGenerator.getFreshIndividual(ExemplarType.VIOLATING);

    OWLClassAssertionAxiom subsumeeAssertionAxiom = owlReasoner
        .addOWLClassAssertionAxiom(subClassAxiom.getSubClass(), individual);
    
    OWLClassAssertionAxiom subsumerAssertionAxiom = null;
    if (subsumptionIsContradiction) {
      subsumerAssertionAxiom = owlReasoner
          .addOWLClassAssertionAxiom(owlReasoner.getOWLThing(), individual, true);      
    } else {
      subsumerAssertionAxiom = owlReasoner
          .addOWLNegatedClassAssertionAxiom(subClassAxiom.getSuperClass(), individual, true);
    }
    
    logger.trace("Trying to add violating assertion:" + subsumeeAssertionAxiom + ", "
        + subsumerAssertionAxiom);

    if (!owlReasoner.isConsistent()) {
      logger.trace(
          "Ontology became inconsistent when trying to add violating axiom for " + subClassAxiom);
      owlReasoner.removeAxiom(subsumeeAssertionAxiom);
      owlReasoner.removeAxiom(subsumerAssertionAxiom);
      if (!owlReasoner.isConsistent()) {
        logger.trace("Ontology is still inconsistent " + subClassAxiom);
        throw new RuntimeException("Could not revert ontology to consistent state. "
            + "It seems that the ontology was inconsistent before trying to add violating exemplar for "
            + subClassAxiom);
      }
      return null;
    }

    owlReasoner.addAnnotation(individual, getComment(subClassAxiom));
    return new ViolatingExemplar(subClassAxiom, individual, subsumeeAssertionAxiom,
        subsumerAssertionAxiom);
  }

  
  @Override
  public boolean isViolating() {
    return true;
  }

  @Override
  public boolean isSatisfying() {
    return false;
  }

  @Override
  public String toString() {
    return "ViolatingExemplar [" + super.toString() + "]";  }

}
