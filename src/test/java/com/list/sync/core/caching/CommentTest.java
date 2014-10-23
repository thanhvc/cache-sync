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
package com.list.sync.core.caching;

import java.util.List;
import java.util.ListIterator;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;

import com.list.sync.core.BaseTest;
import com.list.sync.core.caching.change.DataChange;
import com.list.sync.core.caching.key.IdentityKey;
import com.list.sync.core.data.ActivityDataBuilder;
import com.list.sync.core.data.CachedActivityData;
import com.list.sync.core.data.CachedRelationshipData;
import com.list.sync.core.data.CommentDataBuilder;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 20, 2014  
 */
public class CommentTest extends BaseTest {
  
  @Override
  public void initData() {
    if (CachedActivityData.feedSize(demo) == 0) {
      ActivityDataBuilder.initMore(50, demo).inject();
      List<ExoSocialActivity> feed = CachedActivityData.feed(demo, 0, 20);
      ListIterator<ExoSocialActivity> it = feed.listIterator(feed.size() - 1);
      while (it.hasPrevious()) {
        ExoSocialActivity activity = it.previous();
        CommentDataBuilder.initMore(10, activity, demo).inject();
      }
    }
  }
  
  @Override
  public void initConnecions() {
    List<IdentityKey> demoConnections = CachedRelationshipData.getConnections(demo);
    if (demoConnections == null) {
      CachedRelationshipData.addRelationship(demo, john);
      CachedRelationshipData.addRelationship(demo, mary);
    }
  }

  public void testAddComments() throws Exception {
    List<ExoSocialActivity> feed = CachedActivityData.feed(demo, 0, 20);
    ExoSocialActivity first = feed.get(0);
    ExoSocialActivity second = feed.get(1);
    assertTrue(first.getTitle().indexOf("49") > 0);
    assertTrue(second.getTitle().indexOf("48") > 0);
    List<ExoSocialActivity> comments = CachedActivityData.getComments(first.getId());
    assertEquals(10, comments.size());

    {
      //test What's hot feature
      CommentDataBuilder.initMore(5, second, demo).inject();
      comments = CachedActivityData.getComments(second.getId());
      assertEquals(15, comments.size());
      
      //
      feed = CachedActivityData.feed(demo, 0, 20);
      first = feed.get(0);
      assertTrue(first.getTitle().indexOf("48") > 0);
      
      List<DataChange<String, String>> changes = CachedActivityData.feedChangeList(demo);
      assertEquals(69, changes.size());

      //
      changes = CachedActivityData.feedChangeList(demo, DataChange.Kind.MOVE);
      assertEquals(19, changes.size());
    }
    
    CachedActivityData.reset();
  }
  
  public void testAddCommentRelationship() throws Exception {
    CachedActivityData.reset();
    //
    ActivityDataBuilder.initMore(1, demo).inject();
    
    List<DataChange<String, String>> changes = CachedActivityData.feedChangeList(demo);
    assertEquals(1, changes.size());

    changes = CachedActivityData.feedChangeList(john, DataChange.Kind.ADD_REF);
    assertEquals(1, changes.size());
    
    CachedActivityData.reset();
  }
  
  public void testComplex() throws Exception {
    CachedActivityData.reset();
    //
    ActivityDataBuilder.initMore(2, demo).inject();
    
    List<DataChange<String, String>> changes = CachedActivityData.feedChangeList(demo, DataChange.Kind.ADD);
    assertEquals(2, changes.size());

    {
      //john
      changes = CachedActivityData.feedChangeList(john, DataChange.Kind.ADD_REF);
      assertEquals(2, changes.size());
      
      changes = CachedActivityData.connectionsChangeList(john, DataChange.Kind.ADD_REF);
      assertEquals(2, changes.size());
      
      changes = CachedActivityData.feedChangeList(john, DataChange.Kind.ADD);
      assertEquals(0, changes.size());
    }
    
    {
      //mary
      changes = CachedActivityData.feedChangeList(mary, DataChange.Kind.ADD_REF);
      assertEquals(2, changes.size());
      
      changes = CachedActivityData.connectionsChangeList(mary, DataChange.Kind.ADD_REF);
      assertEquals(2, changes.size());
      
      changes = CachedActivityData.feedChangeList(mary, DataChange.Kind.ADD);
      assertEquals(0, changes.size());
    }
    
    //remove here
    List<ExoSocialActivity> feed = CachedActivityData.feed(demo, 0, 20);
    ExoSocialActivity first = feed.get(0);
    CachedActivityData.removeActivity(first.getId());
    changes = CachedActivityData.feedChangeList(john, DataChange.Kind.ADD_REF);
    assertEquals(1, changes.size());
    //
    changes = CachedActivityData.feedChangeList(john, DataChange.Kind.DELETE);
    assertEquals(0, changes.size());
    
    CachedActivityData.reset();
  }
}
