/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mip.github.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator implements Iterator {
  private Object array[];
  private int pos = 0;

  public ArrayIterator(Object anArray[]) {
    array = anArray;
  }

  @Override
  public boolean hasNext() {
    try {
        return pos < array.length;
    } catch (NullPointerException npe) {
        return false;
    }
  }

  @Override
  public Object next() throws NoSuchElementException {
    if (hasNext())
      return array[pos++];
    else
      throw new NoSuchElementException();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}

