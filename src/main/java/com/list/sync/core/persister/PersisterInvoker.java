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
package com.list.sync.core.persister;

import java.util.List;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.common.service.ProcessContext;
import org.exoplatform.social.common.service.SocialServiceContext;
import org.exoplatform.social.common.service.impl.SocialServiceContextImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.storage.streams.SocialChromatticAsyncProcessor;

import com.list.sync.core.caching.change.DataChange;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 27, 2014  
 */
public class PersisterInvoker {

  private static final Log LOG = ExoLogger.getLogger(PersisterInvoker.class);
  /**
   * Invokes to records the activity to Stream
   * 
   * @param owner
   * @param entity
   * @param mentioners NULL is empty mentioner.
   * @return
   */
  public static <V, O> void persist(Identity identity, List<DataChange<V, O>> listChanges) {
    if (listChanges == null || listChanges.size() == 0) return;
    SocialServiceContext ctx = SocialServiceContextImpl.getInstance();
    StreamProcessContext processCtx = StreamProcessContext.getIntance(StreamProcessContext.PERSIST_ACTIVITIES_STREAM_PROCESS, SocialServiceContextImpl.getInstance());
    try {
      ctx.getServiceExecutor().async(doPersist(identity, listChanges), processCtx);
    } finally {
    }
  }
  
  private static <V, O> SocialChromatticAsyncProcessor doPersist(final Identity identity, final List<DataChange<V, O>> listChanges) {
    return new SocialChromatticAsyncProcessor(SocialServiceContextImpl.getInstance()) {

      @Override
      protected ProcessContext execute(ProcessContext processContext) throws Exception {
        try {
          LOG.info("persist with " + identity.getRemoteId());
          for(DataChange<V, O> change : listChanges) {
            LOG.info("changes " + change.toString());
          }
        } finally {
          listChanges.clear();
        }
        return processContext;
      }

    };
  }
}
