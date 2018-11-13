package org.armstrong.abox.generation.alc;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.closedsets.OrderedSet;
import org.armstrong.abox.context.AxiomsOverM;
import org.armstrong.abox.context.GCIsOverM;
import org.armstrong.abox.generation.alc.exception.IllegalOWLClassExpressionException;
import org.armstrong.abox.generation.alc.exception.MImpermissibleException;
import org.armstrong.abox.reasoner.OWLReasonerFacade;
import org.armstrong.abox.reasoner.OWLReasonerFacadeImpl;
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
public class ArmstrongABoxImpl implements ArmstrongABox {

  private static Logger logger = LoggerFactory.getLogger(ArmstrongABoxImpl.class);

  /**
   * The assumption is that the reasoner has been initialized appropriately to
   * have access to the ontology and the set of owl classes that represent the
   * attributes over which the ontology completion algorithm will run.
   */
  private OWLReasonerFacade owlReasoner;
  
  /**
   * The set M of interesting concept descriptions in linear order. An arbitrary order has to be 
   * defined for the elements of M to be able to use next closure. 
   */
  private SetM setMInLinearOrder;
  
  /**
   * The GCIs over M that follows from the TBox $\mathcal{T}$.
   */
  private AxiomsOverM<OWLClassExpression, OWLSubClassOfAxiom> gciAxiomsOverM;

  private IndividualProvider individualGenerator;
  private Exemplars exemplars;
//  private Set<OWLSubClassOfAxiom> refutedGCIAxioms;

  public ArmstrongABoxImpl(OWLReasonerFacade owlReasoner, 
      IndividualProvider individualGenerator,
      IndexedSet<OWLClassExpression> setMInLinearOrder) {
  
    this.owlReasoner = owlReasoner;
    this.individualGenerator = individualGenerator;
    this.setMInLinearOrder = new SetM(owlReasoner, setMInLinearOrder);    
    this.gciAxiomsOverM = new GCIsOverM(owlReasoner, setMInLinearOrder);
  }

  public ArmstrongABoxImpl(OWLReasonerFacadeImpl owlReasoner,
      IndexedSet<OWLClassExpression> setMInLinearOrder) {
    this(owlReasoner, SimpleIndividualProvider.getInstance(owlReasoner, "x"), setMInLinearOrder);
  }

  public ArmstrongABoxImpl(OWLReasonerFacadeImpl owlReasoner) {
    this(owlReasoner, SimpleIndividualProvider.getInstance(owlReasoner, "x"), 
        new OrderedSet<OWLClassExpression>());
  }
  
  @Override
  public void conceptualExploration() throws MImpermissibleException {
    if (!setMInLinearOrder.isPermissible()) {
      throw new MImpermissibleException(setMInLinearOrder.getReasonsWhyImpermissible());
    }
    
    Set<OWLClassExpression> setM = getM();
    Set<OWLClassExpression> leftHandSide = new LinkedHashSet<OWLClassExpression>();
    Set<OWLClassExpression> nextClosure = new LinkedHashSet<OWLClassExpression>();
    exemplars = new Exemplars();

    while (!leftHandSide.equals(setM)) {
      Set<OWLClassExpression> maxUnrefutedRHS = maximalUnrefutedRightHandSide(leftHandSide);
      logger.trace("{closure(" + leftHandSide + ")} = {" + maxUnrefutedRHS + "}");
      if (!isDecided(leftHandSide, maxUnrefutedRHS)) {
        maxUnrefutedRHS.removeAll(leftHandSide);
        logger.trace("maxUnrefutedRHS after remove=" + maxUnrefutedRHS);
        OWLSubClassOfAxiom gciAxiom = owlReasoner.createAxiom(leftHandSide, maxUnrefutedRHS, setM);
        logger.trace("gciAxiom="+gciAxiom);
        if (followsFrom(gciAxiom)) {
          SatisfyingExemplar satisfyingExemplar = 
              SatisfyingExemplar.build(owlReasoner, individualGenerator, gciAxiom);
          if (!satisfyingExemplar.subsumptionIsTautology()) {
            exemplars.add(satisfyingExemplar);
          }
          gciAxiomsOverM.getAxioms().add(gciAxiom);
          nextClosure = gciAxiomsOverM.nextClosure(leftHandSide);
          logger.debug("NextClosure of {" + leftHandSide + "} is {" + nextClosure + "}.");
          leftHandSide = nextClosure;         
        } else {
          logger.trace("Add violating assertion for " + gciAxiom);
          ViolatingExemplar violatingExemplar = ViolatingExemplar.build(owlReasoner, getM(),
              individualGenerator, gciAxiom);
          if (violatingExemplar != null) {
            exemplars.add(violatingExemplar);
          }         
        }       
      } else {
        nextClosure = gciAxiomsOverM.nextClosure(leftHandSide);
        logger.debug("NextClosure of {" + leftHandSide + "} is {" + nextClosure + "}.");
        leftHandSide = nextClosure;
      }
      if (!owlReasoner.isConsistent()) {
        logger.error("Ontology is inconsistent after adding exemplars for " + leftHandSide + " subclassOf " + maxUnrefutedRHS);
      }
    }
  }

  /**
   * 
   * $\mathcal{K}_{\mathcal{T}, \mathcal{A}}(L) := M \backslash \bigcup\{D \in M \setspacing | 
   * \exists a.\setspacing L \subseteq 
   * \{C \setspacing | \setspacing \mathcal{T},\mathcal{A} \vDash C(a)\} \wedge 
   * \mathcal{T}, \mathcal{A} \vDash \neg D(a)\}$
   * 
   */
  @Override
  public Set<OWLClassExpression> maximalUnrefutedRightHandSide(Set<OWLClassExpression> leftHandSide) {
    Set<OWLClassExpression> setM = new LinkedHashSet<OWLClassExpression>(getM());

    for (ViolatingExemplar violatingExemplar : exemplars.getViolatingExemplars()) {
      // Do we have that $L \subseteq {C | \mathcal{T},\mathcal{A} \vDash C(a)}$ ?
      if (isLSubsetOfClassAssertions(leftHandSide, violatingExemplar.getIndividual())) {
        // Then remove all $\mathcal{T}, \mathcal{A} \vDash \neg D(a)$ from M.
        setM.removeAll(violatingExemplar
            .getSubsumerAssertionAxiom()
            .nestedClassExpressions()
            .collect(Collectors.toSet()));
      }
    }
    return setM;
  }
  
  private boolean isLSubsetOfClassAssertions(Set<OWLClassExpression> leftHandSide, 
      OWLNamedIndividual individual) {
    
    if (!getM().containsAll(leftHandSide))
      throw new IllegalOWLClassExpressionException("leftHandSide = " + leftHandSide
          + " not contained in signature = " + getM() + ".");

    OWLClassExpression intersectionOf = owlReasoner.createConcept(leftHandSide, getM());
    
    if (owlReasoner.isSatisfiable(intersectionOf)) {
      OWLClassAssertionAxiom owlClassAssertionAxiom = owlReasoner
          .getOWLClassAssertionAxiom(intersectionOf, individual);
      return owlReasoner.isEntailed(owlClassAssertionAxiom);
    } else 
      return false;
  } 
  
  @Override
  public boolean isDecided(Set<OWLClassExpression> leftHandSide, 
      Set<OWLClassExpression> rightHandSide) {
    return leftHandSide.equals(rightHandSide);
  }
  
  @Override
  public Exemplars getExemplars() {
    return exemplars;
  }

  @Override
  public IndexedSet<OWLClassExpression> getM() {
    return setMInLinearOrder.getSetM();
  }

  @Override
  public boolean followsFrom(OWLSubClassOfAxiom formula) {
    return gciAxiomsOverM.followsFrom(formula);
  }

  @Override
  public OWLReasonerFacade getReasoner() {
    return owlReasoner;
  }
}
