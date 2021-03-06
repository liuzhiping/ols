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
package nl.lxtreme.ols.tool.state;


import java.awt.*;

import nl.lxtreme.ols.api.data.*;
import nl.lxtreme.ols.api.tools.*;
import nl.lxtreme.ols.tool.base.*;


/**
 * Provides a state analysis tool (??? not sure what it does though ???).
 */
public class StateAnalyser extends BaseAsyncTool<StateAnalysisDialog, CapturedData, StateAnalysisWorker>
{
  // CONSTRUCTORS

  /**
   * Creates a new StateAnalyser instance.
   */
  public StateAnalyser()
  {
    super( Category.OTHER, "State analyser ..." );
  }

  // METHODS

  /**
   * @see nl.lxtreme.ols.tool.base.BaseTool#createDialog(java.awt.Window,
   *      java.lang.String)
   */
  @Override
  protected StateAnalysisDialog createDialog( final Window aOwner, final String aName )
  {
    return new StateAnalysisDialog( aOwner, getName() );
  }

  /**
   * @see nl.lxtreme.ols.tool.base.BaseAsyncTool#createToolWorker(nl.lxtreme.ols.api.data.DataContainer,
   *      ToolContext)
   */
  @Override
  protected StateAnalysisWorker createToolWorker( final DataContainer aData, final ToolContext aContext )
  {
    return new StateAnalysisWorker( aData, aContext );
  }
}

/* EOF */
