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

import junit.framework.TestCase;

import org.exoplatform.social.core.identity.model.Identity;

import com.list.sync.core.data.CachedIdentityData;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 22, 2014  
 */
public abstract class BaseTest extends TestCase {
  protected Identity demo = CachedIdentityData.demo();
  protected Identity john = CachedIdentityData.john();
  protected Identity mary = CachedIdentityData.mary();
  protected Identity root = CachedIdentityData.root();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    initData();
    initConnecions();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public abstract void initData();
  
  public abstract void initConnecions();
}
