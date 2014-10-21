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

import junit.framework.TestCase;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;

import com.list.sync.core.data.ActivityDataBuilder;
import com.list.sync.core.data.CachedActivityData;
import com.list.sync.core.data.CachedIdentityData;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 20, 2014  
 */
public class CachedListActivityTest extends TestCase {
  
  private Identity demo = CachedIdentityData.demo();
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (CachedActivityData.feedSize(demo) == 0) {
      ActivityDataBuilder.initMore(50, demo);
    }
    
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
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

}
