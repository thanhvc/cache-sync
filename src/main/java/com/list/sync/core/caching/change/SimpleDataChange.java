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
 * Oct 20, 2014  
 */
public class SimpleDataChange<V, O> implements DataChange<V, O> {
  
  private final V data;
  
  private final O ownerId;
  
  private final Kind kind;
  
  private long revision; 
  
  public static <V,O> Builder<V, O> create(Kind kind, V data, O ownerId) {
    return new Builder<V, O>(kind, data, ownerId);
  }
  
  public SimpleDataChange(Builder<V, O> builder) {
    this.kind = builder.kind;
    this.data = builder.data;
    this.ownerId = builder.ownerId;
    this.revision = builder.revision;
  }

  public V getData() {
    return this.data;
  }
  
  public O getOwnerId() {
    return this.ownerId;
  }
  
  public DataChange.Kind getKind() {
    return this.kind;
  }
  
  public long getRevision() {
    return this.revision;
  }
  
  public void setRevision(long newRevision) {
    this.revision = newRevision;
  }
  
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleDataChange)) {
      return false;
    }

    SimpleDataChange<?, ?> that = (SimpleDataChange<?, ?>) o;

    if (data != null ? !data.equals(that.data) : that.data != null) {
      return false;
    }
    
    if (kind != null ? !kind.equals(that.kind) : that.kind != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 31 * (data != null ? data.hashCode() : 0);
    result = 31 * (kind != null ? kind.hashCode() : 0); 
    return result;
  }
  
  @Override
  public String toString() {
    return "SimpleDataChange[data = " + this.data + ", kind = " + this.kind.toString() + ", revision = " + this.revision + "]";
  }
  
  public static class Builder<V, O> {
    public final V data;
    
    public final O ownerId;
    
    public final Kind kind;
    
    public long revision; 
    
    public Builder(Kind kind, V data, O ownerId) {
      this.kind = kind;
      this.data = data;
      this.ownerId = ownerId;
      this.revision = System.currentTimeMillis();
    }
    
    public Builder<V, O> revision(long revision) {
      this.revision = revision;
      return this;
    }
    
    public SimpleDataChange<V, O> build() {
      return new SimpleDataChange<V, O>(this);
    }
  }
  
}
