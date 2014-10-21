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

import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.storage.cache.model.data.IdentityData;

import com.list.sync.core.ExoCache;
import com.list.sync.core.caching.key.IdentityKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 21, 2014  
 */
public class CachedIdentityData {

  private static ExoCache<IdentityKey, IdentityData> identityCaching = new ExoCache<IdentityKey, IdentityData>();
  
  static {
    Identity identity = new Identity("john");
    identity.setProviderId(OrganizationIdentityProvider.NAME);
    identity.setRemoteId("john");
    //put JOHN
    identityCaching.put(new IdentityKey(identity.getId()), new IdentityData(identity));
    
    identity = new Identity("root");
    identity.setProviderId(OrganizationIdentityProvider.NAME);
    identity.setRemoteId("root");
    //put ROOT
    identityCaching.put(new IdentityKey(identity.getId()), new IdentityData(identity));
    
    identity = new Identity("mary");
    identity.setProviderId(OrganizationIdentityProvider.NAME);
    identity.setRemoteId("mary");
    //put ROOT
    identityCaching.put(new IdentityKey(identity.getId()), new IdentityData(identity));
    
    identity = new Identity("demo");
    identity.setProviderId(OrganizationIdentityProvider.NAME);
    identity.setRemoteId("demo");
    //put ROOT
    identityCaching.put(new IdentityKey(identity.getId()), new IdentityData(identity));
  }
  
  
  /**
   * Gets demo identity
   * @return
   */
  public static Identity demo() {
    return identityCaching.get(new IdentityKey("demo")).build();
  }
  
  /**
   * Gets john identity
   * @return
   */
  public static Identity john() {
    return identityCaching.get(new IdentityKey("john")).build();
  }
  
  /**
   * Gets root identity
   * @return
   */
  public static Identity root() {
    return identityCaching.get(new IdentityKey("root")).build();
  }
  
  /**
   * Gets root identity
   * @return
   */
  public static Identity mary() {
    return identityCaching.get(new IdentityKey("mary")).build();
  }
}
