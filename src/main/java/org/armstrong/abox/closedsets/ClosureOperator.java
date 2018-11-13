package org.armstrong.abox.closedsets;

import java.util.Set;

/* ArmstrongABoxes: A library for generating Armstrong ABoxes.
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
public interface ClosureOperator<E> {

	/**
	 * Computes the closure of a given set under this closure operator.
	 * 
	 * @param set set for which the closure must be computed.
	 * 
	 * @return 
	 * The closure of <code>set</code> under this closure operator
	 */
	Set<E> closure(Set<E> set);

	/**
	 * Checks whether a given set is closed under this closure operator.
	 * 
	 * @param set the set to be checked whether it is closed under this closure operator.
	 * 
	 * @return <code>true</code> if <code>set</code> is closed, <code>false</code>
	 *         otherwise
	 */
	boolean isClosed(Set<E> set);
}
