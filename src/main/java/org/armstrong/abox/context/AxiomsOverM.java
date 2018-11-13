package org.armstrong.abox.context;

import java.util.Set;

import org.armstrong.abox.closedsets.ClosureOperator;
import org.armstrong.abox.closedsets.NextClosure;


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
 * Axioms define logical formulas that hold for a context based on formal concept analysis (FCA). In
 * FCA an axiom is an implication. For Armstrong ABoxes axioms are GCIs.
 * 
 * @author Henriette Harmse
 *
 * @param <Symbol>
 * @param <Axiom>
 */
public interface AxiomsOverM<Symbol, Axiom> extends NextClosure<Symbol>, ClosureOperator<Symbol>{

  /**
   * An implication $L \rightarrow R$ follows from a TBox $\mathcal{T}$ if 
   * $\mathcal{T} \vDash \sqcap L \sqsubseteq \sqcap R$.
   * 
   * @param axiom
   * @return
   */
  boolean followsFrom(Axiom axiom);
	
  /**
   * Implications/GCIs defined over M.
   * 
   * @return
   */
	Set<Axiom> getAxioms();
}
