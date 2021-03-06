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

import java.util.List;

import com.list.sync.core.caching.change.DataChangeMerger;
import com.list.sync.core.caching.change.stream.StreamChange;
import com.list.sync.core.caching.key.StreamKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 20, 2014  
 */
public class ListActivityData extends AbstractListData<StreamKey, String> {
  public ListActivityData(StreamKey streamKey, List<String> list) {
    super(streamKey, list);
  }
  
  public ListActivityData(StreamKey streamKey) {
    super(streamKey);
  }
  
  
  protected void addChange(StreamChange.Kind kind, String value, String ownerId) {
    DataChangeMerger.merge(kind, this.key, value, ownerId);
  }

}
