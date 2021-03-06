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


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 28, 2014  
 */
public interface DataChangeListener<M> {
  /**
   * An activity is added
   * @param activity the added model
   */
  void onAdd(M target);
  
  /**
   * A comment is added
   * 
   * @param activity the parent activity
   * @param comment the added comment
   */
  void onAddRef(M target);
  
  /**
   * A model is removed
   * @param activity
   */
  void onDelete(M target);
  
  /**
   * A model is updated
   * @param target
   */
  void onUpdate(M target);
  
  public class Base<M> implements DataChangeListener<M> {

    public void onAdd(M target) {};

    public void onDelete(M target) {}

    public void onUpdate(M target) {}

    public void onAddRef(M target) {}

   
  }
  

}
