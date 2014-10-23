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
package com.list.sync.core.caching.change;

import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 23, 2014  
 */
public class DataChangeMerger {
  
  /**
   * The method is optimizing the changes list.
   * The reducing change list makes the storage less stress with a lot of transactions in short time.
   * 
   * For example: In 10s, there are 2 changes occurs during time.
   * 
   * - First: Add action
   * - Second: Move action >> due to add comment.
   * 
   * Poster: Merger makes sure that there is only one ADD action for the given stream.
   * Connecter:Merger makes sure that there is only one ADD_REF action for the given stream.
   * 
   * This means there is saving an action here(reduce 1 action per stream) then making less stress storage.
   * 
   * Case 1: ADD, MOVE, and REMOVE >> removes all of changes for the given activity.
   * Case 2: MOVE, and REMOVE >> only REMOVE action is keeping.
   * 
   * @param listChanges
   * @param kind
   * @param value
   * @param ownerId
   */
  public static <V, O> void merge(List<DataChange<V, O>> listChanges, DataChange.Kind kind, V value, O ownerId) {
    //2 cases need to consider.
    //+ Adds already happened recently(temporary status),....., then REMOVE is next >> All Changes must be clear.
    //+ Pushed to storage, some MOVE ready, then REMOVE, All changes must be clear, just keep REMOVE
    if (kind == DataChange.Kind.DELETE) {
      //test case: ListActivityDataTest#testComplex
      DataChange<V, O> move = SimpleDataChange.create(DataChange.Kind.MOVE, value, ownerId).build();
      boolean hasMove = listChanges.contains(move);
      if (hasMove) {
        listChanges.remove(move);
      }
      //test case: ListActivityDataTest#testComplex1
      DataChange<V, O> add = SimpleDataChange.create(DataChange.Kind.ADD, value, ownerId).build();
      boolean hasAdd = listChanges.contains(add);
      if (hasAdd) {
        listChanges.remove(add);
        return;//don't add remove
      }
      
      //test case: ListActivityDataTest#testComplex1
      DataChange<V, O> addRef = SimpleDataChange.create(DataChange.Kind.ADD_REF, value, ownerId).build();
      boolean hasAddRef = listChanges.contains(addRef);
      if (hasAddRef) {
        listChanges.remove(addRef);
        return;//don't add remove
      }
    }
    DataChange<V, O> change = SimpleDataChange.create(kind, value, ownerId).build();
    boolean isExisting = listChanges.contains(change);
    if(!isExisting) {
      listChanges.add(SimpleDataChange.create(kind, value, ownerId).build());
    }
    
  }

}
