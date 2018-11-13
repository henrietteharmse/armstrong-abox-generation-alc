package org.armstrong.abox.generation.alc;

import java.util.Set;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.generation.alc.exception.MImpermissibleException;
import org.armstrong.abox.reasoner.OWLReasonerFacade;
import org.semanticweb.owlapi.model.OWLClassExpression;
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

/**
 * For a given TBox with an interesting subset M of concept descriptions, Armstrong ABoxes try to 
 * generate an ABox such that there is a satisfying exemplar for each entailment and a violating
 * exemplar for each non-entailment for concept descriptions in M. Clearly in general this is 
 * impossible since a TBox has an infinite number of entailments and non-entailments. 
 * 
 * In order to deal with only a finite number of entailments and non-entailments,  a context is 
 * created for M using ideas from formal concept analysis (FCA). In this context GCIs are translated
 * to implications for which number of implications that hold (do not hold) are finite. It is only 
 * wrt to this context that Armstrong ABoxes are able to generate a finite number of (satisfying/
 * violating) exemplars.
 * 
 * In this context a GCI $\sqcap C \sqsubseteq \sqcap D$ translates to an implication 
 * $C \rightarrow D$ where we often refer to $C$ as the left-hand side and $D$ as the right hand side.
 * 
 * @author Henriette Harmse
 *
 */
public interface ArmstrongABox {
  /**
   * Returns the signature of this Armstrong ABox.
   * 
   * @return the signature
   */
  IndexedSet<OWLClassExpression> getM();

  /**
   * Completes the Armstrong ABox based on conceptual exploration. 
   */
  void conceptualExploration() throws MImpermissibleException;

  /**
   * Given a left-hand side $L$ it calculates the maximal right-hand side $R$ such that the 
   * implication $L \rightarrow R$ is not refuted by this context. See p. 89 and 98 in Formal 
   * Concept Analysis Methods for Description Logics by B. Sertkaya, 2008.
   * 
   * @param  leftHandSide
   * @return maximal right-hand for which the axiom is not refuted.
   */
  Set<OWLClassExpression> maximalUnrefutedRightHandSide(Set<OWLClassExpression> leftHandSide);
  

  /**
   * Determines whether the given <code>gciAxiom</code> follows from the axioms/background 
   * knowledge of this context.
   * 
   * @param  gciAxiom
   * @return
   */
  boolean followsFrom(OWLSubClassOfAxiom gciAxiom);
  
  /**
   * Returns true if the implication $L \rightarrow R$ is decided in the context. I.e., 
   * either $L \rightarrow R$ follows from the TBox $\mathcal{T}$ or $L \rightarrow R$ is refuted 
   * by $(\mathcal{T}, \mathcal{A}_{\armstrong})$ where $\mathcal{A}_{\armstrong}$ represents an 
   * Armstrong ABox.
   * 
   * @param  leftHandSide
   * @param  rightHandSide
   * @return true if 
   */
  boolean isDecided(Set<OWLClassExpression> leftHandSide, Set<OWLClassExpression> rightHandSide);

  Exemplars getExemplars();

  OWLReasonerFacade getReasoner();
}
