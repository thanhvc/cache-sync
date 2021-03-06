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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;

import com.list.sync.core.BaseTest;
import com.list.sync.core.caching.change.DataChangeMerger;
import com.list.sync.core.caching.change.stream.StreamChange;
import com.list.sync.core.caching.key.StreamKey;
import com.list.sync.core.data.ActivityDataBuilder;
import com.list.sync.core.data.CachedActivityData;
import com.list.sync.core.data.CommentDataBuilder;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 20, 2014  
 */
public class ActivityTest extends BaseTest {
  
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
  public void initConnecions() {}
  
  public void testFeed() {
    List<ExoSocialActivity> feed = CachedActivityData.feed(demo, 0, 20);
    assertEquals(20, feed.size());
  }
  
  public void testFeedLoadMore() {
    List<ExoSocialActivity> feed = CachedActivityData.feed(demo, 20, 20);
    assertEquals(20, feed.size());
  }
  
  public void testFeedSize() {
    int got = CachedActivityData.feedSize(demo);
    assertEquals(50, got);
    
  }
  
  public void testOwner() {
    List<ExoSocialActivity> got = CachedActivityData.myActivities(demo, 0, 20);
    assertEquals(20, got.size());
  }
  
  public void testOwnerLoadMore() {
    List<ExoSocialActivity> got = CachedActivityData.myActivities(demo, 20, 20);
    assertEquals(20, got.size());
  }
  
  public void testOwnerSize() {
    int got = CachedActivityData.myActivitiesSize(demo);
    assertEquals(50, got);
  }
  
  public void testChanges() throws Exception {
    CachedActivityData activityData = new CachedActivityData();
    
    List<StreamChange<StreamKey, String>> changes = CachedActivityData.feedChangeList(demo);
    assertEquals(69, changes.size());

    //
    changes = CachedActivityData.feedChangeList(demo, StreamChange.Kind.ADD);
    assertEquals(50, changes.size());

    //
    changes = CachedActivityData.feedChangeList(demo, StreamChange.Kind.MOVE);
    assertEquals(19, changes.size());

    //await for finish persistence.
    CountDownLatch lock = activityData.getScheduler().getSynchronizationLock();
    System.out.println("Change size:" + DataChangeMerger.getSize());
    lock.await(1500, TimeUnit.MILLISECONDS);
    
    changes = CachedActivityData.feedChangeList(demo);
    assertEquals(0, changes.size());
  }
  
  

}
