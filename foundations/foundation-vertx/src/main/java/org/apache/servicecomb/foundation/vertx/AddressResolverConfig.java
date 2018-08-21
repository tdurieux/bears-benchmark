/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.foundation.vertx;

import java.util.Arrays;
import java.util.List;

import com.netflix.config.DynamicPropertyFactory;

import io.vertx.core.dns.AddressResolverOptions;

public class AddressResolverConfig {

  /**
   * get the target endpoints with custom address resolve config
   * @param tag config tag, such as sc.consumer or cc.consumer
   * @return AddressResolverOptions
   */
  public static AddressResolverOptions getAddressResover(String tag) {
    AddressResolverOptions addressResolverOptions = new AddressResolverOptions();
    addressResolverOptions
        .setServers(getStringListProperty(AddressResolverOptions.DEFAULT_SEACH_DOMAINS,
            "addressResolver." + tag + ".servers",
            "addressResolver.servers"));
    addressResolverOptions
        .setOptResourceEnabled(getBooleanProperty(AddressResolverOptions.DEFAULT_OPT_RESOURCE_ENABLED,
            "addressResolver." + tag + ".optResourceEnabled",
            "addressResolver.optResourceEnabled"));
    addressResolverOptions
        .setCacheMinTimeToLive(getIntProperty(AddressResolverOptions.DEFAULT_CACHE_MIN_TIME_TO_LIVE,
            "addressResolver." + tag + ".cacheMinTimeToLive",
            "addressResolver.cacheMinTimeToLive"));
    addressResolverOptions
        .setCacheMaxTimeToLive(getIntProperty(AddressResolverOptions.DEFAULT_CACHE_MAX_TIME_TO_LIVE,
            "addressResolver." + tag + ".cacheMaxTimeToLive",
            "addressResolver.cacheMaxTimeToLive"));
    addressResolverOptions
        .setCacheNegativeTimeToLive(getIntProperty(AddressResolverOptions.DEFAULT_CACHE_NEGATIVE_TIME_TO_LIVE,
            "addressResolver." + tag + ".cacheNegativeTimeToLive",
            "addressResolver.cacheNegativeTimeToLive"));
    addressResolverOptions
        .setQueryTimeout(getIntProperty(AddressResolverOptions.DEFAULT_QUERY_TIMEOUT,
            "addressResolver." + tag + ".queryTimeout",
            "addressResolver.queryTimeout"));
    addressResolverOptions
        .setMaxQueries(getIntProperty(AddressResolverOptions.DEFAULT_MAX_QUERIES,
            "addressResolver." + tag + ".maxQueries",
            "addressResolver.maxQueries"));
    addressResolverOptions
        .setRdFlag(getBooleanProperty(AddressResolverOptions.DEFAULT_RD_FLAG,
            "addressResolver." + tag + ".rdFlag",
            "addressResolver.rdFlag"));
    addressResolverOptions
        .setSearchDomains(getStringListProperty(AddressResolverOptions.DEFAULT_SEACH_DOMAINS,
            "addressResolver." + tag + ".searchDomains",
            "addressResolver.searchDomains"));
    addressResolverOptions
        .setNdots(getIntProperty(AddressResolverOptions.DEFAULT_CACHE_MIN_TIME_TO_LIVE,
            "addressResolver." + tag + ".ndots",
            "addressResolver.ndots"));
    addressResolverOptions
        .setRotateServers(getBooleanProperty(AddressResolverOptions.DEFAULT_ROTATE_SERVERS,
            "addressResolver." + tag + ".rotateServers",
            "addressResolver.rotateServers"));
    return addressResolverOptions;
  }

  private static List<String> getStringListProperty(List<String> defaultValue, String... keys) {
    String property = null;
    for (String key : keys) {
      property = DynamicPropertyFactory.getInstance().getStringProperty(key, null).get();
      if (property != null) {
        break;
      }
    }
    if (property != null) {
      return Arrays.asList(property.split(","));
    } else {
      return defaultValue;
    }
  }

  private static int getIntProperty(int defaultValue, String... keys) {
    int property = -1;
    for (String key : keys) {
      property = DynamicPropertyFactory.getInstance().getIntProperty(key, -1).get();
      if (property > 0) {
        break;
      }
    }
    if (property > 0) {
      return property;
    } else {
      return defaultValue;
    }
  }

  private static boolean getBooleanProperty(boolean defaultValue, String... keys) {
    String property = null;
    for (String key : keys) {
      property = DynamicPropertyFactory.getInstance().getStringProperty(key, null).get();
      if (property != null) {
        break;
      }
    }
    if (property != null) {
      return Boolean.parseBoolean(property);
    } else {
      return defaultValue;
    }
  }
}
