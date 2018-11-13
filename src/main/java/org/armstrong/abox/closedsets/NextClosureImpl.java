package org.armstrong.abox.closedsets;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.javaewah.datastructure.BitSet;

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
public class NextClosureImpl<E, C extends ClosureOperator<E>> implements NextClosure<E> {

	private static Logger logger = LoggerFactory.getLogger(NextClosureImpl.class);

	private IndexedSet<E> linearOrder;
	private C closureOperator;

	public NextClosureImpl(IndexedSet<E> linearOrder, C closureOperator) {
		super();
		this.linearOrder = linearOrder;
		this.closureOperator = closureOperator;
	}

	@Override
	public IndexedSet<E> getLinearOrder() {
		return linearOrder;
	}

	/**
	 * Converts a given set into its bit vector representation.
	 * 
	 * @param set
	 *            the set to be converted to bit vector representation
	 * @return the bit vector representation of <code>s</code>
	 */
	private BitSet setToBitVector(Set<E> set) {
		BitSet bitSet = new BitSet(getLinearOrder().size());

		for (int i = 0; i < getLinearOrder().size(); ++i) {
			if (set.contains(getLinearOrder().getElementAt(i))) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	/**
	 * Converts a given bit vector into its set representation.
	 * 
	 * @param bitSet
	 *            the bit vector to be converted to set representation
	 * @return the set representation of <code>b</code>
	 */
	private Set<E> bitVectorToSet(BitSet bitSet) {
		Set<E> set = new LinkedHashSet<E>();

		for (int i = 0; i < getLinearOrder().size(); ++i) {
			if (bitSet.get(i)) {
				set.add(getLinearOrder().getElementAt(i));
			}
		}
		return set;
	}

	/**
	 * Implements the NextClosure algorithm. See p. 47 of B. Ganter, et al.
	 * Conceptual Exploration, 2016
	 * 
	 */
	@Override
	public Set<E> nextClosure(Set<E> set) {
		BitSet setAsBitSet = setToBitVector(set);

		IndexedSet<E> linearOrder = getLinearOrder();

		if (set.size() == linearOrder.size()) {
			return null;
		}
		label: for (int i = linearOrder.size() - 1; i >= 0; i--) {
			if (setAsBitSet.get(i)) { // m \in A
				setAsBitSet.flip(i); // A := A\{m}
			} else {
				setAsBitSet.flip(i); // A := A \cup {m}

				setAsBitSet = setToBitVector(closureOperator.closure(bitVectorToSet(setAsBitSet))); // B := \varphi(A)

				// B\A contains element < m? If not return B, else continue
				for (int j = setAsBitSet.nextSetBit(0); (j >= 0) && (j < i); j = setAsBitSet.nextSetBit(j + 1)) {
					// Thus, j \in B, now check whether j \in A
					if (!set.contains(linearOrder.getElementAt(j))) {
						// j \notin A and j \in B and j<m
						setAsBitSet = setToBitVector(set);
						setAsBitSet.clear(i, linearOrder.size());
						continue label;
					}
				}
				return bitVectorToSet(setAsBitSet);
			}
		}
		return bitVectorToSet(setAsBitSet);
	}

	@Override
	public Set<Set<E>> allClosures() {
		Set<E> closedSet;
		Set<Set<E>> allClosedSets = new LinkedHashSet<Set<E>>();

		closedSet = closureOperator.closure(new LinkedHashSet<E>());
		allClosedSets.add(closedSet);
		closedSet = nextClosure(closedSet);
		while (closedSet != null) {
			allClosedSets.add(closedSet);
			closedSet = nextClosure(closedSet);
		}
		return allClosedSets;
	}
}
