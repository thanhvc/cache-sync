/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.list.sync.core.caching.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.list.sync.core.caching.change.DataChange;
import com.list.sync.core.caching.change.SimpleDataChange;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 20, 2014  
 */
public abstract class AbstractListData<V, O> {
  /** defines the list keeps the data **/
  private final List<V> list = new ArrayList<V>();
  /** defines the owner data **/
  private final O listOwnerId;
  /** defines the list keeps the changes status **/
  private final List<DataChange<V, O>> listChanges;
  /** define the iterator of changes **/
  private final Iterator<DataChange<V, O>> it;
  /** define the current change**/
  private DataChange<V, O> current;
  
  /**
   * 
   * @param list
   * @param listOwnerId the identityId
   */
  public AbstractListData(final List<V> list, O listOwnerId) {
    this.list.addAll(list);
    this.listOwnerId = listOwnerId;
    this.listChanges = new LinkedList<DataChange<V, O>>();
    this.it = listChanges.iterator();
  }
  
  public AbstractListData(O listOwnerId) {
    this.listOwnerId = listOwnerId;
    this.listChanges = new LinkedList<DataChange<V, O>>();
    this.it = listChanges.iterator();
  }
  
  /**
   * Gets the list of its wrapper 
   * @return
   */
  public List<V> getList() {
    return this.list;
  }
  
  /**
   * Gets the size of elements
   * @return
   */
  public int size() {
    return list.size();
  }

  /**
   * Gets the sublist by given from and to
   * 
   * @param from the given from
   * @param to the given to
   * @return the sublist
   */
  public List<V> subList(int from, int to) {
    return this.list.subList(from, to);
  }
  
  /**
   * Gets all of changes via iterator
   * @return the iterator
   */
  public Iterator<DataChange<V, O>> getChanges() {
    return this.listChanges.iterator();
  }
  
  /**
   * Checks has next element or not
   * 
   * @param kind the given kind of change
   * @return
   */
  private boolean checkNext(DataChange.Kind kind) {
    this.current = null;
    while(this.it.hasNext()) {
      DataChange<V, O> next = it.next();
      if (next.getKind().equals(kind)) {
        this.current = next;
        break;
      }
    }
    
    return (this.current != null);
  }

  /**
   * Gets the changes by DataChange.Kind
   * 
   * @param kind
   * @param reverse
   * @return
   */
  public Iterator<DataChange<V, O>> getChanges(final DataChange.Kind kind, boolean reverse) {
    if (reverse) {
      final ListIterator<DataChange<V, O>> it = listChanges.listIterator(listChanges.size());
      
      return new Iterator<DataChange<V, O>>() {

        public boolean hasNext() {
          return it.hasPrevious();
        }

        public DataChange<V, O> next() {
          return it.previous();
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }

      };
    } else {
      return new Iterator<DataChange<V, O>>() {
        public boolean hasNext() {
          return checkNext(kind);
        }

        public DataChange<V, O> next() {
          return current;
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }

      };
    }
  }
  
  /**
   * Puts the value at the given index
   * @param value
   * @param position
   */
  public void put(int index, V value, O ownerId) {
    beforePut();
    this.list.add(index, value);
    addChange(DataChange.Kind.ADD, value, ownerId);
    afterPut();
  }
  
  /**
   * Puts the reference value at the top of list
   * @param value the given value
   */
  public void putRefAtTop(V value, O ownerId) {
    put(0, value, ownerId);
  }
  
  /**
   * Puts the reference value at the given index
   * @param value
   * @param position
   */
  public void putRef(int index, V value, O ownerId) {
    beforePutRef();
    this.list.add(index, value);
    addChange(DataChange.Kind.ADD_REF, value, ownerId);
    afterPutRef();
  }
  
  /**
   * Puts the value at the top of list
   * @param value the given value
   */
  public void putAtTop(V value, O ownerId) {
    put(0, value, ownerId);
  }
  
  
  public void beforePut() {}
  
  public void afterPut() {}
  
  public void beforePutRef() {}
  
  public void afterPutRef() {}
  
  public void beforeMove() {}
  
  public void afterMove() {}
  
  public void beforeRemove() {}
  
  public void afterRemove() {}
  
  /**
   * Moves the value at the given index
   * @param value
   * @param position
   */
  public void move(int index, V value, O ownerId) {
    beforeMove();
    this.list.remove(value);
    this.list.add(index, value);
    addChange(DataChange.Kind.MOVE, value, ownerId);
    afterMove();
    
  }
  
  /**
   * Moves the value at the top of list
   * @param value the given value
   */
  public void moveTop(V value, O ownerId) {
    move(0, value, ownerId);
  }
  
  /**
   * Moves the value at the given index
   * @param value
   * @param ownerId
   */
  public void remove(V value, O ownerId) {
    beforeRemove();
    this.list.remove(value);
    addChange(DataChange.Kind.DELETE, value, ownerId);
    afterRemove();
  }
  
  
  /**
   * Adds the new changes to keep next processing.
   * TODO separate the changes
   *  
   * @param kind
   * @param value
   * @param owner the given owner data
   */
  private void addChange(DataChange.Kind kind, V value, O ownerId) {
    this.listChanges.add(SimpleDataChange.create(kind, value, this.listOwnerId).build());
  }
}
