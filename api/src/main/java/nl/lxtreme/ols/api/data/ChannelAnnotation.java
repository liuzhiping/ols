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
package nl.lxtreme.ols.api.data;


/**
 * Provides an annotation for a single channel.
 */
public final class ChannelAnnotation implements Comparable<ChannelAnnotation>
{
  // VARIABLES

  private final int startIndex;
  private final int endIndex;
  private final Object data;

  // CONSTRUCTORS

  /**
   * Creates a new ChannelAnnotation instance.
   */
  public ChannelAnnotation( final int aStartIndex, final int aEndIndex, final Object aData )
  {
    this.startIndex = aStartIndex;
    this.endIndex = aEndIndex;
    this.data = aData;
  }

  // METHODS

  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo( final ChannelAnnotation aAnnotation )
  {
    int result = this.startIndex < aAnnotation.startIndex ? -1 : ( this.startIndex == aAnnotation.startIndex ? 0 : 1 );
    if ( result == 0 )
    {
      result = this.endIndex < aAnnotation.endIndex ? -1 : ( this.endIndex == aAnnotation.endIndex ? 0 : 1 );
    }
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals( final Object aObject )
  {
    if ( this == aObject )
    {
      return true;
    }
    if ( ( aObject == null ) || !( aObject instanceof ChannelAnnotation ) )
    {
      return false;
    }

    final ChannelAnnotation other = ( ChannelAnnotation )aObject;
    if ( this.data == null )
    {
      if ( other.data != null )
      {
        return false;
      }
    }
    else if ( !this.data.equals( other.data ) )
    {
      return false;
    }
    if ( this.endIndex != other.endIndex )
    {
      return false;
    }
    if ( this.startIndex != other.startIndex )
    {
      return false;
    }
    return true;
  }

  /**
   * @return the data
   */
  public Object getData()
  {
    return this.data;
  }

  /**
   * @return the endIndex
   */
  public int getEndIndex()
  {
    return this.endIndex;
  }

  /**
   * @return the startIndex
   */
  public int getStartIndex()
  {
    return this.startIndex;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( this.data == null ) ? 0 : this.data.hashCode() );
    result = prime * result + this.endIndex;
    result = prime * result + this.startIndex;
    return result;
  }

  /**
   * Returns whether this annotation starts before the given index and ends
   * after the given index.
   * 
   * @param aIndex
   *          the index.
   * @return <code>true</code> if this annotation is valid at the given index
   *         (see above), <code>false</code> otherwise.
   */
  public boolean isInRange( final int aIndex )
  {
    return ( getStartIndex() <= aIndex ) && ( getEndIndex() >= aIndex );
  }

  /**
   * Returns whether this annotation's start index is before the given end index
   * and its end index is after the given start index.
   * 
   * @param aStartIndex
   *          the starting index;
   * @param aEndIndex
   *          the ending index.
   * @return <code>true</code> if this annotation is valid between the given
   *         range (see above), <code>false</code> otherwise.
   */
  public boolean isInRange( final int aStartIndex, final int aEndIndex )
  {
    return ( getStartIndex() <= aEndIndex ) && ( getEndIndex() >= aStartIndex );
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return this.data == null ? "" : String.valueOf( this.data );
  }
}
