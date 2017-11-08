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
package com.eucalyptus.simplequeue.async;

import com.eucalyptus.simplequeue.persistence.Queue;
import com.eucalyptus.simpleworkflow.common.stateful.NotifyClientUtils;
import com.eucalyptus.util.Consumer;
import org.apache.log4j.Logger;

/**
 *
 */
public class NotifyClient {
  private static class QueueChannelWrapper implements NotifyClientUtils.ChannelWrapper {
    Queue queue;

    private QueueChannelWrapper(Queue queue) {
      this.queue = queue;
    }

    @Override
    public String getChannelName() {
      return queue.getArn();
    }
  }
  private static final Logger logger = Logger.getLogger(NotifyClient.class);

  public static void notifyQueue(final Queue queue) {
    NotifyClientUtils.notifyChannel(new QueueChannelWrapper(queue));
  }

  public static void pollQueue(final Queue queue,
                                  final long timeout,
                                  final Consumer<Boolean> resultConsumer) throws Exception {
    NotifyClientUtils.pollChannel(new QueueChannelWrapper(queue), timeout, resultConsumer);
  }
}
