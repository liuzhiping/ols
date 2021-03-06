/*
 * OpenBench LogicSniffer / SUMP project 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *
 * Copyright (C) 2006-2010 Michael Poppitz, www.sump.org
 * Copyright (C) 2010 J.W. Janssen, www.lxtreme.nl
 */
package nl.lxtreme.ols.client.data.settings;


import java.util.*;
import java.util.Map.Entry;

import nl.lxtreme.ols.api.*;


/**
 * Provides an implementation of {@link UserSettings}.
 */
public final class UserSettingsImpl implements UserSettings
{
  // INNER TYPES

  /**
   * Provides a user settings iterator.
   */
  static final class UserSettingsIterator implements Iterator<Map.Entry<String, Object>>
  {
    // VARIABLES

    private final Iterator<Entry<Object, Object>> entries;

    // CONSTRUCTORS

    /**
     * Creates a new UserSettingsIterator instance.
     * 
     * @param aProperties
     *          the properties to iterate over, cannot be <code>null</code>.
     */
    public UserSettingsIterator( final Properties aProperties )
    {
      this.entries = aProperties.entrySet().iterator();
    }

    // METHODS

    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
      return this.entries.hasNext();
    }

    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public Entry<String, Object> next()
    {
      final Entry<Object, Object> nextEntry = this.entries.next();

      final String key = String.valueOf( nextEntry.getKey() );
      final Object value = nextEntry.getValue();

      return new AbstractMap.SimpleImmutableEntry<String, Object>( key, value );
    }

    /**
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  // CONSTANTS

  private static final long serialVersionUID = 1L;

  // VARIABLES

  private final String name;
  private final Properties properties;

  // CONSTRUCTORS

  /**
   * Creates a new UserSettingsImpl instance.
   * 
   * @param aName
   *          the name of this user settings, cannot be <code>null</code>.
   */
  public UserSettingsImpl( final String aName )
  {
    this( aName, new Properties() );
  }

  /**
   * Creates a new UserSettingsImpl instance.
   * 
   * @param aName
   *          the name of this user settings, cannot be <code>null</code>;
   * @param aProperties
   *          the (initial) properties of this user settings, cannot be
   *          <code>null</code>.
   */
  public UserSettingsImpl( final String aName, final Properties aProperties )
  {
    this.name = aName;
    this.properties = new Properties( aProperties );
  }

  // METHODS

  /**
   * @see nl.lxtreme.ols.api.UserSettings#get(java.lang.String,
   *      java.lang.String)
   */
  @Override
  public String get( final String aName, final String aDefaultValue )
  {
    String result = this.properties.getProperty( aName, aDefaultValue );
    return result;
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#getBoolean(java.lang.String, boolean)
   */
  @Override
  public boolean getBoolean( final String aName, final boolean aDefaultValue )
  {
    String value = this.properties.getProperty( aName );
    if ( value == null )
    {
      return aDefaultValue;
    }
    return Boolean.parseBoolean( value );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#getInt(java.lang.String, int)
   */
  @Override
  public int getInt( final String aName, final int aDefaultValue )
  {
    String value = this.properties.getProperty( aName );
    if ( value == null )
    {
      return aDefaultValue;
    }
    return Integer.parseInt( value );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#getLong(java.lang.String, long)
   */
  @Override
  public long getLong( final String aName, final long aDefaultValue )
  {
    String value = this.properties.getProperty( aName );
    if ( value == null )
    {
      return aDefaultValue;
    }
    return Long.parseLong( value );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#getName()
   */
  @Override
  public String getName()
  {
    return this.name;
  }

  /**
   * Returns the properties representation of these user settings.
   * 
   * @return the properties, never <code>null</code>.
   */
  public Properties getProperties()
  {
    return this.properties;
  }

  /**
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Entry<String, Object>> iterator()
  {
    return new UserSettingsIterator( this.properties );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#put(java.lang.String,
   *      java.lang.String)
   */
  @Override
  public void put( final String aName, final String aValue )
  {
    this.properties.put( aName, aValue );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#putBoolean(java.lang.String, boolean)
   */
  @Override
  public void putBoolean( final String aName, final boolean aValue )
  {
    this.properties.put( aName, Boolean.toString( aValue ) );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#putInt(java.lang.String, int)
   */
  @Override
  public void putInt( final String aName, final int aValue )
  {
    this.properties.put( aName, Integer.toString( aValue ) );
  }

  /**
   * @see nl.lxtreme.ols.api.UserSettings#putLong(java.lang.String, long)
   */
  @Override
  public void putLong( final String aName, final long aValue )
  {
    this.properties.put( aName, Long.toString( aValue ) );
  }
}
