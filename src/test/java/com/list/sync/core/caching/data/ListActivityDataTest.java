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

import java.util.LinkedList;
import java.util.List;

import com.list.sync.core.caching.change.DataChange;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 22, 2014  
 */
public class ListActivityDataTest extends TestCase {

  public void testMoveUp() {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");
    
    ListActivityData data = new ListActivityData(list, "mary");
    
    data.moveTop("5", "mary");
    String got = data.getList().get(0);
    assertEquals("5", got);
    assertEquals(10, data.getList().size());
    
  }
  
  public void testPutUp() {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");
    
    ListActivityData data = new ListActivityData(list, "mary");
    
    data.putAtTop("11", "mary");
    String got = data.getList().get(0);
    assertEquals("11", got);
    got = data.getList().get(1);
    assertEquals("9", got);
    assertEquals(11, data.getList().size());
    
  }
  
  public void testDoublePutUp() {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");
    
    ListActivityData data = new ListActivityData(list, "mary");
    //first
    data.putAtTop("11", "mary");
    String got = data.getList().get(0);
    assertEquals("11", got);
    assertEquals(11, data.getList().size());
    //second
    data.putAtTop("11", "mary");
    assertEquals(1, data.getChangeList().size());
    
    assertEquals(1, data.getChangeList(DataChange.Kind.ADD).size());
  }
  
  public void testDoubleMoveUp() {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");

    ListActivityData data = new ListActivityData(list, "mary");

    // first
    data.moveTop("5", "mary");
    String got = data.getList().get(0);
    assertEquals("5", got);
    assertEquals(10, data.getList().size());

    assertEquals(1, data.getChangeList(DataChange.Kind.MOVE).size());

    // second
    data.moveTop("2", "mary");
    got = data.getList().get(0);
    assertEquals("2", got);
    assertEquals(2, data.getChangeList(DataChange.Kind.MOVE).size());
    
    //one more
    data.moveTop("5", "mary");
    got = data.getList().get(0);
    assertEquals("5", got);
    assertEquals(2, data.getChangeList(DataChange.Kind.MOVE).size());
  }
  
  public void testRemove() {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");

    ListActivityData data = new ListActivityData(list, "mary");
    

    // first
    data.remove("3", "mary");
    assertEquals(9, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    
    
  }
  
  public void testRemoveTop() {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");

    ListActivityData data = new ListActivityData(list, "mary");
    

    // first
    data.remove("9", "mary");
    assertEquals(9, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    String got = data.getList().get(0);
    assertEquals("8", got);
  }
  
  public void testDoubleRemove() throws Exception {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");

    ListActivityData data = new ListActivityData(list, "mary");
    

    // first
    data.remove("3", "mary");
    assertEquals(9, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    //second
    data.remove("3", "mary");
    assertEquals(9, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    
  }
  
  public void testMixPutMoveAndRemove() throws Exception {
    List<String> list = new LinkedList<String>();
    list.add("9");
    list.add("8");
    list.add("7");
    list.add("6");
    list.add("5");
    list.add("4");
    list.add("3");
    list.add("2");
    list.add("1");
    list.add("0");

    ListActivityData data = new ListActivityData(list, "mary");

    // first PUT
    data.putAtTop("11", "mary");
    String got = data.getList().get(0);
    assertEquals("11", got);
    
    data.remove("3", "mary");
    assertEquals(10, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    //second MOVE
    data.remove("3", "mary");
    assertEquals(10, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    //third MOVE
  }
  
  public void testFromBegin() {
    ListActivityData data = new ListActivityData("mary");
    
    data.putAtTop("0", "mary");
    data.putAtTop("1", "mary");
    data.putAtTop("2", "mary");
    data.putAtTop("3", "mary");
    data.putAtTop("4", "mary");
    data.putAtTop("5", "mary");
    assertEquals(6, data.getList().size());
    String top = data.getList().get(0);
    assertEquals("5", top);
    //expecting ADD = 6
    assertEquals(6, data.getChangeList(DataChange.Kind.ADD).size());
    
    //move 0 to top
    data.moveTop("0", "mary");
    top = data.getList().get(0);
    assertEquals("0", top);
    assertEquals(6, data.getList().size());
    assertEquals(1, data.getChangeList(DataChange.Kind.MOVE).size());
    
    //move 1 to top
    data.moveTop("1", "mary");
    top = data.getList().get(0);
    assertEquals("1", top);
    assertEquals(6, data.getList().size());
    assertEquals(2, data.getChangeList(DataChange.Kind.MOVE).size());
    
    //move 0 to top >> expecting 0 at the top, MOVE size = 2
    data.moveTop("0", "mary");
    top = data.getList().get(0);
    assertEquals("0", top);
    assertEquals(6, data.getList().size());
    assertEquals(2, data.getChangeList(DataChange.Kind.MOVE).size());
    assertEquals(6, data.getChangeList(DataChange.Kind.ADD).size());
    
    //move 5 to top >> expecting 0 at the top, MOVE size = 3
    data.moveTop("5", "mary");
    top = data.getList().get(0);
    assertEquals("5", top);
    assertEquals(6, data.getList().size());
    assertEquals(3, data.getChangeList(DataChange.Kind.MOVE).size());
    assertEquals(6, data.getChangeList(DataChange.Kind.ADD).size());
    
    //remove 5 to top >> expecting 0 at the top
    data.remove("5", "mary");
    top = data.getList().get(0);
    assertEquals("0", top);
    assertEquals(5, data.getList().size());
    assertEquals(3, data.getChangeList(DataChange.Kind.MOVE).size());
    assertEquals(6, data.getChangeList(DataChange.Kind.ADD).size());
    assertEquals(1, data.getChangeList(DataChange.Kind.DELETE).size());
    //TODO Here is improving point. when removing action is coming.
    // 2 cases need to consider.
    
    //+ Adds already happened recently(temporary status),....., then REMOVE is next >> All Changes must be clear.
    //+ Pushed to storage, some MOVE ready, then REMOVE, All changes must be clear, just keep REMOVE 
    
  }
}
