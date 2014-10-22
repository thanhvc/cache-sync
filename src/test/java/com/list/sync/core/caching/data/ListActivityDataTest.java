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
    assertEquals(11, data.getList().size());
    
  }
}
