/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mindorks.nybus;

import com.mindorks.nybus.driver.NYBusDriver;
import com.mindorks.nybus.event.Channel;
import com.mindorks.nybus.finder.NYEventClassFinder;
import com.mindorks.nybus.finder.NYSubscribeMethodFinder;
import com.mindorks.nybus.logger.JavaLogger;
import com.mindorks.nybus.logger.Logger;
import com.mindorks.nybus.publisher.NYPublisher;
import com.mindorks.nybus.scheduler.SchedulerProvider;
import com.mindorks.nybus.scheduler.SchedulerProviderImpl;
import com.mindorks.nybus.utils.ListUtils;

import java.util.List;

/**
 * Created by amitshekhar on 14/08/17.
 */

/**
 * NYBus is the entry point for publish/subscribe system and the implementation of the
 * {@link Bus}.
 */
public class NYBus implements Bus {

    private static NYBus sNYBusInstance;

    static {
        NYBus.get().setSchedulerProvider(new SchedulerProviderImpl());
    }

    private NYBusDriver mNYBusDriver;

    private NYBus() {
        mNYBusDriver = new NYBusDriver(new NYPublisher(),
                new NYSubscribeMethodFinder(),
                new NYEventClassFinder(),
                new JavaLogger());
    }

    public static NYBus get() {
        if (sNYBusInstance == null) {
            synchronized (NYBus.class) {
                if (sNYBusInstance == null) {
                    sNYBusInstance = new NYBus();
                }
            }
        }
        return sNYBusInstance;
    }

    /**
     * Set the scheduler provider.
     *
     * @param schedulerProvider the scheduler provider.
     */
    @Override
    public void setSchedulerProvider(SchedulerProvider schedulerProvider) {
        mNYBusDriver.initPublishers(schedulerProvider);
    }

    /**
     * Set the logger.
     *
     * @param logger the logger.
     */
    @Override
    public void setLogger(Logger logger) {
        mNYBusDriver.setLogger(logger);
    }

    /**
     * Register for event.
     *
     * @param object     the context object.
     * @param channelIDs channel ids.
     */
    @Override
    public void register(Object object, String... channelIDs) {
        register(object, ListUtils.convertVarargsToList(channelIDs));
    }

    /**
     * Register for event.
     *
     * @param object    the context object.
     * @param channelId list of channel ids.
     */
    @Override
    public void register(Object object, List<String> channelId) {
        mNYBusDriver.register(object, channelId);
    }

    /**
     * Unregister from the event.
     *
     * @param object     the context object.
     * @param channelIDs channel ids.
     */
    @Override
    public void unregister(Object object, String... channelIDs) {
        unregister(object, ListUtils.convertVarargsToList(channelIDs));
    }

    /**
     * Unregister from the event.
     *
     * @param object    the context object.
     * @param channelId list of channel ids.
     */
    @Override
    public void unregister(Object object, List<String> channelId) {
        mNYBusDriver.unregister(object, channelId);
    }

    /**
     * Post the event.
     *
     * @param object the event.
     */
    @Override
    public void post(Object object) {
        post(object, Channel.DEFAULT);
    }

    /**
     * Post the event with specific channel.
     *
     * @param object    the context object.
     * @param channelId channel id.
     */
    @Override
    public void post(Object object, String channelId) {
        mNYBusDriver.post(object, channelId);
    }

    /**
     * Check if event registered.
     *
     * @param object     the context object.
     * @param channelIDs the channel ids.
     * @return the boolean value.
     */
    @Override
    public boolean isRegistered(Object object, String... channelIDs) {
        return mNYBusDriver.isRegistered(object, ListUtils.convertVarargsToList(channelIDs));
    }

    /**
     * To enable logging.
     */
    @Override
    public void enableLogging() {
        mNYBusDriver.enableLogging();
    }

}
