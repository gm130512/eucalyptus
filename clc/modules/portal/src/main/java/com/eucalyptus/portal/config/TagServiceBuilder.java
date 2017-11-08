/*************************************************************************
 * Copyright 2016 Ent. Services Development Corporation LP
 *
 * Redistribution and use of this software in source and binary forms,
 * with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer
 *   in the documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ************************************************************************/
package com.eucalyptus.portal.config;

import com.eucalyptus.bootstrap.Handles;
import com.eucalyptus.component.AbstractServiceBuilder;
import com.eucalyptus.component.ComponentId;
import com.eucalyptus.component.ComponentIds;
import com.eucalyptus.component.ServiceConfiguration;
import com.eucalyptus.component.ServiceRegistrationException;
import com.eucalyptus.component.annotation.ComponentPart;
import com.eucalyptus.portal.common.Tag;
import com.eucalyptus.portal.common.config.DeregisterTagType;
import com.eucalyptus.portal.common.config.DescribeTagType;
import com.eucalyptus.portal.common.config.ModifyTagAttributeType;
import com.eucalyptus.portal.common.config.RegisterTagType;

/**
 *
 */
@ComponentPart( Tag.class )
@Handles( {
    DeregisterTagType.class,
    DescribeTagType.class,
    ModifyTagAttributeType.class,
    RegisterTagType.class,
} )
public class TagServiceBuilder extends AbstractServiceBuilder<TagServiceConfiguration> {
  @Override
  public TagServiceConfiguration newInstance( ) {
    return new TagServiceConfiguration( );
  }

  @Override
  public TagServiceConfiguration newInstance( String partition, String name, String host, Integer port ) {
    return new TagServiceConfiguration( partition, name, host, port );
  }

  @Override
  public ComponentId getComponentId( ) {
    return ComponentIds.lookup( Tag.class );
  }

  @Override
  public void fireStart( ServiceConfiguration config ) throws ServiceRegistrationException { }

  @Override
  public void fireStop( ServiceConfiguration config ) throws ServiceRegistrationException { }

  @Override
  public void fireEnable( ServiceConfiguration config ) throws ServiceRegistrationException { }

  @Override
  public void fireDisable( ServiceConfiguration config ) throws ServiceRegistrationException { }

  @Override
  public void fireCheck( ServiceConfiguration config ) throws ServiceRegistrationException { }

}
