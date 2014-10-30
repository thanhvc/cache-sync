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
package com.list.sync.core.caching.change.stream;

import com.list.sync.core.caching.change.DataChangeListener;
import com.list.sync.core.caching.key.StreamKey;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 30, 2014  
 */
public class StreamChangeListener implements DataChangeListener<StreamChange<StreamKey, String>> {

  public void onAdd(StreamChange<StreamKey, String> target) {
    System.out.println(Thread.currentThread().getName() + " - ADD - " + target.toString());
    
  }

  public void onAddRef(StreamChange<StreamKey, String> target) {
    System.out.println(Thread.currentThread().getName() + " - ADD_REF - " + target.toString());
  }

  public void onDelete(StreamChange<StreamKey, String> target) {
    System.out.println(Thread.currentThread().getName() + " - REMOVE - " + target.toString());
  }

  public void onUpdate(StreamChange<StreamKey, String> target) {
    System.out.println(Thread.currentThread().getName() + " - UPDATE - " + target.toString());
  }

}
