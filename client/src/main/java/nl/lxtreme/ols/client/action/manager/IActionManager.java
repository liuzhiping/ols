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
package nl.lxtreme.ols.client.action.manager;


import nl.lxtreme.ols.client.action.*;


/**
 * Denotes a manager for {@link IManagedAction}s.
 */
public interface IActionManager
{
  // METHODS

  /**
   * Adds a given action to this manager.
   * 
   * @param aAction
   *          the action to manage, cannot be <code>null</code> and should not
   *          already be registered.
   * @return the added action, never <code>null</code>.
   * @throws IllegalArgumentException
   *           in case the given action was <code>null</code> or already managed
   *           by this manager.
   */
  IManagedAction add( final IManagedAction aAction ) throws IllegalArgumentException;

  /**
   * Returns a managed action by its ID.
   * 
   * @param aId
   *          the ID of the managed action to retrieve, cannot be
   *          <code>null</code>.
   * @return the managed action with the given ID, never <code>null</code>.
   * @throws IllegalArgumentException
   *           in case the given ID did not lead to a valid action, or in case
   *           the given ID was <code>null</code>.
   */
  IManagedAction getAction( final String aId ) throws IllegalArgumentException;

  /**
   * Removes a given action from this manager.
   * 
   * @param aAction
   *          the action to remove, cannot be <code>null</code> and should be
   *          registered.
   * @throws IllegalArgumentException
   *           in case the given action was <code>null</code> or not managed by
   *           this manager.
   */
  void remove( final IManagedAction aAction ) throws IllegalArgumentException;
}

/* EOF */
