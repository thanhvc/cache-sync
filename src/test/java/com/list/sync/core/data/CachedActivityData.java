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
package com.list.sync.core.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.storage.cache.model.key.ActivityType;

import com.list.sync.core.ExoCache;
import com.list.sync.core.caching.change.DataChange;
import com.list.sync.core.caching.data.ActivityData;
import com.list.sync.core.caching.data.ListActivityData;
import com.list.sync.core.caching.key.ActivityKey;
import com.list.sync.core.caching.key.IdentityKey;
import com.list.sync.core.caching.key.StreamKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 21, 2014  
 */
public class CachedActivityData {

  private static ExoCache<StreamKey, ListActivityData> streamCaching = new ExoCache<StreamKey, ListActivityData>();
  private static ExoCache<ActivityKey, ActivityData> activityCaching = new ExoCache<ActivityKey, ActivityData>();
  private static ExoCache<ActivityKey, List<ActivityKey>> commentCaching = new ExoCache<ActivityKey, List<ActivityKey>>();
  
  /**
   * 
   * @param posterId
   * @param activity
   * @return
   */
  public static ExoSocialActivity saveActivity(String posterId, ExoSocialActivity activity) {
    activity.setId(UUID.randomUUID().toString());
    ActivityKey key = new ActivityKey(activity.getId());
    ActivityData data = new ActivityData(activity);
    activityCaching.put(key, data);
    
    putStream(posterId, activity);
    putMyConnectionStream(posterId, activity);
    return activity;
  }
  
  /**
   * Posts the new comment into the existing activity.
   * 
   * @param posterId
   * @param comment
   */
  public static void saveComment(String posterId, ExoSocialActivity comment) {
    comment.setId(UUID.randomUUID().toString());
    ActivityKey commentKey = new ActivityKey(comment.getId());
    ActivityData commentData = new ActivityData(comment);
    activityCaching.put(commentKey, commentData);
    //
    ActivityKey parentKey = new ActivityKey(comment.getParentId());
    ActivityData activityData = activityCaching.get(parentKey);
    
    ExoSocialActivity parent = activityData.build();
    
    List<ActivityKey> comments = commentCaching.get(parentKey);
    if (comments == null) {
      comments = new LinkedList<ActivityKey>();
      commentCaching.put(parentKey, comments);
    }
    comments.add(commentKey);
    
    //update what's hot feature.
    String ownerId = parent.getUserId();
    moveTopStream(ownerId, parent);
    moveTopConnectionsStream(ownerId, parent);
  }
  
  /**
   * 
   * @param activityId
   * @return
   */
  public static List<ExoSocialActivity> getComments(String activityId) {
    ActivityKey parentKey = new ActivityKey(activityId);
    List<ActivityKey> comments = commentCaching.get(parentKey);
    if (comments == null) 
      return Collections.emptyList();
    
    List<ExoSocialActivity> result = new LinkedList<ExoSocialActivity>();
    for(ActivityKey commentKey : comments) {
      ActivityData data = activityCaching.get(commentKey);
      if(data != null) {
        result.add(data.build());
      }
    }
    
    return result;
  }
  
  /**
   * Puts the activity to poster's stream
   * 
   * @param ownerId
   * @param activity
   */
  private static void moveTopStream(String ownerId, ExoSocialActivity activity) {
    moveTopStream(ownerId, activity, ActivityType.FEED);
    moveTopStream(ownerId, activity, ActivityType.USER);
  }
  
  /**
   * Moves the activity to at the top of stream
   * 
   * @param ownerId
   * @param activity
   */
  private static void moveTopConnectionsStream(String ownerId, ExoSocialActivity activity) {
    List<IdentityKey> myConnections = CachedRelationshipData.getConnections(ownerId);
    if (myConnections != null) {
      for(IdentityKey key : myConnections) {
        moveTopStreamForConnection(key.getKey(), activity, ActivityType.FEED);
        moveTopStreamForConnection(key.getKey(), activity, ActivityType.CONNECTION);
      }
    }
  }
  
  /**
   * Moves the activity to at the top of stream
   * 
   * @param ownerId
   * @param activity
   * @param type
   */
  private static void moveTopStream(String ownerId, ExoSocialActivity activity, ActivityType type) {
    StreamKey newKey = StreamKey.init(ownerId).key(type);
    
    ListActivityData data = streamCaching.get(newKey);
    if (data == null) {
      data = new ListActivityData(ownerId);
      streamCaching.put(newKey, data);
    }
    
    data.moveTop(activity.getId(), ownerId);
  }
  
  /**
   * Moves the activity to at the top of stream for relationship
   * 
   * @param ownerId
   * @param activity
   * @param type
   */
  private static void moveTopStreamForConnection(String ownerId, ExoSocialActivity activity, ActivityType type) {
    StreamKey newKey = StreamKey.init(ownerId).key(type);
    
    ListActivityData data = streamCaching.get(newKey);
    if (data == null) {
      data = new ListActivityData(ownerId);
      streamCaching.put(newKey, data);
    }
    
    data.putRefAtTop(activity.getId(), ownerId);
  }
  
  /**
   * Puts the activity to poster's stream
   * 
   * @param posterId
   * @param activity
   */
  private static void putStream(String posterId, ExoSocialActivity activity) {
    List<String> keys = new LinkedList<String>();
    keys.add(activity.getId());
    putToStream(posterId, activity, ActivityType.FEED);
    putToStream(posterId, activity, ActivityType.USER);
  }
  /**
   * Puts the activity to poster's connections stream
   * 
   * @param posterId
   * @param activity
   */
  private static void putMyConnectionStream(String posterId, ExoSocialActivity activity) {
    List<IdentityKey> connections = CachedRelationshipData.getConnections(posterId);
    
    if (connections == null) return;
    
    for(IdentityKey key : connections) {
      List<String> keys = new LinkedList<String>();
      keys.add(activity.getId());
      putToStream(key.getKey(), activity, ActivityType.FEED);
      putToStream(key.getKey(), activity, ActivityType.CONNECTION);
    }
  }

  /**
   * Puts the activity into the stream
   * 
   * @param posterId
   * @param activity
   * @param type
   */
  private static void putToStream(String posterId, ExoSocialActivity activity, ActivityType type) {
    StreamKey newKey = StreamKey.init(posterId).key(type);
    
    ListActivityData data = streamCaching.get(newKey);
    if (data == null) {
      data = new ListActivityData(posterId);
      streamCaching.put(newKey, data);
    }
    
    data.putAtTop(activity.getId(), posterId);
  }
  
  public static ExoSocialActivity getActivityById(String activityId) {
    return activityCaching.get(new ActivityKey(activityId)).build();
  }
  
  /**
   * Gets the feed stream by the given offset and limit.
   * 
   * @param identity
   * @param offset
   * @param limit
   * @return
   */
  public static List<ExoSocialActivity> feed(Identity identity, int offset, int limit) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.FEED);
    ListActivityData data = streamCaching.get(key);
    
    if (data == null) return Collections.emptyList();
    List<String> got = data.subList(offset, offset + limit);
    
    List<ExoSocialActivity> result = new LinkedList<ExoSocialActivity>();
    
    ActivityData activityData = null;
    
    for(String id : got) {
      activityData = activityCaching.get(new ActivityKey(id));
      if (activityData != null) {
        result.add(activityData.build());
      }
    }
    
    return result;
  }
  
  /**
   * Gets the size of feed stream.
   * 
   * @param identity
   * @return
   */
  public static int feedSize(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.FEED);
    ListActivityData data = streamCaching.get(key);
    
    if (data == null) return 0;
    return data.size();
  }
  
  /**
   * Gets the iterator of changes for feed.
   * 
   * @param identity
   * @return
   */
  public static Iterator<DataChange<String, String>> feedChanges(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.FEED);
    ListActivityData data = streamCaching.get(key);
    if (data == null) return null;
    return data.getChanges();
  }
  
  /**
   * Gets the iterator of changes list for feed.
   * 
   * @param identity
   * @return
   */
  public static List<DataChange<String, String>> feedChangeList(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.FEED);
    ListActivityData data = streamCaching.get(key);
    if (data == null) return null;
    return data.getChangeList();
  }
  
  /**
   * Gets the iterator of changes list for feed.
   * 
   * @param identity
   * @return
   */
  public static List<DataChange<String, String>> feedChangeList(Identity identity, DataChange.Kind kind) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.FEED);
    ListActivityData data = streamCaching.get(key);
    if (data == null) return null;
    return data.getChangeList(kind);
  }
  
  /**
   * Gets the connection stream by the given offset and limit.
   * 
   * @param identity
   * @param offset
   * @param limit
   * @return
   */
  public static List<ExoSocialActivity> connections(Identity identity, int offset, int limit) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.CONNECTION);
    ListActivityData data = streamCaching.get(key);
    
    if (data == null) return Collections.emptyList();
    List<String> got = data.subList(offset, offset + limit);
    
    List<ExoSocialActivity> result = new LinkedList<ExoSocialActivity>();
    
    ActivityData activityData = null;
    
    for(String id : got) {
      activityData = activityCaching.get(new ActivityKey(id));
      if (activityData != null) {
        result.add(activityData.build());
      }
    }
    
    return result;
  }
  
  /**
   * Gets the size of connections stream.
   * 
   * @param identity
   * @return
   */
  public static int connectionsSize(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.CONNECTION);
    ListActivityData data = streamCaching.get(key);
    
    if (data == null) return 0;
    return data.size();
  }
  
  /**
   * Gets the iterator of changes for connection.
   * 
   * @param identity
   * @return
   */
  public static Iterator<DataChange<String, String>> connectionsChanges(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.CONNECTION);
    ListActivityData data = streamCaching.get(key);
    if (data == null) return null;
    return data.getChanges();
  }
  
  /**
   * Gets the iterator of changes list for feed.
   * 
   * @param identity
   * @return
   */
  public static List<DataChange<String, String>> connectionsChangeList(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.CONNECTION);
    ListActivityData data = streamCaching.get(key);
    if (data == null) return null;
    return data.getChangeList();
  }
  
  /**
   * Gets the my stream by the given offset and limit.
   * 
   * @param identity
   * @param offset
   * @param limit
   * @return
   */
  public static List<ExoSocialActivity> myActivities(Identity identity, int offset, int limit) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.USER);
    ListActivityData data = streamCaching.get(key);
    
    if (data == null) return Collections.emptyList();
    List<String> got = data.subList(offset, offset + limit);
    
    List<ExoSocialActivity> result = new LinkedList<ExoSocialActivity>();
    
    ActivityData activityData = null;
    
    for(String id : got) {
      activityData = activityCaching.get(new ActivityKey(id));
      if (activityData != null) {
        result.add(activityData.build());
      }
    }
    
    return result;
  }
  
  /**
   * Gets the size of connections stream.
   * 
   * @param identity
   * @return
   */
  public static int myActivitiesSize(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.USER);
    ListActivityData data = streamCaching.get(key);
    
    if (data == null) return 0;
    return data.size();
  }
  
  /**
   * Gets the iterator of changes for my activities.
   * 
   * @param identity
   * @param offset
   * @param limit
   * @return
   */
  public static Iterator<DataChange<String, String>> myActivitiesChanges(Identity identity) {
    
    StreamKey key = StreamKey.init(identity.getId()).key(ActivityType.USER);
    ListActivityData data = streamCaching.get(key);
    if (data == null) return null;
    return data.getChanges();
  }
  
  /**
   * Clear all caching
   */
  public static void reset() {
    streamCaching.clear();
    activityCaching.clear();
    commentCaching.clear();
  }
}
