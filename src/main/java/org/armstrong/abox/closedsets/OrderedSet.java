package org.armstrong.abox.closedsets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.armstrong.abox.closedsets.IndexedSet;


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
public class OrderedSet<E>  implements IndexedSet<E> {
	/**
	 * Elements of this list set.
	 */
	private final ArrayList<E> elements;

	/**
	 * Creates an empty OrdererSet
	 *
	 */
	public OrderedSet() {
		this.elements = new ArrayList<E>();
	}

	public OrderedSet(int size) {
		this.elements = new ArrayList<E>(size);
	}	
	
	/**
	 * Creates a new OrdererSet that contains the elements of a given collection.
	 * Duplicate elements in the given collection are inserted once,
	 * <code>null</code> element is not allowed.
	 * 
	 * @param c
	 *            the collection whose elements are to be contained in this
	 *            OrdererSet initially
	 */
	public OrderedSet(Collection<? extends E> c) {
		this.elements = new ArrayList<E>();
		addAll(c);
	}

	/**
	 * Adds a given object to this list set. Does not allow duplicates.
	 * 
	 * @param o
	 *            the object to be added
	 * @return <code>true</code> if this list set did not already contain
	 *         <code>o</code>
	 * @throws NullPointerException
	 *             if the given object is <code>null</code>
	 */
	@Override
	public boolean add(E o) {
		if (o == null) {
			throw new NullPointerException();
		}
		for (E element : this.elements) {
			if (o.equals(element)) {
				return false;
			}
		}
		this.elements.add(o);
		return true;
	}

	/**
	 * Adds the elements in a given collection to this list set.
	 * 
	 * @param c
	 *            the collection of elements to be added
	 * @return <code>true</code> if this list set changed as a result of the
	 *         call
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retCode = true;
		for (E e : c) {
			retCode = retCode &&  add(e);
		}
		return retCode;
	}

	/**
	 * Removes all of the elements from this list set.
	 */
	@Override
	public void clear() {
		this.elements.clear();
	}

	/**
	 * Checks if a given element occurs in this list set.
	 * 
	 * @param o
	 *            the element whose presence is to be tested
	 * @return <code>true</code> if <code>o</code> occurs in this list set,
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>o</code> is <code>null</code>
	 */
	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		
		for (E element : this.elements) {
			if (element.equals(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the elements of a given collection are contained in this
	 * OrdererSet.
	 * 
	 * @param c
	 *            the given collection whose elements are checked for presence
	 * @return <code>true</code> if all elements of <code>c</code> are contained
	 *         in this OrdererSet, <code>false</code> otherwise
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Iterator<?> it = c.iterator(); it.hasNext();) {
			if (!contains(it.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if this OrdererSet is equal to a given set, i.e., the given set is
	 * contained in this OrdererSet and vice versa. For this, it is enough to check
	 * whether the two sets have the same sizes and this set contains the given
	 * set.
	 * 
	 * @param c
	 *            collection
	 * @return <code>true</code> if this OrdererSet is equal to <code>c</code>
	 */
	public boolean equals(Collection<?> c) {
		return (c.size() == size()) && containsAll(c);
	}
	
	

	/**
	 * Checks if this OrdererSet is empty.
	 * 
	 * @return <code>true</code> if this OrdererSet does not contain any elements,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}


	/**
	 * Iterator class for OrdererSet
	 *
	 */
	public class OrdererSetIterator implements Iterator<E> {
		int currentIndex = 0;

		@Override
		public boolean hasNext() {
			return this.currentIndex < OrderedSet.this.elements.size();
		}

		@Override
		public E next() {
			return getElementAt(this.currentIndex++);
		}

		@Override
		public void remove() {
		}
	}

	/**
	 * Returns an iterator for this OrdererSet
	 */
	@Override
	public Iterator<E> iterator() {
		return new OrdererSetIterator();
	}

	/**
	 * Removes a given element from this OrdererSet.
	 * 
	 * @param o
	 *            the element to be removed
	 * @return <code>true</code> if this OrdererSet contained the given element,
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>o</code> is <code>null</code>
	 */
	@Override
	public boolean remove(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		if (contains(o)) {
			this.elements.remove(o);
			return true;
		}
		return false;
	}

	/**
	 * Removes the elements of a given collection from this OrdererSet.
	 * 
	 * @param c
	 *            the collection whose elements are to be removed
	 * @return <code>true</code> if this OrdererSet changed as a result of the
	 *         call, <code>false</code> otherwise
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean retCode = false;
		for (Iterator<?> it = c.iterator(); it.hasNext();) {
			retCode = retCode || remove(it.next());
		}
		return retCode;
	}

	/**
	 * Removes elements of this OrdererSet that are not contained in a given
	 * collection.
	 * 
	 * @param c
	 *            the collection that defines which elements are going to remain
	 * @return <code>true</code> if this set changed as a result of the call
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean retCode = false;
		for (E element : this) {
			if (!c.contains(element)) {
				retCode = retCode || remove(element);
			}
		}
		return retCode;
	}

	/**
	 * Returns the number of elements of this OrdererSet.
	 * 
	 * @return the size of this OrdererSet
	 */
	@Override
	public int size() {
		return this.elements.size();
	}

	/**
	 * Returns an array containing the elements of this OrdererSet.
	 * 
	 * @return an array containing the elements of this OrdererSet
	 */
	@Override
	public Object[] toArray() {
		return this.elements.toArray();
	}

	/**
	 * Returns an array containing the elements of this OrdererSet.
	 * 
	 * @param a
	 *            the array, into which the elements of this OrdererSet is to be
	 *            stored
	 * @return an array containing the elements of this OrdererSet
	 */
	@Override
	public <E> E[] toArray(E[] a) {
		return this.elements.toArray(a);
	}

	/**
	 * Returns the index of a given element.
	 * 
	 * @param e
	 *            the element whose index is requested
	 * @return the index of <code>e</code>, <code>-1</code> if <code>e</code> is
	 *         not found
	 */
	@Override
	public int getIndexOf(E e) {
		for (int i = 0; i < size(); ++i) {
			if (e == getElementAt(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the element at a specified index.
	 * 
	 * @param i
	 *            the index of the requested element
	 * @return the element at index <code>i</code>
	 * @throws IndexOutOfBoundsException
	 *             if the specified index is out of bounds
	 */
	@Override
	public E getElementAt(int i) throws IndexOutOfBoundsException {
		return this.elements.get(i);
	}

	/**
	 * Returns a string representation of this ordered set.
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		if (isEmpty()) {
			return "{ }\n";
		}
		String tmp = "{ ";
		for (Iterator<?> it = iterator(); it.hasNext();) {
			tmp = tmp + it.next() + " ";
		}
		return tmp + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedSet other = (OrderedSet) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}
	
	
}
