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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.list.sync.core.caching.change.stream.StreamChange;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 20, 2014  
 */
public abstract class AbstractListData<K, V> {
  /** defines the list keeps the data **/
  private final List<V> list = new LinkedList<V>();
  protected final K key;
  
  /**
   * 
   * @param list
   * @param listOwnerId the identityId
   */
  public AbstractListData(K key, final List<V> list) {
    this.list.addAll(list);
    this.key = key;
  }
  
  public AbstractListData(K key) {
    this.key = key;
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
    if (from >= this.list.size()) return Collections.emptyList();
    int newTo = Math.min(to, this.list.size());
    return this.list.subList(from, newTo);
  }
  
  
  /**
   * Puts the value at the given index
   * @param value
   * @param position
   */
  public void put(int index, V value, String ownerId) {
    beforePut();
    this.list.add(index, value);
    addChange(StreamChange.Kind.ADD, value, ownerId);
    afterPut();
  }
  
  /**
   * Puts the reference value at the top of list
   * @param value the given value
   */
  public void putRefAtTop(V value, String ownerId) {
    putRef(0, value, ownerId);
  }
  
  /**
   * Puts the reference value at the given index
   * @param value
   * @param position
   */
  public void putRef(int index, V value, String ownerId) {
    beforePutRef();
    this.list.add(index, value);
    addChange(StreamChange.Kind.ADD_REF, value, ownerId);
    afterPutRef();
  }
  
  /**
   * Puts the value at the top of list
   * @param value the given value
   */
  public void putAtTop(V value, String ownerId) {
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
  public void move(int index, V value, String ownerId) {
    if (this.list.indexOf(value) > 0) {
      beforeMove();
      this.list.remove(value);
      this.list.add(index, value);
      addChange(StreamChange.Kind.MOVE, value, ownerId);
      afterMove();
    }
  }
  
  /**
   * Moves the value at the top of list
   * @param value the given value
   */
  public void moveTop(V value, String ownerId) {
    move(0, value, ownerId);
  }
  
  /**
   * Moves the value at the given index
   * @param value
   * @param ownerId
   */
  public void remove(V value, String ownerId) {
    beforeRemove();
    boolean hasRemoved = this.list.remove(value);
    if (hasRemoved) {
      addChange(StreamChange.Kind.DELETE, value, ownerId);
    }
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
  protected abstract void addChange(StreamChange.Kind kind, V value, String ownerId);
}
