package org.armstrong.abox.context;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.armstrong.abox.closedsets.AbstractClosureOperator;
import org.armstrong.abox.closedsets.ClosureOperator;
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
 * a formal context are finite. This closure operator corresponds with the implication closure 
 * operator of FCA or the functional dependency closure operator of relational databases. See
 * p. 87 - 94 of Conceptual Exploration by B. Ganter and S. Obiedkov, 2016 and Chapter 5 of Formal
 * Concept Analysis Methods for Description Logics by B. Sertkaya, 2008.
 * 
 * In this class we use <code>OWLSubClassOfAxiom</code>s rather than the implications typically used
 * in FCA mainly to avoid having to translate back and forth between the two.
 * 
 * @author Henriette Harmse
 *
 */
public class GCIsOverMClosureOperator extends AbstractClosureOperator<OWLClassExpression> 
		implements ClosureOperator<OWLClassExpression> {
	
	private static Logger logger = LoggerFactory.getLogger(GCIsOverMClosureOperator.class);

	private Set<OWLSubClassOfAxiom> gciAxiomsOverM;	

	public GCIsOverMClosureOperator(Set<OWLSubClassOfAxiom> gciAxiomsOverM) {
		super();
		this.gciAxiomsOverM = gciAxiomsOverM;
	}
	
	
	/**
	 * This implementation is based on the linear algorithm provided in 
	 * D. Maier, The Theory of Relational Databases, Computer Science Press, Rockville, 1983
	 * 
	 */
	@Override
	public Set<OWLClassExpression> closure(Set<OWLClassExpression> set) {
		Set<OWLClassExpression> updateSet = new LinkedHashSet<OWLClassExpression>(set);
		Set<OWLClassExpression> newDepSet = new LinkedHashSet<OWLClassExpression>(set);
		
		// Initialization
		Hashtable<OWLSubClassOfAxiom, Integer> cardinalitiesOfSubsumees = 
				new Hashtable<OWLSubClassOfAxiom, Integer>();		
		Map<OWLClassExpression, Set<OWLSubClassOfAxiom>> subsumeesExpressionsAppearIn = 
				new Hashtable<OWLClassExpression, Set<OWLSubClassOfAxiom>>();
		
		logger.trace("Subsumptionx = " + gciAxiomsOverM);
		for (OWLSubClassOfAxiom subsumption : gciAxiomsOverM) {
			cardinalitiesOfSubsumees.put(subsumption, subsumption.getSubClass().asConjunctSet().size());
			for (OWLClassExpression expression : subsumption.getSubClass().asConjunctSet()) {
				Set<OWLSubClassOfAxiom> subsumeesExpressionAppearIn = subsumeesExpressionsAppearIn.get(expression);
				if (subsumeesExpressionAppearIn == null) {
					subsumeesExpressionAppearIn = new LinkedHashSet<OWLSubClassOfAxiom>();
					subsumeesExpressionsAppearIn.put(expression, subsumeesExpressionAppearIn);
				}
				subsumeesExpressionAppearIn.add(subsumption);			
			}
			if (subsumption.getSubClass().asConjunctSet().isEmpty()) {
				newDepSet.addAll(subsumption.getSuperClass().asConjunctSet());
				updateSet.addAll(subsumption.getSuperClass().asConjunctSet());
			}				
		}
		logger.trace("subsumeesExpressionsAppearIn = " + subsumeesExpressionsAppearIn);
		logger.trace("cardinalitiesOfSubsumees = " + cardinalitiesOfSubsumees);
		logger.trace("updateSet = " + updateSet);
		logger.trace("newDepSet = " + newDepSet);

		// Computation
		OWLClassExpression updateExpression;
		Set<OWLClassExpression> addSet = null;
		while (!updateSet.isEmpty()) {
			Iterator<OWLClassExpression> updateIterator = updateSet.iterator();
			updateExpression = updateIterator.next();
//			logger.trace("updateExpression = " + updateExpression);
			updateSet.remove(updateExpression);
//			logger.trace("updateSet = " + updateSet);
			for (OWLSubClassOfAxiom subsumption : ((subsumeesExpressionsAppearIn.get(updateExpression)==null) ?
							new LinkedHashSet<OWLSubClassOfAxiom>() : 
							subsumeesExpressionsAppearIn.get(updateExpression))) {
				int cardinalityOfSubsumee = cardinalitiesOfSubsumees.get(subsumption);
//				logger.trace("cardinalityOfSubsumee = " + cardinalityOfSubsumee);
				cardinalitiesOfSubsumees.put(subsumption, --cardinalityOfSubsumee);
				if (cardinalityOfSubsumee == 0) {
					addSet = new LinkedHashSet<OWLClassExpression>(subsumption.getSuperClass().asConjunctSet());
					addSet.removeAll(newDepSet);
					newDepSet.addAll(addSet);
					updateSet.addAll(addSet);					
				}
			}
		}
		logger.debug("Closure of {"+set+ "} = {"+newDepSet+"}");
		return newDepSet;
	}

	public Set<OWLSubClassOfAxiom> getSubsumptions() {
		return gciAxiomsOverM;
	}

	public void setSubsumptions(Set<OWLSubClassOfAxiom> subsumptions) {
		this.gciAxiomsOverM = subsumptions;
	}		
}
