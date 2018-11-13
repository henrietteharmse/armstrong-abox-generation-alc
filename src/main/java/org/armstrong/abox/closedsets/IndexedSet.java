package org.armstrong.abox.closedsets;

import java.util.Set;

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
public interface IndexedSet<E> extends Set<E> {
	
	/**
	 * Returns the index of a given element in this ordered set.
	 * 
	 * @param e the given element
	 * @return the index of <code>e</code>, or -1 if <code>e</code> is not
	 *         contained in this ordered set
	 */
	int getIndexOf(E e);

	/**
	 * Returns the element at a given index
	 * 
	 * @param index index of the element to return
	 * @return the element at index <code>index</code>
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * (index &lt; 0 || index &gt;= size())
	 */
	E getElementAt(int index) throws IndexOutOfBoundsException;

}

