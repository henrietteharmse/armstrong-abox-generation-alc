package org.armstrong.abox.context;

import java.util.HashSet;
import java.util.Set;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.closedsets.NextClosure;
import org.armstrong.abox.closedsets.NextClosureImpl;
import org.armstrong.abox.reasoner.OWLReasonerFacade;
import org.semanticweb.owlapi.model.OWLClassExpression;
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

/**
 * For a DL knowledge base the number of entailments and non-entailments are infinite. Hence, the 
 * idea of a closure operator on entailments does not make sense. To be able to deal only with a
 * finite number of entailments Armstrong ABoxes use partial contexts which are based on formal 
 * contexts of formal context analysis (FCA) in which the implications/entailments that can hold for
 * a formal context are finite. This is described in @see org.armstrong.abox.generation.ArmstrongABox).
 * 
 * @author Henriette Harmse
 *
 */
public class GCIsOverM implements AxiomsOverM<OWLClassExpression, OWLSubClassOfAxiom> {

	private static Logger logger = LoggerFactory.getLogger(GCIsOverM.class);

	private GCIsOverMClosureOperator closureOperator;
	private NextClosure<OWLClassExpression> nextClosure;

	private OWLReasonerFacade owlReasoner;
	
	public GCIsOverM(OWLReasonerFacade owlReasoner, 
	    IndexedSet<OWLClassExpression> mInLinearOrder) {
		
		this(owlReasoner, mInLinearOrder, new HashSet<OWLSubClassOfAxiom>());	
	}

  public GCIsOverM(OWLReasonerFacade owlReasoner, 
      IndexedSet<OWLClassExpression> mInLinearOrder,
      Set<OWLSubClassOfAxiom> gciAxiomsOverM) {
    super();
    
    this.owlReasoner = owlReasoner;
    this.closureOperator = new GCIsOverMClosureOperator(gciAxiomsOverM);
    this.nextClosure = new NextClosureImpl<OWLClassExpression, GCIsOverMClosureOperator>(
        mInLinearOrder, closureOperator);
  }

	@Override
	public IndexedSet<OWLClassExpression> getLinearOrder() {
	  return nextClosure.getLinearOrder();
	}

	@Override
	public Set<OWLClassExpression> nextClosure(Set<OWLClassExpression> set) {
		return nextClosure.nextClosure(set);
	}

	@Override
	public Set<Set<OWLClassExpression>> allClosures() {
		return nextClosure.allClosures();
	}

	@Override
	public Set<OWLClassExpression> closure(Set<OWLClassExpression> set) {
		return closureOperator.closure(set);
	}

	@Override
	public boolean isClosed(Set<OWLClassExpression> set) {
		return closureOperator.isClosed(set);
	}

	@Override
	public Set<OWLSubClassOfAxiom> getAxioms() {
		return closureOperator.getSubsumptions();
	}

	@Override
	public boolean followsFrom(OWLSubClassOfAxiom gciAxiom) {
		return owlReasoner.isEntailed(gciAxiom);
	}
}