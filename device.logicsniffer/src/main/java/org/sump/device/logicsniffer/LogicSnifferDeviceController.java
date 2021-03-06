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
package org.sump.device.logicsniffer;


import java.awt.*;
import java.beans.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.*;

import javax.swing.SwingWorker.StateValue;
import nl.lxtreme.ols.api.data.*;
import nl.lxtreme.ols.api.devices.*;
import nl.lxtreme.ols.util.*;

import org.osgi.framework.*;


/**
 * GUI Component that allows the user to control the device and start captures.
 * <p>
 * Its modelled after JFileChooser and should allow for non-dialog
 * implementations making it somewhat reusable.
 * 
 * @version 0.7
 * @author Michael "Mr. Sump" Poppitz
 */
public class LogicSnifferDeviceController implements DeviceController
{
  // CONSTANTS

  private static final String NAME = "OpenBench LogicSniffer";

  private static final Logger LOG = Logger.getLogger( LogicSnifferDeviceController.class.getName() );

  // VARIABLES

  private final LogicSnifferConfig deviceConfig;
  private LogicSnifferDevice device;
  private LogicSnifferConfigDialog configDialog;
  private boolean setup;

  private BundleContext bundleContext;

  // CONSTRUCTORS

  /**
   * Constructs device controller component.
   */
  public LogicSnifferDeviceController()
  {
    this.deviceConfig = new LogicSnifferConfig();
    this.setup = false;
  }

  // METHODS

  /**
   * @see nl.lxtreme.ols.api.devices.DeviceController#cancel()
   */
  @Override
  public void cancel() throws IllegalStateException
  {
    if ( ( this.device != null ) && isCapturing() )
    {
      // this.device.cancel( true );
      this.device.stop();
    }
  }

  /**
   * @see nl.lxtreme.ols.api.devices.DeviceController#captureData(nl.lxtreme.ols.api.devices.CaptureCallback)
   */
  @Override
  public void captureData( final CaptureCallback aCallback ) throws IOException
  {
    // Listen to various properties for reporting it to our callback...
    final PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
    {
      /**
       * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
       */
      @Override
      public void propertyChange( final PropertyChangeEvent aEvent )
      {
        final String propertyName = aEvent.getPropertyName();

        if ( LogicSnifferDevice.PROP_CAPTURE_PROGRESS.equals( propertyName ) )
        {
          final Integer progress = ( Integer )aEvent.getNewValue();

          LOG.log( Level.FINE, "Progress {0}%", progress );

          aCallback.updateProgress( progress.intValue() );
        }
        else if ( LogicSnifferDevice.PROP_CAPTURE_STATE.equals( propertyName ) )
        {
          final StateValue state = ( StateValue )aEvent.getNewValue();
          if ( StateValue.STARTED.equals( state ) )
          {
            final int sampleRate = LogicSnifferDeviceController.this.deviceConfig.getSampleRate();
            final int channelCount = LogicSnifferDeviceController.this.deviceConfig.getChannelCount();
            final int channelMask = LogicSnifferDeviceController.this.deviceConfig.getEnabledChannelsMask();

            // Notify our caller that we're started capturing...
            aCallback.captureStarted( sampleRate, channelCount, channelMask );
          }
        }
      }
    };

    this.device = new LogicSnifferDevice( this.bundleContext, this.deviceConfig )
    {
      /**
       * @see javax.swing.SwingWorker#done()
       */
      @Override
      protected void done()
      {
        try
        {
          final CapturedData data = get();

          aCallback.captureComplete( data );
        }
        catch ( CancellationException exception )
        {
          LOG.log( Level.INFO, "Capture cancelled by user..." );

          // simply canceled by user...
          aCallback.captureAborted( "" );
        }
        catch ( ExecutionException exception )
        {
          final Throwable actualCause = exception.getCause();
          if ( actualCause == null )
          {
            LOG.log( Level.SEVERE, "Exception during capture, but no real cause found?!" );
          }

          // Make sure to handle IO-interrupted exceptions properly!
          if ( !HostUtils.handleInterruptedException( actualCause ) )
          {
            LOG.log( Level.WARNING, "Exception during capture, details:", actualCause );
            aCallback.captureAborted( actualCause.getMessage() );
          }
        }
        catch ( InterruptedException exception )
        {
          // Make sure to handle IO-interrupted exceptions properly!
          if ( !HostUtils.handleInterruptedException( exception ) )
          {
            LOG.log( Level.WARNING, "Interrupted during capture, details:", exception );
            aCallback.captureAborted( exception.getMessage() );
          }
        }
      }

      /**
       * @see org.sump.device.logicsniffer.LogicSnifferDevice#process(java.util.List)
       */
      @Override
      protected void process( final List<Sample> aSamples )
      {
        aCallback.samplesCaptured( aSamples );
      }
    };

    // Tell the device on what port & what rate to do its job...
    this.device.addPropertyChangeListener( propertyChangeListener );

    // Let the capturing take place in a background thread...
    this.device.execute();
  }

  /**
   * @see nl.lxtreme.ols.api.devices.DeviceController#getName()
   */
  public String getName()
  {
    return NAME;
  }

  /**
   * @see nl.lxtreme.ols.api.devices.DeviceController#isCapturing()
   */
  @Override
  public boolean isCapturing()
  {
    return ( this.device != null ) && this.device.isRunning();
  }

  /**
   * @see nl.lxtreme.ols.api.devices.DeviceController#isSetup()
   */
  @Override
  public boolean isSetup()
  {
    return this.setup;
  }

  /**
   * Displays the device controller dialog with enabled configuration portion
   * and waits for user input.
   * 
   * @see nl.lxtreme.ols.api.devices.DeviceController#setupCapture()
   */
  @Override
  public boolean setupCapture( final Window aOwner )
  {
    // check if dialog exists with different owner and dispose if so
    if ( ( this.configDialog != null ) && ( this.configDialog.getOwner() != aOwner ) )
    {
      this.configDialog.dispose();
      this.configDialog = null;
    }
    // if no valid dialog exists, create one
    if ( this.configDialog == null )
    {
      this.configDialog = new LogicSnifferConfigDialog( aOwner, this.deviceConfig );
    }

    this.setup = this.configDialog.showDialog();
    return this.setup;
  }

  /**
   * Called when this class is registered as OSGi service.
   * 
   * @param aBundleContext
   *          the bundle context to use, cannot be <code>null</code>.
   * @throws Exception
   *           in case of errors.
   */
  protected void init( final BundleContext aBundleContext ) throws Exception
  {
    // Keep for later use...
    this.bundleContext = aBundleContext;
  }
}
