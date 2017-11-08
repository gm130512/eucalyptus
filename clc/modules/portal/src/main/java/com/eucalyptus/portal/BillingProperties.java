/*************************************************************************
 * Copyright 2017 Ent. Services Development Corporation LP
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
package com.eucalyptus.portal;

import com.eucalyptus.configurable.ConfigurableClass;
import com.eucalyptus.configurable.ConfigurableField;
import com.eucalyptus.configurable.ConfigurableProperty;
import com.eucalyptus.configurable.ConfigurablePropertyException;
import com.eucalyptus.configurable.PropertyChangeListener;
import com.eucalyptus.simpleworkflow.common.client.Config;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ConfigurableClass(root = "services.billing", description = "Parameters controlling billing service")
public class BillingProperties {
  private static final String DEFAULT_SQS_CLIENT_CONFIG =
          "{\"ConnectionTimeout\": 10000, \"MaxConnections\": 100}";

  private static final String DEFAULT_SENSOR_QUEUE_ATTRIBUTES =
          "{\"DelaySeconds\": \"0\", \"MaximumMessageSize\": \"262144\", " +
                  "\"MessageRetentionPeriod\": \"10800\", \"ReceiveMessageWaitTimeSeconds\": \"0\", " +
                  "\"VisibilityTimeout\": \"120\"}";

  private static final String DEFAULT_SWF_ACTIVITY_WORKER_CONFIG =
          "{\"PollThreadCount\": 4, \"TaskExecutorThreadPoolSize\": 32, \"MaximumPollRateIntervalMilliseconds\": 50, \"MaximumPollRatePerSecond\": 20 }";

  private static final String DEFAULT_SWF_WORKFLOW_WORKER_CONFIG =
          "{ \"DomainRetentionPeriodInDays\": 1, \"PollThreadCount\": 4, \"MaximumPollRateIntervalMilliseconds\": 50, \"MaximumPollRatePerSecond\": 20 }";

  @ConfigurableField( description = "Enable billing's data collection and aggregation", initial = "true" )
  public static Boolean ENABLED = Boolean.TRUE;

  @ConfigurableField(
          initial = DEFAULT_SQS_CLIENT_CONFIG,
          description = "JSON configuration for the billing SQS client",
          changeListener = ClientConfigurationValidatingChangeListener.class )
  public static volatile String SQS_CLIENT_CONFIG = DEFAULT_SQS_CLIENT_CONFIG;


  @ConfigurableField(
          initial = DEFAULT_SENSOR_QUEUE_ATTRIBUTES,
          description = "JSON attributes for the sensor queue",
          changeListener = SensorQueueAttributesChangeListener.class)
  public static String SENSOR_QUEUE_ATTRIBUTES = DEFAULT_SENSOR_QUEUE_ATTRIBUTES;

  @ConfigurableField(
          initial = "BillingDomain",
          description = "The simple workflow service domain for billing",
          changeListener = Config.NameValidatingChangeListener.class )
  public static volatile String SWF_DOMAIN = "BillingDomain";

  @ConfigurableField(
          initial = "BillingTasks",
          description = "The simple workflow service task list for billing",
          changeListener = Config.NameValidatingChangeListener.class )
  public static volatile String SWF_TASKLIST = "BillingTasks";

  @ConfigurableField(
          initial = DEFAULT_SWF_ACTIVITY_WORKER_CONFIG,
          description = "JSON configuration for the billing workflow activity worker",
          changeListener = Config.ActivityWorkerConfigurationValidatingChangeListener.class )
  public static volatile String SWF_ACTIVITY_WORKER_CONFIG = DEFAULT_SWF_ACTIVITY_WORKER_CONFIG;

  @ConfigurableField(
          initial = DEFAULT_SWF_WORKFLOW_WORKER_CONFIG,
          description = "JSON configuration for the billing workflow decision worker",
          changeListener = Config.WorkflowWorkerConfigurationValidatingChangeListener.class )
  public static volatile String SWF_WORKFLOW_WORKER_CONFIG = DEFAULT_SWF_WORKFLOW_WORKER_CONFIG;

  public static String SENSOR_QUEUE_NAME = "BillingSensorQueue";
  public static String INSTANCE_HOUR_SENSOR_QUEUE_NAME = "BillingInstanceHourSensorQueue";

  public static final class ClientConfigurationValidatingChangeListener implements PropertyChangeListener {
    @Override
    public void fireChange(final ConfigurableProperty t, final Object newValue ) throws ConfigurablePropertyException {
      if ( newValue != null && !newValue.toString( ).trim( ).isEmpty( ) ) try {
        SimpleQueueClientManager.buildConfiguration( newValue.toString( ).trim( ) );
      } catch ( final IllegalArgumentException e ) {
        throw new ConfigurablePropertyException( e.getMessage( ) );
      }
    }
  }

  public static final class SensorQueueAttributesChangeListener implements PropertyChangeListener {
    @Override
    public void fireChange(final ConfigurableProperty t, final Object newValue ) throws ConfigurablePropertyException {
      if ( newValue != null && !newValue.toString( ).trim( ).isEmpty( ) ) try {
        SimpleQueueClientManager.getInstance().setQueueAttributes( SENSOR_QUEUE_NAME,
                getQueueAttributes(newValue.toString()) );
      } catch ( final IllegalArgumentException e ) {
        throw new ConfigurablePropertyException( e.getMessage( ) );
      } catch (final IOException e) {
        throw new ConfigurablePropertyException( "Invalid JSON: "+ e.getMessage( ) );
      } catch (final Exception e) {
        throw new ConfigurablePropertyException( e.getMessage( ) );
      }
    }
  }

  public static Map<String, String> getQueueAttributes() throws IOException {
    return getQueueAttributes(SENSOR_QUEUE_ATTRIBUTES);
  }

  public static Map<String, String> getQueueAttributes(final String strAttributes) throws IOException {
    final ObjectMapper mapper = new ObjectMapper( );
    final TypeReference<HashMap<String,String>> typeRef
            = new TypeReference<HashMap<String,String>>() {};
    return mapper.readValue(strAttributes.trim(), typeRef);
  }
}
