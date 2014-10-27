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
package com.list.sync.core;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.common.service.ExecutorServiceManager;
import org.exoplatform.social.common.service.SocialServiceExecutor;
import org.exoplatform.social.common.service.impl.ExecutorServiceManagerImpl;
import org.exoplatform.social.common.service.impl.SocialServiceExecutorImpl;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 27, 2014  
 */
public class SOCContext {
  
  private static final Log LOG = ExoLogger.getLogger(SOCContext.class);
  /** */
  static final String INTERVAL_ACTIVITY_PERSIST_THRESHOLD = "exo.social.interval.activity.persist.threshold";
  /** */
  static final String ACTIVITY_LIMIT_PERSIST_THRESHOLD = "exo.social.activity.limit.persist.threshold";
  /** */
  static final long DEFAULT_INTERVAL_ACTIVITY_PERSIST_THRESHOLD = 20; //2s  = 1000 x 2
  /** */
  static final long DEFAULT_ACTIVITY_LIMIT_PERSIST_THRESHOLD = 100; //number per persist storage
  
  private long intervalPersistThreshold;
  /** */
  private long limitPersistThreshold;
  /** */
  private final SocialServiceExecutor serviceExecutor;
  /** */
  private static ExecutorServiceManager executorServiceManager = new ExecutorServiceManagerImpl();
  
  private static SOCContext instance;

  public SOCContext() {
    this.intervalPersistThreshold = DEFAULT_INTERVAL_ACTIVITY_PERSIST_THRESHOLD;
    this.limitPersistThreshold = DEFAULT_ACTIVITY_LIMIT_PERSIST_THRESHOLD;
    serviceExecutor = new SocialServiceExecutorImpl(executorServiceManager.newDefaultThreadPool("Social"));
  }
  
  /**
   * Implements the singleton pattern
   * @return
   */
  public static SOCContext getInstance() {
    if (instance == null) {
      instance = new SOCContext();
    }
    
    return instance;
  }
  
  /**
   * Gets the Social Service executor
   * 
   * @return
   */
  public SocialServiceExecutor getServiceExecutor() {
    return serviceExecutor;
  }
  
  /**
   * Gets number per persist storage.
   * 
   * @return the number per persist storage.
   */
  public long getLimitPersistThreshold() {
    return limitPersistThreshold;
  }
  
  /**
   * Gets interval activity persist threshold time.
   * 
   * @return The period of time to execute a persistence call.
   */
  public long getIntervalPersistThreshold() {
    return intervalPersistThreshold;
  }

}
